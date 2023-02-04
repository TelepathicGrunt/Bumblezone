package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzDimensionConfigs;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FogRenderer.class)
public class FogRendererMixin {

    @ModifyVariable(method = "setupFog(Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/FogRenderer$FogMode;FZF)V",
            at = @At(value = "HEAD"),
            ordinal = 0,
            argsOnly = true,
            require = 0)
    private static float thebumblezone_reduceFogThickness(float original, Camera cam, FogRenderer.FogMode mode, float farPlaneDistance2, boolean thickFog, float p__) {
        if (mode == FogRenderer.FogMode.FOG_TERRAIN && thickFog && Minecraft.getInstance().player.getLevel().dimension().location().equals(Bumblezone.MOD_DIMENSION_ID)) {
            return (float) (original * BzDimensionConfigs.fogReducer);
        }
        return original;
    }
}