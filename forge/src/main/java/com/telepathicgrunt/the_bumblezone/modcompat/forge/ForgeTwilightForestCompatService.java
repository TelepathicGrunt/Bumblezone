package com.telepathicgrunt.the_bumblezone.modcompat.forge;

import com.telepathicgrunt.the_bumblezone.modcompat.TwilightForestCompatService;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

public class ForgeTwilightForestCompatService implements TwilightForestCompatService {
    public CompoundTag getPersistentData(Entity entity) {
        return entity.getPersistentData();
    }
}
