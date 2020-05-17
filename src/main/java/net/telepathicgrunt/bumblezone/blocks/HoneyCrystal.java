package net.telepathicgrunt.bumblezone.blocks;

import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.items.BzItems;


public class HoneyCrystal extends Block
{
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final DirectionProperty FACING = DirectionalBlock.FACING;
	protected static final VoxelShape DOWN_AABB = Block.makeCuboidShape(0.0D, 1.0D, 0.0D, 16.0D, 16.0D, 16.0D);
	protected static final VoxelShape UP_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D);
	protected static final VoxelShape WEST_AABB = Block.makeCuboidShape(1.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
	protected static final VoxelShape EAST_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 15.0D, 16.0D, 16.0D);
	protected static final VoxelShape NORTH_AABB = Block.makeCuboidShape(0.0D, 0.0D, 1.0D, 16.0D, 16.0D, 16.0D);
	protected static final VoxelShape SOUTH_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 15.0D);
	public static final Map<Direction, VoxelShape> FACING_TO_SHAPE_MAP = Util.make(Maps.newEnumMap(Direction.class), (map) -> {
		    map.put(Direction.NORTH, NORTH_AABB);
		    map.put(Direction.EAST, EAST_AABB);
		    map.put(Direction.SOUTH, SOUTH_AABB);
		    map.put(Direction.WEST, WEST_AABB);
		    map.put(Direction.UP, UP_AABB);
		    map.put(Direction.DOWN, DOWN_AABB);
		});
	private Item item;

	public HoneyCrystal()
	{
		super(Block.Properties.create(Material.GLASS, MaterialColor.ADOBE).lightValue(1).hardnessAndResistance(0.3F).notSolid());
		
		this.setDefaultState(this.stateContainer.getBaseState()
			.with(FACING, Direction.UP)
			.with(WATERLOGGED, Boolean.valueOf(false)));
	}
	
	/**
	 * Setup properties
	 */
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
	    builder.add(WATERLOGGED, FACING);
	}

	/**
	 * Can be waterlogged so return sugar water fluid if so
	 */
	@SuppressWarnings("deprecation")
	@Override
	public IFluidState getFluidState(BlockState state) {
	    return state.get(WATERLOGGED) ? BzBlocks.SUGAR_WATER_FLUID.get().getStillFluidState(false) : super.getFluidState(state);
	}

	/**
	 * Custom shape of this block based on direction
	 */
	@Override
	public VoxelShape getShape(BlockState blockstate, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
	    return FACING_TO_SHAPE_MAP.get(blockstate.get(FACING));
	}

	/**
	 * Checks if block the crystal is on has a solid side facing it.
	 */
	@Override
	public boolean isValidPosition(BlockState blockstate, IWorldReader world, BlockPos pos) {
	    
	    Direction direction = blockstate.get(FACING);
	    BlockState attachedBlockstate = world.getBlockState(pos.offset(direction.getOpposite()));
	    return attachedBlockstate.isSolidSide(world, pos.offset(direction.getOpposite()), direction);
	}

	/**
	 * checks if crystal attachment is still valid and begin fluid tick if waterlogged
	 */
	@SuppressWarnings("deprecation")
	@Override
	public BlockState updatePostPlacement(BlockState blockstate, Direction facing, 
		BlockState facingState, IWorld worldIn, 
		BlockPos currentPos, BlockPos facingPos) {
	    
	    if (facing.getOpposite() == blockstate.get(FACING) && !blockstate.isValidPosition(worldIn, currentPos)) {
		return Blocks.AIR.getDefaultState();
	    } else {
		if (blockstate.get(WATERLOGGED)) {
		    worldIn.getPendingFluidTicks().scheduleTick(currentPos, BzBlocks.SUGAR_WATER_FLUID.get(), BzBlocks.SUGAR_WATER_FLUID.get().getTickRate(worldIn));
		}

		return super.updatePostPlacement(blockstate, facing, facingState, worldIn, currentPos, facingPos);
	    }
	}

	/**
	 * checks if crystal can be placed on block and sets waterlogging as well if replacing water
	 */
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
	    
	    if (!context.replacingClickedOnBlock()) {
		BlockState attachedBlockstate = context.getWorld().getBlockState(context.getPos().offset(context.getFace().getOpposite()));
		if (attachedBlockstate.getBlock() == this && attachedBlockstate.get(FACING) == context.getFace()) {
		    return null;
		}
	    }

	    BlockState blockstate = this.getDefaultState();
	    IWorldReader worldReader = context.getWorld();
	    BlockPos blockpos = context.getPos();
	    IFluidState fluidstate = context.getWorld().getFluidState(context.getPos());

	    for (Direction direction : context.getNearestLookingDirections()) {
		blockstate = blockstate.with(FACING, direction.getOpposite());
		if (blockstate.isValidPosition(worldReader, blockpos)) {
		    return blockstate.with(WATERLOGGED, Boolean.valueOf(fluidstate.getFluid().isIn(FluidTags.WATER)));
		}
	    }

	    return null;
	}

	/**
	 * Allows players to waterlog this block directly with buckets full of water tagged fluids
	 */
	@Override
	@SuppressWarnings("deprecation")
	public ActionResultType onBlockActivated(BlockState blockstate, World world, 
		BlockPos position, PlayerEntity playerEntity, 
		Hand playerHand, BlockRayTraceResult raytraceResult) {
	    
	    ItemStack itemstack = playerEntity.getHeldItem(playerHand);

	    //Player uses bucket with water-tagged fluid and this block is not waterlogged
	    if ((itemstack.getItem() instanceof BucketItem && 
		    ((BucketItem) itemstack.getItem()).getFluid().getTags().contains(FluidTags.WATER.getId())) &&
		    blockstate.getBlock() == this && 
		    !blockstate.get(WATERLOGGED)) {

		//make block waterlogged
		world.setBlockState(position, blockstate.with(WATERLOGGED, true));
		world.getPendingFluidTicks().scheduleTick(position, BzBlocks.SUGAR_WATER_FLUID.get(), BzBlocks.SUGAR_WATER_FLUID.get().getTickRate(world));
		world.playSound(playerEntity, playerEntity.getPosX(), playerEntity.getPosY(), playerEntity.getPosZ(),
			SoundEvents.AMBIENT_UNDERWATER_ENTER, SoundCategory.NEUTRAL, 1.0F, 1.0F);

		//set player bucket to be empty if not in creative
		if (!playerEntity.isCreative()) {
		    playerEntity.setHeldItem(playerHand, new ItemStack(Items.BUCKET));
		}

		return ActionResultType.SUCCESS;
	    }

	    return super.onBlockActivated(blockstate, world, position, playerEntity, playerHand, raytraceResult);
	}

	/**
	 * Breaks by pistons
	 */
	@Override
	public PushReaction getPushReaction(BlockState state){
		return PushReaction.DESTROY;
	}


	/**
	 * Makes this block show up in creative menu to fix the asItem override side-effect
	 */
	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items){
		items.add(new ItemStack(BzItems.HONEY_CRYSTAL_BLOCK.get()));
	}

	/**
	 * Makes this block always spawn Honey Crystal Shards when broken by piston or removal of attached block
	 */
	@Override
	public Item asItem(){
		if (this.item == null)
		{
			this.item = BzItems.HONEY_CRYSTAL_SHARDS.get();
		}

		return this.item.delegate.get();
	}

	/**
	 * This block is translucent and can let some light through
	 */
	@Override
	public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
	    return 1;
	}
}
