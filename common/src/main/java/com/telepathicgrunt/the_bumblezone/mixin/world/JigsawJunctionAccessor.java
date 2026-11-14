package com.telepathicgrunt.the_bumblezone.mixin.world;

import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@org.spongepowered.asm.mixin.Mixin(net.minecraft.world.level.levelgen.structure.pools.JigsawJunction.class)
public interface JigsawJunctionAccessor {
    @Mutable
    @Accessor("sourceX")
    void bumblezone$setSourceX(int sourceX);

    @Mutable
    @Accessor("sourceZ")
    void bumblezone$setSourceZ(int sourceZ);
}
