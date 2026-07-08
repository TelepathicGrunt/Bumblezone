package com.telepathicgrunt.the_bumblezone.mixin.fabric.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.telepathicgrunt.the_bumblezone.client.rendering.essence.EssenceOverlay;
import com.telepathicgrunt.the_bumblezone.client.rendering.essence.KnowingEssenceStructureMessage;
import com.telepathicgrunt.the_bumblezone.client.rendering.essence.RadianceEssenceArmorMessage;
import com.telepathicgrunt.the_bumblezone.client.screens.DimensionTeleportingScreen;
import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import com.telepathicgrunt.the_bumblezone.modinit.BzDimension;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Final
    @Shadow
    private Minecraft minecraft;

    @Inject(method = "extractGui(Lnet/minecraft/client/DeltaTracker;ZZ)V",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/gui/screens/Overlay;extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V"),
            require = 0)
    private void bumblezone$renderTeleportationScreen(
            DeltaTracker deltaTracker,
            boolean shouldRenderLevel,
            boolean resourcesLoaded,
            CallbackInfo ci,
            @Local(name = "graphics") GuiGraphicsExtractor graphics)
    {
        if (this.minecraft.screen instanceof LevelLoadingScreen levelLoadingScreen &&
            GeneralUtilsClient.getClientPlayer() != null &&
            GeneralUtilsClient.getClientPlayer().level().dimension() == BzDimension.BZ_WORLD_KEY)
        {
            DimensionTeleportingScreen.renderScreenAndText(levelLoadingScreen, graphics);
        }
    }
}