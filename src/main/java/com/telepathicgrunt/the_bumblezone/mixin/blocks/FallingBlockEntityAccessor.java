package com.telepathicgrunt.the_bumblezone.mixin.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.entity.item.FallingBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FallingBlockEntity.class)
public interface FallingBlockEntityAccessor {
    @Accessor("blockState")
    void bumblezone_setBlockState(BlockState blockState);
}
