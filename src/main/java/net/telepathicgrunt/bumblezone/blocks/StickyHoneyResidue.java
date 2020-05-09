package net.telepathicgrunt.bumblezone.blocks;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SixWayBlock;
import net.minecraft.block.VineBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class StickyHoneyResidue extends VineBlock {
    public static final BooleanProperty DOWN = SixWayBlock.DOWN;
    protected static final VoxelShape DOWN_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 0.8D, 16.0D);
    public static final Map<Direction, BooleanProperty> FACING_TO_PROPERTY_MAP = 
	    			SixWayBlock.FACING_TO_PROPERTY_MAP.entrySet().stream().collect(Util.toMapCollector());

    public StickyHoneyResidue() {
	super(Block.Properties.create(Material.ORGANIC, MaterialColor.ADOBE).doesNotBlockMovement().hardnessAndResistance(0.1F).notSolid());
	this.setDefaultState(this.stateContainer.getBaseState()
		.with(UP, Boolean.valueOf(false))
		.with(NORTH, Boolean.valueOf(false))
		.with(EAST, Boolean.valueOf(false))
		.with(SOUTH, Boolean.valueOf(false))
		.with(WEST, Boolean.valueOf(false))
		.with(DOWN, Boolean.valueOf(false)));
    }

    /**
     * Set up properties.
     */
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
	builder.add(UP, NORTH, EAST, SOUTH, WEST, DOWN);
    }

    /**
     * Returns the shape based on the state of the block.
     */
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
	VoxelShape voxelshape = VoxelShapes.empty();
	if (state.get(UP)) {
	    voxelshape = VoxelShapes.or(voxelshape, UP_AABB);
	}

	else if (state.get(NORTH)) {
	    voxelshape = VoxelShapes.or(voxelshape, NORTH_AABB);
	}

	else if (state.get(EAST)) {
	    voxelshape = VoxelShapes.or(voxelshape, EAST_AABB);
	}

	else if (state.get(SOUTH)) {
	    voxelshape = VoxelShapes.or(voxelshape, SOUTH_AABB);
	}

	else if (state.get(WEST)) {
	    voxelshape = VoxelShapes.or(voxelshape, WEST_AABB);
	}

	else if (state.get(DOWN)) {
	    voxelshape = VoxelShapes.or(voxelshape, DOWN_AABB);
	}

	return voxelshape;
    }
    
    
    /**
     * Slows all entities inside the block.
     */
    @Deprecated
    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos position, Entity entity) {

	AxisAlignedBB axisalignedbb = getShape(state, world, position, null).getBoundingBox().offset(position);
	List<? extends Entity> list = world.getEntitiesWithinAABB(LivingEntity.class, axisalignedbb);

	if (list.contains(entity)) {
	    entity.setMotionMultiplier(state, new Vec3d(0.35D, (double)0.2F, 0.35D));
	}
    }

    /**
     * Is spot valid (has at least 1 face possible).
     */
    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
	return this.hasAtleastOneAttachment(this.setAttachments(state, worldIn, pos));
    }

    /**
     * Returns true if the block has at least one face (it exists).
     */
    private boolean hasAtleastOneAttachment(BlockState p_196543_1_) {
	return this.numberOfAttachments(p_196543_1_) > 0;
    }

    /**
     * How many faces this block has at this time.
     */
    private int numberOfAttachments(BlockState blockstate) {
	int i = 0;

	for (BooleanProperty booleanproperty : FACING_TO_PROPERTY_MAP.values()) {
	    if (blockstate.get(booleanproperty)) {
		++i;
	    }
	}

	return i;
    }

    /**
     * Set the state based on solid blocks around it.
     */
    private BlockState setAttachments(BlockState blockstate, IBlockReader blockReader, BlockPos blockpos) {
	if (blockstate.get(UP)) {
	    blockstate = blockstate.with(UP, Boolean.valueOf(canAttachTo(blockReader, blockpos.offset(Direction.UP), Direction.UP)));
	}

	if (blockstate.get(DOWN)) {
	    blockstate = blockstate.with(DOWN, Boolean.valueOf(canAttachTo(blockReader, blockpos.offset(Direction.DOWN), Direction.DOWN)));
	}

	BlockState blockstateNearby = null;

	for (Direction direction : Direction.Plane.HORIZONTAL) {
	    BooleanProperty booleanproperty = FACING_TO_PROPERTY_MAP.get(direction);
	    if (blockstate.get(booleanproperty)) {
		boolean flag = canAttachTo(blockReader, blockpos.offset(direction), direction);
		if (!flag) {
		    if (blockstateNearby == null) {
			blockstateNearby = blockReader.getBlockState(blockpos);
		    }

		    flag = blockstateNearby.getBlock() == this && blockstateNearby.get(booleanproperty);
		}

		blockstate = blockstate.with(booleanproperty, Boolean.valueOf(flag));
	    }
	}

	return blockstate;
    }

    /**
     * allows player to add more faces to this block based on player's direction.
     */
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
	BlockState blockstate = context.getWorld().getBlockState(context.getPos());
	boolean flag = blockstate.getBlock() == this;
	BlockState blockstate1 = flag ? blockstate : this.getDefaultState();

	for (Direction direction : context.getNearestLookingDirections()) {
	    BooleanProperty booleanproperty = FACING_TO_PROPERTY_MAP.get(direction);
	    boolean flag1 = flag && blockstate.get(booleanproperty);
	    if (!flag1 && VineBlock.canAttachTo(context.getWorld(), context.getPos().offset(direction), direction)) {
		return blockstate1.with(booleanproperty, Boolean.valueOf(true));
	    }
	}

	return flag ? blockstate1 : null;
    }

    /**
     * double check to make sure this block has at least one face and can attach.
     */
    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
	BlockState blockstate = this.setAttachments(stateIn, worldIn, currentPos);
	return !this.hasAtleastOneAttachment(blockstate) ? Blocks.AIR.getDefaultState() : blockstate;
    }
    
    /**
     * Destroyed by pistons.
     */
    @Override
    public PushReaction getPushReaction(BlockState state) {
	return PushReaction.DESTROY;
    }
    
    /**
     * Will make this block climbable if any side is true except for down.
     */
    @Override
    public boolean isLadder(BlockState state, IWorldReader world, BlockPos pos, net.minecraft.entity.LivingEntity entity) { 
	
	boolean isClimbable = false;
	for (Direction direction : Direction.values()) {
	    if(direction == Direction.DOWN) continue;
	    BooleanProperty booleanproperty = FACING_TO_PROPERTY_MAP.get(direction);
	    if(state.get(booleanproperty))
		isClimbable = true;
	}
	return isClimbable; 
    }
    
    /**
     * No mobs can spawn in this blcok
     */
    @Override
    public boolean canEntitySpawn(BlockState state, IBlockReader worldIn, BlockPos pos, EntityType<?> type) { return false; }
}
