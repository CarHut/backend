package com.carhut.commons.resource;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultDatabaseResourceQueueManager<RESOURCEOBJECT> extends Thread {

    private final Long MAX_RESOURCE_HOLD_TIME;
    private final LinkedBlockingQueue<ResourceHolderThread<RESOURCEOBJECT>> queue;
    private volatile Boolean running;
    private final AtomicBoolean holdsResource;
    private long startTime;
    private ResourceHolderThread<RESOURCEOBJECT> resourceHolderThread = null;

    public DefaultDatabaseResourceQueueManager(LinkedBlockingQueue<ResourceHolderThread<RESOURCEOBJECT>> queue,
                                               Long threadTerminationTimeout) {
        this.MAX_RESOURCE_HOLD_TIME = threadTerminationTimeout;
        this.queue = queue;
        this.running = true;
        this.holdsResource = new AtomicBoolean(false);
    }

    @Override
    public void run() {
        while (running) {
            if (!queue.isEmpty() && !holdsResource.get()) {
                prepareAndStartWorker();
            } else if (System.currentTimeMillis() - startTime > MAX_RESOURCE_HOLD_TIME
                    && resourceHolderThread != null) {
                unlockBlockingResource();
            }
        }
    }

    private void unlockBlockingResource() {
        holdsResource.set(false);
        resourceHolderThread.interrupt();
        resourceHolderThread = null;
    }

    private void prepareAndStartWorker() {
        ResourceHolderThread<RESOURCEOBJECT> resourceHolder = queue.poll();
        System.out.println("Starting new thread [" + resourceHolder.getThreadId() + "]");
        if (resourceHolder != null) {
            resourceHolderThread = resourceHolder;
            resourceHolder.getResultFuture().whenComplete((res, ex) -> {
                System.out.println("Stopping thread [" + resourceHolder.getThreadId() + "]");
                holdsResource.set(false);
            });
            holdsResource.set(true);
            resourceHolder.start();
            startTime = System.currentTimeMillis();
        }
    }

}
