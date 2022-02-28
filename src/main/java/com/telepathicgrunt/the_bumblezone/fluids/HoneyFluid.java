package com.telepathicgrunt.the_bumblezone.fluids;

import com.telepathicgrunt.the_bumblezone.blocks.HoneyFluidBlock;
import com.telepathicgrunt.the_bumblezone.mixin.blocks.FlowingFluidAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzParticles;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Random;

public abstract class HoneyFluid extends FlowingFluid {

    public static final IntegerProperty BOTTOM_LEVEL = HoneyFluidBlock.BOTTOM_LEVEL;
    public static final BooleanProperty ABOVE_FLUID = HoneyFluidBlock.ABOVE_FLUID;

    protected HoneyFluid() { }

    @Override
    public Fluid getFlowing() {
        return BzFluids.HONEY_FLUID_FLOWING;
    }

    @Override
    public Fluid getSource() {
        return BzFluids.HONEY_FLUID;
    }

    @Override
    public Item getBucket() {
        return BzItems.HONEY_BUCKET;
    }

    @Override
    public void animateTick(Level worldIn, BlockPos pos, FluidState state, Random random) {
        if (random.nextInt(82) == 0) {
            worldIn.addParticle(BzParticles.HONEY_PARTICLE,
                    pos.getX() + random.nextFloat(),
                    pos.getY() + random.nextFloat(),
                    pos.getZ() + random.nextFloat(),
                    0.0D,
                    0.0D,
                    0.0D);
        }
    }

    @Override
    public ParticleOptions getDripParticle() {
        return BzParticles.HONEY_PARTICLE;
    }

    @Override
    protected float getExplosionResistance() {
        return 120.0F;
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor world, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
        Block.dropResources(state, world, pos, blockEntity);
    }

    @Override
    public int getSlopeFindDistance(LevelReader world) {
        return 4;
    }

    @Override
    public int getDropOff(LevelReader world) {
        return 1;
    }

    public int getTickDelay(LevelReader world) {
        return 30;
    }

    @Override
    public boolean isSame(Fluid fluid) {
        return fluid.is(BzTags.VISUAL_HONEY_FLUID);
    }

    @Override
    public boolean canBeReplacedWith(FluidState state, BlockGetter world, BlockPos pos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluid.is(FluidTags.WATER);
    }

    @Override
    public BlockState createLegacyBlock(FluidState state) {
        return BzFluids.HONEY_FLUID_BLOCK.defaultBlockState()
                .setValue(LiquidBlock.LEVEL, state.isSource() ? 0 : state.getAmount())
                .setValue(BOTTOM_LEVEL, state.isSource() ? 0 : state.getValue(BOTTOM_LEVEL))
                .setValue(FALLING, !state.isSource() && state.getValue(FALLING))
                .setValue(ABOVE_FLUID, state.getValue(ABOVE_FLUID));
    }

