package com.telepathicgrunt.the_bumblezone.mixin.entities;

import net.minecraft.world.entity.animal.Rabbit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Rabbit.class)
public interface RabbitAccessor {
    @Invoker("setVariant")
    void the_bumblezone$callSetVariant(Rabbit.Variant variant);
}
