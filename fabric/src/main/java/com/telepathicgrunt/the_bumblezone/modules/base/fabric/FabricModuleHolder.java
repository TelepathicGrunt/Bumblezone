package com.telepathicgrunt.the_bumblezone.modules.base.fabric;

import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHolder;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleSerializer;
import com.telepathicgrunt.the_bumblezone.modules.fabric.ModuleComponent;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;

@SuppressWarnings("rawtypes")
public record FabricModuleHolder<T extends Module<T>>(ModuleSerializer<T> serializer, ComponentKey<ModuleComponent> key) implements ModuleHolder<T> {

    public static <T extends Module<T>> FabricModuleHolder<T> of(ModuleSerializer<T> serializer) {
        return new FabricModuleHolder<>(serializer, ComponentRegistry.getOrCreate(serializer.id(), ModuleComponent.class));
    }

    @Override
    public ModuleSerializer<T> getSerializer() {
        return serializer;
    }
}
