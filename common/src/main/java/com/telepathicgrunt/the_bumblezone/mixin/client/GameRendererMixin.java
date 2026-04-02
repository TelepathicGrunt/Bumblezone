package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.april_fools.GuiBees;
import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import com.telepathicgrunt.the_bumblezone.configs.BzClientConfigs;
import com.telepathicgrunt.the_bumblezone.modinit.BzDimension;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Inject(method = "render(FJZ)V",
            at = @At(value = "HEAD"),
            require = 0 // Not important joke. No crashy
    )
    private void bumblezone$april_fools_gui_bees1(float f, long l, boolean bl, CallbackInfo ci) {
        GuiBees.initDateCheck();
        // If player turns off both configs in-game while gui bees are present, turn off right away.
        if (GuiBees.showGuiBeesToday && (BzClientConfigs.showBeesOnGuiOnAprilFools || BzClientConfigs.showBeesOnGuiAllYearRound)) {
            GuiBees.guiClosed(((GameRenderer)(Object)this).getMinecraft().screen);
        }
        // For setting up for Bumblezone's teleporting screen.
        else if (GeneralUtilsClient.getClientPlayer() != null && GeneralUtilsClient.getClientPlayer().level().dimension() == BzDimension.BZ_WORLD_KEY) {
            GuiBees.guiClosed(((GameRenderer)(Object)this).getMinecraft().screen);
        }
    }

    // Mixin so I am on top of all screens
    @Inject(method = "render(FJZ)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;handleDelayedNarration()V"),
            require = 0 // Not important joke. No crashy
    )
    private void bumblezone$april_fools_gui_bees2(
            float deltaPastLastTick,
            long nanoTime,
            boolean bl,
            CallbackInfo ci,
            @Local(ordinal = 0) int mouseX,
            @Local(ordinal = 1) int mouseY,
            @Local(ordinal = 0) GuiGraphics guiGraphics)
    {
        // If player turns off both configs in-game while gui bees are present, turn off right away.
        if (GuiBees.showGuiBeesToday && (BzClientConfigs.showBeesOnGuiOnAprilFools || BzClientConfigs.showBeesOnGuiAllYearRound)) {
            GuiBees.renderBees(((GameRenderer)(Object)this).getMinecraft().screen, guiGraphics, mouseX, mouseY, deltaPastLastTick);
        }
        else if (GeneralUtilsClient.getClientPlayer() != null && GeneralUtilsClient.getClientPlayer().level().dimension() == BzDimension.BZ_WORLD_KEY) {
            GuiBees.renderBees(((GameRenderer)(Object)this).getMinecraft().screen, guiGraphics, mouseX, mouseY, deltaPastLastTick);
        }
    }
}