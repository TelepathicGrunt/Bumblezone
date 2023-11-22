package com.telepathicgrunt.the_bumblezone.modules.base.neoforge;

import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHolder;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleSerializer;
import com.telepathicgrunt.the_bumblezone.modules.neoforge.HackyCapabilityManager;
import net.neoforged.neoforge.common.capabilities.Capability;

public record ForgeModuleHolder<T extends Module<T>>(ModuleSerializer<T> serializer, Capability<T> capability) implements ModuleHolder<T> {

    public static <T extends Module<T>> ForgeModuleHolder<T> of(ModuleSerializer<T> serializer) {
        return new ForgeModuleHolder<>(serializer, HackyCapabilityManager.get(serializer.moduleClass()));
    }

    @Override
    public ModuleSerializer<T> getSerializer() {
        return serializer;
    }
}
