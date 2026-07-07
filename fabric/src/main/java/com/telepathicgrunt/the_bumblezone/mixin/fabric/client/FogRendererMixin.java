package com.telepathicgrunt.the_bumblezone.mixin.fabric.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.telepathicgrunt.the_bumblezone.client.dimension.BzDimensionSpecialEffects;
import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import com.telepathicgrunt.the_bumblezone.configs.BzDimensionConfigs;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = FogRenderer.class, priority = 1200)
public class FogRendererMixin {

    @Inject(method = "setupFog(Lnet/minecraft/client/Camera;ILnet/minecraft/client/DeltaTracker;FLnet/minecraft/client/multiplayer/ClientLevel;)Lnet/minecraft/client/renderer/fog/FogData;",
            at = @At(value = "RETURN"),
            require = 0, locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void bumblezone$reduceFogThickness(Camera camera,
                                                      int renderDistanceInChunks,
                                                      DeltaTracker deltaTracker,
                                                      float darkenWorldAmount,
                                                      ClientLevel level,
                                                      CallbackInfoReturnable<FogData> cir,
                                                      @Local(name = "fogType") FogType fogType,
                                                      @Local(name = "fog") FogData fog)
    {
        BzDimensionSpecialEffects.fogThicknessAdjustments(
                GeneralUtilsClient.getClientPlayer(),
                renderDistanceInChunks,
                BzDimensionConfigs.enableDimensionFog,
                fogType,
                (newFogStart) -> fog.renderDistanceStart = newFogStart,
                (newFogEnd) -> fog.renderDistanceEnd = newFogEnd);
    }
}