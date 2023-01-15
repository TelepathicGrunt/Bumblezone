package com.telepathicgrunt.the_bumblezone.utils;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class LazyOptionalSupplier<T> implements Supplier<T> {

    private final Supplier<T> supplier;
    private T value;

    public LazyOptionalSupplier(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public static <T> LazyOptionalSupplier<T> of(Supplier<T> supplier) {
        return new LazyOptionalSupplier<>(supplier);
    }

    @Override
    public T get() {
        if (value == null) {
            value = supplier.get();
        }
        return value;
    }

    public Optional<T> getOptional() {
        return Optional.ofNullable(value);
    }
}
