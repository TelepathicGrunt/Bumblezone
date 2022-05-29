package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.telepathicgrunt.the_bumblezone.entities.BeeInteractivity;
import com.telepathicgrunt.the_bumblezone.entities.CreatingHoneySlime;
import com.telepathicgrunt.the_bumblezone.items.StinglessBeeHelmet;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerInteractsEntityMixin {

    // Feeding bees honey or sugar water. Or turning Slime into Honey Slime
    @Inject(method = "interactOn",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z", ordinal = 1),
            cancellable = true)
    private void thebumblezone_onBeeFeeding(Entity entity, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
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
}