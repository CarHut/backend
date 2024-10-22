package ratingservice.repository.resourceprovider;

import com.carhut.commons.resource.DatabaseResourceManager;
import com.carhut.commons.resource.DefaultDatabaseResourceQueueManager;
import com.carhut.commons.resource.ResourceHolderThread;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;

public class SellerRatingDatabaseResourceManager<RESOURCE> implements DatabaseResourceManager<RESOURCE> {

    private static SellerRatingDatabaseResourceManager manager = null;
    private final LinkedBlockingQueue<ResourceHolderThread<RESOURCE>> taskQueue;
    private final DefaultDatabaseResourceQueueManager<RESOURCE> queueManager;

    public static SellerRatingDatabaseResourceManager getInstance() {
        if (manager == null) {
            manager = new SellerRatingDatabaseResourceManager();
        }
        return manager;
    }

    private SellerRatingDatabaseResourceManager() {
        this.taskQueue = new LinkedBlockingQueue<>();
        this.queueManager = new DefaultDatabaseResourceQueueManager<>(taskQueue, 10000L);
        this.queueManager.start();
    }

    public synchronized CompletableFuture<RESOURCE> acquireDatabaseResource(
            Function<Void, RESOURCE> function) {
        CompletableFuture<RESOURCE> cf = new CompletableFuture<>();
        ResourceHolderThread<RESOURCE> resourceWorker = new ResourceHolderThread<>(generateThreadId(), function,
                null, cf);
        taskQueue.add(resourceWorker);
        return cf;
    }

    private synchronized String generateThreadId() {
        return UUID.randomUUID().toString();
    }

}
