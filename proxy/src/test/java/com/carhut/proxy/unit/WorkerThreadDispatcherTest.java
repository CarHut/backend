package com.carhut.proxy.unit;

import com.carhut.proxy.dispatcher.WorkerThreadDispatcher;
import com.carhut.proxy.dispatcher.model.WorkerThread;
import com.carhut.proxy.model.UnifiedRESTResponseModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public class WorkerThreadDispatcherTest {

    @Test
    void dispatchThread_functionIsInvalid() throws ExecutionException, InterruptedException {
        CompletableFuture cf = WorkerThreadDispatcher.getInstance().dispatchThread(null);
        Assertions.assertThrowsExactly(ExecutionException.class, cf::get);
    }

    @Test
    void dispatchThread_onlyOneRunningThread() throws ExecutionException, InterruptedException {
        Function fun = Mockito.mock();
        CompletableFuture<UnifiedRESTResponseModel> cf = new CompletableFuture<>();
        Mockito.when(fun.apply(Mockito.any())).thenReturn(cf);
        CompletableFuture<UnifiedRESTResponseModel> returnCf =
                WorkerThreadDispatcher.getInstance().dispatchThread(fun);

        // simulate real time thread work
        Thread.sleep(1000);
        cf.complete(new UnifiedRESTResponseModel("SUCCESS", HttpStatus.OK.value(),
                "Successfully ended thread worker."));

        Assertions.assertEquals("SUCCESS", returnCf.get().getResponseBody());
        Assertions.assertEquals(HttpStatus.OK.value(), returnCf.get().getStatusCode());
        Assertions.assertEquals("Successfully ended thread worker.", returnCf.get().getMessage());
    }

    @Test
    void dispatchThread_overloadDispatcher() throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        Function fun = Mockito.mock();

        List<CompletableFuture<UnifiedRESTResponseModel>> runningCfs = new CopyOnWriteArrayList<>();
        List<CompletableFuture<UnifiedRESTResponseModel>> runningReturnCfs = new CopyOnWriteArrayList<>();

        for (int i = 0; i < 500; i++) {
            CompletableFuture<UnifiedRESTResponseModel> cf = new CompletableFuture<>();
            Mockito.when(fun.apply(Mockito.any())).thenReturn(cf);
            CompletableFuture<UnifiedRESTResponseModel> returnCf =
                    WorkerThreadDispatcher.getInstance().dispatchThread(fun);

            runningReturnCfs.add(returnCf);
            runningCfs.add(cf);
        }

        Field runningThreads = WorkerThreadDispatcher.class.getDeclaredField("runningThreads");
        runningThreads.setAccessible(true);
        Field waitingThreads = WorkerThreadDispatcher.class.getDeclaredField("waitingThreads");
        waitingThreads.setAccessible(true);

        Assertions.assertEquals(500, runningCfs.size());
        Assertions.assertEquals(10, ((List<WorkerThread>) runningThreads.get(null)).size());
        Assertions.assertEquals(490, ((List<WorkerThread>) waitingThreads.get(null)).size());

        runningCfs.forEach(cf -> cf.complete(new UnifiedRESTResponseModel("SUCCESS", HttpStatus.OK.value(),
                "Successfully ended thread worker.")));

        Thread.sleep(1000);

        Assertions.assertEquals(0, ((List<WorkerThread>) runningThreads.get(null)).size());
        Assertions.assertEquals(0, ((List<WorkerThread>) waitingThreads.get(null)).size());

        runningReturnCfs.forEach(returnCf -> {
            try {
                Assertions.assertEquals("SUCCESS", returnCf.get().getResponseBody());
                Assertions.assertEquals(HttpStatus.OK.value(), returnCf.get().getStatusCode());
                Assertions.assertEquals("Successfully ended thread worker.", returnCf.get().getMessage());
            } catch (Exception e) {
                Assertions.fail();
            }
        });
    }


}
