package com.carhut.proxy;

import com.carhut.proxy.dispatcher.WorkerThreadDispatcher;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public class SimulateDispatcherOverload {

    @Test
    void simulateFiftyRequestInQuickSuccession() {
        int numberOfRequests = 500;
        WorkerThreadDispatcher<Void, String> dispatcher = WorkerThreadDispatcher.getInstance();

        Function<Void, CompletableFuture<String>> testFunction = (unused) -> CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(50);
                return "Request completed by " + Thread.currentThread().getName();
            } catch (InterruptedException e) {
                return "Request failed";
            }
        });

        CompletableFuture<String>[] futures = new CompletableFuture[numberOfRequests];

        for (int i = 0; i < numberOfRequests; i++) {
            futures[i] = dispatcher.dispatchThread(testFunction);
        }

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures);
        allOf.join();

        for (int i = 0; i < numberOfRequests; i++) {
            try {
                System.out.println("Response: " + futures[i].get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        System.out.println("All requests completed.");
    }


}
