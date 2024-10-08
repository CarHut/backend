package com.carhut.userservice.repository.resourceprovider;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;

public class DatabaseResourceProviderManager<RESOURCEOBJECT> {

    private static DatabaseResourceProviderManager manager = null;
    private final LinkedBlockingQueue<ResourceHolderThread<RESOURCEOBJECT>> taskQueue;
    private final DatabaseResourceProviderQueueManager queueManager;

    public static DatabaseResourceProviderManager getInstance() {
        if (manager == null) {
            manager = new DatabaseResourceProviderManager();
        }
        return manager;
    }

    private DatabaseResourceProviderManager() {
        this.taskQueue = new LinkedBlockingQueue<>();
        this.queueManager = new DatabaseResourceProviderQueueManager<>(taskQueue);
        this.queueManager.start();
    }

    public synchronized CompletableFuture<RESOURCEOBJECT> acquireDatabaseResource(
        Function<Void, RESOURCEOBJECT> function) {
        CompletableFuture<RESOURCEOBJECT> cf = new CompletableFuture<>();
        ResourceHolderThread<RESOURCEOBJECT> resourceWorker = new ResourceHolderThread<>(generateThreadId(), function,
                null, cf);
        taskQueue.add(resourceWorker);
        return cf;
    }

    private synchronized String generateThreadId() {
        return UUID.randomUUID().toString();
    }
}
