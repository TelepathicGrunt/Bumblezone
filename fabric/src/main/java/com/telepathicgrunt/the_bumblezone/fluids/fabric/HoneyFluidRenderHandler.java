package com.telepathicgrunt.the_bumblezone.fluids.fabric;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.telepathicgrunt.the_bumblezone.client.rendering.HoneyFluidRendering;
import com.telepathicgrunt.the_bumblezone.fluids.HoneyFluid;
import com.telepathicgrunt.the_bumblezone.fluids.HoneyFluidBlock;
import com.telepathicgrunt.the_bumblezone.fluids.base.ClientFluidProperties;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.impl.client.rendering.fluid.FluidRenderHandlerRegistryImpl;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HoneyFluidRenderHandler extends SimpleFluidRenderHandler {

    public HoneyFluidRenderHandler(ClientFluidProperties properties) {
        super(properties.still(), properties.flowing(), properties.overlay());
    }

    @Override
    public void renderFluid(BlockPos blockPos, BlockAndTintGetter level, VertexConsumer vertexConsumer, BlockState blockState, FluidState fluidState) {
        if (fluidState.is(BzTags.SPECIAL_HONEY_LIKE)) {
            HoneyFluidRendering.renderSpecialHoneyFluid(blockPos, level, vertexConsumer, blockState, fluidState, sprites);
        }
        else {
            super.renderFluid(blockPos, level, vertexConsumer, blockState, fluidState);
        }
    }
}
