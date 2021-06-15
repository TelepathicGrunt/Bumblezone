package com.telepathicgrunt.bumblezone.blocks;

import com.telepathicgrunt.bumblezone.mixin.blocks.VineBlockAccessor;
import com.telepathicgrunt.bumblezone.mixin.items.BucketItemAccessor;
import com.telepathicgrunt.bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.bumblezone.modinit.BzItems;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ConnectingBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.VineBlock;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.List;
import java.util.Map;

public class StickyHoneyResidue extends VineBlock {
    public static final BooleanProperty DOWN = ConnectingBlock.DOWN;
    protected static final VoxelShape DOWN_AABB = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 0.8D, 16.0D);
    public static final Map<Direction, BooleanProperty> FACING_TO_PROPERTY_MAP =
            ConnectingBlock.FACING_PROPERTIES.entrySet().stream().collect(Util.toMap());

    public StickyHoneyResidue() {
        super(FabricBlockSettings.of(BzBlocks.RESIDUE, MapColor.TERRACOTTA_ORANGE).noCollision().strength(6.0f, 0.0f).nonOpaque().build());
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(UP, false)
                .with(NORTH, false)
                .with(EAST, false)
                .with(SOUTH, false)
                .with(WEST, false)
                .with(DOWN, false));
    }

    /**
     * Set up properties.
     */
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(UP, NORTH, EAST, SOUTH, WEST, DOWN);
    }

    /**
     * Returns the shape based on the state of the block.
     */
    @Override
    public VoxelShape getOutlineShape(BlockState blockstate, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape voxelshape = VoxelShapes.empty();
        if (blockstate.get(UP)) {
            voxelshape = VoxelShapes.union(voxelshape, VineBlockAccessor.thebumblezone_getUP_SHAPE());
        }

        if (blockstate.get(SOUTH)) {
            voxelshape = VoxelShapes.union(voxelshape, VineBlockAccessor.thebumblezone_getNORTH_SHAPE());
        }

        if (blockstate.get(WEST)) {
            voxelshape = VoxelShapes.union(voxelshape, VineBlockAccessor.thebumblezone_getEAST_SHAPE());
        }

        if (blockstate.get(NORTH)) {
            voxelshape = VoxelShapes.union(voxelshape, VineBlockAccessor.thebumblezone_getSOUTH_SHAPE());
        }

        if (blockstate.get(EAST)) {
            voxelshape = VoxelShapes.union(voxelshape, VineBlockAccessor.thebumblezone_getWEST_SHAPE());
        }

        if (blockstate.get(DOWN)) {
            voxelshape = VoxelShapes.union(voxelshape, DOWN_AABB);
        }

        return voxelshape;
    }


    /**
     * Slows all entities inside the block.
     */
    @Deprecated
    @Override
    public void onEntityCollision(BlockState blockstate, World world, BlockPos pos, Entity entity) {

        Box axisalignedbb = getOutlineShape(blockstate, world, pos, null).getBoundingBox().offset(pos);
        List<? extends Entity> list = world.getNonSpectatingEntities(LivingEntity.class, axisalignedbb);

        if (list.contains(entity)) {
            entity.slowMovement(blockstate, new Vec3d(0.35D, 0.2F, 0.35D));
        }
    }

    /**
     * Is spot valid (has at least 1 face possible).
     */
    @Override
    public boolean canPlaceAt(BlockState blockstate, WorldView world, BlockPos pos) {
        return this.hasAtleastOneAttachment(this.setAttachments(blockstate, world, pos));
    }

    /**
     * Returns true if the block has at least one face (it exists).
     */
    private boolean hasAtleastOneAttachment(BlockState blockstate) {
        return this.numberOfAttachments(blockstate) > 0;
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
    private BlockState setAttachments(BlockState blockstate, WorldView blockReader, BlockPos pos) {

        for (Direction direction : Direction.values()) {
            BooleanProperty booleanproperty = FACING_TO_PROPERTY_MAP.get(direction);
            if (blockstate.get(booleanproperty)) {
                boolean flag = shouldConnectTo(blockReader, pos.offset(direction), direction);
                blockstate = blockstate.with(booleanproperty, flag);
            }
        }

        return blockstate;
    }

    /**
     * allows player to add more faces to this block based on player's direction.
     */
    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockState currentBlockstate = context.getWorld().getBlockState(context.getBlockPos());
        boolean isSameBlock = currentBlockstate.getBlock() == this;
        BlockState newBlockstate = isSameBlock ? currentBlockstate : this.getDefaultState();

        for (Direction direction : context.getPlacementDirections()) {
            BooleanProperty booleanproperty = FACING_TO_PROPERTY_MAP.get(direction);
            boolean faceIsAlreadyTrue = isSameBlock && currentBlockstate.get(booleanproperty);
            if (!faceIsAlreadyTrue && VineBlock.shouldConnectTo(context.getWorld(), context.getBlockPos().offset(direction), direction)) {
                return newBlockstate.with(booleanproperty, true);
            }
        }

        return isSameBlock ? newBlockstate : null;
    }

    /**
     * double check to make sure this block has at least one face and can attach.
     */
    @Override
    public BlockState getStateForNeighborUpdate(BlockState blockstate, Direction facing, BlockState facingState, WorldAccess world, BlockPos currentPos, BlockPos facingPos) {
        BlockState newBlockstate = this.setAttachments(blockstate, world, currentPos);
        return !this.hasAtleastOneAttachment(newBlockstate) ? Blocks.AIR.getDefaultState() : newBlockstate;
    }

    /**
     * Destroyed by pistons.
     */
    @Override
    public PistonBehavior getPistonBehavior(BlockState blockstate) {
        return PistonBehavior.DESTROY;
    }

    /**
     * tell redstone that this can be use with comparator
     */
    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    /**
     * the power fed into comparator (1 - 4)
     */
    @Override
    public int getComparatorOutput(BlockState blockstate, World world, BlockPos pos) {
        return numberOfAttachments(blockstate);
    }


    /**
     * This block is full of holes and can let light through
     */
    @Override
    public int getOpacity(BlockState state, BlockView world, BlockPos pos) {
        return 1;
    }

    /**
     * Allow player to remove this block with water buckets, water bottles, or wet sponges
     */
    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState blockstate, World world, BlockPos position, PlayerEntity playerEntity, Hand playerHand, BlockHitResult raytraceResult) {
        ItemStack itemstack = playerEntity.getStackInHand(playerHand);

        if ((itemstack.getItem() instanceof BucketItem &&
                ((BucketItemAccessor) itemstack.getItem()).thebumblezone_getFluid().isIn(FluidTags.WATER)) ||
                itemstack.getOrCreateTag().getString("Potion").contains("water") ||
                itemstack.getItem() == Items.WET_SPONGE ||
                itemstack.getItem() == BzItems.SUGAR_WATER_BOTTLE) {

            world.breakBlock(position, false);

            world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
                    SoundEvents.ENTITY_PHANTOM_SWOOP, SoundCategory.NEUTRAL, 1.0F, 1.0F);

            if (world instanceof ServerWorld) {
                if (blockstate.get(UP)) {
                    ((ServerWorld) world).spawnParticles((ServerPlayerEntity) playerEntity, ParticleTypes.FALLING_WATER, true, position.getX() + 0.5D, position.getY() + 0.95D, position.getZ() + 0.5D, 6, 0.3D, 0.0D, 0.3D, 1);
                }

                if (blockstate.get(NORTH)) {
                    ((ServerWorld) world).spawnParticles((ServerPlayerEntity) playerEntity, ParticleTypes.FALLING_WATER, true, position.getX() + 0.5D, position.getY() + 0.5D, position.getZ() + 0.05D, 6, 0.3D, 0.3D, 0.0D, 1);
                }

                if (blockstate.get(EAST)) {
                    ((ServerWorld) world).spawnParticles((ServerPlayerEntity) playerEntity, ParticleTypes.FALLING_WATER, true, position.getX() + 0.95D, position.getY() + 0.5D, position.getZ() + 0.5D, 6, 0.0D, 0.3D, 0.3D, 1);
                }

                if (blockstate.get(SOUTH)) {
                    ((ServerWorld) world).spawnParticles((ServerPlayerEntity) playerEntity, ParticleTypes.FALLING_WATER, true, position.getX() + 0.5D, position.getY() + 0.5D, position.getZ() + 0.95D, 6, 0.3D, 0.3D, 0.0D, 1);
                }

                if (blockstate.get(WEST)) {
                    ((ServerWorld) world).spawnParticles((ServerPlayerEntity) playerEntity, ParticleTypes.FALLING_WATER, true, position.getX() + 0.05D, position.getY() + 0.5D, position.getZ() + 0.5D, 6, 0.0D, 0.3D, 0.3D, 1);
                }

                if (blockstate.get(DOWN)) {
                    ((ServerWorld) world).spawnParticles((ServerPlayerEntity) playerEntity, ParticleTypes.FALLING_WATER, true, position.getX() + 0.5D, position.getY() + 0.05D, position.getZ() + 0.5D, 6, 0.3D, 0.0D, 0.3D, 1);
                }
            }

            return ActionResult.SUCCESS;
        }

        return super.onUse(blockstate, world, position, playerEntity, playerHand, raytraceResult);
    }

}
