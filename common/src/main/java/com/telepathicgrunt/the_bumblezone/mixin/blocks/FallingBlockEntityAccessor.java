package com.telepathicgrunt.the_bumblezone.mixin.blocks;

import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FallingBlockEntity.class)
public interface FallingBlockEntityAccessor {
    @Invoker("<init>")
    static FallingBlockEntity bumblezone$FallingBlockEntity(Level p_31953_, double p_31954_, double p_31955_, double p_31956_, BlockState p_31957_) {
        throw new UnsupportedOperationException();
    }

    @Accessor("blockState")
    void bumblezone$setBlockState(BlockState blockState);
}
