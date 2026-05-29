package com.telepathicgrunt.the_bumblezone.mixin.util;

import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@org.spongepowered.asm.mixin.Mixin(net.minecraft.world.phys.Vec3.class)
public interface Vec3Accessor {
    @Mutable
    @Accessor("x")
    void bumblezone$setX(double x);

    @Mutable
    @Accessor("y")
    void bumblezone$setY(double y);

    @Mutable
    @Accessor("z")
    void bumblezone$setZ(double z);

}
