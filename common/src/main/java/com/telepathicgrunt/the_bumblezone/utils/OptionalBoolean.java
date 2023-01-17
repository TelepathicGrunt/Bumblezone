package com.telepathicgrunt.the_bumblezone.utils;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;

import java.util.NoSuchElementException;
import java.util.function.BooleanSupplier;

/**
 * This class is like an Optional but for booleans.
 * This is used to not have to use Optional<Boolean> which uses a boxed boolean.
 */
public class OptionalBoolean {

    private static final OptionalBoolean EMPTY = new OptionalBoolean();
    private static final OptionalBoolean TRUE = new OptionalBoolean();
    private static final OptionalBoolean FALSE = new OptionalBoolean();

    private final boolean value;
    private final boolean isPresent;

    private OptionalBoolean(boolean value) {
        this.value = value;
        this.isPresent = true;
    }

    private OptionalBoolean() {
        this.value = false;
        this.isPresent = false;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public boolean isEmpty() {
        return !isPresent();
    }

    public void ifPresent(BooleanConsumer consumer) {
        if (isPresent()) {
            consumer.accept(value);
        }
    }

    public boolean orElseGet(BooleanSupplier supplier) {
        return isPresent() ? value : supplier.getAsBoolean();
    }

    public boolean get() {
        if (isEmpty()) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    public static OptionalBoolean of(boolean value) {
        return new OptionalBoolean(value);
    }

    public static OptionalBoolean empty() {
        return EMPTY;
    }

    public static OptionalBoolean ofTrue() {
        return TRUE;
    }

    public static OptionalBoolean ofFalse() {
        return FALSE;
    }
}