    @Override
    public void tick(Level world, BlockPos blockPos, FluidState fluidState) {
        boolean justFilledBottom = false;
        // removes self if not source and is not fed.
        // otherwise, schedule fluid tick and update flow.
        if (!fluidState.isSource()) {
            FluidState newFluidState = this.getNewLiquid(world, blockPos, world.getBlockState(blockPos));
            int spreadDelay = this.getSpreadDelay(world, blockPos, fluidState, newFluidState);
            if (newFluidState.isEmpty()) {
                fluidState = newFluidState;
                world.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
            }
            else if (!newFluidState.equals(fluidState)) {
                if(fluidState.getValue(BOTTOM_LEVEL) != 0 && (newFluidState.isSource() || newFluidState.getValue(BOTTOM_LEVEL) == 0))
                    justFilledBottom = true;

                fluidState = newFluidState;
                BlockState blockstate = newFluidState.createLegacyBlock();
                world.setBlock(blockPos, blockstate, 2);
                world.scheduleTick(blockPos, newFluidState.getType(), spreadDelay);
                world.updateNeighborsAt(blockPos, blockstate.getBlock());
            }
        }

        // For spreading downward and to the side.
        // Is basically the spread method but with justFilledBottom boolean
        // used so new fluid is not made in same tick as when fluid just reached bottom layer = 0.
        if (!fluidState.isEmpty()) {
            int bottomFluidLevel = fluidState.isSource() ? 0 : fluidState.getValue(BOTTOM_LEVEL);
            if(bottomFluidLevel == 0) {
                BlockState blockState = world.getBlockState(blockPos);
                BlockPos belowBlockPos = blockPos.below();
                BlockState belowBlockState = world.getBlockState(belowBlockPos);
                FluidState belowFluidState = this.getNewLiquid(world, belowBlockPos, belowBlockState);
                if (!belowBlockState.getFluidState().is(BzTags.HONEY_FLUID) &&
                    this.canSpreadTo(world, blockPos, blockState, Direction.DOWN, belowBlockPos, belowBlockState, world.getFluidState(belowBlockPos), belowFluidState.getType())) {

                    if(!justFilledBottom) {
                        this.spreadDown(world, belowBlockPos, belowBlockState, Direction.DOWN, belowFluidState);
                        if (((FlowingFluidAccessor)this).thebumblezone_callSourceNeighborCount(world, blockPos) >= 3) {
                            ((FlowingFluidAccessor)this).thebumblezone_callSpreadToSides(world, blockPos, fluidState, blockState);
                        }
                    }
                }
                else if (fluidState.isSource() || !belowBlockState.getFluidState().getType().isSame(this)) {
                    ((FlowingFluidAccessor)this).thebumblezone_callSpreadToSides(world, blockPos, fluidState, blockState);
                }
            }
        }
    }

    @Override
    protected void spread(LevelAccessor world, BlockPos blockPos, FluidState fluidState) {
        if (!fluidState.isEmpty()) {
            int bottomFluidLevel = fluidState.getValue(BOTTOM_LEVEL);
            if(bottomFluidLevel == 0) {
                BlockState blockState = world.getBlockState(blockPos);
                BlockPos belowBlockPos = blockPos.below();
                BlockState belowBlockState = world.getBlockState(belowBlockPos);
                FluidState belowFluidState = this.getNewLiquid(world, belowBlockPos, belowBlockState);
                if (!belowBlockState.getFluidState().is(BzTags.HONEY_FLUID) && this.canSpreadTo(world, blockPos, blockState, Direction.DOWN, belowBlockPos, belowBlockState, world.getFluidState(belowBlockPos), belowFluidState.getType())) {
                    this.spreadDown(world, belowBlockPos, belowBlockState, Direction.DOWN, belowFluidState);
                    if (((FlowingFluidAccessor)this).thebumblezone_callSourceNeighborCount(world, blockPos) >= 3) {
                        ((FlowingFluidAccessor)this).thebumblezone_callSpreadToSides(world, blockPos, fluidState, blockState);
                    }
                }
                else if (fluidState.isSource() || !belowBlockState.getFluidState().getType().isSame(this)) {
                    ((FlowingFluidAccessor)this).thebumblezone_callSpreadToSides(world, blockPos, fluidState, blockState);
                }
            }
        }
    }

    protected void spreadDown(LevelAccessor world, BlockPos blockPos, BlockState blockState, Direction direction, FluidState fluidState) {
        if (!blockState.isAir()) {
            this.beforeDestroyingBlock(world, blockPos, blockState);
        }
        world.setBlock(blockPos, fluidState.createLegacyBlock(), 3);
    }

