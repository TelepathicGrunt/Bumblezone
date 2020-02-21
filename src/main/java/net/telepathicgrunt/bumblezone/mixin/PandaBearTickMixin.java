package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.dimension.BzDimensionType;
import net.telepathicgrunt.bumblezone.effects.BzEffectsInit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PandaEntity.class)
public class PandaBearTickMixin
{
    //bees attacks bear mobs that is in the dimension
    @Inject(method = "tick",
            at = @At(value="HEAD"))
    private void onEntityTick(CallbackInfo ci) {

        //Bumblezone.LOGGER.log(Level.INFO, "started");
        World world = ((PandaEntity)(Object)this).world;

        //Make sure we are on actual player's computer and not a dedicated server. Vanilla does this check too.
        //Also checks to make sure we are in dimension and that player isn't in creative or spectator
        if (!world.isClient && ((PandaEntity)(Object)this).dimension == BzDimensionType.BUMBLEZONE_TYPE && Bumblezone.BZ_CONFIG.aggressiveBees)
        {
            ((PandaEntity)(Object)this).addStatusEffect(new StatusEffectInstance(BzEffectsInit.WRATH_OF_THE_HIVE, Bumblezone.BZ_CONFIG.howLongWrathOfTheHiveLasts, 1, false, true));

        }
    }

}