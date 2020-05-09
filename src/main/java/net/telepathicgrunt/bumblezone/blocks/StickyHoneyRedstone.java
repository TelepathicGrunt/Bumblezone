package net.telepathicgrunt.bumblezone.blocks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class StickyHoneyRedstone extends StickyHoneyResidue {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    protected static final AxisAlignedBB DOWN_REAL_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1D, 0.2D, 1D);
    protected static final AxisAlignedBB UP_REAL_AABB = new AxisAlignedBB(0.0D, 0.8D, 0.0D, 1D, 1D, 1D);
    protected static final AxisAlignedBB NORTH_REAL_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1D, 1D, 0.2D);
    protected static final AxisAlignedBB EAST_REAL_AABB = new AxisAlignedBB(0.8D, 0.0D, 0.0D, 1D, 1D, 1D);
    protected static final AxisAlignedBB WEST_REAL_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.2D, 1D, 1D);
    protected static final AxisAlignedBB SOUTH_REAL_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.2D, 1D, 1D, 1D);
    public static final Map<Direction, AxisAlignedBB> FACING_TO_AABB_MAP;
    static {
	Map<Direction, AxisAlignedBB> map = new HashMap<Direction, AxisAlignedBB>();

	map.put(Direction.DOWN, DOWN_REAL_AABB);
	map.put(Direction.UP, UP_REAL_AABB);
	map.put(Direction.EAST, EAST_REAL_AABB);
	map.put(Direction.WEST, WEST_REAL_AABB);
	map.put(Direction.NORTH, NORTH_REAL_AABB);
	map.put(Direction.SOUTH, SOUTH_REAL_AABB);
	
	FACING_TO_AABB_MAP = map;
    }
    
    public StickyHoneyRedstone() {
	super();
	this.setDefaultState(this.stateContainer.getBaseState()
		.with(UP, Boolean.valueOf(false))
		.with(NORTH, Boolean.valueOf(false))
		.with(EAST, Boolean.valueOf(false))
		.with(SOUTH, Boolean.valueOf(false))
		.with(WEST, Boolean.valueOf(false))
		.with(DOWN, Boolean.valueOf(false))
		.with(POWERED, false));
    }

    /**
     * Set up properties.
     */
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
	builder.add(UP, NORTH, EAST, SOUTH, WEST, DOWN, POWERED);
    }

    /**
     * Slows all entities inside the block and triggers being powered.
     */
    @Deprecated
    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos position, Entity entity) {
	updateState(world, position, state, 0);
	super.onEntityCollision(state, world, position, entity);
    }

    @Override
    public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
	return blockState.get(POWERED) ? 1 : 0;
    }
    
    /**
     * Remove vine's ticking with removing power instead.
     */
    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
	this.updateState(worldIn, pos, state, state.get(POWERED) ? 1 : 0);
    }
    
    /**
     * Notifies blocks that this block is attached to of changes
     */
    protected void updateNeighbors(BlockState state, World world, BlockPos pos) {
	if (net.minecraftforge.event.ForgeEventFactory.onNeighborNotify(world, pos, world.getBlockState(pos), java.util.EnumSet.allOf(Direction.class), false).isCanceled())
	    return;

	for (Direction direction : Direction.values()) {
	    BooleanProperty booleanproperty = FACING_TO_PROPERTY_MAP.get(direction);
	    if (state.get(booleanproperty)) {
		world.neighborChanged(pos.offset(direction), state.getBlock(), pos);
	    }
	}
    }
    

    /**
     * notify neighbor of changes when replaced
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
       if (!isMoving && state.getBlock() != newState.getBlock()) {
          if (state.get(POWERED)) {
             this.updateNeighbors(state, worldIn, pos);
          }

          super.onReplaced(state, worldIn, pos, newState, isMoving);
       }
    }
    
    /**
     * Updates the sticky residue block when entity enters or leaves
     */
    protected void updateState(World worldIn, BlockPos pos, BlockState state, int oldRedstoneStrength) {
       int newPower = this.computeRedstoneStrength(state, worldIn, pos);
       boolean flag1 = newPower > 0;
       if (oldRedstoneStrength != newPower) {
          BlockState blockstate = this.setRedstoneStrength(state, newPower);
          worldIn.setBlockState(pos, blockstate, 2);
          this.updateNeighbors(state, worldIn, pos);
          worldIn.markBlockRangeForRenderUpdate(pos, state, blockstate);
       }

       if (flag1) {
          worldIn.getPendingBlockTicks().scheduleTick(new BlockPos(pos), this, this.tickRate(worldIn));
       }
    }


    /**
     * Set if block is powered or not
     */
    protected BlockState setRedstoneStrength(BlockState state, int strength) {
	return state.with(POWERED, Boolean.valueOf(strength > 0));
    }

    /**
     * Detects if any entity is inside this block and outputs power if so
     */
    protected int computeRedstoneStrength(BlockState state, World worldIn, BlockPos pos) {

	AxisAlignedBB axisalignedbb = getShape(state, worldIn, pos, null).getBoundingBox().offset(pos);
	List<? extends Entity> list = worldIn.getEntitiesWithinAABB(LivingEntity.class, axisalignedbb);

	if (!list.isEmpty()) {
	    return 1;
	}
//	for (Direction direction : Direction.values()) {
//	    BooleanProperty booleanproperty = FACING_TO_PROPERTY_MAP.get(direction);
//	    if (state.get(booleanproperty)) {
//	    }
//	}

	return 0;
    }
}
