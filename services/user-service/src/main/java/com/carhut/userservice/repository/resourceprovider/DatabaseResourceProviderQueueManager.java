package com.carhut.userservice.repository.resourceprovider;

import com.carhut.userservice.repository.resourceprovider.model.ResourceHolderThread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class DatabaseResourceProviderQueueManager<RESOURCEOBJECT> extends Thread {

    private final LinkedBlockingQueue<ResourceHolderThread<RESOURCEOBJECT>> queue;
    private volatile Boolean running;
    private final AtomicBoolean holdsResource;

    public DatabaseResourceProviderQueueManager(LinkedBlockingQueue<ResourceHolderThread<RESOURCEOBJECT>> queue) {
        this.queue = queue;
        this.running = true;
        this.holdsResource = new AtomicBoolean(false);
    }

    @Override
    public void run() {
        System.out.println("Resource Queue Manager is up and running.");
        while (running) {
            if (!queue.isEmpty() && !holdsResource.get()) {
                prepareAndStartWorker();
            }
        }
    }

    private void prepareAndStartWorker() {
        ResourceHolderThread<RESOURCEOBJECT> resourceHolder = queue.poll();
        if (resourceHolder != null) {
            resourceHolder.getResultFuture().whenComplete((res, ex) -> {
                holdsResource.set(false);
            });
            holdsResource.set(true);
            resourceHolder.start();
        }
    }

}
