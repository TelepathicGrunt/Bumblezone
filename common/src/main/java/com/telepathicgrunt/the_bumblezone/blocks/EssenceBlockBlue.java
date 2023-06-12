package com.telepathicgrunt.the_bumblezone.blocks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.MapColor;


public class EssenceBlockBlue extends EssenceBlock {
    public EssenceBlockBlue() {
        super(Properties.of().mapColor(MapColor.COLOR_BLUE));
    }

    @Override
    public ResourceLocation getArenaNbt() {
        return null;
    }

    @Override
    public long getEventTimeFrame() {
        return 2000;
    }
}
