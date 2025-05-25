package com.telepathicgrunt.the_bumblezone.modcompat.fabric;

import com.telepathicgrunt.the_bumblezone.modcompat.TwilightForestCompatService;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

import java.util.Optional;

public class FabricTwilightForestCompatService implements TwilightForestCompatService {
    @Override
    public Optional<CompoundTag> getPersistentData(Entity entity) {
        // From Porting Lib's implementation
        return entity.saveWithoutId(new CompoundTag()).getCompound("ForgeData");
    }
}
