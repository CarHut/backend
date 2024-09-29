package com.carhut.proxy.dispatcher.model;

import com.carhut.proxy.dispatcher.WorkerThreadDispatcher;
import com.carhut.proxy.util.logger.ProxyLogger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class WorkerThread<INP, RES> extends Thread implements Runnable {

    private final Function<INP, CompletableFuture<RES>> runnableFunction;
    private final INP input;
    private final CompletableFuture<RES> resultFuture;
    private final String threadId;
    private AtomicInteger rank;
    private WorkerThreadDispatcher workerThreadDispatcher = WorkerThreadDispatcher.getInstance();
    private static final ProxyLogger logger = ProxyLogger.getInstance();

    public WorkerThread(String threadId, Function<INP, CompletableFuture<RES>> runnableFunction, INP input,
                        CompletableFuture<RES> resultFuture) {
        this.runnableFunction = runnableFunction;
        this.input = input;
        this.resultFuture = resultFuture;
        this.threadId = threadId;
        this.rank = new AtomicInteger(0);
    }

    @Override
    public void run() {
        logger.logInfo("Starting worker thread with ID: " + this.getThreadId());
        try {
            CompletableFuture<RES> future = runnableFunction.apply(input);
            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    logger.logError("Worker thread [ID: " + this.getThreadId() + "] completed task exceptionally. Exception: " + ex.getMessage());
                    resultFuture.completeExceptionally(ex);
                } else {
                    logger.logInfo("Worker thread [ID: " + this.getThreadId() + "] completed task successfully.");
                    resultFuture.complete(result);
                }
                workerThreadDispatcher.removeThreadFromPool(threadId);
            });
        } catch (Exception e) {
            System.err.println("WorkerThread encountered an unexpected error: " + e.getMessage());
            logger.logInfo("Worker thread [ID: " + this.getThreadId() + "] encountered an unexpected error. Exception: " + e.getMessage());
            resultFuture.completeExceptionally(e);
            workerThreadDispatcher.removeThreadFromPool(threadId);
        }
    }

    public String getThreadId() {
        return threadId;
    }

    public void increaseRank() {
        this.rank.incrementAndGet();
    }

    public int getRank() {
        return this.rank.get();
    }
}
