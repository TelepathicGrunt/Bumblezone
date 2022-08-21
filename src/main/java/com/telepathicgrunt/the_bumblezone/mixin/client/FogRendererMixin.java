package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzDimensionConfigs;
import com.telepathicgrunt.the_bumblezone.fluids.HoneyFluid;
import com.telepathicgrunt.the_bumblezone.fluids.HoneyFluidBlock;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FogRenderer.class)
public class FogRendererMixin {

    @ModifyVariable(method = "setupFog(Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/FogRenderer$FogMode;FZF)V",
            at = @At(value = "HEAD"),
            ordinal = 0,
            argsOnly = true,
            require = 0)
    private static float thebumblezone_changeFluidHeight(float original, Camera cam, FogRenderer.FogMode mode, float farPlaneDistance2, boolean thickFog, float p__) {
        if (mode == FogRenderer.FogMode.FOG_TERRAIN && thickFog && Minecraft.getInstance().player.getLevel().dimension().location().equals(Bumblezone.MOD_DIMENSION_ID)) {
            return (float) (original * BzDimensionConfigs.fogReducer.get());
        }
        return original;
    }
}