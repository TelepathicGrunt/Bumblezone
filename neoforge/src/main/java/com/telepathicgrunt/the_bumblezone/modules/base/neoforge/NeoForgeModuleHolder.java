package com.telepathicgrunt.the_bumblezone.modules.base.neoforge;

import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHolder;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleSerializer;
import net.minecraft.resources.ResourceLocation;

public record NeoForgeModuleHolder<T extends Module<T>>(ModuleSerializer<T> serializer, ResourceLocation id) implements ModuleHolder<T> {

    public static <T extends Module<T>> NeoForgeModuleHolder<T> of(ModuleSerializer<T> serializer) {
        return new NeoForgeModuleHolder<>(serializer, serializer.id());
    }

    @Override
    public ModuleSerializer<T> getSerializer() {
        return serializer;
    }
}
