package com.carhut.proxy.controller.ratelimit;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class RateLimitAspect {
    public static final String ERROR_MESSAGE = "To many requests at endpoint %s from IP [%s]. Please try again later.";
    private final ConcurrentHashMap<String, List<Long>> requestsCounts = new ConcurrentHashMap<>();

    @Value("${app.rate.limit}")
    private int rateLimit;

    @Value("${app.rate.durationinms}")
    private long rateDuration;

    @Before("@annotation(com.carhut.proxy.controller.ratelimit.RateLimitingProtection)")
    public void rateLimit() {
        final ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        final String key = requestAttributes.getRequest().getRemoteAddr();
        final long currentTime = System.currentTimeMillis();
        requestsCounts.putIfAbsent(key, new ArrayList<>());
        requestsCounts.get(key).add(currentTime);
        cleanUpRequestCounts(currentTime);
        if (requestsCounts.get(key).size() > rateLimit) {
            throw new RateLimitException(String.format(ERROR_MESSAGE, requestAttributes.getRequest().getRequestURI(), key));
        }
    }

    private void cleanUpRequestCounts(long currentTime) {
        requestsCounts.values().forEach(list -> {
            list.removeIf(time -> isInbetweenTimeOld(currentTime, time));
        });
    }

    private boolean isInbetweenTimeOld(long currentTime, long timeToCheck) {
        return currentTime - timeToCheck > rateDuration;
    }
}
