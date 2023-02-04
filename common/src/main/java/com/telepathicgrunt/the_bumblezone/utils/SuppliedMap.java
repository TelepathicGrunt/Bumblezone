package com.telepathicgrunt.the_bumblezone.utils;

import java.util.Map;
import java.util.function.Supplier;

public class SuppliedMap<K, V> {

    private final Map<K, V> map;
    private Map<Supplier<K>, Supplier<V>> suppliers;

    public SuppliedMap(Map<K, V> backingMap, Map<Supplier<K>, Supplier<V>> backingSupplierMap) {
        this.map = backingMap;
        this.suppliers = backingSupplierMap;
    }

    public void put(Supplier<K> key, Supplier<V> value) {
        if (suppliers == null) {
            map.put(key.get(), value.get());
        } else {
            suppliers.put(key, value);
        }
    }

    private void supply() {
        if (suppliers == null) return;
        for (Map.Entry<Supplier<K>, Supplier<V>> entry : suppliers.entrySet()) {
            map.put(entry.getKey().get(), entry.getValue().get());
        }
        suppliers.clear();
        suppliers = null;
    }

    public V get(K key) {
        supply();
        return map.get(key);
    }

    public V getOrDefault(K key, V defaultValue) {
        supply();
        return map.getOrDefault(key, defaultValue);
    }
}
