package com.telepathicgrunt.the_bumblezone.mixin.fabricbase.client;

import com.telepathicgrunt.the_bumblezone.client.rendering.essence.EssenceOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;F)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;getPlayerMode()Lnet/minecraft/world/level/GameType;",
                    ordinal = 0,
                    shift = At.Shift.BEFORE),
            require = 0)
    private void bumblezone$renderEssenceOverlay(GuiGraphics guiGraphics, float f, CallbackInfo ci) {
        if (Minecraft.getInstance().player != null) {
            EssenceOverlay.nearEssenceOverlay(Minecraft.getInstance().player, guiGraphics);
        }
    }
}