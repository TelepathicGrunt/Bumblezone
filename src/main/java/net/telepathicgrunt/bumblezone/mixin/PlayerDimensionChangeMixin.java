package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.dimension.DimensionType;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.dimension.BzDimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class PlayerDimensionChangeMixin {
    // Handles storing of past non-bumblezone dimension the player is leaving
    @Inject(method = "dimensionChanged",
            at = @At(value = "HEAD"))
    private void onDimensionChange(ServerWorld targetWorld, CallbackInfo ci) {

        //Target world isnt actually target world. It's the world we are leaving.
        DimensionType dimensionLeaving = targetWorld.getWorld().getDimension();

        //Updates the non-BZ dimension that the player is leaving
        if (dimensionLeaving != BzDimensionType.BUMBLEZONE_TYPE) {
            Bumblezone.PLAYER_COMPONENT.get(this).setNonBZDimension(dimensionLeaving);
        }
    }
}