    @Override
    protected FluidState getNewLiquid(LevelReader worldReader, BlockPos blockPos, BlockState blockState) {
        boolean isBzFluidBlock = blockState.getBlock() instanceof HoneyFluidBlock;
        int lowestNeighboringFluidLevel = isBzFluidBlock ? blockState.getValue(BOTTOM_LEVEL) : HoneyFluidBlock.maxBottomLayer;
        int currentFluidLevel = isBzFluidBlock ? blockState.getValue(HoneyFluidBlock.LEVEL) : 0;
        int highestNeighboringFluidLevel = currentFluidLevel;
        int neighboringFluidSource = 0;
        boolean hasAboveFluid = isBzFluidBlock ? blockState.getValue(ABOVE_FLUID) : false;

        BlockPos aboveBlockPos = blockPos.above();
        BlockState aboveBlockState = worldReader.getBlockState(aboveBlockPos);
        BlockState belowBlockState = worldReader.getBlockState(blockPos.below());
        boolean canPassThroughBelow = ((FlowingFluidAccessor)this).thebumblezone_callCanPassThroughWall(Direction.DOWN, worldReader, blockPos, blockState, blockPos.below(), belowBlockState);

        for(Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos sideBlockPos = blockPos.relative(direction);
            BlockState sideBlockState = worldReader.getBlockState(sideBlockPos);
            FluidState sideFluidState = sideBlockState.getFluidState();
            if (sideFluidState.getType().isSame(this) && ((FlowingFluidAccessor)this).thebumblezone_callCanPassThroughWall(direction, worldReader, blockPos, blockState, sideBlockPos, sideBlockState)) {
                if (sideFluidState.isSource()) {
                    ++neighboringFluidSource;
                }

                highestNeighboringFluidLevel = Math.max(highestNeighboringFluidLevel, sideFluidState.getAmount());
                if(sideFluidState.is(BzTags.BZ_HONEY_FLUID) && !(canPassThroughBelow && !sideFluidState.isSource() && sideBlockState.getValue(HoneyFluidBlock.FALLING) && aboveBlockState.getFluidState().is(BzTags.BZ_HONEY_FLUID))) {
                    lowestNeighboringFluidLevel = Math.min(lowestNeighboringFluidLevel, sideFluidState.isSource() ? 0 : sideFluidState.getValue(BOTTOM_LEVEL));
                }
            }
        }

        FluidState aboveFluidState = aboveBlockState.getFluidState();
        boolean aboveFluidIsThisFluid = !aboveFluidState.isEmpty() && aboveFluidState.getType().isSame(this);
        int newBottomFluidLevel = Math.max(lowestNeighboringFluidLevel - 1, 0);
        boolean isFalling = true;
        int newFluidLevel = 8;
        int dropOffValue = this.getDropOff(worldReader);
        if(hasAboveFluid && !aboveFluidIsThisFluid) {
            dropOffValue = 0;
        }

        if (aboveFluidIsThisFluid && ((FlowingFluidAccessor)this).thebumblezone_callCanPassThroughWall(Direction.UP, worldReader, blockPos, blockState, aboveBlockPos, aboveBlockState)) {
            if(!aboveFluidState.isSource() && aboveFluidState.is(BzTags.BZ_HONEY_FLUID) && aboveFluidState.getValue(BOTTOM_LEVEL) != 0) {
                newFluidLevel = highestNeighboringFluidLevel - dropOffValue;
            }
        }
        else {
            isFalling = aboveFluidState.isEmpty() && neighboringFluidSource == 0 && highestNeighboringFluidLevel <= currentFluidLevel && canPassThroughBelow;
            newFluidLevel = highestNeighboringFluidLevel - dropOffValue;
        }

        return newFluidLevel <= 0 ?
                Fluids.EMPTY.defaultFluidState() :
                this.getFlowing(newFluidLevel, isFalling)
                        .setValue(BOTTOM_LEVEL, newBottomFluidLevel)
                        .setValue(ABOVE_FLUID, aboveFluidIsThisFluid && (aboveFluidState.isSource() || aboveFluidState.getValue(BOTTOM_LEVEL) == 0));
    }

