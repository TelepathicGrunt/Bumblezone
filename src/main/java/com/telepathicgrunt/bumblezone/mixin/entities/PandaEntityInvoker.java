package com.telepathicgrunt.bumblezone.mixin.entities;

import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.PandaEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PandaEntity.class)
public interface PandaEntityInvoker {

    @Invoker("sneeze")
    void thebumblezone_callSneeze();
}
