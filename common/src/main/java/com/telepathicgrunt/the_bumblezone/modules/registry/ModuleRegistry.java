package com.telepathicgrunt.the_bumblezone.modules.registry;

import com.telepathicgrunt.the_bumblezone.modules.EntityPosAndDimModule;
import com.telepathicgrunt.the_bumblezone.modules.FlyingSpeedModule;
import com.telepathicgrunt.the_bumblezone.modules.LivingEntityDataModule;
import com.telepathicgrunt.the_bumblezone.modules.PlayerDataModule;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHolder;

public class ModuleRegistry {

    public static final ModuleHolder<FlyingSpeedModule> FLYING_SPEED = new ModuleHolder<>(FlyingSpeedModule.ID, FlyingSpeedModule.CODEC, FlyingSpeedModule::new);
    public static final ModuleHolder<LivingEntityDataModule> LIVING_ENTITY_DATA = new ModuleHolder<>(LivingEntityDataModule.ID, LivingEntityDataModule.CODEC, LivingEntityDataModule::new);
    public static final ModuleHolder<EntityPosAndDimModule> ENTITY_POS_AND_DIM = new ModuleHolder<>(EntityPosAndDimModule.ID, EntityPosAndDimModule.CODEC, EntityPosAndDimModule::new);
    public static final ModuleHolder<PlayerDataModule> PLAYER_DATA = new ModuleHolder<>(PlayerDataModule.ID, PlayerDataModule.CODEC, PlayerDataModule::new);

    public static void register(ModuleRegistrar registrar) {
        registrar.registerLivingEntityModule(FLYING_SPEED, false);
        registrar.registerLivingEntityModule(LIVING_ENTITY_DATA, false);
        registrar.registerLivingEntityModule(ENTITY_POS_AND_DIM, true);
        registrar.registerPlayerModule(PLAYER_DATA, true);
    }
}
