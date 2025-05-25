package com.telepathicgrunt.the_bumblezone.modules.base.forge;

import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHolder;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleSerializer;
import net.minecraft.world.entity.Entity;

import java.util.Optional;

public class ModuleHelperImpl {
    public static <T extends Module<T>> ModuleHolder<T> createHolder(ModuleSerializer<T> serializer) {
        return ForgeModuleHolder.of(serializer);
    }

    public static <T extends Module<T>> Optional<T> getModule(Entity entity, ModuleHolder<T> holder) {
        if (holder instanceof ForgeModuleHolder<T> forgeHolder) {
            return entity.getCapability(forgeHolder.capability()).resolve();
        }
        return Optional.empty();
    }
}
