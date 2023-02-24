package com.telepathicgrunt.the_bumblezone.modcompat.forge;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

public class TwilightForestCompatImpl {
    public static CompoundTag getPersistentData(Entity entity) {
        return entity.getPersistentData();
    }
}
