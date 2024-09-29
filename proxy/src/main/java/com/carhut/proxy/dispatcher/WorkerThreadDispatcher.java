package com.carhut.proxy.dispatcher;

import com.carhut.proxy.dispatcher.model.WorkerThread;
import com.carhut.proxy.util.logger.ProxyLogger;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

public final class WorkerThreadDispatcher<T, R> implements ThreadDispatcher<Function<T, CompletableFuture<R>>, CompletableFuture<R>> {
    private final Integer MAX_THREAD_POOL_SIZE = 10;
    private static WorkerThreadDispatcher workerThreadDispatcher = null;
    private static List<WorkerThread> runningThreads = new CopyOnWriteArrayList<>();
    private static List<WorkerThread> waitingThreads = new CopyOnWriteArrayList<>();
    public static final ProxyLogger logger = ProxyLogger.getInstance();

    public static WorkerThreadDispatcher getInstance() {
        if (workerThreadDispatcher == null) {
            workerThreadDispatcher = new WorkerThreadDispatcher<>();
        }
        return workerThreadDispatcher;
    }

    @Override
    public synchronized CompletableFuture<R> dispatchThread(Function<T, CompletableFuture<R>> function) {
        final String threadId = generateThreadId();
        logger.logInfo("Dispatching new worker thread [ID: " + threadId + "]");
        CompletableFuture<R> resultFuture = new CompletableFuture<>();
        final WorkerThread<T, R> workerThread = new WorkerThread<>(threadId, function, null, resultFuture);
        if (runningThreads.size() + 1 > MAX_THREAD_POOL_SIZE) {
            logger.logWarn("Running workers thread pool is maxed out. Thread [ID: " + threadId + "] is added to waiting worker threads.");
            waitingThreads.add(workerThread);
            return resultFuture;
        }
        logger.logInfo("Thread [ID: " + threadId + "] is added to running thread pool.");
        runningThreads.add(workerThread);
        workerThread.start();
        return resultFuture;
    }

    public synchronized void removeThreadFromPool(String threadId) {
        if (runningThreads.removeIf(thread -> thread.getThreadId().equals(threadId))) {
            logger.logInfo("Worker thread [" + threadId + "] was removed from running pool.");
            notifyWaitingThreads();
        } else {
            System.out.println("Worker thread is not present in running pool.");
            logger.logWarn("Worker thread [" + threadId + "] is not present in running pool. Removing nothing from running pool.");
        }
    }

    private synchronized void notifyWaitingThreads() {
        if (waitingThreads.isEmpty()) {
            return;
        }

        final WorkerThread workerThread = waitingThreads.stream().max(Comparator.comparingInt(WorkerThread::getRank))
                .orElse(null);

        if (workerThread == null) {
            System.out.println("Cannot notify waiting threads.");
            logger.logWarn("Cannot notify waiting threads. Couldn't find any.");
            return;
        }

        logger.logWarn("Notifying and starting waiting thread [ID: " + workerThread.getThreadId() + "].");
        runningThreads.add(workerThread);
        waitingThreads.remove(workerThread);
        workerThread.start();
        waitingThreads.forEach(WorkerThread::increaseRank);
    }

    private synchronized String generateThreadId() {
        return UUID.randomUUID().toString();
    }
}
