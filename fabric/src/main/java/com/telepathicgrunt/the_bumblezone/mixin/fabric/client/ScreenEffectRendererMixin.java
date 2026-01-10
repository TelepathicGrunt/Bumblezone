package com.telepathicgrunt.the_bumblezone.mixin.fabric.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.events.client.BzBlockRenderedOnScreenEvent;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public abstract class ScreenEffectRendererMixin {

    @Inject(method = "renderScreenEffect(ZF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ScreenEffectRenderer;renderTex(Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;)V"),
            cancellable = true)
    private void bumblezone$blockRenderedOnScreenEvent(boolean bl, float f, CallbackInfo ci, @Local PoseStack poseStack, @Local Player player, @Local BlockState blockState) {
        if (BzBlockRenderedOnScreenEvent.EVENT.invoke(new BzBlockRenderedOnScreenEvent(player, poseStack, BzBlockRenderedOnScreenEvent.Type.BLOCK, blockState, player.blockPosition()))) {
            ci.cancel();
        }
    }
}
