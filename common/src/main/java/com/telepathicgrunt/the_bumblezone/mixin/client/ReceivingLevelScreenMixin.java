package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.client.DimensionTeleportingScreen;
import com.telepathicgrunt.the_bumblezone.modinit.BzDimension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ReceivingLevelScreen.class)
public class ReceivingLevelScreenMixin extends Screen {
    protected ReceivingLevelScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;IIF)V",
            at = @At(value = "HEAD"), cancellable = true, require = 0)
    private void thebumblezone_glowNearbyBeesForPlayer(PoseStack poseStack, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.level.dimension() == BzDimension.BZ_WORLD_KEY) {
            DimensionTeleportingScreen.renderScreenAndText(((ReceivingLevelScreen)(Object)this), poseStack);
            super.render(poseStack, mouseX, mouseY, partialTick);
            ci.cancel();
        }
    }
}