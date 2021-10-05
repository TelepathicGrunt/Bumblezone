package com.telepathicgrunt.bumblezone.blocks;

import com.google.common.collect.Maps;
import com.telepathicgrunt.bumblezone.modinit.BzFluids;
import com.telepathicgrunt.bumblezone.modinit.BzItems;
import com.telepathicgrunt.bumblezone.utils.GeneralUtils;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Map;


public class HoneyCrystal extends ProperFacingBlock implements Waterloggable {
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    protected static final VoxelShape DOWN_AABB = Block.createCuboidShape(0.0D, 1.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape UP_AABB = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D);
    protected static final VoxelShape WEST_AABB = Block.createCuboidShape(1.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape EAST_AABB = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 15.0D, 16.0D, 16.0D);
    protected static final VoxelShape NORTH_AABB = Block.createCuboidShape(0.0D, 0.0D, 1.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SOUTH_AABB = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 15.0D);
    public static final Map<Direction, VoxelShape> FACING_TO_SHAPE_MAP = Util.make(Maps.newEnumMap(Direction.class), (map) -> {
        map.put(Direction.NORTH, NORTH_AABB);
        map.put(Direction.EAST, EAST_AABB);
        map.put(Direction.SOUTH, SOUTH_AABB);
        map.put(Direction.WEST, WEST_AABB);
        map.put(Direction.UP, UP_AABB);
        map.put(Direction.DOWN, DOWN_AABB);
    });
    private Item item;

    public HoneyCrystal() {
        super(FabricBlockSettings.of(Material.GLASS, MapColor.ORANGE).lightLevel(1).strength(0.3F, 0.3f).nonOpaque());

        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.UP)
                .with(WATERLOGGED, Boolean.FALSE));
    }

    /**
     * Setup properties
     */
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, FACING);
    }

    /**
     * Can be waterlogged so return sugar water fluid if so
     */
    @SuppressWarnings("deprecation")
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? BzFluids.SUGAR_WATER_FLUID.getStill(false) : super.getFluidState(state);
    }

    /**
     * Custom shape of this block based on direction
     */
    @Override
    public VoxelShape getOutlineShape(BlockState blockstate, BlockView worldIn, BlockPos pos, ShapeContext context) {
        return FACING_TO_SHAPE_MAP.get(blockstate.get(FACING));
    }

    /**
     * Checks if block the crystal is on has a solid side facing it.
     */
    @Override
    public boolean canPlaceAt(BlockState blockstate, WorldView world, BlockPos pos) {

        Direction direction = blockstate.get(FACING);
        BlockState attachedBlockstate = world.getBlockState(pos.offset(direction.getOpposite()));
        return attachedBlockstate.isSideSolidFullSquare(world, pos.offset(direction.getOpposite()), direction);
    }

    /**
     * checks if crystal attachment is still valid and begin fluid tick if waterlogged
     */
    @SuppressWarnings("deprecation")
    @Override
    public BlockState getStateForNeighborUpdate(BlockState blockstate, Direction facing,
                                                BlockState facingState, WorldAccess world,
                                                BlockPos currentPos, BlockPos facingPos) {

        if (facing.getOpposite() == blockstate.get(FACING) && !blockstate.canPlaceAt(world, currentPos)) {
            return Blocks.AIR.getDefaultState();
        } else {
            if (blockstate.get(WATERLOGGED)) {
                world.getFluidTickScheduler().schedule(currentPos, BzFluids.SUGAR_WATER_FLUID, BzFluids.SUGAR_WATER_FLUID.getTickRate(world));
            }

            return super.getStateForNeighborUpdate(blockstate, facing, facingState, world, currentPos, facingPos);
        }
    }

    /**
     * checks if crystal can be placed on block and sets waterlogging as well if replacing water
     */
    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {

        if (!context.canReplaceExisting()) {
            BlockState attachedBlockstate = context.getWorld().getBlockState(context.getBlockPos().offset(context.getSide().getOpposite()));
            if (attachedBlockstate.getBlock() == this && attachedBlockstate.get(FACING) == context.getSide()) {
                return null;
            }
        }

        BlockState blockstate = this.getDefaultState();
        WorldView worldReader = context.getWorld();
        BlockPos blockpos = context.getBlockPos();
        FluidState fluidstate = context.getWorld().getFluidState(context.getBlockPos());

        for (Direction direction : context.getPlacementDirections()) {
            blockstate = blockstate.with(FACING, direction.getOpposite());
            if (blockstate.canPlaceAt(worldReader, blockpos)) {
                return blockstate.with(WATERLOGGED, fluidstate.getFluid().isIn(FluidTags.WATER) && fluidstate.isStill());
            }
        }

        return null;
    }

    /**
     * Allows players to waterlog this block directly with buckets full of water tagged fluids
     */
    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState blockstate, World world,
                              BlockPos position, PlayerEntity playerEntity,
                              Hand playerHand, BlockHitResult raytraceResult) {

        ItemStack itemstack = playerEntity.getStackInHand(playerHand);

        if (itemstack.getItem() == Items.GLASS_BOTTLE) {

            world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
                    SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);

            itemstack.decrement(1);
            GeneralUtils.givePlayerItem(playerEntity, playerHand, new ItemStack(BzItems.SUGAR_WATER_BOTTLE), false);
            return ActionResult.SUCCESS;
        }

        return super.onUse(blockstate, world, position, playerEntity, playerHand, raytraceResult);
    }

    /**
     * Breaks by pistons
     */
    @Override
    public PistonBehavior getPistonBehavior(BlockState state) {
        return PistonBehavior.DESTROY;
    }


    /**
     * Makes this block show up in creative menu to fix the asItem override side-effect
     */
    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> items) {
        items.add(new ItemStack(BzItems.HONEY_CRYSTAL));
    }

    /**
     * Makes this block always spawn Honey Crystal Shards when broken by piston or removal of attached block
     */
    @Override
    public Item asItem() {
        if (this.item == null) {
            this.item = BzItems.HONEY_CRYSTAL_SHARDS;
        }

        return this.item;
    }

    /**
     * This block is translucent and can let some light through
     */
    @Override
    public int getOpacity(BlockState state, BlockView worldIn, BlockPos pos) {
        return 1;
    }

    @Override
    public boolean canFillWithFluid(BlockView world, BlockPos blockPos, BlockState blockState, Fluid fluid) {
        return !blockState.get(WATERLOGGED) && fluid.isIn(FluidTags.WATER) && fluid.getDefaultState().isStill();
    }

    @Override
    public boolean tryFillWithFluid(WorldAccess world, BlockPos blockPos, BlockState blockState, FluidState fluidState) {
        if (!blockState.get(WATERLOGGED) && fluidState.getFluid().isIn(FluidTags.WATER) && fluidState.isStill()) {
            if (!world.isClient()) {
                world.setBlockState(blockPos, blockState.with(WATERLOGGED, true), 3);
                world.getFluidTickScheduler().schedule(blockPos, BzFluids.SUGAR_WATER_FLUID, BzFluids.SUGAR_WATER_FLUID.getTickRate(world));
            }
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public ItemStack tryDrainFluid(WorldAccess world, BlockPos blockPos, BlockState blockState) {
        if (blockState.get(WATERLOGGED)) {
            world.setBlockState(blockPos, blockState.with(WATERLOGGED, false), 3);
            return new ItemStack(BzItems.SUGAR_WATER_BUCKET);
        }
        else {
            return ItemStack.EMPTY;
        }
    }
}
