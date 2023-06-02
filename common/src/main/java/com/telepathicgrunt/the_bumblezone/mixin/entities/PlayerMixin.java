package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.telepathicgrunt.the_bumblezone.items.essence.CalmingEssence;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin {

    @Inject(method = "canBeSeenAsEnemy()Z",
            at = @At(value = "RETURN"),
            cancellable = true)
    private void bumblezone$preventAngerableAtPlayer1(CallbackInfoReturnable<Boolean> cir) {
        Player player = (Player) (Object) this;
        if (CalmingEssence.IsCalmingEssenceActive(player)) {
            cir.setReturnValue(false);
        }
    }
}