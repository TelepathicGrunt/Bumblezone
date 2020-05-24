package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BucketItem.class)
public interface BucketItemAccessor {

    @Accessor("fluid")
    public Fluid getFluid();
}
