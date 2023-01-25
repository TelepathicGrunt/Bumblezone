package com.telepathicgrunt.the_bumblezone.mixin.fabricbase.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.client.rendering.pileofpollen.PileOfPollenRenderer;
import com.telepathicgrunt.the_bumblezone.events.client.BlockRenderedOnScreenEvent;
import com.telepathicgrunt.the_bumblezone.fluids.base.BzFlowingFluid;
import com.telepathicgrunt.the_bumblezone.fluids.base.ClientFluidProperties;
import com.telepathicgrunt.the_bumblezone.platform.BzEntityHooks;
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

    @Inject(method = "renderScreenEffect", at = @At("HEAD"))
    private static void bumblezone$renderFluidOverlay(Minecraft minecraft, PoseStack poseStack, CallbackInfo ci) {
        if (minecraft.player instanceof BzEntityHooks hook) {
            if (hook.bz$getFluidOnEyes().getType() instanceof BzFlowingFluid bzFluid) {
                ClientFluidProperties properties = ClientFluidProperties.get(bzFluid.info().properties().id());
                properties.fluidOverlay(minecraft, poseStack);
            }
        }
    }

    @Inject(method = "renderScreenEffect(Lnet/minecraft/client/Minecraft;Lcom/mojang/blaze3d/vertex/PoseStack;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ScreenEffectRenderer;renderTex(Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;Lcom/mojang/blaze3d/vertex/PoseStack;)V"),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true)
    private static void thebumblezone$blockRenderedOnScreenEvent(Minecraft minecraft, PoseStack poseStack, CallbackInfo ci, Player player, BlockState blockState) {
        if (BlockRenderedOnScreenEvent.EVENT.invoke(new BlockRenderedOnScreenEvent(player, poseStack, BlockRenderedOnScreenEvent.Type.BLOCK, blockState, player.blockPosition()))) {
            ci.cancel();
        }
    }
}
