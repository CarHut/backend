package com.carhut.userservice.repository.resourceprovider;

import com.carhut.userservice.repository.resourceprovider.model.ResourceHolderThread;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

public class DatabaseResourceProviderManager<RESOURCEOBJECT> {

    private final Long MAX_RESOURCE_HOLD_TIME = 10000L;
    private final List<ResourceHolderThread> waitingInstances = new CopyOnWriteArrayList<>();
    private static DatabaseResourceProviderManager databaseResourceProviderManager = null;
    private LockTimeoutHandler lockTimeoutHandler;
    private final ReentrantLock lock = new ReentrantLock();
    private AtomicInteger count1 = new AtomicInteger(0);
    private AtomicInteger count2 = new AtomicInteger(0);
    private final AtomicBoolean isLockAcquired;

    public synchronized static DatabaseResourceProviderManager getInstance() {
        if (databaseResourceProviderManager == null) {
            databaseResourceProviderManager = new DatabaseResourceProviderManager();
        }
        return databaseResourceProviderManager;
    }

    private DatabaseResourceProviderManager() {
        this.isLockAcquired = new AtomicBoolean(false);
    }

    public synchronized CompletableFuture<RESOURCEOBJECT> acquireDatabaseResource(Function<Void, CompletableFuture<RESOURCEOBJECT>> function) {
        synchronized (isLockAcquired) {
            if (function == null) {
                CompletableFuture<RESOURCEOBJECT> future = new CompletableFuture<>();
                future.completeExceptionally(new ExecutionException("Passed invalid function resource provider.", new RuntimeException()));
                return future;
            }

            final String threadId = generateThreadId();
            final CompletableFuture<RESOURCEOBJECT> resultFuture = new CompletableFuture<>();
            final ResourceHolderThread resourceHolderThread = new ResourceHolderThread(threadId, function,
                    null, resultFuture, lock);

            resultFuture.whenComplete((resp, ex) -> {
               if (ex != null) {
                   System.out.println("Completable future in thread ended exceptionally. Unlocking resource.");
               }
               lock.unlock();
               unlockResource(resourceHolderThread);
            });

            if (isLockAcquired.get()) {
                waitingInstances.add(resourceHolderThread);
                return resultFuture;
            } else {
                lockTheLock(resourceHolderThread);
            }

            return resultFuture;
        }
    }

    private synchronized void lockTheLock(ResourceHolderThread resourceHolderThread) {
        synchronized (isLockAcquired) {
            isLockAcquired.set(true);
            System.out.println("LOCKING COUNT: " + count1.incrementAndGet() + "    " + resourceHolderThread.getThreadId());
            resourceHolderThread.start();
        }
    }

    public synchronized void unlockResource(ResourceHolderThread resourceHolderThread) {
        synchronized (isLockAcquired) {
            isLockAcquired.set(false);
            System.out.println("UNLOCK COUNT: " + count2.incrementAndGet() + "    " + resourceHolderThread.getThreadId());
            notifyWaitingThreads();
        }
    }

    private synchronized void notifyWaitingThreads() {
        synchronized (isLockAcquired) {
            if (waitingInstances.isEmpty()) {
                return;
            }

            final ResourceHolderThread resourceHolderThread = waitingInstances.stream().max(Comparator.comparingInt(ResourceHolderThread::getRank))
                    .orElse(null);

            if (resourceHolderThread == null) {
                System.out.println("Cannot notify waiting threads.");
                return;
            }

            waitingInstances.remove(resourceHolderThread);
            waitingInstances.forEach(ResourceHolderThread::increaseRank);
            isLockAcquired.set(true);
            resourceHolderThread.start();
        }
    }

    private synchronized String generateThreadId() {
        synchronized (isLockAcquired) {
            return UUID.randomUUID().toString();
        }
    }

}
