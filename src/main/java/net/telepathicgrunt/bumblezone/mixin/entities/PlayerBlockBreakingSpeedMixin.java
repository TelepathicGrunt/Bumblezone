package net.telepathicgrunt.bumblezone.mixin.entities;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.telepathicgrunt.bumblezone.enchantments.CombCutterEnchantment;
import net.telepathicgrunt.bumblezone.modinit.BzEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerBlockBreakingSpeedMixin {

    // makes Comb Cutter cut all blocks that have "comb" in the name faster.
    @Inject(method = "getBlockBreakingSpeed(Lnet/minecraft/block/BlockState;)F",
            at = @At(value = "RETURN"), cancellable = true)
    private void blockBreakingSpeed(BlockState block, CallbackInfoReturnable<Float> cir) {
        if(BzEnchantments.COMB_CUTTER.getTargetBlocks().contains(block.getBlock())){
            cir.setReturnValue(CombCutterEnchantment.fasterMiningCombs(cir.getReturnValue(), ((PlayerEntity) (Object) this)));
        }
    }
}