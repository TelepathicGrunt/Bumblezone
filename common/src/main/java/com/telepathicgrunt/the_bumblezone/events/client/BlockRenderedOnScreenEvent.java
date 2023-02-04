package com.telepathicgrunt.the_bumblezone.events.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.events.base.CancellableEventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public record BlockRenderedOnScreenEvent(Player player, PoseStack stack, Type type, BlockState state, BlockPos pos) {

    public static final CancellableEventHandler<BlockRenderedOnScreenEvent> EVENT = new CancellableEventHandler<>();

    public enum Type {
        FIRE,
        BLOCK,
        WATER
    }
}
