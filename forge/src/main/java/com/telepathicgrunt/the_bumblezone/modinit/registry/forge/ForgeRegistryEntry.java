package com.telepathicgrunt.the_bumblezone.modinit.registry.forge;

import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;

public class ForgeRegistryEntry<T> implements RegistryEntry<T> {

    private final RegistryObject<T> object;

    public ForgeRegistryEntry(RegistryObject<T> object) {
        this.object = object;
    }

    @Override
    public T get() {
        return object.get();
    }

    @Override
    public ResourceLocation getId() {
        return object.getId();
    }
}
