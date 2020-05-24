package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.util.math.BlockPos;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import net.telepathicgrunt.bumblezone.fluids.SugarWaterClientOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(WaterFluid.class)
public class WaterEquivalenceMixin {

    @Inject(method = "matchesType",
            at = @At(value = "TAIL"),
            cancellable = true)
    private void isEquivalentToSugarWater(Fluid fluid, CallbackInfoReturnable<Boolean> cir) {
        if(fluid == BzBlocks.SUGAR_WATER_FLUID || fluid == BzBlocks.SUGAR_WATER_FLUID_FLOWING)
            cir.setReturnValue(true);
    }

}