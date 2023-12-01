package com.telepathicgrunt.the_bumblezone.modules.registry;

import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHolder;

import java.util.function.Supplier;

public interface ModuleRegistrar {

    <T extends Module<T>> void registerPlayerModule(ModuleHolder<T> serializer, Supplier<T> factory, boolean runDataCloneForPlayer);

    <T extends Module<T>> void registerLivingEntityModule(ModuleHolder<T> serializer, Supplier<T> factory, boolean runDataCloneForPlayer);
}
