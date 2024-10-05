package com.carhut.userservice.repository.resourceprovider.model;

import com.carhut.userservice.repository.resourceprovider.DatabaseResourceProviderManager;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

public class ResourceHolderThread<RESOURCEOBJECT> extends Thread implements Runnable {

    private final Function<Void, RESOURCEOBJECT> runnableFunction;
    private final Void input;
    private final CompletableFuture<RESOURCEOBJECT> resultFuture;
    private final String threadId;
    private AtomicInteger rank;
    private DatabaseResourceProviderManager databaseResourceProviderManager = DatabaseResourceProviderManager.getInstance();
    private final ReentrantLock lock;

    public ResourceHolderThread(String threadId, Function<Void, RESOURCEOBJECT> runnableFunction,
                                Void input, CompletableFuture<RESOURCEOBJECT> resultFuture, ReentrantLock lock) {
        this.runnableFunction = runnableFunction;
        this.input = input;
        this.resultFuture = resultFuture;
        this.threadId = threadId;
        this.rank = new AtomicInteger(0);
        this.lock = lock;
        this.setName("ResourceHolderThread #" + threadId);
    }

    @Override
    public void run() {
        try {
            lock.lock();
            RESOURCEOBJECT resourceobject = runnableFunction.apply(input);
            resultFuture.complete(resourceobject);
        }
        catch (Exception e) {
            System.out.println("Resource holder thread encountered an unexpected error: " + e.getMessage());
            resultFuture.completeExceptionally(e);
        }
    }

    public String getThreadId() {
        return threadId;
    }

    public void increaseRank() {
        this.rank.incrementAndGet();
    }

    public int getRank() {
        return this.rank.get();
    }
}
