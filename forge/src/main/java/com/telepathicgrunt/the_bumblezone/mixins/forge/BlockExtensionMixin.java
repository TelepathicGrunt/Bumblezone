package com.telepathicgrunt.the_bumblezone.mixins.forge;

import com.telepathicgrunt.the_bumblezone.platform.BlockExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.common.extensions.IForgeBlock;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;

@Mixin(BlockExtension.class)
public interface BlockExtensionMixin extends IForgeBlock {

    @Shadow
    BlockPathTypes bz$getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, Mob mob);

    @Shadow
    boolean bz$isStickyBlock(BlockState state);

    @Shadow
    Optional<Boolean> bz$canStickTo(BlockState state, BlockState other);

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
}
