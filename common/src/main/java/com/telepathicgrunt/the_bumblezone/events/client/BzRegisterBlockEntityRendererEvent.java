package com.telepathicgrunt.the_bumblezone.events.client;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.BiConsumer;

public record BzRegisterBlockEntityRendererEvent<T extends BlockEntity, S extends BlockEntityRenderState>(BiConsumer<BlockEntityType<T>, BlockEntityRendererProvider<T, S>> renderers) {

    public static final EventHandler<BzRegisterBlockEntityRendererEvent<?, ?>> EVENT = new EventHandler<>();

    public void register(BlockEntityType<T> blockEntityType, BlockEntityRendererProvider<T, S> blockEntityRendererProvider) {
        renderers.accept(blockEntityType, blockEntityRendererProvider);
    }
}

