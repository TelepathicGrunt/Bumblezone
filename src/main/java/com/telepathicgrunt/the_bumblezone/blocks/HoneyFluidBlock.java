package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.mixin.entities.BeeEntityInvoker;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;

public class HoneyFluidBlock extends LiquidBlock {

    public static final int maxBottomLayer = 8;
    public static final IntegerProperty BOTTOM_LEVEL = IntegerProperty.create("bottom_level", 0, maxBottomLayer);
    public static final BooleanProperty FALLING = BlockStateProperties.FALLING;
    public static final BooleanProperty ABOVE_FLUID = BooleanProperty.create("above_support");

    public HoneyFluidBlock(FlowingFluid fluid) {
        super(fluid, BlockBehaviour.Properties.of(Material.WATER).noCollission().strength(100.0F, 100.0F).noDrops().speedFactor(0.15F));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(LEVEL, 0)
                .setValue(BOTTOM_LEVEL, 0)
                .setValue(FALLING, false)
                .setValue(ABOVE_FLUID, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(LEVEL, BOTTOM_LEVEL, FALLING, ABOVE_FLUID);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (this.neighboringFluidInteractions(world, pos)) {
            world.scheduleTick(pos, state.getFluidState().getType(), this.fluid.getTickDelay(world));
        }
    }

    @Override
    public void onPlace(BlockState blockState, Level world, BlockPos blockPos, BlockState previousBlockState, boolean notify) {
        if (this.neighboringFluidInteractions(world, blockPos)) {
            world.scheduleTick(blockPos, blockState.getFluidState().getType(), this.fluid.getTickDelay(world));
        }
    }

    private boolean neighboringFluidInteractions(Level world, BlockPos pos)  {
        boolean lavaflag = false;

        for (Direction direction : Direction.values()) {
            FluidState fluidState = world.getFluidState(pos.relative(direction));
            if (fluidState.is(FluidTags.LAVA)) {
                lavaflag = true;
                break;
            }
            else if(!fluidState.getType().equals(BzFluids.SUGAR_WATER_FLUID) && fluidState.is(FluidTags.WATER) && fluidState.isSource()) {
                world.setBlock(pos.relative(direction), BzFluids.SUGAR_WATER_BLOCK.defaultBlockState(), 3);
            }
        }

        if (lavaflag) {
            FluidState ifluidstate = world.getFluidState(pos);
            if (ifluidstate.isSource()) {
                world.setBlockAndUpdate(pos, BzBlocks.SUGAR_INFUSED_STONE.defaultBlockState());
                this.triggerMixEffects(world, pos);
                return false;
            }

            if (ifluidstate.getHeight(world, pos) >= 0.44444445F) {
                world.setBlockAndUpdate(pos, BzBlocks.SUGAR_INFUSED_COBBLESTONE.defaultBlockState());
                this.triggerMixEffects(world, pos);
                return false;
            }
        }

        return true;
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        int fluidLevel = Math.min(Math.max(blockState.getValue(LEVEL), 0), 8);
        int bottomFluidLevel = Math.min(Math.max(blockState.getValue(BOTTOM_LEVEL), 0), maxBottomLayer);
        boolean isFalling = blockState.getValue(FALLING);
        FluidState fluidState;
        if(fluidLevel == 0) {
            fluidState = fluid.getSource(false);
        }
        else {
            fluidState = fluid.getFlowing(fluidLevel, isFalling).setValue(BOTTOM_LEVEL, bottomFluidLevel);
        }
        return fluidState.setValue(ABOVE_FLUID, blockState.getValue(ABOVE_FLUID));
    }

    /**
     * Heal bees if they are damaged or create honey source if pollinated
     */
    @Override
    public void entityInside(BlockState state, Level world, BlockPos position, Entity entity) {
        double verticalSpeedDeltaLimit = 0.01D;
        if (entity instanceof Bee beeEntity) {
            if(beeEntity.hasNectar() && !state.getFluidState().isSource()) {
                ((BeeEntityInvoker)entity).thebumblezone_callSetHasNectar(false);
                world.setBlock(position, BzFluids.HONEY_FLUID.defaultFluidState().createLegacyBlock(), 3);
            }

            if (beeEntity.getHealth() < beeEntity.getMaxHealth()) {
                float diff = beeEntity.getMaxHealth() - beeEntity.getHealth();
                beeEntity.heal(diff);
                BlockState currentState = world.getBlockState(position);
                if(currentState.is(BzFluids.HONEY_FLUID_BLOCK)) {
                    world.setBlock(position, currentState.setValue(HoneyFluidBlock.LEVEL, Math.max(currentState.getValue(HoneyFluidBlock.LEVEL) - (int)Math.ceil(diff), 1)), 3);
                }
            }
        }
        else if(Math.abs(entity.getDeltaMovement().y()) > verticalSpeedDeltaLimit && entity.fallDistance <= 0.2D) {
            Vec3 vector3d = entity.getDeltaMovement();
            entity.setDeltaMovement(new Vec3(vector3d.x(), Math.copySign(verticalSpeedDeltaLimit, vector3d.y()), vector3d.z()));
        }

        super.entityInside(state, world, position, entity);
    }

    private void triggerMixEffects(Level world, BlockPos pos) {
        world.levelEvent(1501, pos, 0);
    }
}
