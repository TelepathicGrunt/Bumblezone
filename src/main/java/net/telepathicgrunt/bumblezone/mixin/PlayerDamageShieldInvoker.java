package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PlayerEntity.class)
public interface PlayerDamageShieldInvoker {

    @Invoker("damageShield")
    void callDamagedShield(float amount);

}
