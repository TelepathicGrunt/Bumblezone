package com.telepathicgrunt.the_bumblezone.mixin;

import com.telepathicgrunt.the_bumblezone.entities.BeeInteractivity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(PlayerEntity.class)
public class PlayerInteractsEntityMixin {
    // Feeding bees honey or sugar water
    @Inject(method = "interactOn",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", ordinal = 0))
    private void onBeeFeeding(Entity entity, Hand hand, CallbackInfoReturnable<ActionResultType> cir) {
        BeeInteractivity.beeFeeding(entity.world, ((PlayerEntity)(Object)this), hand, entity);
    }
}