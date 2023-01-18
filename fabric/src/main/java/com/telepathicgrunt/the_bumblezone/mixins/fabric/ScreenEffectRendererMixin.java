package com.telepathicgrunt.the_bumblezone.mixins.fabric;

import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.fabric.hooks.BzEntityHooks;
import com.telepathicgrunt.the_bumblezone.fluids.base.BzFlowingFluid;
import com.telepathicgrunt.the_bumblezone.fluids.base.ClientFluidProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
}
