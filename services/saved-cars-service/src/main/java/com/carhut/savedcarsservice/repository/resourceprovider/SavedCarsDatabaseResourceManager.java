package com.carhut.savedcarsservice.repository.resourceprovider;

import com.carhut.commons.resource.DatabaseResourceManager;
import com.carhut.commons.resource.DefaultDatabaseResourceQueueManager;
import com.carhut.commons.resource.ResourceHolderThread;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;

public class SavedCarsDatabaseResourceManager<RESOURCE> implements DatabaseResourceManager<RESOURCE> {

    private final LinkedBlockingQueue<ResourceHolderThread<RESOURCE>> taskQueue;
    private final DefaultDatabaseResourceQueueManager<RESOURCE> queueManager;
    private static SavedCarsDatabaseResourceManager manager = null;

    private SavedCarsDatabaseResourceManager() {
        this.taskQueue = new LinkedBlockingQueue<>();
        this.queueManager = new DefaultDatabaseResourceQueueManager<>(taskQueue, 10000L);
        this.queueManager.start();
    }

    public static SavedCarsDatabaseResourceManager getInstance() {
        if (manager == null) {
            manager = new SavedCarsDatabaseResourceManager();
        }
        return manager;
    }

    @Override
    public CompletableFuture<RESOURCE> acquireDatabaseResource(Function<Void, RESOURCE> fun) {
        CompletableFuture<RESOURCE> cf = new CompletableFuture<>();
        ResourceHolderThread<RESOURCE> resourceWorker = new ResourceHolderThread<>(generateThreadId(), fun,
                null, cf);
        taskQueue.add(resourceWorker);
        return cf;
    }

    private synchronized String generateThreadId() {
        return UUID.randomUUID().toString();
    }

}
