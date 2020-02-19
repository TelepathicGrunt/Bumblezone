package net.telepathicgrunt.bumblezone.mixin;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.dimension.DimensionType;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.dimension.BzDimension;
import net.telepathicgrunt.bumblezone.dimension.BzDimensionType;
import net.telepathicgrunt.bumblezone.dimension.BzPlacement;
import net.telepathicgrunt.bumblezone.effects.BzEffectsInit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(PlayerEntity.class)
public class PlayerTeleportationTickMixin
{
    // Handles player teleportation out of Bumblezone dimension
    @Inject(method = "tick",
            at = @At(value="TAIL"))
    private void onEntityTick(CallbackInfo ci) {

        //Bumblezone.LOGGER.log(Level.INFO, "started");
        //grabs the capability attached to player for dimension hopping
        PlayerEntity playerEntity = ((PlayerEntity)(Object)this);

        //Makes it so player does not get killed for falling into the void
        if(playerEntity.dimension == BzDimensionType.BUMBLEZONE_TYPE)
        {
            if(playerEntity.getY() < -3)
            {
                playerEntity.setPos(playerEntity.getX(), -3.01D, playerEntity.getZ());
                playerEntity.updatePosition(playerEntity.getX(), -3.01D, playerEntity.getZ());

                if(!playerEntity.world.isClient)
                {
                    FabricDimensions.teleport(playerEntity, Bumblezone.PLAYER_COMPONENT.get(playerEntity).getNonBZDimension(), BzPlacement.LEAVING);
                    reAddStatusEffect(playerEntity);
                }
            }
            else if(playerEntity.getY() > 255)
            {
                if(!playerEntity.world.isClient)
                {
                    FabricDimensions.teleport(playerEntity, Bumblezone.PLAYER_COMPONENT.get(playerEntity).getNonBZDimension(), BzPlacement.LEAVING);
                    reAddStatusEffect(playerEntity);
                }
            }
        }
        //teleport to bumblezone
        else if(Bumblezone.PLAYER_COMPONENT.get(playerEntity).getIsTeleporting())
        {
            FabricDimensions.teleport(playerEntity, BzDimensionType.BUMBLEZONE_TYPE, BzPlacement.ENTERING);
            Bumblezone.PLAYER_COMPONENT.get(playerEntity).setIsTeleporting(false);
            reAddStatusEffect(playerEntity);
        }
    }

	/**
	 * Temporary fix until Mojang patches the bug that makes potion effect icons disappear when changing dimension.
	 * To fix it ourselves, we remove the effect and re-add it to the player.
	 */
	private static void reAddStatusEffect(PlayerEntity playerEntity)
	{
		//re-adds potion effects so the icon remains instead of disappearing when changing dimensions due to a bug
		ArrayList<StatusEffectInstance> effectInstanceList = new ArrayList<StatusEffectInstance>(playerEntity.getStatusEffects());
		for(int i = effectInstanceList.size() - 1; i >= 0; i--)
		{
            StatusEffectInstance effectInstance = effectInstanceList.get(i);
			if(effectInstance != null)
			{
				playerEntity.removeStatusEffect(effectInstance.getEffectType());
				playerEntity.addStatusEffect(
						new StatusEffectInstance(
								effectInstance.getEffectType(),
								effectInstance.getDuration(),
								effectInstance.getAmplifier(),
								effectInstance.isAmbient(),
								effectInstance.shouldShowParticles(),
								effectInstance.shouldShowIcon()));
			}
		}
	}
}