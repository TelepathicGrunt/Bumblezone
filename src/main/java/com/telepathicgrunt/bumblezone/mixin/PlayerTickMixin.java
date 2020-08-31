package com.telepathicgrunt.bumblezone.mixin;

import net.minecraft.entity.player.PlayerEntity;
import com.telepathicgrunt.bumblezone.entities.BeeAggression;
import com.telepathicgrunt.bumblezone.entities.PlayerTeleportation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerTickMixin {
    // Handles where wrath of the hive can be on,
    // change player fog color when effect is active
    @Inject(method = "tick",
            at = @At(value = "TAIL"))
    private void onEntityTick(CallbackInfo ci) {
        PlayerEntity playerEntity = ((PlayerEntity) (Object) this);

        BeeAggression.playerTick(playerEntity);

        if(!playerEntity.world.isRemote)
            PlayerTeleportation.playerTick(playerEntity);
    }
}