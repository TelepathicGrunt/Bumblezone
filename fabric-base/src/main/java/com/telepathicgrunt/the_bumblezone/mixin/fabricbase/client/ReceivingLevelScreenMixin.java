package com.telepathicgrunt.the_bumblezone.mixin.fabricbase.client;

import com.telepathicgrunt.the_bumblezone.client.DimensionTeleportingScreen;
import com.telepathicgrunt.the_bumblezone.modinit.BzDimension;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtilsClient;
import net.minecraft.client.gui.GuiGraphics;
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

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V",
            at = @At(value = "HEAD"), cancellable = true, require = 0)
    private void bumblezone$renderNewScreenWhenEnteringBumblezone(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (GeneralUtilsClient.getClientPlayer() != null && GeneralUtilsClient.getClientPlayer().level().dimension() == BzDimension.BZ_WORLD_KEY) {
            DimensionTeleportingScreen.renderScreenAndText(((ReceivingLevelScreen)(Object)this), guiGraphics);
            super.render(guiGraphics, mouseX, mouseY, partialTick);
            ci.cancel();
        }
    }
}