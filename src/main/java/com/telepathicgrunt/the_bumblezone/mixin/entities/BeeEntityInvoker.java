package com.telepathicgrunt.the_bumblezone.mixin.entities;

import net.minecraft.world.entity.animal.Bee;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Bee.class)
public interface BeeEntityInvoker {

    @Invoker("setHasNectar")
    void thebumblezone_callSetHasNectar(boolean value);
}
