package com.carhut.userservice.repository.resourceprovider;

import java.util.concurrent.atomic.AtomicBoolean;

public class LockTimeoutHandler extends Thread {

    private final Long timeoutMillis;
    private AtomicBoolean running;
    private final DatabaseResourceProviderManager manager;

    public LockTimeoutHandler(DatabaseResourceProviderManager manager, final Long timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
        this.running = new AtomicBoolean(false);
        this.manager = manager;
        this.setName("LockTimeoutHandler Thread - monitoring blocking locks for DatabaseResourceProviderManager.");
    }

    @Override
    public void run() {
//        this.running = new AtomicBoolean(true);
//        ResourceHolderThread before = manager.getLockOwner();

//        try {
//            Thread.sleep(timeoutMillis);
//        } catch (InterruptedException e) {
//            running = new AtomicBoolean(false);
//            return;
//        }
//
//        ResourceHolderThread after = manager.getLockOwner();

//        if (before.getThreadId().equals(after.getThreadId())) {
//            running = new AtomicBoolean(false);
//            // Kill the blocking resource holder thread
//            System.out.println("Killed thread a blocking thread [" + after.getName() +"]");
//            after.interrupt();
//        }

    }

    public synchronized AtomicBoolean isRunning() {
        return this.running;
    }

}
