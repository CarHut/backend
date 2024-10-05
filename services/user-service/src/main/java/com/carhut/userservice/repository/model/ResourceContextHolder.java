package com.carhut.userservice.repository.model;

import java.util.concurrent.atomic.AtomicInteger;

public class ResourceContextHolder {
    private String id;
    private AtomicInteger rank;

    public ResourceContextHolder(String id) {
        this.id = id;
        this.rank = new AtomicInteger();
    }

    public String getId() {
        return id;
    }

    public AtomicInteger getRank() {
        return rank;
    }

    public void incrementRank() {
        rank.incrementAndGet();
    }
}
