package com.carhut.commons.resource;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class ResourceHolderThread<RESOURCEOBJECT> extends Thread implements Runnable {

    private final Function<Void, RESOURCEOBJECT> runnableFunction;
    private final Void input;
    private final CompletableFuture<RESOURCEOBJECT> resultFuture;
    private final String threadId;

    public ResourceHolderThread(String threadId, Function<Void, RESOURCEOBJECT> runnableFunction,
                                Void input, CompletableFuture<RESOURCEOBJECT> resultFuture) {
        this.runnableFunction = runnableFunction;
        this.input = input;
        this.resultFuture = resultFuture;
        this.threadId = threadId;
        this.setName("ResourceHolderThread #" + threadId);
    }

    @Override
    public void run() {
        try {
            RESOURCEOBJECT resourceobject = runnableFunction.apply(input);
            resultFuture.complete(resourceobject);
        }
        catch (Exception e) {
            System.out.println("Resource holder thread encountered an unexpected error: " + e.getMessage());
            resultFuture.completeExceptionally(e);
        }
    }

    public String getThreadId() {
        return threadId;
    }

    public CompletableFuture<RESOURCEOBJECT> getResultFuture() {
        return resultFuture;
    }
}
