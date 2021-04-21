package com.telepathicgrunt.bumblezone.mixin.items;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PlayerEntity.class)
public interface PlayerDamageShieldInvoker {

    @Invoker("damageShield")
    void bz_callDamagedShield(float amount);

}
