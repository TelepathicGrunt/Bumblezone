package com.telepathicgrunt.the_bumblezone.mixin.items;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PlayerEntity.class)
public interface PlayerDamageShieldInvoker {

    @Invoker("hurtCurrentlyUsedShield")
    void thebumblezone_callHurtCurrentlyUsedShield(float amount);

}
