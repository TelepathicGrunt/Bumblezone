package com.telepathicgrunt.the_bumblezone.blocks;

import com.google.common.collect.Maps;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.*;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.Map;


public class HoneyCrystal extends Block {
    private static final ResourceLocation EMPTY_FLUID_RL = new ResourceLocation("minecraft:empty");
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

    public HoneyCrystal() {
        super(Block.Properties.create(Material.GLASS, MaterialColor.ADOBE).setLightLevel((blockstate) -> 1).hardnessAndResistance(0.3F, 0.3f).notSolid());

        this.setDefaultState(this.stateContainer.getBaseState()
                .with(FACING, Direction.UP)
                .with(WATERLOGGED, Boolean.FALSE));
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
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? BzFluids.SUGAR_WATER_FLUID.get().getStillFluidState(false) : super.getFluidState(state);
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
                                                BlockState facingState, IWorld world,
                                                BlockPos currentPos, BlockPos facingPos) {

        if (facing.getOpposite() == blockstate.get(FACING) && !blockstate.isValidPosition(world, currentPos)) {
            return Blocks.AIR.getDefaultState();
        } else {
            if (blockstate.get(WATERLOGGED)) {
                world.getPendingFluidTicks().scheduleTick(currentPos, BzFluids.SUGAR_WATER_FLUID.get(), BzFluids.SUGAR_WATER_FLUID.get().getTickRate(world));
            }

            return super.updatePostPlacement(blockstate, facing, facingState, world, currentPos, facingPos);
        }
    }

    /**
     * checks if crystal can be placed on block and sets waterlogging as well if replacing water
     */
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
        FluidState fluidstate = context.getWorld().getFluidState(context.getPos());

        for (Direction direction : context.getNearestLookingDirections()) {
            blockstate = blockstate.with(FACING, direction.getOpposite());
            if (blockstate.isValidPosition(worldReader, blockpos)) {
                return blockstate.with(WATERLOGGED, fluidstate.getFluid().isIn(FluidTags.WATER));
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

        if(blockstate.getBlock() != this)
            return super.onBlockActivated(blockstate, world, position, playerEntity, playerHand, raytraceResult);

        ItemStack itemstack = playerEntity.getHeldItem(playerHand);

        //Player uses bucket with water-tagged fluid and this block is not waterlogged
        if (itemstack.getItem() instanceof BucketItem) {
            if(((BucketItem) itemstack.getItem()).getFluid().isIn(FluidTags.WATER) &&
                 !blockstate.get(WATERLOGGED)){

                //make block waterlogged
                world.setBlockState(position, blockstate.with(WATERLOGGED, true));
                world.getPendingFluidTicks().scheduleTick(position, BzFluids.SUGAR_WATER_FLUID.get(), BzFluids.SUGAR_WATER_FLUID.get().getTickRate(world));
                world.playSound(
                        playerEntity,
                        playerEntity.getPosX(),
                        playerEntity.getPosY(),
                        playerEntity.getPosZ(),
                        SoundEvents.AMBIENT_UNDERWATER_ENTER,
                        SoundCategory.NEUTRAL,
                        1.0F,
                        1.0F);

                //set player bucket to be empty if not in creative
                if (!playerEntity.isCreative()) {
                    playerEntity.setHeldItem(playerHand, new ItemStack(Items.BUCKET));
                }

                return ActionResultType.SUCCESS;
            }
            else if (((BucketItem) itemstack.getItem()).getFluid().getRegistryName().equals(EMPTY_FLUID_RL) &&
                    blockstate.get(WATERLOGGED)) {

                //make block waterlogged
                world.setBlockState(position, blockstate.with(WATERLOGGED, false));
                world.playSound(
                        playerEntity,
                        playerEntity.getPosX(),
                        playerEntity.getPosY(),
                        playerEntity.getPosZ(),
                        SoundEvents.AMBIENT_UNDERWATER_ENTER,
                        SoundCategory.NEUTRAL,
                        1.0F,
                        1.0F);

                //set player bucket to be full of sugar water if not in creative
                if (!playerEntity.isCreative()) {
                    playerEntity.setHeldItem(playerHand, new ItemStack(BzItems.SUGAR_WATER_BUCKET.get()));
                }

                return ActionResultType.SUCCESS;
            }
        }
        else if (itemstack.getItem() == Items.GLASS_BOTTLE) {

            world.playSound(playerEntity, playerEntity.getPosX(), playerEntity.getPosY(), playerEntity.getPosZ(),
                    SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);

            itemstack.shrink(1); // remove current honey bottle

            if (itemstack.isEmpty()) {
                playerEntity.setHeldItem(playerHand, new ItemStack(BzItems.SUGAR_WATER_BOTTLE.get())); // places sugar water bottle in hand
            }
            else if (!playerEntity.inventory.addItemStackToInventory(new ItemStack(BzItems.SUGAR_WATER_BOTTLE.get()))) // places sugar water bottle in inventory
            {
                playerEntity.dropItem(new ItemStack(BzItems.SUGAR_WATER_BOTTLE.get()), false); // drops sugar water bottle if inventory is full
            }

            return ActionResultType.SUCCESS;
        }

        return super.onBlockActivated(blockstate, world, position, playerEntity, playerHand, raytraceResult);
    }

    /**
     * Breaks by pistons
     */
    @Override
    public PushReaction getPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }


    /**
     * Makes this block show up in creative menu to fix the asItem override side-effect
     */
    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
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
    public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 1;
    }

 }
