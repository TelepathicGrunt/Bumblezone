package com.telepathicgrunt.the_bumblezone.blocks;

import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;


public class LuminescentWaxChannel extends AxisFacingBlock implements LuminescentWaxBase{

    public LuminescentWaxChannel(MapColor mapColor, LuminescentWaxBase.COLOR color) {
        super(Properties.of()
                .mapColor(mapColor)
                .instrument(NoteBlockInstrument.BASS)
                .lightLevel((blockState) -> color != LuminescentWaxBase.COLOR.NONE ? 0 : 14)
                .strength(2.0F, 16.0F));
    }
}
