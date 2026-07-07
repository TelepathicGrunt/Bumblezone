package com.telepathicgrunt.the_bumblezone.mixin.fabric.client;

import com.telepathicgrunt.the_bumblezone.client.screens.DimensionTeleportingScreen;
import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import com.telepathicgrunt.the_bumblezone.modinit.BzDimension;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LevelLoadingScreen.class, priority = 1200)
public class LevelLoadingScreenMixin extends Screen {
    protected LevelLoadingScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V",
            at = @At(value = "HEAD"), cancellable = true, require = 0)
    private void bumblezone$renderNewScreenWhenEnteringBumblezone(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (GeneralUtilsClient.getClientPlayer() != null && GeneralUtilsClient.getClientPlayer().level().dimension() == BzDimension.BZ_WORLD_KEY) {
            DimensionTeleportingScreen.renderScreenAndText(((LevelLoadingScreen)(Object)this), guiGraphics);
            ci.cancel();
        }
    }
}