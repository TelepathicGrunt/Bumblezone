package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.HoneyBottleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.entities.BeeAggression;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(HoneyBottleItem.class)
public class HoneyDrinkMixin {

    //bees attack player that drinks honey bottles
    @Inject(method = "onItemUseFinish",
            at = @At("HEAD"))
    private void onHoneyDrink(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        BeeAggression.honeyDrinkAnger(stack, world, user);
    }
}