    @Override
    public float getHeight(FluidState fluidState, BlockGetter world, BlockPos blockPos) {
        BlockPos aboveBlockPos = blockPos.above();
        BlockState aboveBlockState = world.getBlockState(aboveBlockPos);
        FluidState aboveFluidState = aboveBlockState.getFluidState();
        boolean aboveFluidIsThisFluid =
                    !aboveFluidState.isEmpty() &&
                    aboveFluidState.getType().isSame(this) &&
                    (aboveFluidState.isSource() || !aboveFluidState.is(BzTags.BZ_HONEY_FLUID) || aboveFluidState.getValue(BOTTOM_LEVEL) == 0);

        return fluidState.getValue(ABOVE_FLUID) || aboveFluidIsThisFluid ? 1.0f : fluidState.getOwnHeight();
    }

    public static float getHoneyFluidHeight(BlockGetter world, BlockPos blockPos, Fluid fluid) {
        float totalHeight = 0.0F;
        int checkedSides = 0;
        int fluidSides = 0;
        FluidState currentMatchingFluidState = null;

        // Checks in a square. One spot will be the current fluid
        for(int xOffset = -2; xOffset <= 1; xOffset++) {
            for(int zOffset = -2; zOffset <= 1; zOffset++) {
                BlockPos currentBlockPos = blockPos.offset(xOffset, 0, zOffset);

                if(xOffset == -2 || zOffset == -2 || xOffset == 1 || zOffset == 1) {
                    if (world.getFluidState(currentBlockPos).getType().isSame(fluid)) {
                        fluidSides++;
                    }
                    continue;
                }

                FluidState aboveFluidState = world.getFluidState(currentBlockPos.above());
                if (aboveFluidState.getType().isSame(fluid) && (aboveFluidState.isSource() || !aboveFluidState.is(BzTags.BZ_HONEY_FLUID) || aboveFluidState.getValue(BOTTOM_LEVEL) == 0)) {
                    return 1.0F;
                }

                FluidState currentFluidState = world.getFluidState(currentBlockPos);
                if (currentFluidState.getType().isSame(fluid)) {
                    currentMatchingFluidState = currentFluidState;
                    fluidSides++;
                    float fluidStateHeight = currentFluidState.getHeight(world, currentBlockPos);
                    if (fluidStateHeight >= 0.8F) {
                        totalHeight += fluidStateHeight * 10.0F;
                        checkedSides += 10;
                    } else {
                        totalHeight += fluidStateHeight;
                        checkedSides++;
                    }
                } else if (!world.getBlockState(currentBlockPos).getMaterial().isSolid()) {
                    checkedSides++;
                }
            }
        }

        if(fluidSides == 1 &&
            currentMatchingFluidState.getType().isSame(fluid) &&
            !currentMatchingFluidState.isSource() &&
            currentMatchingFluidState.getValue(FALLING))
        {
            return currentMatchingFluidState.getHeight(world, blockPos);
        }

        return totalHeight / (float)checkedSides;
    }

    // Used in FluidRendererMixin
    public static void setBottomFluidHeight(Args args, BlockPos blockPos, FluidState fluidState) {
        if(fluidState.is(BzTags.BZ_HONEY_FLUID)) {
            double blockY = (blockPos.getY() & 15);
            args.set(2, blockY + (fluidState.isSource() ? 0f : fluidState.getValue(HoneyFluidBlock.BOTTOM_LEVEL) / 8f));
        }
    }

