package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

import java.util.Optional;

public interface TwilightForestCompatService {
    TwilightForestCompatService INSTANCE = GeneralUtils.loadService(TwilightForestCompatService.class);

    Optional<CompoundTag> getPersistentData(Entity entity);
}
