package com.telepathicgrunt.the_bumblezone.mixin.items;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Player.class)
public interface PlayerDamageShieldInvoker {

    @Invoker("hurtCurrentlyUsedShield")
    void callDamagedShield(float amount);
}
