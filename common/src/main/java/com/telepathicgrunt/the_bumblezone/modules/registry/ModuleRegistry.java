package com.telepathicgrunt.the_bumblezone.modules.registry;

import com.telepathicgrunt.the_bumblezone.modules.EntityPosAndDimModule;
import com.telepathicgrunt.the_bumblezone.modules.FlyingSpeedModule;
import com.telepathicgrunt.the_bumblezone.modules.LivingEntityDataModule;
import com.telepathicgrunt.the_bumblezone.modules.PlayerDataModule;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHelper;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHolder;

public class ModuleRegistry {

    public static final ModuleHolder<FlyingSpeedModule> FLYING_SPEED = ModuleHelper.createHolder(FlyingSpeedModule.SERIALIZER);
    public static final ModuleHolder<LivingEntityDataModule> LIVING_ENTITY_DATA = ModuleHelper.createHolder(LivingEntityDataModule.SERIALIZER);
    public static final ModuleHolder<EntityPosAndDimModule> ENTITY_POS_AND_DIM = ModuleHelper.createHolder(EntityPosAndDimModule.SERIALIZER);
    public static final ModuleHolder<PlayerDataModule> PLAYER_DATA = ModuleHelper.createHolder(PlayerDataModule.SERIALIZER);

    public static void register(ModuleRegistrar registrar) {
        registrar.registerLivingEntityModule(FLYING_SPEED, p -> new FlyingSpeedModule(), false);
        registrar.registerLivingEntityModule(LIVING_ENTITY_DATA, p -> new LivingEntityDataModule(), false);
        registrar.registerLivingEntityModule(ENTITY_POS_AND_DIM, p -> new EntityPosAndDimModule(), true);
        registrar.registerPlayerModule(PLAYER_DATA, p -> new PlayerDataModule(), true);
    }
}
