package com.telepathicgrunt.the_bumblezone.mixin.forge.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.telepathicgrunt.the_bumblezone.client.rendering.fluids.HoneyFluidRendering;
import com.telepathicgrunt.the_bumblezone.fluids.HoneyFluid;
import com.telepathicgrunt.the_bumblezone.fluids.base.ClientFluidProperties;
import com.telepathicgrunt.the_bumblezone.fluids.forge.BzClientFluidExtension;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LiquidBlockRenderer.class, priority = 1200)
public class LiquidBlockRendererMixin {


    @Inject(method = "tesselate(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/material/FluidState;)V",
            at = @At(value = "HEAD"), cancellable = true)
    private void bumblezone$honeyFluidSpecialRendering(BlockAndTintGetter blockAndTintGetter, BlockPos blockPos, VertexConsumer vertexConsumer, BlockState blockState, FluidState fluidState, CallbackInfo ci) {
        if (fluidState.is(BzTags.SPECIAL_HONEY_LIKE) ) {
            ClientFluidProperties props = ClientFluidProperties.get(ForgeRegistries.FLUID_TYPES.get().getKey(fluidState.getType().getFluidType()));
            if (props != null) {
                TextureAtlasSprite[] fluidSpritesOriginal = ForgeHooksClient.getFluidSprites(blockAndTintGetter, blockPos, fluidState);
                TextureAtlasSprite[] fluidSprites = new TextureAtlasSprite[4];
                fluidSprites[0] = fluidSpritesOriginal[0];
                fluidSprites[1] = fluidSpritesOriginal[1];
                fluidSprites[2] = fluidSpritesOriginal[2];
                fluidSprites[3] = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(props.diagonal());
                HoneyFluidRendering.renderSpecialHoneyFluid(blockPos, blockAndTintGetter, vertexConsumer, blockState, fluidState, fluidSprites);
                ci.cancel();
            }
        }
    }

    //////////////////////////////////////////

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
        int textureAtlasSpriteCount = textureAtlasSprites.length;
        if (fluidState.is(BzTags.SUGAR_WATER_FLUID) && textureAtlasSpriteCount > 0 &&  textureAtlasSprites[textureAtlasSpriteCount - 1] != null) {
            return textureAtlasSprites[textureAtlasSpriteCount - 1];
        }

        return waterOverlay;
    }
}