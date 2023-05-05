package com.telepathicgrunt.the_bumblezone.fluids;

import com.telepathicgrunt.the_bumblezone.items.EssenceOfTheBees;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

import static net.minecraft.world.level.material.FlowingFluid.FALLING;

public class HoneyFluidBlock extends LiquidBlock {

    public static final int maxBottomLayer = 8;
    public static final IntegerProperty BOTTOM_LEVEL = IntegerProperty.create("bottom_level", 0, maxBottomLayer);
    public static final BooleanProperty ABOVE_FLUID = BooleanProperty.create("above_support");

    public HoneyFluidBlock(Supplier<? extends FlowingFluid> fluid) {
        super(fluid, BlockBehaviour.Properties.of(Material.WATER).noCollission().strength(100.0F, 100.0F).noLootTable().speedFactor(0.15F));
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
    public void neighborChanged(BlockState blockState, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (this.neighboringFluidInteractions(world, pos)) {
            world.scheduleTick(pos, blockState.getFluidState().getType(), this.getFluid().getTickDelay(world));
        }
    }

    @Override
    public void onPlace(BlockState blockState, Level world, BlockPos blockPos, BlockState previousBlockState, boolean notify) {
        if (this.neighboringFluidInteractions(world, blockPos)) {
            world.scheduleTick(blockPos, blockState.getFluidState().getType(), this.getFluid().getTickDelay(world));
        }
    }

    private boolean neighboringFluidInteractions(Level world, BlockPos pos)  {
        boolean lavaflag = false;
        boolean lavadownflag = false;

        for (Direction direction : Direction.values()) {
            BlockPos sidePos = pos.relative(direction);
            FluidState sideFluid = world.getFluidState(sidePos);
            if (sideFluid.is(FluidTags.LAVA)) {
                lavaflag = true;
                if (direction == Direction.DOWN) {
                    lavadownflag = true;
                }
                break;
            }
            else if(!sideFluid.isEmpty() && !sideFluid.is(BzTags.HONEY_FLUID)) {
                FluidState currentFluid = world.getFluidState(pos);
                if (direction == Direction.DOWN && currentFluid.hasProperty(BOTTOM_LEVEL) && currentFluid.getValue(BOTTOM_LEVEL) != 0) {
                    continue;
                }

                if (direction == Direction.UP) {
                    world.setBlock(pos, BzBlocks.GLISTERING_HONEY_CRYSTAL.get().defaultBlockState(), 3);
                    return false;
                }

                BlockState sideState = world.getBlockState(sidePos);
                Material material = sideState.getMaterial();
                if (sideState.getBlock() instanceof LiquidBlock || material.isLiquid() || material.isReplaceable()) {
                    world.setBlock(sidePos, BzBlocks.GLISTERING_HONEY_CRYSTAL.get().defaultBlockState(), 3);
                }
                else if (!currentFluid.isSource()) {
                    world.setBlock(pos, BzBlocks.GLISTERING_HONEY_CRYSTAL.get().defaultBlockState(), 3);
                    return false;
                }
            }
        }

        if (lavaflag) {
            FluidState currentFluid = world.getFluidState(pos);
            if (currentFluid.isSource()) {
                world.setBlockAndUpdate(pos, BzBlocks.SUGAR_INFUSED_STONE.get().defaultBlockState());
                this.triggerMixEffects(world, pos);
                return false;
            }

            if (currentFluid.getHeight(world, pos) >= 0.44444445F || (lavadownflag && currentFluid.hasProperty(BOTTOM_LEVEL) && currentFluid.getValue(BOTTOM_LEVEL) == 0)) {
                world.setBlockAndUpdate(pos, BzBlocks.SUGAR_INFUSED_COBBLESTONE.get().defaultBlockState());
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
            fluidState = this.getFluid().getSource(false);
        }
        else {
            fluidState = this.getFluid().getFlowing(fluidLevel, isFalling).setValue(BOTTOM_LEVEL, bottomFluidLevel);
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
            if (beeEntity.getHealth() < beeEntity.getMaxHealth() && entity.getEyeInFluidType().isAir()) {
                float diff = beeEntity.getMaxHealth() - beeEntity.getHealth();
                beeEntity.heal(diff);
                BlockState currentState = world.getBlockState(position);
                if (currentState.is(BzFluids.HONEY_FLUID_BLOCK.get())) {
                    world.setBlock(position, currentState.setValue(HoneyFluidBlock.LEVEL, Math.max(currentState.getValue(HoneyFluidBlock.LEVEL) - (int) Math.ceil(diff), 1)), 3);
                }
            }
        }
        else if(Math.abs(entity.getDeltaMovement().y()) > verticalSpeedDeltaLimit && entity.fallDistance <= 0.2D) {
            Vec3 vec3 = entity.getDeltaMovement();
            entity.setDeltaMovement(new Vec3(vec3.x(), Math.copySign(verticalSpeedDeltaLimit, vec3.y()), vec3.z()));
        }

        if (entity instanceof ServerPlayer serverPlayer && EssenceOfTheBees.hasEssence(serverPlayer)) {
            serverPlayer.addEffect(new MobEffectInstance(
                    MobEffects.REGENERATION,
                    20,
                    0,
                    false,
                    false,
                    true));
        }

        super.entityInside(state, world, position, entity);
    }

    private void triggerMixEffects(Level world, BlockPos pos) {
        world.levelEvent(1501, pos, 0);
    }
}
