package com.telepathicgrunt.the_bumblezone.client.blocks.blocktintsources;

import com.telepathicgrunt.the_bumblezone.blocks.blockentities.PotionCandleBlockEntity;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PotionCandleBlockTintSource {
    public static BlockTintSource indexOneTintSource() {
        return new BlockTintSource() {
            @Override
            public int color(BlockState state) {
                return -1;
            }

            @Override
            public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof PotionCandleBlockEntity potionCandleBlockEntity) {

                    return potionCandleBlockEntity.getColor();
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
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof PotionCandleBlockEntity potionCandleBlockEntity) {
                    int currentColor = potionCandleBlockEntity.getColor();

                    // Change tint of lit top
                    int red = Math.max((currentColor >> 16 & 255), 40);
                    int green = Math.max((currentColor >> 8 & 255), 10);
                    int blue = Math.max((currentColor & 255), 5);

                    return (Math.min(red + 60, 255) << 16) + (Math.min(green + 30, 255) << 8) + Math.min(blue + 25, 255);
                }

                return color(state);
            }
        };
    }

}
