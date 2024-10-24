package com.carhut.proxy.cache;

import com.carhut.proxy.model.RequestModel;
import com.carhut.proxy.model.ResponseModel;
import com.carhut.proxy.model.UnifiedRESTResponseModel;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class RedisCacheHandler {

    private final String proxySaltPort = "8080";
    // Time to live for cache in memory
    private final Long TTL = 180000L; // millis
    // Time to live for cache to have significance of creating cache content for certain request
    private final Long SIGNIFICANCE_TTL = 60000L; // millis
    // Threshold amount of request needed to make a valid candidate for cache
    private final Integer SIGNIFICANCE_THRESHOLD = 5;
    // <Hash, Info such as count of requests and significance TTL>
    private final ConcurrentHashMap<String, CacheInformationHolder> possibleCacheCandidates = new ConcurrentHashMap<>();
    // <Hash, Start of TTL (millis)>
    private final ConcurrentHashMap<String, Long> availableCacheHashes = new ConcurrentHashMap<>();
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCacheHandler() {
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory();
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(lettuceConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        lettuceConnectionFactory.start();
        this.redisTemplate = template;
    }

    public synchronized CacheState cacheAvailability(RequestModel request) {
        try {
            String hash = hashRequest(request);
            Long timeToLive = availableCacheHashes.get(hash);
            if (timeToLive == null) {
                return checkPossibleCandidates(hash);
            } else if (System.currentTimeMillis() - timeToLive > TTL) {
                availableCacheHashes.remove(hash);
                return checkPossibleCandidates(hash);
            } else {
                return CacheState.CACHE_AVAILABLE;
            }
        } catch (Exception e) {
            return CacheState.ERROR;
        } finally {
            cleanUpFailedCandidates();
        }
    }

    private void cleanUpFailedCandidates() {
        possibleCacheCandidates.keySet().forEach(candidate -> {
            Long millis = System.currentTimeMillis();
            if (millis - possibleCacheCandidates.get(candidate).getFirstRequestSentAt() > SIGNIFICANCE_TTL) {
                possibleCacheCandidates.remove(candidate);
            }
        });
    }

    private synchronized CacheState checkPossibleCandidates(String hash) {
        CacheInformationHolder info = possibleCacheCandidates.get(hash);
        // add request to possible cache candidates
        if (info == null && availableCacheHashes.get(hash) == null) {
            possibleCacheCandidates.put(hash, new CacheInformationHolder(System.currentTimeMillis(), 1));
            return CacheState.REQUEST_IS_ADDED_TO_CACHE_CANDIDATES;
        } else if (availableCacheHashes.get(hash) == null) {
            return checkWaitingCandidates(hash, info);
        }
        return CacheState.ERROR;
    }

    private CacheState checkWaitingCandidates(String hash, CacheInformationHolder info) {
        if (info != null) {
            // candidate is old, renew info
            if (System.currentTimeMillis() - info.getFirstRequestSentAt() > SIGNIFICANCE_TTL) {
                possibleCacheCandidates.remove(hash);
                possibleCacheCandidates.put(hash, new CacheInformationHolder(System.currentTimeMillis(), 1));
                return CacheState.CACHE_REQUEST_CANDIDATE_EXPIRED_ADDED_NEW_CANDIDATE;
            }
            // candidate reached significance threshold
            else if (SIGNIFICANCE_THRESHOLD == info.getCount()) {
                possibleCacheCandidates.get(hash).setReadyToBeCandidate(true);
                return CacheState.REQUEST_ADDED_TO_CACHE_PROVIDE_RESPONSE;
            } else {
                possibleCacheCandidates.get(hash).setCount(info.getCount() + 1);
                return CacheState.INCREMENTED_SIGNIFICANCE_FOR_REQUEST;
            }
        } else {
            return CacheState.ERROR;
        }
    }

    public void addCache(RequestModel input, ResponseModel model) {
        String hash = hashRequest(input);

        // Response is unavailable or failed
        if (model == null) {
            possibleCacheCandidates.remove(hash);
            return;
        }

        try {
            redisTemplate.opsForValue().set("cache:" + proxySaltPort + ":" + hash,
                    model.toJson(), TTL, TimeUnit.MILLISECONDS);
            availableCacheHashes.put(hash, System.currentTimeMillis());
            possibleCacheCandidates.remove(hash);
            System.out.println("Successfully added cache to Redis database.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            availableCacheHashes.remove(hash);
        }
    }

    public ResponseModel getCache(RequestModel request) {
        String hash = hashRequest(request);
        Object cache = redisTemplate.opsForValue().get("cache:" + proxySaltPort + ":" + hash);
        return new UnifiedRESTResponseModel((String) cache, 200, "Successfully fetched cache.");
    }

    private String hashRequest(RequestModel requestModel) {
        String input = requestModel.toCacheJson();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
