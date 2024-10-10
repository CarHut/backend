package com.carhut.savedsearchesservice.repository.resourceprovider;

import com.carhut.commons.resource.DatabaseResourceManager;
import com.carhut.commons.resource.DefaultDatabaseResourceQueueManager;
import com.carhut.commons.resource.ResourceHolderThread;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;

public class SavedSearchesResourceManager<RESOURCE> implements DatabaseResourceManager<RESOURCE> {

    private final LinkedBlockingQueue<ResourceHolderThread<RESOURCE>> taskQueue;
    private final DefaultDatabaseResourceQueueManager<RESOURCE> queueManager;
    private static SavedSearchesResourceManager savedSearchesResourceManager = null;

    private SavedSearchesResourceManager() {
        this.taskQueue = new LinkedBlockingQueue<>();
        this.queueManager = new DefaultDatabaseResourceQueueManager<>(taskQueue, 10000L);
        this.queueManager.start();
    }

    public static SavedSearchesResourceManager getInstance() {
        if (savedSearchesResourceManager == null) {
            savedSearchesResourceManager = new SavedSearchesResourceManager<>();
        }
        return savedSearchesResourceManager;
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
