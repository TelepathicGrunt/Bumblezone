package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.dimension.BzDimensionType;
import net.telepathicgrunt.bumblezone.effects.BzEffectsInit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PolarBearEntity.class)
public class PolarBearTickMixin
{
    //bees attacks bear mobs that is in the dimension
    @Inject(method = "tick",
            at = @At(value="HEAD"))
    private void onEntityTick(CallbackInfo ci) {

        //Bumblezone.LOGGER.log(Level.INFO, "started");
        World world = ((PolarBearEntity)(Object)this).world;

        //Make sure we are on actual player's computer and not a dedicated server. Vanilla does this check too.
        //Also checks to make sure we are in dimension and that player isn't in creative or spectator
        if (!world.isClient && ((PolarBearEntity)(Object)this).dimension == BzDimensionType.BUMBLEZONE_TYPE)// && BzConfig.aggressiveBees)
        {
//            ((PolarBearEntity)(Object)this).addStatusEffect(new StatusEffectInstance(BzEffectsInit.WRATH_OF_THE_HIVE, BzConfig.howLongWrathOfTheHiveLasts, 1, false, true));
            ((PolarBearEntity)(Object)this).addStatusEffect(new StatusEffectInstance(BzEffectsInit.WRATH_OF_THE_HIVE, 350, 1, false, true));
        }
    }

}