package com.telepathicgrunt.the_bumblezone.blocks;

import com.google.common.collect.Maps;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
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

import java.util.Map;

public class HoneyCrystal extends ProperFacingBlock {
    private static final ResourceLocation EMPTY_FLUID_RL = new ResourceLocation("minecraft:empty");
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
        super(AbstractBlock.Properties.of(Material.GLASS, MaterialColor.COLOR_ORANGE).lightLevel((blockstate) -> 1).strength(0.3F, 0.3f).noOcclusion());

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.UP)
                .setValue(WATERLOGGED, Boolean.FALSE));
    }

    /**
     * Setup properties
     */
    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, FACING);
    }

    /**
     * Can be waterlogged so return sugar water fluid if so
     */
    @SuppressWarnings("deprecation")
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? BzFluids.SUGAR_WATER_FLUID.get().getSource(false) : super.getFluidState(state);
    }

    /**
     * Custom shape of this block based on direction
     */
    @Override
    public VoxelShape getShape(BlockState blockstate, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return FACING_TO_SHAPE_MAP.get(blockstate.getValue(FACING));
    }

    /**
     * Checks if block the crystal is on has a solid side facing it.
     */
    @Override
    public boolean canSurvive(BlockState blockstate, IWorldReader world, BlockPos pos) {

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
                                                BlockState facingState, IWorld world,
                                                BlockPos currentPos, BlockPos facingPos) {

        if (facing.getOpposite() == blockstate.getValue(FACING) && !blockstate.canSurvive(world, currentPos)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            if (blockstate.getValue(WATERLOGGED)) {
                world.getLiquidTicks().scheduleTick(currentPos, BzFluids.SUGAR_WATER_FLUID.get(), BzFluids.SUGAR_WATER_FLUID.get().getTickDelay(world));
            }

            return super.updateShape(blockstate, facing, facingState, world, currentPos, facingPos);
        }
    }

    /**
     * checks if crystal can be placed on block and sets waterlogging as well if replacing water
     */
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {

        if (!context.replacingClickedOnBlock()) {
            BlockState attachedBlockstate = context.getLevel().getBlockState(context.getClickedPos().relative(context.getClickedFace().getOpposite()));
            if (attachedBlockstate.getBlock() == this && attachedBlockstate.getValue(FACING) == context.getClickedFace()) {
                return null;
            }
        }

        BlockState blockstate = this.defaultBlockState();
        IWorldReader worldReader = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());

        for (Direction direction : context.getNearestLookingDirections()) {
            blockstate = blockstate.setValue(FACING, direction.getOpposite());
            if (blockstate.canSurvive(worldReader, blockpos)) {
                return blockstate.setValue(WATERLOGGED, fluidstate.getType().is(FluidTags.WATER));
            }
        }

        return null;
    }

    /**
     * Allows players to waterlog this block directly with buckets full of water tagged fluids
     */
    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType use(BlockState blockstate, World world,
                              BlockPos position, PlayerEntity playerEntity,
                              Hand playerHand, BlockRayTraceResult raytraceResult) {

        if(blockstate.getBlock() != this)
            return super.use(blockstate, world, position, playerEntity, playerHand, raytraceResult);

        ItemStack itemstack = playerEntity.getItemInHand(playerHand);

        //Player uses bucket with water-tagged fluid and this block is not waterlogged
        if (itemstack.getItem() instanceof BucketItem) {
            if(((BucketItem) itemstack.getItem()).getFluid().is(FluidTags.WATER) &&
                 !blockstate.getValue(WATERLOGGED)){

                //make block waterlogged
                world.setBlockAndUpdate(position, blockstate.setValue(WATERLOGGED, true));
                world.getLiquidTicks().scheduleTick(position, BzFluids.SUGAR_WATER_FLUID.get(), BzFluids.SUGAR_WATER_FLUID.get().getTickDelay(world));
                world.playSound(
                        playerEntity,
                        playerEntity.getX(),
                        playerEntity.getY(),
                        playerEntity.getZ(),
                        SoundEvents.AMBIENT_UNDERWATER_ENTER,
                        SoundCategory.NEUTRAL,
                        1.0F,
                        1.0F);

                //set player bucket to be empty if not in creative
                if (!playerEntity.isCreative()) {
                    Item item = itemstack.getItem();
                    itemstack.shrink(1);
                    GeneralUtils.givePlayerItem(playerEntity, playerHand, new ItemStack(item), true);
                }

                return ActionResultType.SUCCESS;
            }
            else if (((BucketItem) itemstack.getItem()).getFluid().getRegistryName().equals(EMPTY_FLUID_RL) &&
                    blockstate.getValue(WATERLOGGED)) {

                //make block waterlogged
                world.setBlockAndUpdate(position, blockstate.setValue(WATERLOGGED, false));
                world.playSound(
                        playerEntity,
                        playerEntity.getX(),
                        playerEntity.getY(),
                        playerEntity.getZ(),
                        SoundEvents.AMBIENT_UNDERWATER_ENTER,
                        SoundCategory.NEUTRAL,
                        1.0F,
                        1.0F);

                //set player bucket to be full of sugar water if not in creative
                if (!playerEntity.isCreative()) {
                    itemstack.shrink(1);
                    GeneralUtils.givePlayerItem(playerEntity, playerHand, new ItemStack(BzItems.SUGAR_WATER_BUCKET.get()), false);
                }

                return ActionResultType.SUCCESS;
            }
        }
        else if (itemstack.getItem() == Items.GLASS_BOTTLE) {

            world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
                    SoundEvents.BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);

            itemstack.shrink(1);
            GeneralUtils.givePlayerItem(playerEntity, playerHand, new ItemStack(BzItems.SUGAR_WATER_BOTTLE.get()), false);
            return ActionResultType.SUCCESS;
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
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        items.add(new ItemStack(BzItems.HONEY_CRYSTAL.get()));
    }

    /**
     * Makes this block always spawn Honey Crystal Shards when broken by piston or removal of attached block
     */
    @Override
    public Item asItem() {
        if (this.item == null) {
            this.item = BzItems.HONEY_CRYSTAL_SHARDS.get();
        }

        return this.item;
    }

    /**
     * This block is translucent and can let some light through
     */
    @Override
    public int getLightBlock(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 1;
    }

 }
