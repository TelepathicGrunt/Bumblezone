package com.telepathicgrunt.the_bumblezone.modules.base.fabric;

import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHolder;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleSerializer;
import com.telepathicgrunt.the_bumblezone.modules.fabric.ModuleComponent;
import net.minecraft.world.entity.Entity;

import java.util.Optional;

public class ModuleHelperImpl {
    public static <T extends Module<T>> ModuleHolder<T> createHolder(ModuleSerializer<T> serializer) {
        return FabricModuleHolder.of(serializer);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Module<T>> Optional<T> getModule(Entity entity, ModuleHolder<T> holder) {
        if (holder instanceof FabricModuleHolder<T> fabricHolder) {
            ModuleComponent<T> component = fabricHolder.key().getNullable(entity);
            return Optional.ofNullable(component).map(ModuleComponent::module);
        }
        return Optional.empty();
    }
}
