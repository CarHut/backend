package com.carhut.proxy.cache;

public enum CacheState {
    REQUEST_IS_ADDED_TO_CACHE_CANDIDATES,
    CACHE_REQUEST_CANDIDATE_EXPIRED_ADDED_NEW_CANDIDATE,
    REQUEST_ADDED_TO_CACHE_PROVIDE_RESPONSE,
    CACHE_AVAILABLE,
    INCREMENTED_SIGNIFICANCE_FOR_REQUEST, ERROR

}
