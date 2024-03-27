package com.telepathicgrunt.the_bumblezone.fluids.fabric;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.telepathicgrunt.the_bumblezone.client.rendering.fluids.HoneyFluidRendering;
import com.telepathicgrunt.the_bumblezone.fluids.base.ClientFluidProperties;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

public class HoneyFluidRenderHandler extends SimpleFluidRenderHandler {

    protected final TextureAtlasSprite[] sprites;
    protected final ResourceLocation diagonalTexture;

    public HoneyFluidRenderHandler(ClientFluidProperties properties) {
        super(properties.still(), properties.flowing(), properties.overlay());
        this.diagonalTexture = properties.diagonal();
        this.sprites = new TextureAtlasSprite[4];
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

    @Override
    public TextureAtlasSprite[] getFluidSprites(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state) {
        return sprites;
    }

    @Override
    public void reloadTextures(TextureAtlas textureAtlas) {
        sprites[0] = textureAtlas.getSprite(stillTexture);
        sprites[1] = textureAtlas.getSprite(flowingTexture);
        sprites[2] = textureAtlas.getSprite(overlayTexture);
        sprites[3] = textureAtlas.getSprite(diagonalTexture);
    }
}
