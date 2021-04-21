package com.telepathicgrunt.bumblezone.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;

public class ProperFacingBlock extends FacingBlock {
    protected ProperFacingBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.get(FACING)));
    }
}
