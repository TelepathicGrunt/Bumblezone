package com.telepathicgrunt.the_bumblezone.modcompat.neoforge;

import com.telepathicgrunt.the_bumblezone.modcompat.TwilightForestCompatService;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

import java.util.Optional;

public class NeoTwilightForestCompatService implements TwilightForestCompatService {
    public Optional<CompoundTag> getPersistentData(Entity entity) {
        return Optional.of(entity.getPersistentData());
    }
}
