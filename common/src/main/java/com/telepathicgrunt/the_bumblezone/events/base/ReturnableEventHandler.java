package com.telepathicgrunt.the_bumblezone.events.base;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ReturnableEventHandler<T, R> {

    private final List<ReturnableFunction<T, R>> listeners = new ArrayList<>();

    public ReturnableEventHandler() {

    }

    public void addListener(ReturnableFunction<T, R> listener) {
        listeners.add(listener);
    }

    public void addListener(Function<T, R> listener) {
        listeners.add((value, event) -> listener.apply(event));
    }

    public void addListener(BiConsumer<R, T> listener) {
        addListener((value, event) -> {
            listener.accept(value, event);
            return null;
        });
    }

    public void removeListener(ReturnableFunction<T, R> listener) {
        listeners.remove(listener);
    }

    public R invoke(T event, R defaultValue) {
        R value = defaultValue;
        for (ReturnableFunction<T, R> listener : listeners) {
            R result = listener.apply(value, event);
            if (result != null) {
                value = result;
            }
        }
        return value;
    }

    public R invoke(T event) {
        return invoke(event, null);
    }

    @FunctionalInterface
    public interface ReturnableFunction<T, R> {

        @Nullable
        R apply(R result, T event);
    }
    
}
