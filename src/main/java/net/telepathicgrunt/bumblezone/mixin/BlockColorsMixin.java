package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.WaterFluid;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockColors.class)
public class BlockColorsMixin {

    @Inject(method = "create",
            at = @At(value = "TAIL"))
    private static void addBlockColors(CallbackInfoReturnable<BlockColors> cir) {
        cir.getReturnValue().registerColorProvider((state, world, pos, tintIndex) -> {
                return world != null && pos != null ? BiomeColors.getWaterColor(world, pos) : -1;
            }, BzBlocks.SUGAR_WATER_BLOCK);
    }

}