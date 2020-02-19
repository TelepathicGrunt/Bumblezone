package net.telepathicgrunt.bumblezone.mixin;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
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
                playerEntity.setPos(playerEntity.getX(), -3, playerEntity.getZ());
                playerEntity.updatePosition(playerEntity.getX(), -3, playerEntity.getZ());

                if(!playerEntity.world.isClient)
                    FabricDimensions.teleport(playerEntity, Bumblezone.PLAYER_COMPONENT.get(playerEntity).getNonBZDimension(), BzPlacement.LEAVING);
            }
            else if(playerEntity.getY() > 255)
            {
                if(!playerEntity.world.isClient)
                    FabricDimensions.teleport(playerEntity, Bumblezone.PLAYER_COMPONENT.get(playerEntity).getNonBZDimension(), BzPlacement.LEAVING);
            }
        }
        //teleport to bumblezone
        else if(Bumblezone.PLAYER_COMPONENT.get(playerEntity).getIsTeleporting())
        {
            FabricDimensions.teleport(playerEntity, BzDimensionType.BUMBLEZONE_TYPE, BzPlacement.ENTERING);
            Bumblezone.PLAYER_COMPONENT.get(playerEntity).setIsTeleporting(false);
        }
    }

}