package com.telepathicgrunt.the_bumblezone.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;


public class AncientWaxStairs extends StairBlock {

    public AncientWaxStairs(BlockState state) {
        super(state, Properties.of()
                .mapColor(MapColor.TERRACOTTA_BROWN)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F, 16.0F));
    }
}
