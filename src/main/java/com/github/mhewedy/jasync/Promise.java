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

    private Promise(CompletableFuture<T> future) {
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

    public static <T> Promise<T> async(Supplier<T> s) {
        return async(s, null);
    }

    public static Promise<?> async(Runnable r) {
        return async(r, null);
    }

    public static <T> Promise<T> async(Supplier<T> s, Executor executor) {
        log.trace("submit job on executor: {}", executor);
        CompletableFuture<T> cf;

        if (executor == null) {
            Executor springTaskExecutor = SpringPromise.getSpringTaskExecutor();

            if (springTaskExecutor != null) {
                log.trace("found spring task executor");
                cf = CompletableFuture.supplyAsync(s, springTaskExecutor);
            } else {
                cf = CompletableFuture.supplyAsync(s);
            }

        } else {
            cf = CompletableFuture.supplyAsync(s, executor);
        }

        return new Promise<>(cf);
    }

    public static Promise<?> async(Runnable r, Executor executor) {
        log.trace("submit job on executor: {}", executor);
        CompletableFuture<?> cf;

        if (executor == null) {
            Executor springTaskExecutor = SpringPromise.getSpringTaskExecutor();

            if (springTaskExecutor != null) {
                log.trace("found spring task executor");
                cf = CompletableFuture.runAsync(r, springTaskExecutor);
            } else {
                cf = CompletableFuture.runAsync(r);
            }

        } else {
            cf = CompletableFuture.runAsync(r, executor);
        }

        return new Promise<>(cf);
    }

    public static <T> T await(Promise<T> promise) {
        try {
            return promise.future.join();
        } catch (CompletionException e) {
            throw getCauseOrRuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void await(Promise<?>... promises) {
        try {
            CompletableFuture<?> all = CompletableFuture.allOf(
                    Arrays.stream(promises)
                            .filter(Objects::nonNull)
                            .map(it -> it.future)
                            .toArray(CompletableFuture<?>[]::new)
            );
            //https://stackoverflow.com/a/51628402/171950
            Arrays.stream(promises)
                    .filter(Objects::nonNull)
                    .map(it -> it.future)
                    .forEach(f -> f.exceptionally(e -> {
                        log.trace("a future has failed due to '{}'. canceling all other futures.", e.toString());
                        all.completeExceptionally(e);
                        return null;
                    }));

            all.join();
        } catch (CompletionException e) {
            throw getCauseOrRuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static RuntimeException getCauseOrRuntimeException(CompletionException e) {
        if (e.getCause() instanceof RuntimeException) {
            return (RuntimeException) e.getCause();
        } else {
            return new RuntimeException(e.getCause());
        }
    }
}
