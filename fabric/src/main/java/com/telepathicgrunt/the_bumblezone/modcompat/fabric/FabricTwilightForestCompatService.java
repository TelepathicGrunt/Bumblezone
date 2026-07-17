package com.telepathicgrunt.the_bumblezone.modcompat.fabric;

import com.telepathicgrunt.the_bumblezone.modcompat.TwilightForestCompatService;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

public class FabricTwilightForestCompatService implements TwilightForestCompatService {
    @Override
    public CompoundTag getPersistentData(Entity entity) {
        // From Porting Lib's implementation
        return entity.saveWithoutId(new CompoundTag()).getCompound("ForgeData");
    }
}
