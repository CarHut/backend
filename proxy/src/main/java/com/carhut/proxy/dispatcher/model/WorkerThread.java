package com.carhut.proxy.dispatcher.model;

import com.carhut.proxy.dispatcher.WorkerThreadDispatcher;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class WorkerThread<INP, RES> extends Thread implements Runnable {

    private final Function<INP, CompletableFuture<RES>> runnableFunction;
    private final INP input;
    private final CompletableFuture<RES> resultFuture;
    private final String threadId;
    private int rank;

    public WorkerThread(String threadId, Function<INP, CompletableFuture<RES>> runnableFunction, INP input, CompletableFuture<RES> resultFuture) {
        this.runnableFunction = runnableFunction;
        this.input = input;
        this.resultFuture = resultFuture;
        this.threadId = threadId;
        this.rank = 0;
    }

    @Override
    public void run() {
        try {
            // Execute the runnable function
            CompletableFuture<RES> future = runnableFunction.apply(input);

            // Complete the resultFuture when the CompletableFuture from runnableFunction completes
            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    resultFuture.completeExceptionally(ex);
                } else {
                    resultFuture.complete(result);
                }
//                 Clean up the running thread list
//                synchronized (WorkerThreadDispatcher.class) {
//                    WorkerThreadDispatcher.getInstance().terminateThreadAndNotifyOthers(threadId);
//                }
            });

        } catch (Exception e) {
            System.err.println("WorkerThread encountered an unexpected error: " + e.getMessage());
            e.printStackTrace();
            resultFuture.completeExceptionally(e); // Handle exception and complete the future
        }
    }

    public String getThreadId() {
        return threadId;
    }

    public void increaseRank() {
        this.rank++;
    }

    public int getRank() {
        return this.rank;
    }
}
