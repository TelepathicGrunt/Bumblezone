package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.telepathicgrunt.bumblezone.entities.BeeAggression;
import net.telepathicgrunt.bumblezone.entities.PlayerTeleportation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerTeleportationTickMixin {
    // Handles player teleportation out of Bumblezone dimension
    @Inject(method = "tick",
            at = @At(value = "TAIL"))
    private void onEntityTick(CallbackInfo ci) {
        PlayerTeleportation.playerTick(((PlayerEntity) (Object) this));
        BeeAggression.playerTick(((PlayerEntity) (Object) this));
    }

}