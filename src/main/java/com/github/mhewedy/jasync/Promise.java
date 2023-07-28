package com.github.mhewedy.jasync;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class Promise<T> {

    public final CompletableFuture<T> future;

    Promise(CompletableFuture<T> future) {
        this.future = future;
    }

    public T await() {
        return Task.await(this);
    }
}
