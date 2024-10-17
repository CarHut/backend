package com.carhut.services.sr.carhutapisr.repository.resourceprovider;

import com.carhut.commons.resource.DatabaseResourceManager;
import com.carhut.commons.resource.DefaultDatabaseResourceQueueManager;
import com.carhut.commons.resource.ResourceHolderThread;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;

public class CarHutApiDatabaseResourceManager<RESOURCEOBJECT> implements DatabaseResourceManager<RESOURCEOBJECT> {

    private static CarHutApiDatabaseResourceManager manager = null;
    private final LinkedBlockingQueue<ResourceHolderThread<RESOURCEOBJECT>> taskQueue;
    private final DefaultDatabaseResourceQueueManager<RESOURCEOBJECT> queueManager;

    public static CarHutApiDatabaseResourceManager getInstance() {
        if (manager == null) {
            manager = new CarHutApiDatabaseResourceManager();
        }
        return manager;
    }

    private CarHutApiDatabaseResourceManager() {
        this.taskQueue = new LinkedBlockingQueue<>();
        this.queueManager = new DefaultDatabaseResourceQueueManager<>(taskQueue, 10000L);
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
