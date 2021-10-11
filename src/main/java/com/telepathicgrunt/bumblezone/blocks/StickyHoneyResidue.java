package com.telepathicgrunt.bumblezone.blocks;

import com.telepathicgrunt.bumblezone.mixin.blocks.VineBlockAccessor;
import com.telepathicgrunt.bumblezone.mixin.items.BucketItemAccessor;
import com.telepathicgrunt.bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.bumblezone.modinit.BzItems;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;
import java.util.Map;

public class StickyHoneyResidue extends VineBlock {
    public static final BooleanProperty DOWN = PipeBlock.DOWN;
    protected static final VoxelShape DOWN_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 0.8D, 16.0D);
    public static final Map<Direction, BooleanProperty> FACING_TO_PROPERTY_MAP =
            PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().collect(Util.toMap());

    public StickyHoneyResidue() {
        super(FabricBlockSettings.of(BzBlocks.ORANGE_NOT_SOLID, MaterialColor.TERRACOTTA_ORANGE).noCollission().strength(6.0f, 0.0f).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(UP, false)
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(DOWN, false));
    }

    public StickyHoneyResidue(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(UP, false)
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(DOWN, false));
    }

    /**
     * Set up properties.
     */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(UP, NORTH, EAST, SOUTH, WEST, DOWN);
    }

    /**
     * Returns the shape based on the state of the block.
     */
    @Override
    public VoxelShape getShape(BlockState blockstate, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape voxelshape = Shapes.empty();
        if (blockstate.getValue(UP)) {
            voxelshape = Shapes.or(voxelshape, VineBlockAccessor.thebumblezone_getUP_SHAPE());
        }

        if (blockstate.getValue(SOUTH)) {
            voxelshape = Shapes.or(voxelshape, VineBlockAccessor.thebumblezone_getSOUTH_SHAPE());
        }

        if (blockstate.getValue(WEST)) {
            voxelshape = Shapes.or(voxelshape, VineBlockAccessor.thebumblezone_getWEST_SHAPE());
        }

        if (blockstate.getValue(NORTH)) {
            voxelshape = Shapes.or(voxelshape, VineBlockAccessor.thebumblezone_getNORTH_SHAPE());
        }

        if (blockstate.getValue(EAST)) {
            voxelshape = Shapes.or(voxelshape, VineBlockAccessor.thebumblezone_getEAST_SHAPE());
        }

        if (blockstate.getValue(DOWN)) {
            voxelshape = Shapes.or(voxelshape, DOWN_AABB);
        }

        return voxelshape;
    }


    /**
     * Slows all entities inside the block.
     */
    @Deprecated
    @Override
    public void entityInside(BlockState blockstate, Level world, BlockPos pos, Entity entity) {

        AABB axisalignedbb = getShape(blockstate, world, pos, null).bounds().move(pos);
        List<? extends Entity> list = world.getEntitiesOfClass(LivingEntity.class, axisalignedbb);

        if (list.contains(entity)) {
            entity.makeStuckInBlock(blockstate, new Vec3(0.35D, 0.2F, 0.35D));
        }
    }

    /**
     * Is spot valid (has at least 1 face possible).
     */
    @Override
    public boolean canSurvive(BlockState blockstate, LevelReader world, BlockPos pos) {
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
            if (blockstate.getValue(booleanproperty)) {
                ++i;
            }
        }

        return i;
    }

    /**
     * Set the state based on solid blocks around it.
     */
    private BlockState setAttachments(BlockState blockstate, LevelReader blockReader, BlockPos pos) {

        for (Direction direction : Direction.values()) {
            BooleanProperty booleanproperty = FACING_TO_PROPERTY_MAP.get(direction);
            if (blockstate.getValue(booleanproperty)) {
                boolean flag = isAcceptableNeighbour(blockReader, pos.relative(direction), direction);
                blockstate = blockstate.setValue(booleanproperty, flag);
            }
        }

        return blockstate;
    }

    /**
     * allows player to add more faces to this block based on player's direction.
     */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState currentBlockstate = context.getLevel().getBlockState(context.getClickedPos());
        boolean isSameBlock = currentBlockstate.getBlock() == this;
        BlockState newBlockstate = isSameBlock ? currentBlockstate : this.defaultBlockState();

        for (Direction direction : context.getNearestLookingDirections()) {
            BooleanProperty booleanproperty = FACING_TO_PROPERTY_MAP.get(direction);
            boolean faceIsAlreadyTrue = isSameBlock && currentBlockstate.getValue(booleanproperty);
            if (!faceIsAlreadyTrue && VineBlock.isAcceptableNeighbour(context.getLevel(), context.getClickedPos().relative(direction), direction)) {
                return newBlockstate.setValue(booleanproperty, true);
            }
        }

        return isSameBlock ? newBlockstate : null;
    }

    /**
     * double check to make sure this block has at least one face and can attach.
     */
    @Override
    public BlockState updateShape(BlockState blockstate, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
        BlockState newBlockstate = this.setAttachments(blockstate, world, currentPos);
        return !this.hasAtleastOneAttachment(newBlockstate) ? Blocks.AIR.defaultBlockState() : newBlockstate;
    }

    /**
     * Destroyed by pistons.
     */
    @Override
    public PushReaction getPistonPushReaction(BlockState blockstate) {
        return PushReaction.DESTROY;
    }

    /**
     * tell redstone that this can be use with comparator
     */
    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    /**
     * the power fed into comparator (1 - 4)
     */
    @Override
    public int getAnalogOutputSignal(BlockState blockstate, Level world, BlockPos pos) {
        return numberOfAttachments(blockstate);
    }


    /**
     * This block is full of holes and can let light through
     */
    @Override
    public int getLightBlock(BlockState state, BlockGetter world, BlockPos pos) {
        return 1;
    }

    /**
     * Allow player to remove this block with water buckets, water bottles, or wet sponges
     */
    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState blockstate, Level world, BlockPos position, Player playerEntity, InteractionHand playerHand, BlockHitResult raytraceResult) {
        ItemStack itemstack = playerEntity.getItemInHand(playerHand);

        if ((itemstack.getItem() instanceof BucketItem &&
                ((BucketItemAccessor) itemstack.getItem()).thebumblezone_getFluid().is(FluidTags.WATER)) ||
                itemstack.getOrCreateTag().getString("Potion").contains("water") ||
                itemstack.getItem() == Items.WET_SPONGE ||
                itemstack.getItem() == BzItems.SUGAR_WATER_BOTTLE) {

            world.destroyBlock(position, false);

            world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
                    SoundEvents.PHANTOM_SWOOP, SoundSource.NEUTRAL, 1.0F, 1.0F);

            if (world instanceof ServerLevel) {
                if (blockstate.getValue(UP)) {
                    ((ServerLevel) world).sendParticles((ServerPlayer) playerEntity, ParticleTypes.FALLING_WATER, true, position.getX() + 0.5D, position.getY() + 0.95D, position.getZ() + 0.5D, 6, 0.3D, 0.0D, 0.3D, 1);
                }

                if (blockstate.getValue(NORTH)) {
                    ((ServerLevel) world).sendParticles((ServerPlayer) playerEntity, ParticleTypes.FALLING_WATER, true, position.getX() + 0.5D, position.getY() + 0.5D, position.getZ() + 0.05D, 6, 0.3D, 0.3D, 0.0D, 1);
                }

                if (blockstate.getValue(EAST)) {
                    ((ServerLevel) world).sendParticles((ServerPlayer) playerEntity, ParticleTypes.FALLING_WATER, true, position.getX() + 0.95D, position.getY() + 0.5D, position.getZ() + 0.5D, 6, 0.0D, 0.3D, 0.3D, 1);
                }

                if (blockstate.getValue(SOUTH)) {
                    ((ServerLevel) world).sendParticles((ServerPlayer) playerEntity, ParticleTypes.FALLING_WATER, true, position.getX() + 0.5D, position.getY() + 0.5D, position.getZ() + 0.95D, 6, 0.3D, 0.3D, 0.0D, 1);
                }

                if (blockstate.getValue(WEST)) {
                    ((ServerLevel) world).sendParticles((ServerPlayer) playerEntity, ParticleTypes.FALLING_WATER, true, position.getX() + 0.05D, position.getY() + 0.5D, position.getZ() + 0.5D, 6, 0.0D, 0.3D, 0.3D, 1);
                }

                if (blockstate.getValue(DOWN)) {
                    ((ServerLevel) world).sendParticles((ServerPlayer) playerEntity, ParticleTypes.FALLING_WATER, true, position.getX() + 0.5D, position.getY() + 0.05D, position.getZ() + 0.5D, 6, 0.3D, 0.0D, 0.3D, 1);
                }
            }

            return InteractionResult.SUCCESS;
        }

        return super.use(blockstate, world, position, playerEntity, playerHand, raytraceResult);
    }

}
