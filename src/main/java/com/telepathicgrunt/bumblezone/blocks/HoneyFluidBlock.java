package com.telepathicgrunt.bumblezone.blocks;

import com.telepathicgrunt.bumblezone.mixin.entities.BeeEntityInvoker;
import com.telepathicgrunt.bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.bumblezone.modinit.BzFluids;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class HoneyFluidBlock extends FluidBlock {

    public static final int maxBottomLayer = 8;
    public static final IntProperty BOTTOM_LEVEL = IntProperty.of("bottom_level", 0, maxBottomLayer);
    public static final BooleanProperty FALLING = Properties.FALLING;
    public static final BooleanProperty ABOVE_FLUID = BooleanProperty.of("above_support");

    public HoneyFluidBlock(FlowableFluid fluid) {
        super(fluid, AbstractBlock.Settings.of(Material.WATER).noCollision().strength(100.0F, 100.0F).dropsNothing().velocityMultiplier(0.15F));
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(LEVEL, 0)
                .with(BOTTOM_LEVEL, 0)
                .with(FALLING, false)
                .with(ABOVE_FLUID, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(LEVEL, BOTTOM_LEVEL, FALLING, ABOVE_FLUID);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (this.neighboringFluidInteractions(world, pos)) {
            world.getFluidTickScheduler().schedule(pos, state.getFluidState().getFluid(), this.fluid.getTickRate(world));
        }
    }

    @Override
    public void onBlockAdded(BlockState blockState, World world, BlockPos blockPos, BlockState previousBlockState, boolean notify) {
        if (this.neighboringFluidInteractions(world, blockPos)) {
            world.getFluidTickScheduler().schedule(blockPos, blockState.getFluidState().getFluid(), this.fluid.getTickRate(world));
        }
    }

    private boolean neighboringFluidInteractions(World world, BlockPos pos)  {
        boolean lavaflag = false;

        for (Direction direction : Direction.values()) {
            FluidState fluidState = world.getFluidState(pos.offset(direction));
            if (fluidState.isIn(FluidTags.LAVA)) {
                lavaflag = true;
                break;
            }
            else if(!fluidState.getFluid().equals(BzFluids.SUGAR_WATER_FLUID) && fluidState.isIn(FluidTags.WATER) && fluidState.isStill()) {
                world.setBlockState(pos.offset(direction), BzFluids.SUGAR_WATER_BLOCK.getDefaultState(), 3);
            }
        }

        if (lavaflag) {
            FluidState ifluidstate = world.getFluidState(pos);
            if (ifluidstate.isStill()) {
                world.setBlockState(pos, BzBlocks.SUGAR_INFUSED_STONE.getDefaultState());
                this.triggerMixEffects(world, pos);
                return false;
            }

            if (ifluidstate.getHeight(world, pos) >= 0.44444445F) {
                world.setBlockState(pos, BzBlocks.SUGAR_INFUSED_COBBLESTONE.getDefaultState());
                this.triggerMixEffects(world, pos);
                return false;
            }
        }

        return true;
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        int fluidLevel = Math.min(Math.max(blockState.get(LEVEL), 0), 8);
        int bottomFluidLevel = Math.min(Math.max(blockState.get(BOTTOM_LEVEL), 0), maxBottomLayer);
        boolean isFalling = blockState.get(FALLING);
        FluidState fluidState;
        if(fluidLevel == 0) {
            fluidState = fluid.getStill(false);
        }
        else {
            fluidState = fluid.getFlowing(fluidLevel, isFalling).with(BOTTOM_LEVEL, bottomFluidLevel);
        }
        return fluidState.with(ABOVE_FLUID, blockState.get(ABOVE_FLUID));
    }

    /**
     * Heal bees if they are damaged or create honey source if pollinated
     */
    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos position, Entity entity) {
        double verticalSpeedDeltaLimit = 0.01D;
        if (entity instanceof BeeEntity beeEntity) {
            if(beeEntity.hasNectar() && !state.getFluidState().isStill()) {
                ((BeeEntityInvoker)entity).thebumblezone_callSetHasNectar(false);
                world.setBlockState(position, BzFluids.HONEY_FLUID.getDefaultState().getBlockState(), 3);
            }

            if (beeEntity.getHealth() < beeEntity.getMaxHealth()) {
                float diff = beeEntity.getMaxHealth() - beeEntity.getHealth();
                beeEntity.heal(diff);
                BlockState currentState = world.getBlockState(position);
                if(currentState.isOf(BzFluids.HONEY_FLUID_BLOCK)) {
                    world.setBlockState(position, currentState.with(HoneyFluidBlock.LEVEL, Math.max(currentState.get(HoneyFluidBlock.LEVEL) - (int)Math.ceil(diff), 1)), 3);
                }
            }
        }
        else if(Math.abs(entity.getVelocity().getY()) > verticalSpeedDeltaLimit && entity.fallDistance <= 0.2D){
            Vec3d vector3d = entity.getVelocity();
            entity.setVelocity(new Vec3d(vector3d.getX(), Math.copySign(verticalSpeedDeltaLimit, vector3d.getY()), vector3d.getZ()));
        }

        super.onEntityCollision(state, world, position, entity);
    }

    private void triggerMixEffects(World world, BlockPos pos) {
        world.syncWorldEvent(1501, pos, 0);
    }
}
