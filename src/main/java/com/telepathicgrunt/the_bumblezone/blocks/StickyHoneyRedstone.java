package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.AABB;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class StickyHoneyRedstone extends StickyHoneyResidue {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    protected static final AABB DOWN_REAL_AABB = new AABB(0.0D, 0.0D, 0.0D, 1D, 0.2D, 1D);
    protected static final AABB UP_REAL_AABB = new AABB(0.0D, 0.8D, 0.0D, 1D, 1D, 1D);
    protected static final AABB NORTH_REAL_AABB = new AABB(0.0D, 0.0D, 0.0D, 1D, 1D, 0.2D);
    protected static final AABB EAST_REAL_AABB = new AABB(0.8D, 0.0D, 0.0D, 1D, 1D, 1D);
    protected static final AABB WEST_REAL_AABB = new AABB(0.0D, 0.0D, 0.0D, 0.2D, 1D, 1D);
    protected static final AABB SOUTH_REAL_AABB = new AABB(0.0D, 0.0D, 0.2D, 1D, 1D, 1D);
    public static final Map<Direction, AABB> FACING_TO_AABB_MAP;

    static {
        Map<Direction, AABB> map = new HashMap<Direction, AABB>();

        map.put(Direction.DOWN, DOWN_REAL_AABB);
        map.put(Direction.UP, UP_REAL_AABB);
        map.put(Direction.EAST, EAST_REAL_AABB);
        map.put(Direction.WEST, WEST_REAL_AABB);
        map.put(Direction.NORTH, NORTH_REAL_AABB);
        map.put(Direction.SOUTH, SOUTH_REAL_AABB);

        FACING_TO_AABB_MAP = map;
    }

    public StickyHoneyRedstone() {
        super(FabricBlockSettings.of(BzBlocks.ORANGE_NOT_SOLID, MaterialColor.TERRACOTTA_ORANGE).lightLevel(blockState -> blockState.getValue(POWERED) ? 1 : 0).noCollission().strength(6.0f, 0.0f).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(UP, false)
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(DOWN, false)
                .setValue(POWERED, false));
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
    public void entityInside(BlockState blockstate, Level world, BlockPos pos, Entity entity) {
        updateState(world, pos, blockstate, 0);
        super.entityInside(blockstate, world, pos, entity);
    }

    protected int getTickRate() {
        return 20;
    }

    /**
     * Remove vine's ticking with removing power instead.
     */
    @Override
    public void tick(BlockState blockstate, ServerLevel world, BlockPos pos, Random rand) {
        this.updateState(world, pos, blockstate, blockstate.getValue(POWERED) ? 1 : 0);
    }

    /**
     * Notifies blocks that this block is attached to of changes
     */
    protected void updateNeighbors(BlockState blockstate, Level world, BlockPos pos) {
        if (blockstate.getBlock() != BzBlocks.STICKY_HONEY_REDSTONE)
            return;

        if (blockstate.getValue(StickyHoneyResidue.FACING_TO_PROPERTY_MAP.get(Direction.DOWN))) {
            world.blockUpdated(pos, this);
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
            world.onBlockStateChange(pos, oldBlockstate, newBlockstate);
        }

        if (flag1) {
            world.scheduleTick(new BlockPos(pos), this, this.getTickRate());
        }
    }

    /**
     * notify neighbor of changes when replaced
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState blockstate, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!isMoving && blockstate.getBlock() != newState.getBlock()) {
            if (blockstate.getValue(POWERED)) {
                this.updateTarget(world, pos, blockstate);
            }

            super.onRemove(blockstate, world, pos, newState, false);
        }
    }

    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean moved) {
        this.updateTarget(world, pos, state);
    }

    protected void updateTarget(Level world, BlockPos pos, BlockState blockstate) {
        for (Direction direction : Direction.values()) {
            if (blockstate.getValue(StickyHoneyResidue.FACING_TO_PROPERTY_MAP.get(direction))) {
                BlockPos blockPos = pos.relative(direction);
                world.neighborChanged(blockPos, this, pos);
                world.updateNeighborsAtExceptFromFacing(blockPos, this, direction);
            }
        }
    }

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

        AABB axisalignedbb = getShape(blockstate, world, pos, null).bounds().move(pos);
        List<? extends Entity> list = world.getEntitiesOfClass(LivingEntity.class, axisalignedbb);

        if (!list.isEmpty()) {
            return 1;
        }

        return 0;
    }
}
