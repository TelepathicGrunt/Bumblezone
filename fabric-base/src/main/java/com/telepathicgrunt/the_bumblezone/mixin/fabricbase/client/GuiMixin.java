package com.telepathicgrunt.the_bumblezone.mixin.fabricbase.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.telepathicgrunt.the_bumblezone.client.rendering.essence.EssenceOverlay;
import com.telepathicgrunt.the_bumblezone.fluids.base.BzFlowingFluid;
import com.telepathicgrunt.the_bumblezone.fluids.base.ClientFluidProperties;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.level.material.FluidState;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

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