package com.telepathicgrunt.the_bumblezone.modinit.registry.quilt;

import com.telepathicgrunt.the_bumblezone.modinit.registry.BasicRegistryEntry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntries;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.function.Supplier;

public class QuiltResourcefulRegistry<T> implements ResourcefulRegistry<T> {

    private final RegistryEntries<T> entries = new RegistryEntries<>();
    private final Registry<T> registry;
    private final String id;

    public QuiltResourcefulRegistry(Registry<T> registry, String id) {
        this.registry = registry;
        this.id = id;
    }

    @Override
    public <I extends T> RegistryEntry<I> register(String id, Supplier<I> supplier) {
        I value = Registry.register(registry, id, supplier.get());
        return entries.add(new BasicRegistryEntry<>(new ResourceLocation(this.id, id), value));
    }

    @Override
    public Collection<RegistryEntry<T>> getEntries() {
        return this.entries.getEntries();
    }

    @Override
    public void init() {
        // NO-OP
    }
}
