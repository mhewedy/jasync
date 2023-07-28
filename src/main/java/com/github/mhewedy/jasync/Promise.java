package com.github.mhewedy.jasync;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

@Slf4j
public class Promise<T> {

    public final CompletableFuture<T> future;

    Promise(CompletableFuture<T> future) {
        this.future = future;
    }

    /**
     * Delegate for {@link CompletableFuture#get()} that wrap {@link InterruptedException} and {@link ExecutionException}
     * into a {@link RuntimeException}
     *
     * @see CompletableFuture#get()
     */
    public T get() {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
