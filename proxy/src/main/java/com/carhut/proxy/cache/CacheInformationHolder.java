package com.carhut.proxy.cache;

public class CacheInformationHolder {
    private Long firstRequestSentAt;
    private Integer count;
    private Boolean readyToBeCandidate;

    public CacheInformationHolder(Long firstRequestSentAt, Integer count) {
        this.firstRequestSentAt = firstRequestSentAt;
        this.count = count;
        this.readyToBeCandidate = false;
    }

    public Long getFirstRequestSentAt() {
        return firstRequestSentAt;
    }

    public Integer getCount() {
        return count;
    }

    public void setFirstRequestSentAt(Long firstRequestSentAt) {
        this.firstRequestSentAt = firstRequestSentAt;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Boolean getReadyToBeCandidate() {
        return readyToBeCandidate;
    }

    public void setReadyToBeCandidate(Boolean readyToBeCandidate) {
        this.readyToBeCandidate = readyToBeCandidate;
    }
}

