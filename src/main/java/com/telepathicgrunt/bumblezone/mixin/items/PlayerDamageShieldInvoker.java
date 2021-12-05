package com.telepathicgrunt.bumblezone.mixin.items;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Player.class)
public interface PlayerDamageShieldInvoker {

    @Invoker("hurtCurrentlyUsedShield")
    void thebumblezone_callDamagedShield(float amount);
}
