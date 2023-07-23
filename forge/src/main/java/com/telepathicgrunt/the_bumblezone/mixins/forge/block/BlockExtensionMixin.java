package com.telepathicgrunt.the_bumblezone.mixins.forge.block;

import com.telepathicgrunt.the_bumblezone.platform.BlockExtension;
import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.common.extensions.IForgeBlock;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockExtension.class)
public interface BlockExtensionMixin extends IForgeBlock {

    @Shadow
    BlockPathTypes bz$getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, Mob mob);

    @Shadow
    boolean bz$isStickyBlock(BlockState state);

    @Shadow
    OptionalBoolean bz$canStickTo(BlockState state, BlockState other);

    @Shadow
    OptionalBoolean bz$shouldNotDisplayFluidOverlay();

    @Override
    default @Nullable BlockPathTypes getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob) {
        return this.bz$getBlockPathType(state, level, pos, mob);
    }

    @Override
    default boolean isStickyBlock(BlockState state) {
        return this.bz$isStickyBlock(state);
    }

    @Override
    default boolean canStickTo(BlockState state, BlockState other) {
        return this.bz$canStickTo(state, other)
                .orElseGet(() -> IForgeBlock.super.canStickTo(state, other));
    }

    @Override
    default boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter level, BlockPos pos, FluidState fluidState) {
        return this.bz$shouldNotDisplayFluidOverlay()
                .orElseGet(() -> IForgeBlock.super.shouldDisplayFluidOverlay(state, level, pos, fluidState));
    }
}
