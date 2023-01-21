package com.telepathicgrunt.the_bumblezone.events.client;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;

public record RegisterItemColorEvent(BiConsumer<ItemColor, ItemLike[]> colors, BlockColorProvider blockColors) {

    public static final EventHandler<RegisterItemColorEvent> EVENT = new EventHandler<>();

    public void register(ItemColor color, ItemLike... items) {
        colors.accept(color, items);
    }


    @FunctionalInterface
    public interface BlockColorProvider {

        BlockColor get(Block block);

        default int getColor(BlockState blockState, @Nullable BlockAndTintGetter blockAndTintGetter, @Nullable BlockPos blockPos, int i) {
            return get(blockState.getBlock()).getColor(blockState, blockAndTintGetter, blockPos, i);
        }
    }
}
