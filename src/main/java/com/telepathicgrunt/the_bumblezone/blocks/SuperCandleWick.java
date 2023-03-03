package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.blocks.blockentities.IncenseCandleBlockEntity;
import com.telepathicgrunt.the_bumblezone.configs.BzConfig;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SuperCandleWick extends Block implements SimpleWaterloggedBlock {
    public static final BooleanProperty LIT = AbstractCandleBlock.LIT;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape AABB = Block.box(7.0D, 0.0D, 7.0D, 9.0D, 6.0D, 9.0D);
    private static final int NORMAL_LIGHT_LEVEL = 15;
    private static final int SOUL_LIGHT_LEVEL = 14;
    private final boolean isSoul;

    public SuperCandleWick(boolean isSoul) {
        super(Properties.of(Material.AIR, MaterialColor.COLOR_BLACK).noCollission().lightLevel((blockState) -> blockState.getValue(LIT) ? (isSoul ? SOUL_LIGHT_LEVEL : NORMAL_LIGHT_LEVEL) : 0));
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.FALSE).setValue(WATERLOGGED, Boolean.FALSE));
        this.isSoul = isSoul;
    }

    public boolean isSoul() {
        return isSoul;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, WATERLOGGED);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        return levelReader.getBlockState(blockPos.below()).getBlock() instanceof SuperCandle;
    }

    @Override
    public void neighborChanged(BlockState blockstate, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (!this.canSurvive(blockstate, level, pos)) {
            extinguish(null, blockstate, level, pos);
            level.destroyBlock(pos, false);
        }
        else {
            super.neighborChanged(blockstate, level, pos, block, fromPos, notify);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(BzTags.CANDLE_WICKS) || !newState.is(BzTags.CANDLE_WICKS)) {
            setBelowLit(level, pos, false);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter world, BlockPos blockPos, BlockState blockState, Fluid fluid) {
        return !blockState.getValue(WATERLOGGED) && fluid.is(FluidTags.WATER) && fluid.defaultFluidState().isSource();
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        if (!state.getValue(WATERLOGGED) && fluidState.is(FluidTags.WATER) && fluidState.isSource()) {
            BlockState blockstate = state.setValue(WATERLOGGED, Boolean.TRUE);
            if (state.getValue(LIT)) {
                extinguish(null, blockstate, level, pos);
            }
            else {
                level.setBlock(pos, blockstate, 3);
            }

            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!state.getValue(LIT) && entity instanceof Projectile projectile) {
            if (!level.isClientSide && projectile.isOnFire() && SuperCandle.canBeLit(level, state, pos.below())) {
                boolean litWick = SuperCandleWick.setLit(level, level.getBlockState(pos), pos, true);
                if (litWick && projectile.getOwner() instanceof ServerPlayer serverPlayer) {
                    BlockEntity blockEntity = level.getBlockEntity(pos.below());
                    if (blockEntity instanceof IncenseCandleBlockEntity incenseCandleBlockEntity &&
                            incenseCandleBlockEntity.getMobEffect() != null &&
                            incenseCandleBlockEntity.getMobEffect().isInstantenous() &&
                            !incenseCandleBlockEntity.getMobEffect().isBeneficial())
                    {
                        BzCriterias.PROJECTILE_LIGHT_INSTANT_INCENSE_CANDLE_TRIGGER.trigger(serverPlayer);
                    }
                }
            }
        }

        if (state.getValue(LIT) && !level.isClientSide()) {
            boolean isProjectile = entity instanceof Projectile;
            boolean entityInSpace =
                    isProjectile ||
                    Shapes.joinIsNotEmpty(
                            AABB.move(pos.getX(), pos.getY(), pos.getZ()),
                            Shapes.create(entity.getBoundingBox()),
                            BooleanOp.AND);

            if (entityInSpace && (BzConfig.superCandlesBurnsMobs || isProjectile)) {
                if (!entity.fireImmune()) {
                    entity.setRemainingFireTicks(entity.getRemainingFireTicks() + 1);
                    if (entity.getRemainingFireTicks() <= 0) {
                        entity.setSecondsOnFire(1);
                    }

                    entity.hurt(DamageSource.IN_FIRE, 0.5f);
                }
            }
        }
        super.entityInside(state, level, pos, entity);
    }

    // passed in position should be the spot directly below the wick
    public static boolean isSoulBelowInRange(LevelAccessor levelAccessor, BlockPos blockPos) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        mutableBlockPos.set(blockPos);
        ChunkAccess chunkAccess = levelAccessor.getChunk(blockPos);
        for (int i = 0; i < mutableBlockPos.getY() - chunkAccess.getMinBuildHeight(); i++) {
            BlockState currentState = chunkAccess.getBlockState(mutableBlockPos);
            if (currentState.is(BlockTags.SOUL_FIRE_BASE_BLOCKS)) {
                return true;
            }
            else if (!currentState.is(BzTags.CANDLE_BASES)) {
                return false;
            }
            mutableBlockPos.move(Direction.DOWN);
        }
        return false;
    }

    // passed in position should be the base candle itself
    public static BlockPos getLitWickPositionAbove(LevelAccessor levelAccessor, BlockPos blockPos) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        mutableBlockPos.set(blockPos);
        ChunkAccess chunkAccess = levelAccessor.getChunk(blockPos);
        for (int i = 0; i < chunkAccess.getMaxBuildHeight() - mutableBlockPos.getY(); i++) {
            BlockState currentState = chunkAccess.getBlockState(mutableBlockPos);
            if (currentState.is(BzTags.CANDLE_WICKS)) {
                return mutableBlockPos.immutable();
            }
            else if (!currentState.is(BzTags.CANDLE_BASES)) {
                return null;
            }
            mutableBlockPos.move(Direction.UP);
        }
        return null;
    }

    public static boolean setLit(LevelAccessor levelAccessor, BlockState blockState, BlockPos blockPos, boolean lit) {
        if (blockState.getBlock() instanceof SuperCandleWick && !(lit && blockState.getValue(WATERLOGGED))) {
            boolean isBelowSoul = isSoulBelowInRange(levelAccessor, blockPos.below());
            Block wickBlock = (isBelowSoul && lit) ? BzBlocks.SUPER_CANDLE_WICK_SOUL : BzBlocks.SUPER_CANDLE_WICK;
            boolean litWick = levelAccessor.setBlock(blockPos, wickBlock.defaultBlockState()
                        .setValue(LIT, lit)
                        .setValue(WATERLOGGED, blockState.getValue(WATERLOGGED)
                    ), 11) && lit;

            if (lit) {
                levelAccessor.playSound(null, blockPos, BzSounds.SUPER_CANDLE_WICK_LIT, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            setBelowLit(levelAccessor, blockPos, lit);
            return litWick;
        }
        return false;
    }

    public static void setBelowLit(LevelAccessor levelAccessor, BlockPos blockPos, boolean lit) {
        BlockPos belowPos = blockPos.below();
        BlockState candleBase = levelAccessor.getBlockState(belowPos);
        if (candleBase.getBlock() instanceof SuperCandle) {
            levelAccessor.setBlock(belowPos, candleBase.setValue(LIT, lit), 11);
        }
    }

    public static void extinguish(Player player, BlockState blockState, LevelAccessor levelAccessor, BlockPos blockPos) {
        setLit(levelAccessor, blockState, blockPos, false);
        if (blockState.getBlock() instanceof SuperCandleWick) {
            levelAccessor.addParticle(ParticleTypes.SMOKE, (double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.2, (double)blockPos.getZ() + 0.5, 0.0D, 0.1F, 0.0D);
        }

        levelAccessor.playSound(null, blockPos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
        levelAccessor.gameEvent(player, GameEvent.BLOCK_CHANGE, blockPos);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(LIT)) {
            addParticlesAndSound(level,
                new Vec3(
                    pos.getX() + 0.5D + ((random.nextDouble() * 0.26D) - 0.13D),
                    pos.getY() + 0.75D + (random.nextDouble() * 0.15D),
                    pos.getZ() + 0.5D + ((random.nextDouble() * 0.26D) - 0.13D)),
                random,
                this.isSoul);

            level.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5D, pos.getY() + 0.55D, pos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
        if (blockState.is(BzTags.CANDLE_WICKS) && blockState.getValue(LIT)) {
            if (isSoul) {
                return 3;
            }
            else {
                return 5;
            }
        }
        return 0;
    }

    private static void addParticlesAndSound(Level level, Vec3 offset, RandomSource random, boolean isSoul) {
        float chance = random.nextFloat();
        if (chance < 0.5F) {
            level.addParticle(ParticleTypes.SMOKE, offset.x, offset.y, offset.z, 0.0D, 0.0D, 0.0D);
            if (chance < 0.17F) {
                level.playLocalSound(offset.x, offset.y + 0.5D, offset.z, SoundEvents.CANDLE_AMBIENT, SoundSource.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
            }
        }

        if (chance < 0.2F) {
            if (isSoul) {
                level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, offset.x, offset.y - 0.75d, offset.z, 0.0D, 0.0D, 0.0D);
            }
            else {
                level.addParticle(ParticleTypes.SMALL_FLAME, offset.x, offset.y - 0.75d, offset.z, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return !(state.hasProperty(LIT) && state.getValue(LIT));
    }

    public static BlockPathTypes getBlockPathType(BlockState state) {
        if (state.hasProperty(LIT) && state.getValue(LIT)) {
            return BlockPathTypes.DAMAGE_FIRE;
        }
        return null;
    }

    public static void attemptCandleLighting(LevelAccessor levelAccessor, BlockPos blockPos) {
        BlockState currentState = levelAccessor.getBlockState(blockPos);
        if (currentState.is(BzTags.CANDLE_BASES)) {
            currentState = levelAccessor.getBlockState(blockPos.above());
        }

        if (currentState.getBlock() instanceof SuperCandleWick && !currentState.getValue(LIT) && !currentState.getValue(WATERLOGGED)) {
            boolean isBelowSoul = isSoulBelowInRange(levelAccessor, blockPos.below());
            Block wickBlock = isBelowSoul ? BzBlocks.SUPER_CANDLE_WICK_SOUL : BzBlocks.SUPER_CANDLE_WICK;
            levelAccessor.setBlock(blockPos, wickBlock.defaultBlockState().setValue(LIT, true), 11);
            levelAccessor.playSound(null, blockPos, BzSounds.SUPER_CANDLE_WICK_LIT, SoundSource.BLOCKS, 1.0F, 1.0F);
            setBelowLit(levelAccessor, blockPos, true);
        }
    }
}
