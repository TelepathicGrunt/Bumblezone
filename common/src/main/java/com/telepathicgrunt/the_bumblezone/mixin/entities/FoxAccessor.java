package com.telepathicgrunt.the_bumblezone.mixin.entities;

import net.minecraft.world.entity.animal.Fox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.UUID;

@Mixin(Fox.class)
public interface FoxAccessor {
    @Invoker("trusts")
    boolean callTrusts(UUID uUID);
}
