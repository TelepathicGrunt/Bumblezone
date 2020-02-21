package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.dimension.BzDimension;
import net.telepathicgrunt.bumblezone.dimension.BzDimensionType;
import net.telepathicgrunt.bumblezone.effects.BzEffectsInit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerHiveWrathTickMixin
{
    // Handles where wrath of the hive can be on,
    // change player fog color when effect is active,
    // and prevent player from falling into void.
    @Inject(method = "tick",
            at = @At(value="TAIL"))
    private void onEntityTick(CallbackInfo ci) {

        //Bumblezone.LOGGER.log(Level.INFO, "started");
        //grabs the capability attached to player for dimension hopping
        PlayerEntity playerEntity = ((PlayerEntity)(Object)this);

        //removes the wrath of the hive if it is disallowed outside dimension
        if(!(playerEntity.dimension == BzDimensionType.BUMBLEZONE_TYPE || Bumblezone.BZ_CONFIG.allowWrathOfTheHiveOutsideBumblezone) &&
            playerEntity.hasStatusEffect(BzEffectsInit.WRATH_OF_THE_HIVE))
        {
            playerEntity.removeStatusEffect(BzEffectsInit.WRATH_OF_THE_HIVE);
        }

        //Makes the fog redder when this effect is active
        if(playerEntity.hasStatusEffect(BzEffectsInit.WRATH_OF_THE_HIVE))
        {
            BzDimension.ACTIVE_WRATH = true;
        }
        else
        {
            BzDimension.ACTIVE_WRATH = false;
        }
    }

}