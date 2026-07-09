package com.telepathicgrunt.the_bumblezone.client.blocks.blocktintsources;

import com.telepathicgrunt.the_bumblezone.blocks.blockentities.InfinityBarrierBlockEntity;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class InfinityBarrierBlockTintSource {
    public static BlockTintSource indexOneTintSource() {
        return new BlockTintSource() {
            @Override
            public int color(BlockState state) {
                return -1;
            }

            @Override
            public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
                if (level != null) {
                    BlockEntity blockEntity = level.getBlockEntity(pos);
                    if (blockEntity instanceof InfinityBarrierBlockEntity infinityBarrierBlockEntity) {
                        return infinityBarrierBlockEntity.getPrimaryColor();
                    }
                }
                return color(state);
            }
        };
    }

    public static BlockTintSource indexTwoTintSource() {
        return new BlockTintSource() {
            @Override
            public int color(BlockState state) {
                return -1;
            }

            @Override
            public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
                if (level != null) {
                    BlockEntity blockEntity = level.getBlockEntity(pos);
                    if (blockEntity instanceof InfinityBarrierBlockEntity infinityBarrierBlockEntity) {
                            return infinityBarrierBlockEntity.getSecondaryColor();
                    }
                }
                return color(state);
            }
        };
    }
}
