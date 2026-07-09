package com.telepathicgrunt.the_bumblezone.events.client;

import com.mojang.serialization.MapCodec;
import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.BlockAndLightGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public record BzRegisterItemColorEvent(BiConsumer<Identifier, MapCodec<? extends ItemTintSource>> colors, BlockColorProvider blockColors) {

    public static final EventHandler<BzRegisterItemColorEvent> EVENT = new EventHandler<>();

    public void register(Identifier identifier, MapCodec<? extends ItemTintSource> mapCodec) {
        colors.accept(identifier, mapCodec);
    }


    @FunctionalInterface
    public interface BlockColorProvider {

        int getColor(BlockState blockState, @Nullable BlockAndLightGetter blockAndTintGetter, @Nullable BlockPos blockPos, int i);
    }
}
