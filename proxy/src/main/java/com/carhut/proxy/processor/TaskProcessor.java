package com.carhut.proxy.processor;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * Interface pattern for classes processing some sort of task.
 * @param <RES> Type of result
 * @param <INP> Type of input
 */
public interface TaskProcessor<INP, RES> {
    /**
     * Processes request and returns response.
     * @param inputObject input of type <INP> that will be processed
     * @return response of type <RES>
     */
    RES processRequest(INP inputObject);

    Function<Void, RES> buildFunctionToExecute(INP input);
}
