package com.telepathicgrunt.the_bumblezone.fluids.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class BzFlowingFluid extends FlowingFluid {

    private final FluidInfo info;

    public BzFlowingFluid(FluidInfo info, boolean source) {
        this.info = info;
        if (source) {
            info.setSource(() -> this);
        } else {
            info.setFlowing(() -> this);
        }
    }

    public FluidInfo info() {
        return info;
    }

    @Override
    public Fluid getFlowing() {
        return info.flowing();
    }

    @Override
    public Fluid getSource() {
        return info.source();
    }

    @Override
    protected boolean canConvertToSource(@NotNull Level level) {
        return info.properties().canConvertToSource();
    }

    @Override
    protected void beforeDestroyingBlock(@NotNull LevelAccessor level, @NotNull BlockPos pos, BlockState blockState) {
        final BlockEntity blockEntity = blockState.hasBlockEntity() ? level.getBlockEntity(pos) : null;
        Block.dropResources(blockState, level, pos, blockEntity);
    }

    @Override
    protected int getSlopeFindDistance(@NotNull LevelReader level) {
        return info.properties().slopeFindDistance();
    }

    @Override
    protected int getDropOff(@NotNull LevelReader level) {
        return info.properties().dropOff();
    }

    @Override
    public Item getBucket() {
        final Item bucket = info.bucket();
        return bucket == null ? Items.AIR : bucket;
    }

    @Override
    protected boolean canBeReplacedWith(@NotNull FluidState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull Fluid fluid, @NotNull Direction direction) {
        return Direction.DOWN == direction && !isSame(fluid);
    }

    @Override
    public int getTickDelay(@NotNull LevelReader level) {
        return info.properties().tickDelay();
    }

    @Override
    protected float getExplosionResistance() {
        return info.properties().explosionResistance();
    }

    @Override
    protected BlockState createLegacyBlock(@NotNull FluidState state) {
        Block block = info.block();
        if (block == null) return Blocks.AIR.defaultBlockState();
        return block.defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
    }

    @Override
    public boolean isSame(@NotNull Fluid fluid) {
        return fluid == info.source() || fluid == info.flowing();
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        final SoundEvent event = info.properties().sounds().getOrDefault("bucket_fill", () -> null).get();
        return event == null ? Optional.of(SoundEvents.BUCKET_FILL) : Optional.of(event);
    }

    public static class Source extends BzFlowingFluid {
        public Source(FluidInfo info) {
            super(info, true);
        }

        @Override
        public boolean isSource(@NotNull FluidState state) {
            return true;
        }

        @Override
        public int getAmount(@NotNull FluidState state) {
            return 8;
        }
    }

    public static class Flowing extends BzFlowingFluid {
        public Flowing(FluidInfo info) {
            super(info, false);
            this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, 7));
        }

        @Override
        protected void createFluidStateDefinition(@NotNull StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        public boolean isSource(@NotNull FluidState state) {
            return false;
        }

        @Override
        public int getAmount(@NotNull FluidState state) {
            return state.getValue(LEVEL);
        }
    }
}
