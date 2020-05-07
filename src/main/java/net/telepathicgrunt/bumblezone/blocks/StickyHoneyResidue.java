package net.telepathicgrunt.bumblezone.blocks;

import java.util.Map;
import java.util.Random;

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
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class StickyHoneyResidue extends VineBlock {
    public static final BooleanProperty DOWN = SixWayBlock.DOWN;
    protected static final VoxelShape DOWN_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
    public static final Map<Direction, BooleanProperty> FACING_TO_PROPERTY_MAP = SixWayBlock.FACING_TO_PROPERTY_MAP
	    .entrySet().stream().collect(Util.toMapCollector());

    public StickyHoneyResidue() {
	super(Block.Properties.create(Material.ORGANIC, MaterialColor.ADOBE).speedFactor(0.1F).hardnessAndResistance(0.1F).notSolid());
	this.setDefaultState(this.stateContainer.getBaseState()
		.with(UP, Boolean.valueOf(false))
		.with(NORTH, Boolean.valueOf(false))
		.with(EAST, Boolean.valueOf(false))
		.with(SOUTH, Boolean.valueOf(false))
		.with(WEST, Boolean.valueOf(false))
		.with(DOWN, Boolean.valueOf(false)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
	builder.add(UP, NORTH, EAST, SOUTH, WEST, DOWN);
    }

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
    
    @Deprecated
    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos position, Entity entity) {
	entity.setMotionMultiplier(state, new Vec3d(0.35D, (double)0.2F, 0.35D));
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
	return this.hasAtleastOneAttachment(this.setAttachments(state, worldIn, pos));
    }

    private boolean hasAtleastOneAttachment(BlockState p_196543_1_) {
	return this.numberOfAttachments(p_196543_1_) > 0;
    }

    private int numberOfAttachments(BlockState blockstate) {
	int i = 0;

	for (BooleanProperty booleanproperty : FACING_TO_PROPERTY_MAP.values()) {
	    if (blockstate.get(booleanproperty)) {
		++i;
	    }
	}

	return i;
    }

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

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
	BlockState blockstate = this.setAttachments(stateIn, worldIn, currentPos);
	return !this.hasAtleastOneAttachment(blockstate) ? Blocks.AIR.getDefaultState() : blockstate;
    }
    
    @Override
    public PushReaction getPushReaction(BlockState state) {
	return PushReaction.DESTROY;
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
    }

}
