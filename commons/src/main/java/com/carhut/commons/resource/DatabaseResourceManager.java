package com.carhut.commons.resource;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface DatabaseResourceManager<T> {
    CompletableFuture<T> acquireDatabaseResource(Function<Void, T> fun);
}
