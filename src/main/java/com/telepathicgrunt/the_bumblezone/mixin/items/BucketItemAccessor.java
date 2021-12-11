package com.telepathicgrunt.the_bumblezone.mixin.items;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BucketItem.class)
public interface BucketItemAccessor {

    @Accessor("content")
    Fluid thebumblezone_getFluid();
}
