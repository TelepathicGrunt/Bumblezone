package com.telepathicgrunt.bumblezone.blocks;

import com.telepathicgrunt.bumblezone.modinit.BzBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class StickyHoneyRedstone extends StickyHoneyResidue {
    public static final BooleanProperty POWERED = Properties.POWERED;
    protected static final Box DOWN_REAL_AABB = new Box(0.0D, 0.0D, 0.0D, 1D, 0.2D, 1D);
    protected static final Box UP_REAL_AABB = new Box(0.0D, 0.8D, 0.0D, 1D, 1D, 1D);
    protected static final Box NORTH_REAL_AABB = new Box(0.0D, 0.0D, 0.0D, 1D, 1D, 0.2D);
    protected static final Box EAST_REAL_AABB = new Box(0.8D, 0.0D, 0.0D, 1D, 1D, 1D);
    protected static final Box WEST_REAL_AABB = new Box(0.0D, 0.0D, 0.0D, 0.2D, 1D, 1D);
    protected static final Box SOUTH_REAL_AABB = new Box(0.0D, 0.0D, 0.2D, 1D, 1D, 1D);
    public static final Map<Direction, Box> FACING_TO_AABB_MAP;

    static {
        Map<Direction, Box> map = new HashMap<Direction, Box>();

        map.put(Direction.DOWN, DOWN_REAL_AABB);
        map.put(Direction.UP, UP_REAL_AABB);
        map.put(Direction.EAST, EAST_REAL_AABB);
        map.put(Direction.WEST, WEST_REAL_AABB);
        map.put(Direction.NORTH, NORTH_REAL_AABB);
        map.put(Direction.SOUTH, SOUTH_REAL_AABB);

        FACING_TO_AABB_MAP = map;
    }

    public StickyHoneyRedstone() {
        super(FabricBlockSettings.of(BzBlocks.ORANGE_NOT_SOLID, MapColor.TERRACOTTA_ORANGE).luminance(blockState -> blockState.get(POWERED) ? 1 : 0).noCollision().strength(6.0f, 0.0f).nonOpaque());
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(UP, false)
                .with(NORTH, false)
                .with(EAST, false)
                .with(SOUTH, false)
                .with(WEST, false)
                .with(DOWN, false)
                .with(POWERED, false));
    }

    /**
     * Set up properties.
     */
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add().add(UP, NORTH, EAST, SOUTH, WEST, DOWN, POWERED);
    }

    /**
     * Slows all entities inside the block and triggers being powered.
     */
    @Deprecated
    @Override
    public void onEntityCollision(BlockState blockstate, World world, BlockPos pos, Entity entity) {
        updateState(world, pos, blockstate, 0);
        super.onEntityCollision(blockstate, world, pos, entity);
    }

    protected int getTickRate() {
        return 20;
    }

    /**
     * Remove vine's ticking with removing power instead.
     */
    @Override
    public void scheduledTick(BlockState blockstate, ServerWorld world, BlockPos pos, Random rand) {
        this.updateState(world, pos, blockstate, blockstate.get(POWERED) ? 1 : 0);
    }

    /**
     * Notifies blocks that this block is attached to of changes
     */
    protected void updateNeighbors(BlockState blockstate, World world, BlockPos pos) {
        if (blockstate.getBlock() != BzBlocks.STICKY_HONEY_REDSTONE)
            return;

        if (blockstate.get(StickyHoneyResidue.FACING_TO_PROPERTY_MAP.get(Direction.DOWN))) {
            world.updateNeighbors(pos, this);
        }

        for (Direction direction : Direction.values()) {
            BooleanProperty booleanproperty = StickyHoneyResidue.FACING_TO_PROPERTY_MAP.get(direction);
            if (blockstate.get(booleanproperty)) {
                world.updateNeighborsAlways(pos.offset(direction), this);
            }
        }
    }

    /**
     * Updates the sticky residue block when entity enters or leaves
     */
    protected void updateState(World world, BlockPos pos, BlockState oldBlockstate, int oldRedstoneStrength) {
        int newPower = this.computeRedstoneStrength(oldBlockstate, world, pos);
        boolean flag1 = newPower > 0;
        if (oldRedstoneStrength != newPower) {
            BlockState newBlockstate = this.setRedstoneStrength(oldBlockstate, newPower);
            world.setBlockState(pos, newBlockstate, 2);
            this.updateNeighbors(oldBlockstate, world, pos);
            world.onBlockChanged(pos, oldBlockstate, newBlockstate);
        }

        if (flag1) {
            world.getBlockTickScheduler().schedule(new BlockPos(pos), this, this.getTickRate());
        }
    }

    /**
     * notify neighbor of changes when replaced
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onStateReplaced(BlockState blockstate, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!isMoving && blockstate.getBlock() != newState.getBlock()) {
            if (blockstate.get(POWERED)) {
                this.updateTarget(world, pos, blockstate);
            }

            super.onStateReplaced(blockstate, world, pos, newState, false);
        }
    }

    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean moved) {
        this.updateTarget(world, pos, state);
    }

    protected void updateTarget(World world, BlockPos pos, BlockState blockstate) {
        for (Direction direction : Direction.values()) {
            if (blockstate.get(StickyHoneyResidue.FACING_TO_PROPERTY_MAP.get(direction))) {
                BlockPos blockPos = pos.offset(direction);
                world.updateNeighbor(blockPos, this, pos);
                world.updateNeighborsExcept(blockPos, this, direction);
            }
        }
    }

    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (this.computeRedstoneStrength(state, world, pos) > 0) {
            world.getBlockTickScheduler().schedule(pos, this, 1);
        }

    }


    ///////////////////////////////////REDSTONE////////////////////////////////////////


    /**
     * Tells game that this block can generate a Redstone signal
     */
    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    /**
     * Powers the block it's attached to. Or powers blocks next to if it's on the floor
     */
    @Override
    public int getWeakRedstonePower(BlockState blockstate, BlockView blockAccess, BlockPos pos, Direction side) {
        //power nearby blocks if on floor
        if (blockstate.get(POWERED) && blockstate.get(StickyHoneyResidue.FACING_TO_PROPERTY_MAP.get(Direction.DOWN))) {
            for (Direction horizontal : Direction.Type.HORIZONTAL) {
                if(horizontal == side) {
                    return 1;
                }
            }
        }

        //return power for block it is attached on.
        return blockstate.get(POWERED) && blockstate.get(StickyHoneyResidue.FACING_TO_PROPERTY_MAP.get(side.getOpposite())) ? 1 : 0;
    }

    /**
     * Powers through the block that it is attached to.
     */
    @Override
    public int getStrongRedstonePower(BlockState blockstate, BlockView blockAccess, BlockPos pos, Direction side) {
        return getWeakRedstonePower(blockstate, blockAccess, pos, side);
    }


    /**
     * Set if block is powered or not
     */
    protected BlockState setRedstoneStrength(BlockState blockstate, int strength) {
        return blockstate.with(POWERED, strength > 0);
    }

    /**
     * Detects if any entity is inside this block and outputs power if so
     */
    protected int computeRedstoneStrength(BlockState blockstate, World world, BlockPos pos) {

        Box axisalignedbb = getOutlineShape(blockstate, world, pos, null).getBoundingBox().offset(pos);
        List<? extends Entity> list = world.getNonSpectatingEntities(LivingEntity.class, axisalignedbb);

        if (!list.isEmpty()) {
            return 1;
        }

        return 0;
    }
}
