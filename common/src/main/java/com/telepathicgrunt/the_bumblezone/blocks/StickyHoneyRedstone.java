package com.telepathicgrunt.the_bumblezone.blocks;

import com.mojang.serialization.MapCodec;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ARGB;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class StickyHoneyRedstone extends StickyHoneyResidue {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public static final MapCodec<StickyHoneyRedstone> CODEC = Block.simpleCodec(StickyHoneyRedstone::new);

    public StickyHoneyRedstone(Properties properties) {
        super(properties
                .mapColor(MapColor.TERRACOTTA_RED)
                .lightLevel(blockState -> blockState.getValue(POWERED) ? 1 : 0)
                .noCollision()
                .strength(6.0f, 0.0f)
                .noOcclusion()
                .replaceable()
                .pushReaction(PushReaction.DESTROY));

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(UP, false)
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(DOWN, false)
                .setValue(POWERED, false));
    }

    @Override
    public MapCodec<? extends StickyHoneyRedstone> codec() {
        return CODEC;
    }

    /**
     * Set up properties.
     */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add().add(UP, NORTH, EAST, SOUTH, WEST, DOWN, POWERED);
    }

    /**
     * Slows all entities inside the block and triggers being powered.
     */
    @Deprecated
    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        updateState(level, blockPos, blockState, 0);
        super.entityInside(blockState, level, blockPos, entity, effectApplier, isPrecise);
    }

    protected int getTickRate() {
        return 20;
    }

    /**
     * Remove vine's ticking with removing power instead.
     */
    @Override
    public void tick(BlockState blockstate, ServerLevel world, BlockPos pos, RandomSource rand) {
        this.updateState(world, pos, blockstate, blockstate.getValue(POWERED) ? 1 : 0);
    }

    /**
     * Notifies blocks that this block is attached to of changes
     */
    protected void updateNeighbors(BlockState blockstate, Level world, BlockPos pos) {
        if (blockstate.getBlock() != BzBlocks.STICKY_HONEY_REDSTONE.get())
            return;

        if (blockstate.getValue(StickyHoneyResidue.FACING_TO_PROPERTY_MAP.get(Direction.DOWN))) {
            world.updateNeighborsAt(pos, this);
        }

        for (Direction direction : Direction.values()) {
            BooleanProperty booleanproperty = StickyHoneyResidue.FACING_TO_PROPERTY_MAP.get(direction);
            if (blockstate.getValue(booleanproperty)) {
                world.updateNeighborsAt(pos.relative(direction), this);
            }
        }
    }

    /**
     * Updates the sticky residue block when entity enters or leaves
     */
    protected void updateState(Level world, BlockPos pos, BlockState oldBlockstate, int oldRedstoneStrength) {
        int newPower = this.computeRedstoneStrength(oldBlockstate, world, pos);
        boolean flag1 = newPower > 0;
        if (oldRedstoneStrength != newPower) {
            BlockState newBlockstate = this.setRedstoneStrength(oldBlockstate, newPower);
            world.setBlock(pos, newBlockstate, 2);
            this.updateNeighbors(oldBlockstate, world, pos);
        }

        if (flag1) {
            world.scheduleTick(new BlockPos(pos), this, this.getTickRate());
        }
    }

    /**
     * notify neighbor of changes when replaced
     */
    @Override
    protected void affectNeighborsAfterRemoval(BlockState blockState, ServerLevel level, BlockPos blockPos, boolean pushed) {
        if (!pushed) {
            if (blockState.getValue(POWERED)) {
                this.updateTarget(level, blockPos, blockState);
            }

            super.affectNeighborsAfterRemoval(blockState, level, blockPos, false);
        }
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean moved) {
        this.updateTarget(world, pos, state);
    }

    protected void updateTarget(Level level, BlockPos pos, BlockState blockstate) {
        Orientation orientation = ExperimentalRedstoneUtils.initialOrientation(level, null, Direction.UP);

        for (Direction direction : Direction.values()) {
            if (blockstate.getValue(StickyHoneyResidue.FACING_TO_PROPERTY_MAP.get(direction))) {
                BlockPos blockPos = pos.relative(direction);
                level.neighborChanged(blockPos, this, orientation.withFront(direction));
                level.updateNeighborsAtExceptFromFacing(blockPos, this, direction, orientation.withFront(direction));
            }
        }
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (this.computeRedstoneStrength(state, world, pos) > 0) {
            world.scheduleTick(pos, this, 1);
        }
    }


    ///////////////////////////////////REDSTONE////////////////////////////////////////


    /**
     * Tells game that this block can generate a Redstone signal
     */
    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    /**
     * Powers the block it's attached to. Or powers blocks next to if it's on the floor
     */
    @Override
    public int getSignal(BlockState blockstate, BlockGetter blockAccess, BlockPos pos, Direction side) {
        //power nearby blocks if on floor
        if (blockstate.getValue(POWERED) && blockstate.getValue(StickyHoneyResidue.FACING_TO_PROPERTY_MAP.get(Direction.DOWN))) {
            for (Direction horizontal : Direction.Plane.HORIZONTAL) {
                if(horizontal == side) {
                    return 1;
                }
            }
        }

        //return power for block it is attached on.
        return blockstate.getValue(POWERED) && blockstate.getValue(StickyHoneyResidue.FACING_TO_PROPERTY_MAP.get(side.getOpposite())) ? 1 : 0;
    }

    /**
     * Powers through the block that it is attached to.
     */
    @Override
    public int getDirectSignal(BlockState blockstate, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return getSignal(blockstate, blockAccess, pos, side);
    }


    /**
     * Set if block is powered or not
     */
    protected BlockState setRedstoneStrength(BlockState blockstate, int strength) {
        return blockstate.setValue(POWERED, strength > 0);
    }

    /**
     * Detects if any entity is inside this block and outputs power if so
     */
    protected int computeRedstoneStrength(BlockState blockstate, Level world, BlockPos pos) {

        VoxelShape voxelShape = getShape(blockstate, world, pos, null);
        if (voxelShape.isEmpty()) {
            return 0;
        }

        AABB axisalignedbb = voxelShape.bounds().move(pos);
        List<? extends Entity> list = world.getEntitiesOfClass(LivingEntity.class, axisalignedbb);

        if (!list.isEmpty()) {
            return 1;
        }

        return 0;
    }

    @Override
    public void animateTick(BlockState blockState, Level world, BlockPos position, RandomSource random) {
        super.animateTick(blockState, world, position, random);
        if (blockState.getValue(POWERED)) {
            for (int i = 0; i == random.nextInt(2); ++i) {
                Direction randomDirection = Direction.values()[random.nextInt(Direction.values().length)];
                this.addParticle(new DustParticleOptions(ARGB.colorFromFloat(1.0F, 255, 0, 0), 1.0F), random, world, position, blockState, randomDirection);
            }
        }
    }
}
