package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import com.telepathicgrunt.the_bumblezone.entities.mobs.HoneySlimeEntity;
import com.telepathicgrunt.the_bumblezone.items.HoneyBeeLeggings;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import it.unimi.dsi.fastutil.objects.Object2ShortMap;
import it.unimi.dsi.fastutil.objects.Object2ShortOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectArrayMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
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
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.Rotation;
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

import java.util.Map;

public class StickyHoneyResidue extends Block {
    public static final BooleanProperty UP = PipeBlock.UP;
    public static final BooleanProperty DOWN = PipeBlock.DOWN;
    public static final BooleanProperty WEST = PipeBlock.WEST;
    public static final BooleanProperty EAST = PipeBlock.EAST;
    public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
    public static final BooleanProperty NORTH = PipeBlock.NORTH;
    private static final VoxelShape[] BASE_SHAPES_BY_DIRECTION_ORDINAL = new VoxelShape[] {
            Block.box(0, 0, 0, 16, 1, 16),
            Block.box(0, 15, 0, 16, 16, 16),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D),
            Block.box(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D),
            Block.box(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)
    };

    protected final Short2ObjectMap<VoxelShape> shapeByIndex = new Short2ObjectArrayMap<>();
    protected final Short2ObjectMap<AABB> aabbShapeByIndex = new Short2ObjectArrayMap<>();
    private final Object2ShortMap<BlockState> stateToIndex = new Object2ShortOpenHashMap<>();
    public static final Map<Direction, BooleanProperty> FACING_TO_PROPERTY_MAP =
            PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().collect(Util.toMap());

    public StickyHoneyResidue() {
        super(BlockBehaviour.Properties.of(BzBlocks.ORANGE_NOT_SOLID, MaterialColor.TERRACOTTA_ORANGE).noCollission().strength(6.0f, 0.0f).noOcclusion());
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

    protected short getShapeIndex(BlockState blockState) {
        return this.stateToIndex.computeIfAbsent(blockState, (a) -> {
            short bitFlag = 0;
            for (Direction direction : Direction.values()) {
                if (blockState.getValue(FACING_TO_PROPERTY_MAP.get(direction))) {
                    bitFlag |= (1 << direction.ordinal());
                }
            }
            return bitFlag;
        });
    }

    /**
     * Returns the shape based on the state of the block.
     * Shape is cached
     */
    @Override
    public VoxelShape getShape(BlockState blockstate, BlockGetter world, BlockPos pos, CollisionContext context) {
        return shapeByIndex.computeIfAbsent(
            getShapeIndex(blockstate),
            (bitFlag) -> {
                VoxelShape shape = Shapes.empty();
                for (Direction direction : Direction.values()) {
                    if (((bitFlag >> direction.ordinal()) & 1) != 0) {
                        shape = Shapes.or(shape, BASE_SHAPES_BY_DIRECTION_ORDINAL[direction.ordinal()]);
                    }
                }
                return shape;
            }
        );
    }

    public AABB getAABBShape(BlockState blockstate, BlockGetter world, BlockPos pos, CollisionContext context) {
        return aabbShapeByIndex.computeIfAbsent(
                getShapeIndex(blockstate),
                (bitFlag) -> {
                    VoxelShape shape = getShape(blockstate, world, pos, context);
                    if (shape.isEmpty()) {
                        return AABB.ofSize(new Vec3(0,0,0),0,0,0);
                    }
                    return shape.bounds();
                }
        );
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_181239_, BlockGetter p_181240_, BlockPos p_181241_) {
        return true;
    }

    public static boolean isAcceptableNeighbour(BlockGetter p_57854_, BlockPos p_57855_, Direction p_57856_) {
        BlockState blockstate = p_57854_.getBlockState(p_57855_);
        return Block.isFaceFull(blockstate.getCollisionShape(p_57854_, p_57855_), p_57856_.getOpposite());
    }

    private int countFaces(BlockState blockState) {
        int i = 0;
        for(BooleanProperty booleanproperty : FACING_TO_PROPERTY_MAP.values()) {
            if (blockState.getValue(booleanproperty)) {
                ++i;
            }
        }
        return i;
    }

    @Override
    public boolean canBeReplaced(BlockState blockState, BlockPlaceContext blockPlaceContext) {
        BlockState blockstate = blockPlaceContext.getLevel().getBlockState(blockPlaceContext.getClickedPos());
        if (blockstate.is(this)) {
            return this.countFaces(blockstate) < FACING_TO_PROPERTY_MAP.size();
        }
        else {
            return super.canBeReplaced(blockState, blockPlaceContext);
        }
    }

    /**
     * Slows all entities inside the block.
     */
    @Deprecated
    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if (entity instanceof Bee || entity instanceof BeehemothEntity || entity instanceof HoneySlimeEntity) {
            return;
        }

        ItemStack beeLeggings = HoneyBeeLeggings.getEntityBeeLegging(entity);
        if(!beeLeggings.isEmpty()) {
            super.entityInside(blockState, level, blockPos, entity);
            return;
        }

        AABB aabb = getAABBShape(blockState, level, blockPos, null).move(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        if (aabb.intersects(entity.getBoundingBox())) {

            entity.makeStuckInBlock(blockState, new Vec3(0.35D, 0.2F, 0.35D));
            if (entity instanceof LivingEntity livingEntity && !(entity instanceof Player player && player.isCreative())) {
                livingEntity.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN,
                        200,
                        1,
                        false,
                        false,
                        true));
            }
        }

        super.entityInside(blockState, level, blockPos, entity);
    }

    /**
     * Is spot valid (has at least 1 face possible).
     */
    @Override
    public boolean canSurvive(BlockState blockstate, LevelReader world, BlockPos pos) {
        return hasAtleastOneAttachment(this.setAttachments(blockstate, world, pos));
    }

    /**
     * Returns true if the block has at least one face (it exists).
     */
    public static boolean hasAtleastOneAttachment(BlockState blockstate) {
        return numberOfAttachments(blockstate) > 0;
    }

    /**
     * How many faces this block has at this time.
     */
    private static int numberOfAttachments(BlockState blockstate) {
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
        return !hasAtleastOneAttachment(newBlockstate) ? Blocks.AIR.defaultBlockState() : newBlockstate;
    }

    @Override
    public void tick(BlockState blockstate, ServerLevel world, BlockPos currentPos, RandomSource random) {
        super.tick(blockstate, world, currentPos, random);
        BlockState newBlockstate = this.setAttachments(blockstate, world, currentPos);
        world.setBlock(currentPos, newBlockstate, 3);
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

        if ((itemstack.getItem() instanceof BucketItem bucketItem &&
                bucketItem.getFluid().is(FluidTags.WATER)) ||
                (itemstack.hasTag() && itemstack.getOrCreateTag().getString("Potion").contains("water")) ||
                itemstack.getItem() == Items.WET_SPONGE ||
                itemstack.getItem() == BzItems.SUGAR_WATER_BOTTLE.get()) {

            if(itemstack.getItem() == Items.WET_SPONGE && playerEntity instanceof ServerPlayer) {
                BzCriterias.CLEANUP_STICKY_HONEY_RESIDUE_TRIGGER.trigger((ServerPlayer) playerEntity);
            }

            world.destroyBlock(position, false);

            world.playSound(
                    playerEntity,
                    playerEntity.getX(),
                    playerEntity.getY(),
                    playerEntity.getZ(),
                    BzSounds.WASHING_RESIDUES.get(),
                    SoundSource.PLAYERS,
                    1.0F,
                    1.0F);

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

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return switch (rotation) {
            case CLOCKWISE_180 -> blockState.setValue(NORTH, blockState.getValue(SOUTH)).setValue(EAST, blockState.getValue(WEST)).setValue(SOUTH, blockState.getValue(NORTH)).setValue(WEST, blockState.getValue(EAST));
            case COUNTERCLOCKWISE_90 -> blockState.setValue(NORTH, blockState.getValue(EAST)).setValue(EAST, blockState.getValue(SOUTH)).setValue(SOUTH, blockState.getValue(WEST)).setValue(WEST, blockState.getValue(NORTH));
            case CLOCKWISE_90 -> blockState.setValue(NORTH, blockState.getValue(WEST)).setValue(EAST, blockState.getValue(NORTH)).setValue(SOUTH, blockState.getValue(EAST)).setValue(WEST, blockState.getValue(SOUTH));
            default -> blockState;
        };
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return switch (mirror) {
            case LEFT_RIGHT -> blockState.setValue(NORTH, blockState.getValue(SOUTH)).setValue(SOUTH, blockState.getValue(NORTH));
            case FRONT_BACK -> blockState.setValue(EAST, blockState.getValue(WEST)).setValue(WEST, blockState.getValue(EAST));
            default -> super.mirror(blockState, mirror);
        };
    }

    /**
     * Called periodically clientside on blocks near the player to show honey particles
     */
    @Override
    public void animateTick(BlockState blockState, Level world, BlockPos position, RandomSource random) {
        //chance of particle in this tick
        for (int i = 0; i == random.nextInt(50); ++i) {
            Direction randomDirection = Direction.values()[random.nextInt(Direction.values().length)];
            if (randomDirection != Direction.DOWN) {
                this.addParticle(ParticleTypes.DRIPPING_HONEY, random, world, position, blockState, randomDirection);
            }
        }
    }

    /**
     * intermediary method to apply the blockshape and ranges that the particle can spawn in for the next addParticle method
     */
    protected void addParticle(ParticleOptions particleType, RandomSource random, Level world, BlockPos blockPos, BlockState blockState, Direction direction) {
        short bitFlag = getShapeIndex(blockState);
        if(((bitFlag >> direction.ordinal()) & 1) != 0) {
            VoxelShape chosenSide = BASE_SHAPES_BY_DIRECTION_ORDINAL[direction.ordinal()];
            this.addParticle(
                    particleType,
                    world,
                    random,
                    blockPos.getX() + chosenSide.min(Direction.Axis.X),
                    blockPos.getX() + chosenSide.max(Direction.Axis.X),
                    blockPos.getY() + chosenSide.min(Direction.Axis.Y),
                    blockPos.getY() + chosenSide.max(Direction.Axis.Y),
                    blockPos.getZ() + chosenSide.min(Direction.Axis.Z),
                    blockPos.getZ() + chosenSide.max(Direction.Axis.Z));
        }
    }

    /**
     * Adds the actual particle into the world within the given range
     */
    private void addParticle(ParticleOptions particleType, Level world, RandomSource random, double xMin, double xMax, double yMin, double yMax, double zMax, double zMin) {
        world.addParticle(particleType, Mth.lerp(random.nextDouble(), xMin, xMax), Mth.lerp(random.nextDouble(), yMin, yMax), Mth.lerp(random.nextDouble(), zMin, zMax), 0.0D, 0.0D, 0.0D);
    }
}
