package com.carhut.proxy.threading.models;

import com.carhut.proxy.models.dtos.SerializedResponseDto;
import lombok.Getter;

import java.util.concurrent.CompletableFuture;

@Getter
public class WorkerThread extends Thread {
    private CompletableFuture<SerializedResponseDto> cf;

    public WorkerThread(CompletableFuture<SerializedResponseDto> cf) {
        this.cf = cf;
    }

    @Override
    public void run() {
        // Do some work
//        T object = ;
//        cf.complete(object);
        cf.complete(null);
    }
}
