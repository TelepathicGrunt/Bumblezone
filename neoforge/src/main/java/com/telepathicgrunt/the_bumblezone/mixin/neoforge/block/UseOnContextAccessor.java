package com.telepathicgrunt.the_bumblezone.mixin.neoforge.block;

import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(UseOnContext.class)
public interface UseOnContextAccessor {

    @Accessor("hitResult")
    BlockHitResult bz$getHitResult();
}
