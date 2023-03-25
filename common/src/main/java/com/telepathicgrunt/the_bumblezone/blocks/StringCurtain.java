package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.events.player.PlayerItemUseOnBlockEvent;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Map;


public class StringCurtain extends Block {

    public static final BooleanProperty ATTACHED = BooleanProperty.create("attached");
    public static final BooleanProperty CENTER = BooleanProperty.create("center");
    public static final BooleanProperty IS_END = BooleanProperty.create("is_end");
    public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
    protected final Map<Pair<Direction, Boolean>, VoxelShape> collisionShapeByMap;

    public StringCurtain() {
        this(Properties.of(Material.CLOTH_DECORATION, MaterialColor.WOOL)
                .noOcclusion()
                .lightLevel((blockState) -> 1)
                .sound(SoundType.WOOL)
                .strength(0.3F));
    }

    public StringCurtain(Properties properties) {
        super(properties);
        this.collisionShapeByMap = this.makeShapes();
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(ATTACHED, true)
                .setValue(CENTER, true)
                .setValue(IS_END, true)
                .setValue(HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(ATTACHED, CENTER, IS_END, HORIZONTAL_FACING);
    }

    protected Map<Pair<Direction, Boolean>, VoxelShape> makeShapes() {
        Map<Pair<Direction, Boolean>, VoxelShape> shapeMap = new Object2ObjectArrayMap<>();

        shapeMap.put(Pair.of(Direction.NORTH, false), Block.box(0, 0, 14, 16, 16, 16));
        shapeMap.put(Pair.of(Direction.SOUTH, false), Block.box(0, 0, 0, 16, 16, 2));
        shapeMap.put(Pair.of(Direction.WEST, false), Block.box(14, 0, 0, 16, 16, 16));
        shapeMap.put(Pair.of(Direction.EAST, false), Block.box(0, 0, 0, 2, 16, 16));

        VoxelShape temp = Block.box(0, 0, 7, 16, 16, 9);
        shapeMap.put(Pair.of(Direction.NORTH, true), temp);
        shapeMap.put(Pair.of(Direction.SOUTH, true), temp);

        temp = Block.box(7, 0, 0, 9, 16, 16);
        shapeMap.put(Pair.of(Direction.WEST, true), temp);
        shapeMap.put(Pair.of(Direction.EAST, true), temp);

        return shapeMap;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return collisionShapeByMap.get(Pair.of(blockState.getValue(HORIZONTAL_FACING), blockState.getValue(CENTER)));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        if (context instanceof EntityCollisionContext ctx) {
            Entity entity = ctx.getEntity();
            if (entity == null) {
                return getShape(state, worldIn, pos, context);
            }

            if ((entity instanceof Bee || entity.getType().is(BzTags.STRING_CURTAIN_BLOCKS_PATHFINDING_FOR_NON_BEE_MOB)) &&
                !entity.getType().is(BzTags.STRING_CURTAIN_FORCE_ALLOW_PATHFINDING))
            {
                if (state.is(BzTags.STRING_CURTAINS)) {
                    return getShape(state, worldIn, pos, context);
                }
            }
        }
        return Shapes.empty();
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        boolean entityShouldBePushed = shouldBlockOffEntity(entity);
        if (entityShouldBePushed) {
            if (!entity.hasControllingPassenger() &&
                !entity.isPassenger() &&
                entity.isPushable() &&
                blockState.is(BzTags.STRING_CURTAINS))
            {
                Vec3 centerPos = blockState.getShape(level, blockPos).bounds().getCenter().add(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                Vec3 beeCenter = entity.position();
                double speedSlowdown = 1.4f;
                double forcePushSpeed = 0.03f;
                if (blockState.getValue(HORIZONTAL_FACING).getAxis() == Direction.Axis.X) {
                    if (centerPos.x() > beeCenter.x()) {
                        if (entity.getDeltaMovement().x() > 0) {
                            entity.setDeltaMovement(
                                    entity.getDeltaMovement().x() / speedSlowdown,
                                    entity.getDeltaMovement().y(),
                                    entity.getDeltaMovement().z()
                            );
                        }
                        entity.push(-forcePushSpeed, 0, 0);
                    }
                    else {
                        if (entity.getDeltaMovement().x() < 0) {
                            entity.setDeltaMovement(
                                    entity.getDeltaMovement().x() / speedSlowdown,
                                    entity.getDeltaMovement().y(),
                                    entity.getDeltaMovement().z()
                            );
                        }
                        entity.push(forcePushSpeed, 0, 0);
                    }
                }
                else if (blockState.getValue(HORIZONTAL_FACING).getAxis() == Direction.Axis.Z) {
                    if (centerPos.z() > beeCenter.z()) {
                        if (entity.getDeltaMovement().z() > 0) {
                            entity.setDeltaMovement(
                                    entity.getDeltaMovement().x(),
                                    entity.getDeltaMovement().y(),
                                    entity.getDeltaMovement().z() / speedSlowdown
                            );
                        }
                        entity.push(0, 0, -forcePushSpeed);
                    }
                    else {
                        if (entity.getDeltaMovement().z() < 0) {
                            entity.setDeltaMovement(
                                    entity.getDeltaMovement().x(),
                                    entity.getDeltaMovement().y(),
                                    entity.getDeltaMovement().z() / speedSlowdown
                            );
                        }
                        entity.push(0, 0, forcePushSpeed);
                    }
                }
            }
        }

        super.entityInside(blockState, level, blockPos, entity);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext placeContext) {
        Level level = placeContext.getLevel();
        BlockPos blockpos = placeContext.getClickedPos().relative(placeContext.getClickedFace().getOpposite());
        BlockState clickedBlock = level.getBlockState(blockpos);
        if (!clickedBlock.isFaceSturdy(level, blockpos, placeContext.getClickedFace())) {
            return null;
        }

        BlockState belowState = level.getBlockState(placeContext.getClickedPos().below());

        if(placeContext.getClickedFace().getAxis() != Direction.Axis.Y) {
            return defaultBlockState()
                    .setValue(ATTACHED, true)
                    .setValue(CENTER, false)
                    .setValue(IS_END, !belowState.is(BzTags.STRING_CURTAINS))
                    .setValue(HORIZONTAL_FACING, placeContext.getClickedFace());
        }
        if(placeContext.getClickedFace() == Direction.DOWN) {
            return defaultBlockState()
                    .setValue(ATTACHED, true)
                    .setValue(CENTER, true)
                    .setValue(IS_END, !belowState.is(BzTags.STRING_CURTAINS))
                    .setValue(HORIZONTAL_FACING, placeContext.getHorizontalDirection());
        }

        return null;
    }

    @Override
    public boolean canSurvive(BlockState blockstate, LevelReader world, BlockPos pos) {
        if (!blockstate.is(BzTags.STRING_CURTAINS)) {
            return false;
        }

        if (blockstate.getValue(CENTER) || !blockstate.getValue(ATTACHED)) {
            BlockState aboveState = world.getBlockState(pos.above());
            return (aboveState.is(BzTags.STRING_CURTAINS) && !blockstate.getValue(ATTACHED)) ||
                    (aboveState.isFaceSturdy(world, pos.above(), Direction.DOWN) && blockstate.getValue(ATTACHED));
        }
        else {
            Direction facing = blockstate.getValue(HORIZONTAL_FACING);
            BlockState sideState = world.getBlockState(pos.relative(facing.getOpposite()));
            BlockState aboveState = world.getBlockState(pos.above());
            return sideState.isFaceSturdy(world, pos.relative(facing.getOpposite()), facing) || aboveState.isFaceSturdy(world, pos.above(), Direction.DOWN);
        }
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        blockUpdateCurtainChainUpward(level, blockPos);
        super.onRemove(blockState, level, blockPos, blockState2, bl);
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        if (!level.isClientSide) {
            if (!blockState.canSurvive(level, blockPos)) {
                level.destroyBlock(blockPos, true);
            }

            BlockState belowState = level.getBlockState(blockPos.below());
            boolean belowCurtain = belowState.is(BzTags.STRING_CURTAINS);
            if (belowCurtain == blockState.getValue(IS_END)) {
                boolean showEnds = !belowCurtain ||
                        (belowState.getValue(CENTER) != blockState.getValue(CENTER) ||
                        belowState.getValue(HORIZONTAL_FACING) != blockState.getValue(HORIZONTAL_FACING));

                level.setBlock(blockPos,
                        defaultBlockState()
                        .setValue(ATTACHED, blockState.getValue(ATTACHED))
                        .setValue(CENTER, blockState.getValue(CENTER))
                        .setValue(IS_END, showEnds)
                        .setValue(HORIZONTAL_FACING, blockState.getValue(HORIZONTAL_FACING)),
                        3);
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState blockstate, Level world, BlockPos position, Player playerEntity, InteractionHand playerHand, BlockHitResult raytraceResult) {
        ItemStack itemstack = playerEntity.getItemInHand(playerHand);
        if (blockstate.is(BzTags.STRING_CURTAINS)) {
            if (itemstack.is(BzTags.STRING_CURTAINS_CURTAIN_EXTENDING_ITEMS)) {
                boolean success = extendCurtainIfPossible(blockstate, world, position);
                if (success) {
                    if (!playerEntity.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }

                    if(playerEntity instanceof ServerPlayer) {
                        BzCriterias.EXTEND_STRING_CURTAIN_TRIGGER.trigger((ServerPlayer) playerEntity);
                    }
                }
                return InteractionResult.SUCCESS;
            }
            else if (itemstack.is(BzTags.STRING_CURTAINS_ITEMS)) {
                playerEntity.displayClientMessage(Component.translatable("block.the_bumblezone.string_curtain.extending_clarification").withStyle(ChatFormatting.WHITE), true);
                return InteractionResult.FAIL;
            }
        }
        return super.use(blockstate, world, position, playerEntity, playerHand, raytraceResult);
    }

    public static InteractionResult onBlockInteractEvent(PlayerItemUseOnBlockEvent event) {
        Player player = event.user();
        InteractionHand interactionHand = event.hand();
        if (player != null && player.getItemInHand(interactionHand).is(BzTags.STRING_CURTAINS_CURTAIN_EXTENDING_ITEMS)) {
            BlockHitResult hitResult = event.hitResult();
            BlockState clickedState = event.level().getBlockState(hitResult.getBlockPos());
            if (clickedState.is(BzTags.STRING_CURTAINS)) {
                return null;
            }

            BlockPos pos = hitResult.getBlockPos().relative(hitResult.getDirection()).above();
            BlockState aboveState = player.getLevel().getBlockState(pos);
            if (aboveState.is(BzTags.STRING_CURTAINS)) {
                InteractionResult interactionResult = aboveState.use(player.getLevel(), player, interactionHand, new BlockHitResult(
                        hitResult.getLocation().add(0, 1, 0),
                        hitResult.getDirection(),
                        pos,
                        hitResult.isInside()
                ));

                if (interactionResult != InteractionResult.PASS) {
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return null;
    }

    public static boolean extendCurtainIfPossible(BlockState blockstate, Level world, BlockPos position) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos().set(position);
        BlockState belowState = world.getBlockState(mutableBlockPos.move(Direction.DOWN));
        while (belowState.is(BzTags.STRING_CURTAINS) && world.isInWorldBounds(mutableBlockPos)) {
            belowState = world.getBlockState(mutableBlockPos.move(Direction.DOWN));
        }

        if (world.isInWorldBounds(mutableBlockPos) && belowState.isAir()) {
            world.setBlock(
                    mutableBlockPos,
                    blockstate.getBlock()
                        .defaultBlockState()
                        .setValue(ATTACHED, false)
                        .setValue(CENTER, blockstate.getValue(CENTER))
                        .setValue(HORIZONTAL_FACING, blockstate.getValue(HORIZONTAL_FACING)),
                    3);

            blockUpdateCurtainChainUpward(world, mutableBlockPos);
            return true;
        }
        return false;
    }

    private static void blockUpdateCurtainChainUpward(Level world, BlockPos position) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos().set(position);
        BlockState aboveState = world.getBlockState(mutableBlockPos.move(Direction.UP));
        while (aboveState.is(BzTags.STRING_CURTAINS) && world.isInWorldBounds(mutableBlockPos)) {
            world.blockUpdated(mutableBlockPos, aboveState.getBlock());
            mutableBlockPos.move(Direction.UP);
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(HORIZONTAL_FACING, rot.rotate(state.getValue(HORIZONTAL_FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(HORIZONTAL_FACING)));
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
        int comparatorPower = 0;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos().set(pos);
        BlockState currentState = level.getBlockState(mutableBlockPos.move(Direction.DOWN));

        while (currentState.is(BzTags.STRING_CURTAINS) && comparatorPower < 15) {
            comparatorPower++;
            currentState = level.getBlockState(mutableBlockPos.move(Direction.DOWN));
        }

        return comparatorPower;
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
        return PushReaction.DESTROY;
    }

    @Nullable
    public static BlockPathTypes getCurtainBlockPathType(Entity mob, BlockGetter blockGetter, BlockPos blockPos, BlockPathTypes blockPathType) {
        if (blockPathType == BlockPathTypes.OPEN && mob != null) {
            boolean shouldBlockPathfinding = shouldBlockOffEntity(mob);
            if (shouldBlockPathfinding && blockGetter.getBlockState(blockPos).is(BzTags.STRING_CURTAINS)) {
                return BlockPathTypes.BLOCKED;
            }
        }
        return null;
    }

    public static boolean shouldBlockOffEntity(Entity mob) {
        boolean shouldBlockPathfinding =
                (mob instanceof Bee || mob.getType().is(BzTags.STRING_CURTAIN_BLOCKS_PATHFINDING_FOR_NON_BEE_MOB)) &&
                    !mob.getType().is(BzTags.STRING_CURTAIN_FORCE_ALLOW_PATHFINDING);

        if (!shouldBlockPathfinding && !ModChecker.HOST_BEE_COMPATS.isEmpty()) {
            for (ModCompat compat : ModChecker.HOST_BEE_COMPATS) {
                if (compat.isHostBee(mob)) {
                    shouldBlockPathfinding = true;
                    break;
                }
            }
        }

        return shouldBlockPathfinding;
    }
}
