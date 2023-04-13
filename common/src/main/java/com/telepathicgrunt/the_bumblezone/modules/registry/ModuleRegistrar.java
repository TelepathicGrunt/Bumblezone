package com.telepathicgrunt.the_bumblezone.modules.registry;

import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleFactory;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public interface ModuleRegistrar {

    <T extends Module<T>> void registerPlayerModule(ModuleHolder<T> serializer, ModuleFactory<Player, T> factory, boolean runDataCloneForPlayer);

    <T extends Module<T>> void registerLivingEntityModule(ModuleHolder<T> serializer, ModuleFactory<LivingEntity, T> factory, boolean runDataCloneForPlayer);
}
