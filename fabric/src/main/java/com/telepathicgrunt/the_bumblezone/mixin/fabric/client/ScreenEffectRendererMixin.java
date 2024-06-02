package com.telepathicgrunt.the_bumblezone.mixin.fabric.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.events.client.BlockRenderedOnScreenEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ScreenEffectRenderer.class)
public abstract class ScreenEffectRendererMixin {

    @Inject(method = "renderScreenEffect(Lnet/minecraft/client/Minecraft;Lcom/mojang/blaze3d/vertex/PoseStack;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ScreenEffectRenderer;renderTex(Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;Lcom/mojang/blaze3d/vertex/PoseStack;)V"),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true)
    private static void bumblezone$blockRenderedOnScreenEvent(Minecraft minecraft, PoseStack poseStack, CallbackInfo ci, Player player, BlockState blockState) {
        if (BlockRenderedOnScreenEvent.EVENT.invoke(new BlockRenderedOnScreenEvent(player, poseStack, BlockRenderedOnScreenEvent.Type.BLOCK, blockState, player.blockPosition()))) {
            ci.cancel();
        }
    }
}
