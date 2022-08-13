package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.ToIntFunction;

public class CandleWick extends Block implements SimpleWaterloggedBlock {
    public static final BooleanProperty LIT = AbstractCandleBlock.LIT;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final ToIntFunction<BlockState> LIGHT_EMISSION = (blockState) -> blockState.getValue(LIT) ? 15 : 0;
    private static final VoxelShape AABB = Block.box(7.0D, 0.0D, 7.0D, 9.0D, 6.0D, 9.0D);

    public CandleWick() {
        super(Properties.of(Material.AIR, MaterialColor.COLOR_BLACK).noCollission().lightLevel(CandleWick.LIGHT_EMISSION));
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.FALSE).setValue(WATERLOGGED, Boolean.FALSE));
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
        return levelReader.getBlockState(blockPos.below()).getBlock() instanceof CandleBase;
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
        setBelowLit(level, pos, false);
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
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        if (!state.getValue(WATERLOGGED) && fluidState.getType() == Fluids.WATER) {
            BlockState blockstate = state.setValue(WATERLOGGED, Boolean.TRUE);
            if (state.getValue(LIT)) {
                extinguish(null, blockstate, level, pos);
            }
            else {
                level.setBlock(pos, blockstate, 3);
            }

            level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(level));
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
        if (state.getValue(LIT)) {
            VoxelShape voxelShape = AABB;
            voxelShape = voxelShape.move(pos.getX(), pos.getY(), pos.getZ());
            if (Shapes.joinIsNotEmpty(voxelShape, Shapes.create(entity.getBoundingBox()), BooleanOp.AND)) {
                if (!entity.fireImmune()) {
                    entity.setRemainingFireTicks(entity.getRemainingFireTicks() + 1);
                    if (entity.getRemainingFireTicks() == 0) {
                        entity.setSecondsOnFire(1);
                    }
                }

                entity.hurt(DamageSource.IN_FIRE, 0.5f);
            }
        }
        super.entityInside(state, level, pos, entity);
    }

    public static void setLit(LevelAccessor levelAccessor, BlockState blockState, BlockPos blockPos, boolean lit) {
        if (blockState.getBlock() instanceof CandleWick && !(lit && blockState.getValue(WATERLOGGED))) {
            levelAccessor.setBlock(blockPos, blockState.setValue(LIT, lit), 11);
            setBelowLit(levelAccessor, blockPos, lit);
        }
    }

    public static void setBelowLit(LevelAccessor levelAccessor, BlockPos blockPos, boolean lit) {
        BlockPos belowPos = blockPos.below();
        BlockState candleBase = levelAccessor.getBlockState(belowPos);
        if (candleBase.getBlock() instanceof CandleBase) {
            levelAccessor.setBlock(belowPos, candleBase.setValue(LIT, lit), 11);
        }
    }

    public static void extinguish(Player player, BlockState blockState, LevelAccessor levelAccessor, BlockPos blockPos) {
        setLit(levelAccessor, blockState, blockPos, false);
        if (blockState.getBlock() instanceof CandleWick) {
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
                    pos.getX() + 0.5D + ((random.nextDouble() * 0.3D) - 0.15D),
                    pos.getY() + 0.1D + (random.nextDouble() * 0.7D),
                    pos.getZ() + 0.5D + ((random.nextDouble() * 0.3D) - 0.15D)),
                random);

            level.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5D, pos.getY() + 0.55D, pos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
        }
    }

    private static void addParticlesAndSound(Level level, Vec3 offset, RandomSource random) {
        float chance = random.nextFloat();
        if (chance < 0.75F) {
            level.addParticle(ParticleTypes.SMOKE, offset.x, offset.y, offset.z, 0.0D, 0.0D, 0.0D);
            if (chance < 0.17F) {
                level.playLocalSound(offset.x, offset.y + 0.5D, offset.z, SoundEvents.CANDLE_AMBIENT, SoundSource.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
            }
        }

        level.addParticle(ParticleTypes.SMALL_FLAME, offset.x, offset.y, offset.z, 0.0D, 0.0D, 0.0D);
    }
}
