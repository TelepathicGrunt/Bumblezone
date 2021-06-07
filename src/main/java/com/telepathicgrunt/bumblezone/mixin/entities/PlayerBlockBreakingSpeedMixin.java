package com.telepathicgrunt.bumblezone.mixin.entities;

import com.telepathicgrunt.bumblezone.enchantments.CombCutterEnchantment;
import com.telepathicgrunt.bumblezone.modinit.BzEnchantments;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerBlockBreakingSpeedMixin {

    // makes Comb Cutter cut all blocks that have "comb" in the name faster.
    @Inject(method = "getBlockBreakingSpeed(Lnet/minecraft/block/BlockState;)F",
            at = @At(value = "RETURN"), cancellable = true)
    private void thebumblezone_blockBreakingSpeed(BlockState block, CallbackInfoReturnable<Float> cir) {
        if(BzEnchantments.COMB_CUTTER.getTargetBlocks().contains(block.getBlock())){
            cir.setReturnValue(CombCutterEnchantment.attemptFasterMining(cir.getReturnValue(), false, ((PlayerEntity) (Object) this)));
        }
        else if(BzEnchantments.COMB_CUTTER.getLesserTargetBlocks().contains(block.getBlock())){
            cir.setReturnValue(CombCutterEnchantment.attemptFasterMining(cir.getReturnValue(), true, ((PlayerEntity) (Object) this)));
        }
    }
}