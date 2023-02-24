package com.telepathicgrunt.the_bumblezone.modcompat.fabricbase;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

public class TwilightForestCompatImpl {
    public static CompoundTag getPersistentData(Entity entity) {
        return entity.saveWithoutId(new CompoundTag());
    }
}
