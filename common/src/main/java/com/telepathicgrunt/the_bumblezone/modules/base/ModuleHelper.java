package com.telepathicgrunt.the_bumblezone.modules.base;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.Entity;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Optional;

public final class ModuleHelper {

    private ModuleHelper() {}

    @ExpectPlatform
    public static <T extends Module<T>> ModuleHolder<T> createHolder(ModuleSerializer<T> serializer) {
        throw new NotImplementedException("ModuleHelper.registerSerializer() is not implemented on this platform.");
    }

    @ExpectPlatform
    public static <T extends Module<T>> Optional<T> getModule(Entity entity, ModuleHolder<T> holder) {
        throw new NotImplementedException("ModuleHelper.getModule() is not implemented on this platform.");
    }
}
