package com.telepathicgrunt.the_bumblezone.events.base;

import java.util.ArrayList;
import java.util.List;

public class CancellableEventHandler<T> {

    private final List<CancellableFunction<T>> listeners = new ArrayList<>();

    public CancellableEventHandler() {

    }

    public void addListener(CancellableFunction<T> listener) {
        listeners.add(listener);
    }

    public void addListener(CancellableFunctionNoReturn<T> listener) {
        listeners.add((b, t) -> {
            listener.apply(b, t);
            return false;
        });
    }

    public void removeListener(CancellableFunction<T> listener) {
        listeners.remove(listener);
    }

    public boolean invoke(T event) {
        boolean isCancelled = false;
        for (CancellableFunction<T> listener : listeners) {
            if (listener.apply(isCancelled, event)) {
                isCancelled = true;
            }
        }
        return isCancelled;
    }

    @FunctionalInterface
    public interface CancellableFunction<T> {

        boolean apply(boolean cancelled, T event);
    }

    @FunctionalInterface
    public interface CancellableFunctionNoReturn<T> {

        void apply(boolean cancelled, T event);
    }
    
}
