package com.telepathicgrunt.the_bumblezone.mixin.util;

import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@org.spongepowered.asm.mixin.Mixin(net.minecraft.world.phys.AABB.class)
public interface AABBAccessor {
    @Mutable
    @Accessor("maxZ")
    void bumblezone$setMaxZ(double maxZ);

    @Mutable
    @Accessor("maxY")
    void bumblezone$setMaxY(double maxY);

    @Mutable
    @Accessor("maxX")
    void bumblezone$setMaxX(double maxX);

    @Mutable
    @Accessor("minZ")
    void bumblezone$setMinZ(double minZ);

    @Mutable
    @Accessor("minY")
    void bumblezone$setMinY(double minY);

    @Mutable
    @Accessor("minX")
    void bumblezone$setMinX(double minX);
}
