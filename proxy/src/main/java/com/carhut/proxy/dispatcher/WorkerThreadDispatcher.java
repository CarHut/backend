package com.carhut.proxy.dispatcher;

import com.carhut.proxy.dispatcher.model.WorkerThread;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public final class WorkerThreadDispatcher<T, R> implements ThreadDispatcher<Function<T, CompletableFuture<R>>, CompletableFuture<R>> {
    private final Integer MAX_THREAD_POOL_SIZE = 10;
    private static WorkerThreadDispatcher workerThreadDispatcher = null;
    private final List<WorkerThread> runningThreads = new ArrayList<>();
    private final Queue<WorkerThread> waitingThreads = new PriorityQueue<>();
    
    public static WorkerThreadDispatcher getInstance() {
        if (workerThreadDispatcher == null) {
            workerThreadDispatcher = new WorkerThreadDispatcher<>();
        }
        return workerThreadDispatcher;
    }

    @Override
    public synchronized CompletableFuture<R> dispatchThread(Function<T, CompletableFuture<R>> function) {
        CompletableFuture<R> resultFuture = new CompletableFuture<>();
        final WorkerThread<T, R> workerThread = new WorkerThread<>(generateThreadId(), function, null, resultFuture);
        workerThread.start();
        return resultFuture;
    }

    private synchronized String generateThreadId() {
        return UUID.randomUUID().toString();
    }
}
