package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.telepathicgrunt.the_bumblezone.client.rendering.fluids.SugarWaterClientProperties;
import com.telepathicgrunt.the_bumblezone.fluids.HoneyFluidBlock;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LiquidBlockRenderer.class)
public class LiquidBlockRendererMixin {
    @ModifyExpressionValue(method = "tesselate(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/material/FluidState;)V",
            at = @At(value = "FIELD",
                    target = "Lnet/minecraft/client/renderer/block/LiquidBlockRenderer;waterOverlay:Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;"),
            require = 0)
    private TextureAtlasSprite bumblezone$dontRenderBackwardsFaceForSugarWater(
            TextureAtlasSprite waterOverlay,
            BlockAndTintGetter blockAndTintGetter,
            BlockPos blockPos,
            VertexConsumer vertexConsumer,
            BlockState blockState,
            FluidState fluidState,
            @Local(ordinal = 0) TextureAtlasSprite[] textureAtlasSprites)
    {
        if (fluidState.is(BzTags.SUGAR_WATER_FLUID) && textureAtlasSprites[2] != null) {
            return textureAtlasSprites[2];
        }

        return waterOverlay;
    }
}