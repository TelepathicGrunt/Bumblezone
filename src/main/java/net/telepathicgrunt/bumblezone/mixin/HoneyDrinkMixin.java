package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoneyBottleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.configs.BzConfig;
import net.telepathicgrunt.bumblezone.dimension.BzDimensionType;
import net.telepathicgrunt.bumblezone.effects.BzEffectsInit;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;


@Mixin(HoneyBottleItem.class)
public class HoneyDrinkMixin
{
    //bees attack player that drinks honey bottles
    @Inject(method = "finishUsing",
            at = @At("HEAD"))
    private void onHoneyDrink(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> ci) {

       // Bumblezone.LOGGER.log(Level.INFO, "just drank");
        if(user instanceof PlayerEntity){
            PlayerEntity playerEntity = (PlayerEntity)user;

            //Make sure we are on actual player's computer and not a dedicated server. Vanilla does this check too.
            //Also checks to make sure we are in dimension and that player isn't in creative or spectator
            if (!world.isClient &&
                    (playerEntity.dimension == BzDimensionType.BUMBLEZONE_TYPE || BzConfig.allowWrathOfTheHiveOutsideBumblezone.get().equals("yes")) &&
                    !playerEntity.isCreative() &&
                    !playerEntity.isSpectator())
            {
                //if player drinks honey, bees gets very mad...
                if(stack.getItem() == Items.HONEY_BOTTLE){// && BzConfig.aggressiveBees){
//                    playerEntity.addStatusEffect(new StatusEffectInstance(BzEffectsInit.WRATH_OF_THE_HIVE, BzConfig.howLongWrathOfTheHiveLasts, 2, false, BzConfig.showWrathOfTheHiveParticles, true));
                    playerEntity.addStatusEffect(new StatusEffectInstance(BzEffectsInit.WRATH_OF_THE_HIVE, 350, 2, false, true, true));
                }
            }
        }
    }

}