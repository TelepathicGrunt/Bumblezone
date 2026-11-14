package com.telepathicgrunt.the_bumblezone.modcompat.neoforge;

import com.telepathicgrunt.the_bumblezone.modcompat.TwilightForestCompatService;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

import java.util.Optional;

public class NeoTwilightForestCompatService implements TwilightForestCompatService {
    private static final String ENDER_BOW_ATTACHED_TAG = "twilightforest:ender";

    public boolean getEnderArrowData(Entity entity) {
        return Optional.of(entity.getPersistentData()).orElse(new CompoundTag()).getBoolean(ENDER_BOW_ATTACHED_TAG).orElse(false) ;
    }
}
