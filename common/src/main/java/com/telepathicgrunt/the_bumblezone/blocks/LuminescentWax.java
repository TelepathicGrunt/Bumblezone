package com.telepathicgrunt.the_bumblezone.blocks;

import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;


public class LuminescentWax extends AxisFacingBlock {

    public LuminescentWax(MapColor mapColor) {
        super(Properties.of()
                .mapColor(mapColor)
                .instrument(NoteBlockInstrument.BASS)
                .lightLevel((blockState) -> 14)
                .strength(2.0F, 16.0F));
    }
}
