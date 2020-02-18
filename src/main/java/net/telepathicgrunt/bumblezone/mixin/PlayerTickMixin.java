package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.dimension.BzDimension;
import net.telepathicgrunt.bumblezone.dimension.BzDimensionType;
import net.telepathicgrunt.bumblezone.effects.BzEffectsInit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerTickMixin
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

        //removes the wrath of the hive if it is disallowed outside dimension  BzConfig.allowWrathOfTheHiveOutsideBumblezone ||
        if(!(playerEntity.dimension == BzDimensionType.BUMBLEZONE_TYPE) &&
            playerEntity.hasStatusEffect(BzEffectsInit.WRATH_OF_THE_HIVE))
        {
            playerEntity.removeStatusEffect(BzEffectsInit.WRATH_OF_THE_HIVE);
        }

        //Makes it so player does not get killed for falling into the void
        if(playerEntity.dimension == BzDimensionType.BUMBLEZONE_TYPE && playerEntity.getY() < -3)
        {
            playerEntity.setPos(playerEntity.getX(), -3, playerEntity.getZ());
            playerEntity.updatePosition(playerEntity.getX(), -3, playerEntity.getZ());
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