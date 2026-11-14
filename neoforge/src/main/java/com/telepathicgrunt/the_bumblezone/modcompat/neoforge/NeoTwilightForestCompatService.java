package com.telepathicgrunt.the_bumblezone.modcompat.neoforge;

import com.telepathicgrunt.the_bumblezone.modcompat.TwilightForestCompatService;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class NeoTwilightForestCompatService implements TwilightForestCompatService {
    private static final String ENDER_BOW_ATTACHED_TAG = "twilightforest:ender";

    public boolean getIsEnderArrow(Entity entity) {
        return entity.getPersistentData().getBoolean(ENDER_BOW_ATTACHED_TAG);
    }
}
