package com.telepathicgrunt.the_bumblezone.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;

public class ProperFacingBlock extends DirectionalBlock {

    public static final MapCodec<ProperFacingBlock> CODEC = Block.simpleCodec(ProperFacingBlock::new);

    protected ProperFacingBlock(Properties settings) {
        super(settings);
    }

    @Override
    public MapCodec<? extends ProperFacingBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }
}
