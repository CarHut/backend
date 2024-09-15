package com.carhut.proxy.threading.dispatchers;

import com.carhut.proxy.models.dtos.SerializedResponseDto;
import com.carhut.proxy.threading.models.WorkerThread;

import java.util.concurrent.CompletableFuture;

public class DispatcherManager {

    public static CompletableFuture<SerializedResponseDto> dispatchAndStartWorker() {
        CompletableFuture<SerializedResponseDto> cf = new CompletableFuture<>();
        WorkerThread workerThread = new WorkerThread(cf);
        workerThread.start();
        return cf;
    }


}
