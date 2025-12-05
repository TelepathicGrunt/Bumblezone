package com.telepathicgrunt.the_bumblezone.modcompat.neoforge;

import com.telepathicgrunt.the_bumblezone.modcompat.TwilightForestCompatService;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

public class NeoTwilightForestCompatService implements TwilightForestCompatService {
    public CompoundTag getPersistentData(Entity entity) {
        return entity.getPersistentData();
    }
}
