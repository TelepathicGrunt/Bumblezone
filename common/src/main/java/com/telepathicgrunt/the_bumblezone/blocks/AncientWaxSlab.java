package com.telepathicgrunt.the_bumblezone.blocks;

import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;


public class AncientWaxSlab extends SlabBlock implements AncientWaxBase {

    public AncientWaxSlab() {
        super(Properties.of()
                .mapColor(MapColor.TERRACOTTA_BROWN)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F, 16.0F));
    }
}
