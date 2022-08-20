package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;


public class SuperCandleBase extends Block implements SimpleWaterloggedBlock, SuperCandle {
    public static final BooleanProperty LIT = AbstractCandleBlock.LIT;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape AABB = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 16.0D, 11.0D);

    public SuperCandleBase() {
        super(Properties.of(Material.DECORATION, MaterialColor.SAND).noOcclusion().strength(0.1F).sound(SoundType.CANDLE).lightLevel((blockState) -> blockState.getValue(LIT) ? 15 : 0));
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.FALSE).setValue(WATERLOGGED, Boolean.FALSE));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return AABB;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, WATERLOGGED);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        SuperCandle.placeWickIfPossible(level, pos, false);
        super.setPlacedBy(level, pos, state, placer, stack);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        boolean flag = fluidstate.getType() == Fluids.WATER;
        return super.getStateForPlacement(context).setValue(WATERLOGGED, flag);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return Block.canSupportCenter(level, pos.below(), Direction.UP);
    }

    @Override
    public void neighborChanged(BlockState blockstate, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        SuperCandle.placeWickIfPossible(world, pos, false);
        super.neighborChanged(blockstate, world, pos, block, fromPos, notify);
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
                SuperCandleWick.extinguish(null, level.getBlockState(pos.above()), level, pos.above());
                level.setBlock(pos, blockstate.setValue(LIT, false), 3);
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
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (player.getAbilities().mayBuild) {
            ItemStack handItem = player.getItemInHand(interactionHand);
            if (handItem.isEmpty() && blockState.getValue(LIT)) {
                SuperCandleWick.extinguish(player, level.getBlockState(blockPos.above()), level, blockPos.above());
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            else if (!blockState.getValue(LIT)) {
                if (handItem.is(BzTags.INFINITE_CANDLE_LIGHTING_ITEMS)) {
                    SuperCandleWick.setLit(level, level.getBlockState(blockPos.above()), blockPos.above(), true);
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
                else if (handItem.is(BzTags.DAMAGABLE_CANDLE_LIGHTING_ITEMS)) {
                    SuperCandleWick.setLit(level, level.getBlockState(blockPos.above()), blockPos.above(), true);
                    if (player instanceof ServerPlayer serverPlayer && !player.getAbilities().instabuild) {
                        handItem.hurt(1, level.getRandom(), serverPlayer);
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
                else if (handItem.is(BzTags.CONSUMABLE_CANDLE_LIGHTING_ITEMS)) {
                    SuperCandleWick.setLit(level, level.getBlockState(blockPos.above()), blockPos.above(), true);
                    if (!player.getAbilities().instabuild) {
                        handItem.shrink(1);
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        if (!level.isClientSide && projectile.isOnFire() && SuperCandle.canBeLit(level, state, hit.getBlockPos())) {
            SuperCandleWick.setLit(level, level.getBlockState(hit.getBlockPos().above()), hit.getBlockPos().above(), true);
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
        return blockState.is(BzTags.CANDLES) && blockState.getValue(LIT) ? 5 : 0;
    }
}
