package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.telepathicgrunt.the_bumblezone.entities.TemporaryPlayerData;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = LivingEntity.class, priority = 800)
public abstract class LaterLivingEntityMixin {

    // Stop Origins allowing people to cheese some structures by climbing
    @WrapMethod(method = "onClimbable()Z")
    private boolean bumblezone$stopClimbingInHeavyAir(Operation<Boolean> original) {
        if (!original.call()) {
            return false;
        }

        if (this instanceof TemporaryPlayerData temporaryPlayerData && temporaryPlayerData.bumblezonePlayerInHeavyAir()) {
            return false;
        }

        return true;
    }
}
