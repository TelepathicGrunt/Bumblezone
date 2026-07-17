package com.telepathicgrunt.the_bumblezone.modules.base;

import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.world.entity.Entity;

import java.util.Optional;

public interface ModuleHelperService {
    ModuleHelperService INSTANCE = GeneralUtils.loadService(ModuleHelperService.class);

    <T extends Module<T>> ModuleHolder<T> createHolder(ModuleSerializer<T> serializer);

    <T extends Module<T>> Optional<T> getModule(Entity entity, ModuleHolder<T> holder);
}
