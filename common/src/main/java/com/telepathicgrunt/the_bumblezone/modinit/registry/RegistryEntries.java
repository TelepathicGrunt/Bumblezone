package com.telepathicgrunt.the_bumblezone.modinit.registry;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class RegistryEntries<T> {

    private final List<RegistryEntry<T>> entries = new ArrayList<>();

    public <I extends T> RegistryEntry<I> add(RegistryEntry<I> entry) {
        //noinspection unchecked
        entries.add((RegistryEntry<T>) entry);
        return entry;
    }

    public List<RegistryEntry<T>> getEntries() {
        return ImmutableList.copyOf(entries);
    }

}