package com.telepathicgrunt.the_bumblezone.modcompat.fabricbase;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

public class TwilightForestCompatImpl {
    public static CompoundTag getPersistentData(Entity entity) {
        // From Porting Lib's implementation
        return entity.saveWithoutId(new CompoundTag()).getCompound("ForgeData");
    }
}
