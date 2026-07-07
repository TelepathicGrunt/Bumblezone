package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.telepathicgrunt.the_bumblezone.client.screens.GuiBees;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Inject(method = "render(Lnet/minecraft/client/DeltaTracker;Z)V",
            at = @At(value = "HEAD"),
            require = 0 // Not important. No crashy
    )
    private void bumblezone$gui_bees1(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci) {
        GuiBees.initDateCheck();
        if (GuiBees.isGuiBeeAllowedByConfig()) {
            GuiBees.guiClosed(((GameRenderer)(Object)this).getMinecraft().screen);
        }
    }

    // Mixin so I am on top of all screens
    @Inject(method = "render(Lnet/minecraft/client/DeltaTracker;Z)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;handleDelayedNarration()V"),
            require = 0 // Not important. No crashy
    )
    private void bumblezone$gui_bees2(
            DeltaTracker deltaTracker,
            boolean renderLevel,
            CallbackInfo ci,
            @Local(ordinal = 0) int mouseX,
            @Local(ordinal = 1) int mouseY,
            @Local(ordinal = 0) GuiGraphicsExtractor guiGraphics)
    {
        if (GuiBees.isGuiBeeAllowedByConfig()) {
            GuiBees.renderBees(((GameRenderer)(Object)this).getMinecraft().screen, false, guiGraphics, mouseX, mouseY, deltaTracker.getRealtimeDeltaTicks());
        }
        else if (GuiBees.isPlayerInDimensionTeleportScreen(((GameRenderer)(Object)this).getMinecraft().screen)) {
            GuiBees.renderBees(((GameRenderer)(Object)this).getMinecraft().screen, true, guiGraphics, mouseX, mouseY, deltaTracker.getRealtimeDeltaTicks());
        }
    }
}