package com.telepathicgrunt.the_bumblezone.modules.registry;

import com.telepathicgrunt.the_bumblezone.modules.EntityMiscModule;
import com.telepathicgrunt.the_bumblezone.modules.EntityPosAndDimModule;
import com.telepathicgrunt.the_bumblezone.modules.FlyingSpeedModule;
import com.telepathicgrunt.the_bumblezone.modules.NeurotoxinsMissedCounterModule;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHelper;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHolder;

public class ModuleRegistry {

    public static final ModuleHolder<FlyingSpeedModule> FLYING_SPEED = ModuleHelper.createHolder(FlyingSpeedModule.SERIALIZER);
    public static final ModuleHolder<NeurotoxinsMissedCounterModule> NEUROTOXINS_MISSED = ModuleHelper.createHolder(NeurotoxinsMissedCounterModule.SERIALIZER);
    public static final ModuleHolder<EntityPosAndDimModule> ENTITY_POS_AND_DIM = ModuleHelper.createHolder(EntityPosAndDimModule.SERIALIZER);
    public static final ModuleHolder<EntityMiscModule> ENTITY_MISC = ModuleHelper.createHolder(EntityMiscModule.SERIALIZER);

    public static void register(ModuleRegistrar registrar) {
        registrar.registerLivingEntityModule(FLYING_SPEED, p -> new FlyingSpeedModule(), false);
        registrar.registerLivingEntityModule(NEUROTOXINS_MISSED, p -> new NeurotoxinsMissedCounterModule(), false);
        registrar.registerLivingEntityModule(ENTITY_POS_AND_DIM, p -> new EntityPosAndDimModule(), true);
        registrar.registerPlayerModule(ENTITY_MISC, p -> new EntityMiscModule(), true);
    }
}
