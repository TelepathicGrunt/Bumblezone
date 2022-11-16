package com.telepathicgrunt.the_bumblezone.blocks;

import com.google.common.collect.Maps;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;

import java.util.Map;


public class HoneyCrystal extends ProperFacingBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final VoxelShape DOWN_AABB = Block.box(0.0D, 1.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape UP_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D);
    protected static final VoxelShape WEST_AABB = Block.box(1.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape EAST_AABB = Block.box(0.0D, 0.0D, 0.0D, 15.0D, 16.0D, 16.0D);
    protected static final VoxelShape NORTH_AABB = Block.box(0.0D, 0.0D, 1.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SOUTH_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 15.0D);
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
        super(QuiltBlockSettings.of(Material.GLASS, MaterialColor.TERRACOTTA_YELLOW).lightLevel((blockState) -> 1).strength(0.3F, 0.3f).noOcclusion());

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.UP)
                .setValue(WATERLOGGED, Boolean.FALSE));
    }

    /**
     * Setup properties
     */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, FACING);
    }

    /**
     * Can be waterlogged so return sugar water fluid if so
     */
    @SuppressWarnings("deprecation")
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? BzFluids.SUGAR_WATER_FLUID.getSource(false) : super.getFluidState(state);
    }

    /**
     * Custom shape of this block based on direction
     */
    @Override
    public VoxelShape getShape(BlockState blockstate, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return FACING_TO_SHAPE_MAP.get(blockstate.getValue(FACING));
    }

    /**
     * Checks if block the crystal is on has a solid side facing it.
     */
    @Override
    public boolean canSurvive(BlockState blockstate, LevelReader world, BlockPos pos) {

        Direction direction = blockstate.getValue(FACING);
        BlockState attachedBlockstate = world.getBlockState(pos.relative(direction.getOpposite()));
        return attachedBlockstate.isFaceSturdy(world, pos.relative(direction.getOpposite()), direction);
    }

    /**
     * checks if crystal attachment is still valid and begin fluid tick if waterlogged
     */
    @SuppressWarnings("deprecation")
    @Override
    public BlockState updateShape(BlockState blockstate, Direction facing,
                                                BlockState facingState, LevelAccessor world,
                                                BlockPos currentPos, BlockPos facingPos) {

        if (facing.getOpposite() == blockstate.getValue(FACING) && !blockstate.canSurvive(world, currentPos)) {
            return Blocks.AIR.defaultBlockState();
        }
        else {
            if (blockstate.getValue(WATERLOGGED)) {
                world.scheduleTick(currentPos, BzFluids.SUGAR_WATER_FLUID, BzFluids.SUGAR_WATER_FLUID.getTickDelay(world));
            }

            return super.updateShape(blockstate, facing, facingState, world, currentPos, facingPos);
        }
    }

    /**
     * checks if crystal can be placed on block and sets waterlogging as well if replacing water
     */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {

        if (!context.replacingClickedOnBlock()) {
            BlockState attachedBlockstate = context.getLevel().getBlockState(context.getClickedPos().relative(context.getClickedFace().getOpposite()));
            if (attachedBlockstate.getBlock() == this && attachedBlockstate.getValue(FACING) == context.getClickedFace()) {
                return null;
            }
        }

        BlockState blockstate = this.defaultBlockState();
        LevelReader worldReader = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());

        for (Direction direction : context.getNearestLookingDirections()) {
            blockstate = blockstate.setValue(FACING, direction.getOpposite());
            if (blockstate.canSurvive(worldReader, blockpos)) {
                return blockstate.setValue(WATERLOGGED, fluidstate.getType().is(BzTags.CONVERTIBLE_TO_SUGAR_WATER) && fluidstate.isSource());
            }
        }

        return null;
    }

    /**
     * Allows players to waterlog this block directly with buckets full of water tagged fluids
     */
    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState blockstate, Level world,
                              BlockPos position, Player playerEntity,
                              InteractionHand playerHand, BlockHitResult raytraceResult) {

        ItemStack itemstack = playerEntity.getItemInHand(playerHand);

        if (itemstack.getItem() == Items.GLASS_BOTTLE) {

            world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
                    SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);

            GeneralUtils.givePlayerItem(playerEntity, playerHand, new ItemStack(BzItems.SUGAR_WATER_BOTTLE), false, true);
            return InteractionResult.SUCCESS;
        }

        return super.use(blockstate, world, position, playerEntity, playerHand, raytraceResult);
    }

    /**
     * Breaks by pistons
     */
    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }


    /**
     * Makes this block show up in creative menu to fix the asItem override side-effect
     */
    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
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
     * Return this blockitem for creative middle click (pick block)
     */
    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return BzItems.HONEY_CRYSTAL.getDefaultInstance();
    }

    /**
     * This block is translucent and can let some light through
     */
    @Override
    public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 1;
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter world, BlockPos blockPos, BlockState blockState, Fluid fluid) {
        return !blockState.getValue(WATERLOGGED) && fluid.is(BzTags.CONVERTIBLE_TO_SUGAR_WATER) && fluid.defaultFluidState().isSource();
    }

    @Override
    public boolean placeLiquid(LevelAccessor world, BlockPos blockPos, BlockState blockState, FluidState fluidState) {
        if (!blockState.getValue(WATERLOGGED) && fluidState.getType().is(BzTags.CONVERTIBLE_TO_SUGAR_WATER) && fluidState.isSource()) {
            if (!world.isClientSide()) {
                world.setBlock(blockPos, blockState.setValue(WATERLOGGED, true), 3);
                world.scheduleTick(blockPos, BzFluids.SUGAR_WATER_FLUID, BzFluids.SUGAR_WATER_FLUID.getTickDelay(world));
            }
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public ItemStack pickupBlock(LevelAccessor world, BlockPos blockPos, BlockState blockState) {
        if (blockState.getValue(WATERLOGGED)) {
            world.setBlock(blockPos, blockState.setValue(WATERLOGGED, false), 3);
            return new ItemStack(BzItems.SUGAR_WATER_BUCKET);
        }
        else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
        return false;
    }
}
