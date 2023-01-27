package com.telepathicgrunt.the_bumblezone.utils;

import java.util.Optional;
import java.util.function.Supplier;

public class LazySupplier<T> implements Supplier<T> {

    private final Supplier<T> supplier;
    private boolean initialized;
    private T value;

    public LazySupplier(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public static <T> LazySupplier<T> of(Supplier<T> supplier) {
        return new LazySupplier<>(supplier);
    }

    @Override
    public T get() {
        if (value == null) {
            value = supplier.get();
            initialized = true;
        }
        return value;
    }

    public Optional<T> getOptional() {
        return initialized ? Optional.ofNullable(value) : Optional.ofNullable(get());
    }
}
