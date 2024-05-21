package com.telepathicgrunt.the_bumblezone.modules.registry;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHolder;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public interface ModuleRegistrar {

    <T extends Module<T>> void registerPlayerModule(ModuleHolder<T> moduleHolder, boolean runDataCloneForPlayer);

    <T extends Module<T>> void registerLivingEntityModule(ModuleHolder<T> moduleHolder, boolean runDataCloneForPlayer);
}
