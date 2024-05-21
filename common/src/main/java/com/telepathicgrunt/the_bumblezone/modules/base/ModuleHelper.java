package com.telepathicgrunt.the_bumblezone.modules.base;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Contract;

import java.util.Optional;

public final class ModuleHelper {

    private ModuleHelper() {}

    @ExpectPlatform
    @Contract(pure = true)
    public static <T extends Module<T>> Optional<T> getModule(Entity entity, ModuleHolder<T> moduleHolder) {
        throw new NotImplementedException("ModuleHelper.getModule() is not implemented on this platform.");
    }
}
