package com.telepathicgrunt.the_bumblezone.client.blockentityrenderer.renderstates;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;

import java.util.EnumSet;
import java.util.Set;

public class EssenceBlockEntityRendererState extends BlockEntityRenderState {
    public final Set<Direction> facesToShow = EnumSet.noneOf(Direction.class);
}