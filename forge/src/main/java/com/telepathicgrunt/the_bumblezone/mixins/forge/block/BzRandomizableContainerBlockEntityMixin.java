package com.telepathicgrunt.the_bumblezone.mixins.forge.block;

import com.telepathicgrunt.the_bumblezone.blocks.blockentities.BzRandomizableContainerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BzRandomizableContainerBlockEntity.class)
public abstract class BzRandomizableContainerBlockEntityMixin extends RandomizableContainerBlockEntity implements WorldlyContainer {

    @Shadow
    abstract Direction getInputDirection();

    protected BzRandomizableContainerBlockEntityMixin(BlockEntityType<?> arg, BlockPos arg2, BlockState arg3) {
        super(arg, arg2, arg3);
    }

    @Override
    protected @NotNull IItemHandler createUnSidedHandler() {
        return new SidedInvWrapper(this, getInputDirection());
    }
}
