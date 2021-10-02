package com.telepathicgrunt.bumblezone.mixin.entities;

import com.telepathicgrunt.bumblezone.enchantments.CombCutterEnchantment;
import com.telepathicgrunt.bumblezone.entities.BeeInteractivity;
import com.telepathicgrunt.bumblezone.entities.CreatingHoneySlime;
import com.telepathicgrunt.bumblezone.modinit.BzEnchantments;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityInteractionMixin {

    // Feeding bees honey or sugar water. Or turning Slime into Honey Slime
    @Inject(method = "interact",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", ordinal = 0))
    private void thebumblezone_onBeeFeeding(Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if(entity instanceof BeeEntity beeEntity) {
            if(BeeInteractivity.beeFeeding(entity.world, ((PlayerEntity)(Object)this), hand, beeEntity) == ActionResult.SUCCESS)
                cir.setReturnValue(ActionResult.SUCCESS);
            else if(BeeInteractivity.beeUnpollinating(entity.world, ((PlayerEntity)(Object)this), hand, beeEntity) == ActionResult.SUCCESS)
                cir.setReturnValue(ActionResult.SUCCESS);
        }
        else if (entity instanceof SlimeEntity slimeEntity) {
            if(CreatingHoneySlime.createHoneySlime(entity.world, ((PlayerEntity)(Object)this), hand, slimeEntity) == ActionResult.SUCCESS)
                cir.setReturnValue(ActionResult.SUCCESS);
        }
    }

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