    public static boolean shouldNotCullSide(BlockGetter world, BlockPos blockPos, Direction direction, FluidState currentFluidState) {
        if(direction == Direction.UP) {
            FluidState aboveFluidState = world.getBlockState(blockPos.above()).getFluidState();
            return aboveFluidState.is(BzTags.BZ_HONEY_FLUID) && !aboveFluidState.isSource() &&
                    (aboveFluidState.getValue(BOTTOM_LEVEL) != 0 || currentFluidState.getAmount() != 8);
        }
        else if(direction == Direction.DOWN) {
            FluidState belowFluidState = world.getBlockState(blockPos.below()).getFluidState();
            return belowFluidState.is(BzTags.BZ_HONEY_FLUID) && !currentFluidState.isSource() &&
                    (belowFluidState.getAmount() != 8 || currentFluidState.getValue(BOTTOM_LEVEL) != 0);
        }
        else {
            FluidState sideFluidState = world.getBlockState(blockPos.relative(direction)).getFluidState();
            if(sideFluidState.is(BzTags.BZ_HONEY_FLUID)) {
                int bottomLayerCurrent = currentFluidState.isSource() ? 0 : currentFluidState.getValue(BOTTOM_LEVEL);
                int bottomLayerSide = sideFluidState.isSource() ? 0 : sideFluidState.getValue(BOTTOM_LEVEL);
                return bottomLayerCurrent < bottomLayerSide;
            }
        }

        return false;
    }

    public static void breathing(LivingEntity thisEntity) {
        boolean invulnerable = thisEntity instanceof Player && ((Player)thisEntity).getAbilities().invulnerable;
        if (thisEntity.isAlive()) {
            if (thisEntity.isEyeInFluid(BzTags.BZ_HONEY_FLUID)) {
                if (!thisEntity.canBreatheUnderwater() && !MobEffectUtil.hasWaterBreathing(thisEntity) && !invulnerable) {
                    thisEntity.setAirSupply(
                        decreaseAirSupply(
                            thisEntity.getAirSupply() - 4, // -4 to counteract the +4 for rebreathing as vanilla thinks the honey fluid is air
                            thisEntity,
                            thisEntity.level.random)
                    );
                    if (thisEntity.getAirSupply() == -20) {
                        thisEntity.setAirSupply(0);
                        Vec3 vector3d = thisEntity.getDeltaMovement();

                        for(int i = 0; i < 8; ++i) {
                            double d2 = thisEntity.level.random.nextDouble() - thisEntity.level.random.nextDouble();
                            double d3 = thisEntity.level.random.nextDouble() - thisEntity.level.random.nextDouble();
                            double d4 = thisEntity.level.random.nextDouble() - thisEntity.level.random.nextDouble();
                            thisEntity.level.addParticle(BzParticles.HONEY_PARTICLE, thisEntity.getX() + d2, thisEntity.getY() + d3, thisEntity.getZ() + d4, vector3d.x, vector3d.y, vector3d.z);
                        }

                        thisEntity.hurt(DamageSource.DROWN, 2.0F);
                    }
                }

                if (!thisEntity.level.isClientSide() && thisEntity.isPassenger() && thisEntity.getVehicle() != null && !thisEntity.getVehicle().rideableUnderWater()) {
                    thisEntity.stopRiding();
                }
            }
        }
    }

    protected static int decreaseAirSupply(int airSupply, LivingEntity entity, Random random) {
        int respiration = EnchantmentHelper.getRespiration(entity);
        return respiration > 0 && random.nextInt(respiration + 1) > 0 ? airSupply : airSupply - 1;
    }

    public static class Flowing extends HoneyFluid {
        public Flowing() {
            registerDefaultState(getStateDefinition().any()
                    .setValue(LEVEL, 8)
                    .setValue(BOTTOM_LEVEL, 0)
                    .setValue(ABOVE_FLUID, false)
            );
        }

        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
            builder.add(BOTTOM_LEVEL);
            builder.add(ABOVE_FLUID);
        }

        @Override
        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }

        @Override
        public boolean isSource(FluidState state) {
            return false;
        }

        @Override
        protected boolean canConvertToSource() {
            return true;
        }
    }

    public static class Source extends HoneyFluid {

        public Source() {
            registerDefaultState(getStateDefinition().any().setValue(ABOVE_FLUID, false));
        }

        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(ABOVE_FLUID);
        }

        @Override
        public int getAmount(FluidState state) {
            return 8;
        }

        @Override
        public boolean isSource(FluidState state) {
            return true;
        }

        @Override
        protected boolean canConvertToSource() {
            return false;
        }
    }
}
