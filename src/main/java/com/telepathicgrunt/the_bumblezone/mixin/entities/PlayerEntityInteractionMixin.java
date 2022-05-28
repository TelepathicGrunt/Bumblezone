package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.telepathicgrunt.the_bumblezone.enchantments.CombCutterEnchantment;
import com.telepathicgrunt.the_bumblezone.entities.BeeInteractivity;
import com.telepathicgrunt.the_bumblezone.entities.CreatingHoneySlime;
import com.telepathicgrunt.the_bumblezone.items.StinglessBeeHelmet;
import com.telepathicgrunt.the_bumblezone.modinit.BzEnchantments;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerEntityInteractionMixin {

    // Feeding bees honey or sugar water. Or turning Slime into Honey Slime
    @Inject(method = "interactOn",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z", ordinal = 0),
            cancellable = true)
    private void thebumblezone_onBeeInteract(Entity entity, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if(entity instanceof Bee beeEntity) {
            if(BeeInteractivity.beeFeeding(entity.level, ((Player)(Object)this), hand, beeEntity) == InteractionResult.SUCCESS)
                cir.setReturnValue(InteractionResult.SUCCESS);
            else if(StinglessBeeHelmet.addBeePassenger(entity.level, ((Player)(Object)this), hand, beeEntity) == InteractionResult.SUCCESS)
                cir.setReturnValue(InteractionResult.SUCCESS);
            else if(BeeInteractivity.beeUnpollinating(entity.level, ((Player)(Object)this), hand, beeEntity) == InteractionResult.SUCCESS)
                cir.setReturnValue(InteractionResult.SUCCESS);
        }
        else if (entity instanceof Slime slimeEntity) {
            if(CreatingHoneySlime.createHoneySlime(entity.level, ((Player)(Object)this), hand, slimeEntity) == InteractionResult.SUCCESS)
                cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }

    // makes Comb Cutter cut all blocks that have "comb" in the name faster.
    @Inject(method = "getDestroySpeed(Lnet/minecraft/world/level/block/state/BlockState;)F",
            at = @At(value = "RETURN"), cancellable = true)
    private void thebumblezone_blockBreakingSpeed(BlockState block, CallbackInfoReturnable<Float> cir) {
        if(BzEnchantments.COMB_CUTTER.getTargetBlocks().contains(block.getBlock())) {
            cir.setReturnValue(CombCutterEnchantment.attemptFasterMining(cir.getReturnValue(), false, ((Player) (Object) this)));
        }
        else if(BzEnchantments.COMB_CUTTER.getLesserTargetBlocks().contains(block.getBlock())) {
            cir.setReturnValue(CombCutterEnchantment.attemptFasterMining(cir.getReturnValue(), true, ((Player) (Object) this)));
        }
    }
}