package com.example.socialApp.shared;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class BaseConverter<E, T> {

    protected <U> void convertIfNotNull(Consumer<U> consumer, Supplier<U> supplier) {
        U u = supplier.get();
        if (u != null)
            consumer.accept(u);
    }

    public abstract Function<T, E> toEntity();
    public abstract Function<E, T> toDto();
}
