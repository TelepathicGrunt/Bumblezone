package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.apache.logging.log4j.core.jmx.Server;

public class HoneyFluidBlock extends FlowingFluidBlock {

    public static final int maxBottomLayer = 8;
    public static final IntegerProperty BOTTOM_LEVEL = IntegerProperty.create("bottom_level", 0, maxBottomLayer);
    public static final BooleanProperty FALLING = BlockStateProperties.FALLING;
    public static final BooleanProperty ABOVE_FLUID = BooleanProperty.create("above_support");

    public HoneyFluidBlock(java.util.function.Supplier<? extends FlowingFluid> supplier) {
        super(supplier, Properties.of(Material.WATER).noCollission().strength(100.0F, 100.0F).noDrops().speedFactor(0.15F));
        this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, 0).setValue(BOTTOM_LEVEL, 0).setValue(FALLING, false).setValue(ABOVE_FLUID, false));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(LEVEL).add(BOTTOM_LEVEL).add(FALLING).add(ABOVE_FLUID);
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (this.receiveNeighborFluids(world, pos, state)) {
            world.getLiquidTicks().scheduleTick(pos, state.getFluidState().getType(), this.getFluid().getTickDelay(world));
        }
    }

    private boolean receiveNeighborFluids(World world, BlockPos pos, BlockState state)  {
        boolean lavaflag = false;

        for (Direction direction : Direction.values()) {
            FluidState fluidState = world.getFluidState(pos.relative(direction));
            if (fluidState.is(FluidTags.LAVA)) {
                lavaflag = true;
                break;
            }
            else if(!fluidState.getType().equals(BzFluids.SUGAR_WATER_FLUID.get()) && fluidState.is(FluidTags.WATER) && fluidState.isSource()) {
                world.setBlock(pos.relative(direction), BzFluids.SUGAR_WATER_BLOCK.get().defaultBlockState(), 3);
            }
        }

        if (lavaflag) {
            FluidState ifluidstate = world.getFluidState(pos);
            if (ifluidstate.isSource()) {
                world.setBlockAndUpdate(pos, BzBlocks.SUGAR_INFUSED_STONE.get().defaultBlockState());
                this.triggerMixEffects(world, pos);
                return false;
            }

            if (ifluidstate.getHeight(world, pos) >= 0.44444445F) {
                world.setBlockAndUpdate(pos, BzBlocks.SUGAR_INFUSED_COBBLESTONE.get().defaultBlockState());
                this.triggerMixEffects(world, pos);
                return false;
            }
        }

        return true;
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        int fluidLevel = blockState.getValue(LEVEL);
        int bottomFluidLevel = blockState.getValue(BOTTOM_LEVEL);
        boolean isFalling = blockState.getValue(FALLING);
        FluidState fluidState;
        if(fluidLevel == 0) {
            fluidState = getFluid().getSource(false);
        }
        else {
            fluidState = getFluid().getFlowing(fluidLevel, isFalling).setValue(BOTTOM_LEVEL, bottomFluidLevel);
        }
        return fluidState.setValue(ABOVE_FLUID, blockState.getValue(ABOVE_FLUID));
    }

    /**
     * Heal bees if they are damaged or create honey source if pollinated
     */
    @Deprecated
    @Override
    public void entityInside(BlockState state, World world, BlockPos position, Entity entity) {
        double verticalSpeedDeltaLimit = 0.01D;
        if (entity instanceof BeeEntity) {
            BeeEntity beeEntity = ((BeeEntity) entity);
            if(beeEntity.hasNectar() && !state.getFluidState().isSource()) {
                ((BeeEntity)entity).setFlag(8, false);
                world.setBlock(position, BzFluids.HONEY_FLUID.get().defaultFluidState().createLegacyBlock(), 3);
            }

            if (beeEntity.getHealth() < beeEntity.getMaxHealth()) {
                float diff = beeEntity.getMaxHealth() - beeEntity.getHealth();
                beeEntity.heal(diff);
                BlockState currentState = world.getBlockState(position);
                if(currentState.is(BzFluids.HONEY_FLUID_BLOCK.get())) {
                    world.setBlock(position, currentState.setValue(HoneyFluidBlock.LEVEL, Math.max(currentState.getValue(HoneyFluidBlock.LEVEL) - (int)Math.ceil(diff), 1)), 3);
                }
            }
        }
        else if(Math.abs(entity.getDeltaMovement().y()) > verticalSpeedDeltaLimit && entity.fallDistance <= 0.2D){
            Vector3d vector3d = entity.getDeltaMovement();
            entity.setDeltaMovement(new Vector3d(vector3d.x, Math.copySign(verticalSpeedDeltaLimit, vector3d.y), vector3d.z));
        }

        super.entityInside(state, world, position, entity);
    }

    private void triggerMixEffects(World world, BlockPos pos) {
        world.levelEvent(1501, pos, 0);
    }
}
