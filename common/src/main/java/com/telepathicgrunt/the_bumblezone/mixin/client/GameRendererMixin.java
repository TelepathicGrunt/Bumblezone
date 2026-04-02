package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.telepathicgrunt.the_bumblezone.client.april_fools.GuiBees;
import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import com.telepathicgrunt.the_bumblezone.configs.BzClientConfigs;
import com.telepathicgrunt.the_bumblezone.items.essence.KnowingEssence;
import com.telepathicgrunt.the_bumblezone.items.essence.RagingEssence;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Inject(method = "render(Lnet/minecraft/client/DeltaTracker;Z)V",
            at = @At(value = "HEAD"),
            require = 0 // Not important joke. No crashy
    )
    private void bumblezone$april_fools_gui_bees1(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci) {
        GuiBees.initDateCheck();
        // If player turns off both configs in-game while gui bees are present, turn off right away.
        if (GuiBees.showGuiBeesToday && (BzClientConfigs.showBeesOnGuiOnAprilFools || BzClientConfigs.showBeesOnGuiAllYearRound)) {
            GuiBees.guiClosed(((GameRenderer)(Object)this).getMinecraft().screen);
        }
    }

    // Mixin so I am on top of all screens
    @Inject(method = "render(Lnet/minecraft/client/DeltaTracker;Z)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;handleDelayedNarration()V"),
            require = 0 // Not important joke. No crashy
    )
    private void bumblezone$april_fools_gui_bees2(
            DeltaTracker deltaTracker,
            boolean renderLevel,
            CallbackInfo ci,
            @Local(ordinal = 0) int mouseX,
            @Local(ordinal = 1) int mouseY,
            @Local(ordinal = 0) GuiGraphics guiGraphics)
    {
        // If player turns off both configs in-game while gui bees are present, turn off right away.
        if (GuiBees.showGuiBeesToday && (BzClientConfigs.showBeesOnGuiOnAprilFools || BzClientConfigs.showBeesOnGuiAllYearRound)) {
            GuiBees.renderBees(((GameRenderer)(Object)this).getMinecraft().screen, guiGraphics, mouseX, mouseY, deltaTracker.getRealtimeDeltaTicks());
        }
    }
}