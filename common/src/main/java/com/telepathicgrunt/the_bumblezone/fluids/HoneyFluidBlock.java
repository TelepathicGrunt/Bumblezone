package com.telepathicgrunt.the_bumblezone.fluids;

import com.teamresourceful.resourcefullib.common.fluid.data.FluidData;
import com.telepathicgrunt.the_bumblezone.fluids.base.FluidGetter;
import com.telepathicgrunt.the_bumblezone.items.essence.EssenceOfTheBees;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.services.PlatformService;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.Vec3;

import static net.minecraft.world.level.material.FlowingFluid.FALLING;

public class HoneyFluidBlock extends LiquidBlock implements FluidGetter {

    public static final int maxBottomLayer = 8;
    public static final IntegerProperty BOTTOM_LEVEL = IntegerProperty.create("bottom_level", 0, maxBottomLayer);
    public static final BooleanProperty ABOVE_FLUID = BooleanProperty.create("above_support");

    public HoneyFluidBlock(FluidData baseFluid) {
        super(baseFluid.still().get(), BlockBehaviour.Properties.of()
                .mapColor(MapColor.TERRACOTTA_ORANGE)
                .liquid()
                .noCollision()
                .strength(100.0F, 100.0F)
                .speedFactor(0.15F)
                .noLootTable()
                .replaceable()
                .sound(SoundType.EMPTY)
                .pushReaction(PushReaction.DESTROY));

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(LEVEL, 0)
                .setValue(BOTTOM_LEVEL, 0)
                .setValue(FALLING, false)
                .setValue(ABOVE_FLUID, false));

        baseFluid.setBlock(() -> this);
    }

    @Override
    public FlowingFluid getFluid() {
        return this.fluid;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(LEVEL, BOTTOM_LEVEL, FALLING, ABOVE_FLUID);
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, Orientation orientation, boolean notify) {
        if (this.neighboringFluidInteractions(level, blockPos)) {
            level.scheduleTick(blockPos, blockState.getFluidState().getType(), HoneyFluid.adjustedFlowSpeed(this.getFluid().getTickDelay(level), level, blockPos));
        }
        else if (blockState.getFluidState().isSource()) {
            level.scheduleTick(blockPos, blockState.getFluidState().getType(), HoneyFluid.adjustedFlowSpeed(this.getFluid().getTickDelay(level), level, blockPos));
        }
    }

    @Override
    public void onPlace(BlockState blockState, Level world, BlockPos blockPos, BlockState previousBlockState, boolean notify) {
        if (this.neighboringFluidInteractions(world, blockPos)) {
            world.scheduleTick(blockPos, blockState.getFluidState().getType(), HoneyFluid.adjustedFlowSpeed(this.getFluid().getTickDelay(world), world, blockPos));
        }
    }

    private boolean neighboringFluidInteractions(Level level, BlockPos pos)  {
        boolean lavaflag = false;
        boolean lavadownflag = false;

        for (Direction direction : Direction.values()) {
            BlockPos sidePos = pos.relative(direction);
            FluidState sideFluid = level.getFluidState(sidePos);
            if (sideFluid.is(FluidTags.LAVA)) {
                lavaflag = true;
                if (direction == Direction.DOWN) {
                    lavadownflag = true;
                }
                break;
            }
            else if(!sideFluid.isEmpty() && !sideFluid.is(BzTags.HONEY_FLUID)) {
                FluidState currentFluid = level.getFluidState(pos);
                if (direction == Direction.DOWN && currentFluid.hasProperty(BOTTOM_LEVEL) && currentFluid.getValue(BOTTOM_LEVEL) != 0) {
                    continue;
                }

                if (direction == Direction.UP) {
                    level.setBlock(pos, BzBlocks.GLISTERING_HONEY_CRYSTAL.get().defaultBlockState(), 3);
                    return false;
                }

                BlockState sideState = level.getBlockState(sidePos);
                if (sideState.getBlock() instanceof LiquidBlock || (!sideState.getFluidState().isEmpty() && sideState.getCollisionShape(level, sidePos).isEmpty()) || sideState.canBeReplaced()) {
                    level.setBlock(sidePos, BzBlocks.GLISTERING_HONEY_CRYSTAL.get().defaultBlockState(), 3);
                }
                else if (!currentFluid.isSource()) {
                    level.setBlock(pos, BzBlocks.GLISTERING_HONEY_CRYSTAL.get().defaultBlockState(), 3);
                    return false;
                }
            }
        }

        if (lavaflag) {
            FluidState currentFluid = level.getFluidState(pos);
            if (currentFluid.isSource()) {
                BlockState resultBlockState = BzBlocks.SUGAR_INFUSED_STONE.get().defaultBlockState();
                for (ModCompat compat : ModChecker.HONEY_FLUID_LAVA_INTERACTION_COMPATS) {
                    BlockState moddedResultBlockState = compat.honeyLavaResultBlock(currentFluid);
                    if (moddedResultBlockState != null) {
                        resultBlockState = moddedResultBlockState;
                    }
                }

                level.setBlockAndUpdate(pos, resultBlockState);
                this.triggerMixEffects(level, pos);
                return false;
            }

            if (!lavadownflag || currentFluid.hasProperty(BOTTOM_LEVEL) && currentFluid.getValue(BOTTOM_LEVEL) == 0) {
                BlockState resultBlockState = BzBlocks.SUGAR_INFUSED_COBBLESTONE.get().defaultBlockState();
                for (ModCompat compat : ModChecker.HONEY_FLUID_LAVA_INTERACTION_COMPATS) {
                    BlockState moddedResultBlockState = compat.honeyLavaResultBlock(currentFluid);
                    if (moddedResultBlockState != null) {
                        resultBlockState = moddedResultBlockState;
                    }
                }

                level.setBlockAndUpdate(pos, resultBlockState);
                this.triggerMixEffects(level, pos);
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
    public void entityInside(BlockState state, Level world, BlockPos position, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        double verticalSpeedDeltaLimit = 0.01D;
        if (entity instanceof Bee beeEntity && !beeEntity.isDeadOrDying()) {
            if (beeEntity.getHealth() < beeEntity.getMaxHealth() && PlatformService.INSTANCE.isEyesInNoFluid(entity)) {
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

        super.entityInside(state, world, position, entity, effectApplier, isPrecise);
    }

    private void triggerMixEffects(Level world, BlockPos pos) {
        world.levelEvent(1501, pos, 0);
    }
}
