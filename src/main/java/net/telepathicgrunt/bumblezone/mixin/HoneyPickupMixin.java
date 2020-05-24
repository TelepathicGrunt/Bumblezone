package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.dimension.BzDimensionType;
import net.telepathicgrunt.bumblezone.effects.BzEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemEntity.class)
public class HoneyPickupMixin
{
    //bees attack player that picks up honey blocks
    @Inject(method = "onPlayerCollision",
            at = @At(value="INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;sendPickup(Lnet/minecraft/entity/Entity;I)V"),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onItemPickup(PlayerEntity player, CallbackInfo ci, ItemStack itemStack, Item item, int i) {

        //Bumblezone.LOGGER.log(Level.INFO, "started");
        World world = player.world;

        //Make sure we are on actual player's computer and not a dedicated server. Vanilla does this check too.
        //Also checks to make sure we are in dimension and that player isn't in creative or spectator
        if ((player.dimension == BzDimensionType.BUMBLEZONE_TYPE || Bumblezone.BZ_CONFIG.allowWrathOfTheHiveOutsideBumblezone) &&
            !player.isCreative() &&
            !player.isSpectator())
        {
            //if player picks up a honey block, bees gets very mad...
            if(item == Items.HONEY_BLOCK && Bumblezone.BZ_CONFIG.aggressiveBees)
            {
                //Bumblezone.LOGGER.log(Level.INFO, "ANGRY BEES");
                player.addStatusEffect(new StatusEffectInstance(BzEffects.WRATH_OF_THE_HIVE, Bumblezone.BZ_CONFIG.howLongWrathOfTheHiveLasts, 2, false, Bumblezone.BZ_CONFIG.showWrathOfTheHiveParticles, true));
            }
        }
    }

}