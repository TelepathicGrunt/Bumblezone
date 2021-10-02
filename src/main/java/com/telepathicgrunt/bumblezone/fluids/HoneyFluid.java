package com.telepathicgrunt.bumblezone.fluids;

import com.telepathicgrunt.bumblezone.blocks.HoneyFluidBlock;
import com.telepathicgrunt.bumblezone.mixin.blocks.FlowingFluidAccessor;
import com.telepathicgrunt.bumblezone.modinit.BzFluids;
import com.telepathicgrunt.bumblezone.modinit.BzItems;
import com.telepathicgrunt.bumblezone.modinit.BzParticles;
import com.telepathicgrunt.bumblezone.tags.BzFluidTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Random;

public abstract class HoneyFluid extends FlowableFluid {

    public static final IntProperty BOTTOM_LEVEL = HoneyFluidBlock.BOTTOM_LEVEL;
    public static final BooleanProperty ABOVE_FLUID = HoneyFluidBlock.ABOVE_FLUID;

    protected HoneyFluid() { }

    @Override
    public Fluid getFlowing() {
        return BzFluids.HONEY_FLUID_FLOWING;
    }

    @Override
    public Fluid getStill() {
        return BzFluids.HONEY_FLUID;
    }

    @Override
    public Item getBucketItem() {
        return BzItems.HONEY_BUCKET;
    }

    @Override
    public void randomDisplayTick(World worldIn, BlockPos pos, FluidState state, Random random) {
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
    public ParticleEffect getParticle() {
        return BzParticles.HONEY_PARTICLE;
    }

    @Override
    protected float getBlastResistance() {
        return 120.0F;
    }

    @Override
    protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
        Block.dropStacks(state, world, pos, blockEntity);
    }

    @Override
    public int getFlowSpeed(WorldView world) {
        return 4;
    }

    @Override
    public int getLevelDecreasePerBlock(WorldView world) {
        return 1;
    }

    public int getTickRate(WorldView world) {
        return 30;
    }

    @Override
    public boolean matchesType(Fluid fluid) {
        return fluid.isIn(BzFluidTags.HONEY_FLUID);
    }

    @Override
    public boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluid.isIn(FluidTags.WATER);
    }

    @Override
    public BlockState toBlockState(FluidState state) {
        return BzFluids.HONEY_FLUID_BLOCK.getDefaultState()
                .with(FluidBlock.LEVEL, state.isStill() ? 0 : state.getLevel())
                .with(BOTTOM_LEVEL, state.isStill() ? 0 : state.get(BOTTOM_LEVEL))
                .with(FALLING, !state.isStill() && state.get(FALLING))
                .with(ABOVE_FLUID, state.get(ABOVE_FLUID));
    }

    @Override
    public void onScheduledTick(World world, BlockPos blockPos, FluidState fluidState) {
        boolean justFilledBottom = false;
        // removes self if not source and is not fed.
        // otherwise, schedule fluid tick and update flow.
        if (!fluidState.isStill()) {
            FluidState newFluidState = this.getUpdatedState(world, blockPos, world.getBlockState(blockPos));
            int spreadDelay = this.getNextTickDelay(world, blockPos, fluidState, newFluidState);
            if (newFluidState.isEmpty()) {
                fluidState = newFluidState;
                world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 3);
            }
            else if (!newFluidState.equals(fluidState)) {
                if(fluidState.get(BOTTOM_LEVEL) != 0 && (newFluidState.isStill() || newFluidState.get(BOTTOM_LEVEL) == 0))
                    justFilledBottom = true;

                fluidState = newFluidState;
                BlockState blockstate = newFluidState.getBlockState();
                world.setBlockState(blockPos, blockstate, 2);
                world.getFluidTickScheduler().schedule(blockPos, newFluidState.getFluid(), spreadDelay);
                world.updateNeighborsAlways(blockPos, blockstate.getBlock());
            }
        }

        // For spreading downward and to the side.
        // Is basically the spread method but with justFilledBottom boolean
        // used so new fluid is not made in same tick as when fluid just reached bottom layer = 0.
        if (!fluidState.isEmpty()) {
            int bottomFluidLevel = fluidState.isStill() ? 0 : fluidState.get(BOTTOM_LEVEL);
            if(bottomFluidLevel == 0) {
                BlockState blockState = world.getBlockState(blockPos);
                BlockPos belowBlockPos = blockPos.down();
                BlockState belowBlockState = world.getBlockState(belowBlockPos);
                FluidState belowFluidState = this.getUpdatedState(world, belowBlockPos, belowBlockState);
                if (!belowBlockState.getFluidState().isIn(BzFluidTags.HONEY_FLUID) &&
                    this.canFlow(world, blockPos, blockState, Direction.DOWN, belowBlockPos, belowBlockState, world.getFluidState(belowBlockPos), belowFluidState.getFluid())) {

                    if(!justFilledBottom) {
                        this.spreadDown(world, belowBlockPos, belowBlockState, Direction.DOWN, belowFluidState);
                        if (((FlowingFluidAccessor)this).thebumblezone_callSourceNeighborCount(world, blockPos) >= 3) {
                            ((FlowingFluidAccessor)this).thebumblezone_callSpreadToSides(world, blockPos, fluidState, blockState);
                        }
                    }
                }
                else if (fluidState.isStill() || !belowBlockState.getFluidState().getFluid().matchesType(this)) {
                    ((FlowingFluidAccessor)this).thebumblezone_callSpreadToSides(world, blockPos, fluidState, blockState);
                }
            }
        }
    }

    @Override
    protected void tryFlow(WorldAccess world, BlockPos blockPos, FluidState fluidState) {
        if (!fluidState.isEmpty()) {
            int bottomFluidLevel = fluidState.get(BOTTOM_LEVEL);
            if(bottomFluidLevel == 0) {
                BlockState blockState = world.getBlockState(blockPos);
                BlockPos belowBlockPos = blockPos.down();
                BlockState belowBlockState = world.getBlockState(belowBlockPos);
                FluidState belowFluidState = this.getUpdatedState(world, belowBlockPos, belowBlockState);
                if (!belowBlockState.getFluidState().isIn(BzFluidTags.HONEY_FLUID) && this.canFlow(world, blockPos, blockState, Direction.DOWN, belowBlockPos, belowBlockState, world.getFluidState(belowBlockPos), belowFluidState.getFluid())) {
                    this.spreadDown(world, belowBlockPos, belowBlockState, Direction.DOWN, belowFluidState);
                    if (((FlowingFluidAccessor)this).thebumblezone_callSourceNeighborCount(world, blockPos) >= 3) {
                        ((FlowingFluidAccessor)this).thebumblezone_callSpreadToSides(world, blockPos, fluidState, blockState);
                    }
                }
                else if (fluidState.isStill() || !belowBlockState.getFluidState().getFluid().matchesType(this)) {
                    ((FlowingFluidAccessor)this).thebumblezone_callSpreadToSides(world, blockPos, fluidState, blockState);
                }
            }
        }
    }

    protected void spreadDown(WorldAccess world, BlockPos blockPos, BlockState blockState, Direction direction, FluidState fluidState) {
        if (!blockState.isAir()) {
            this.beforeBreakingBlock(world, blockPos, blockState);
        }
        world.setBlockState(blockPos, fluidState.getBlockState(), 3);
    }

    @Override
    protected FluidState getUpdatedState(WorldView worldReader, BlockPos blockPos, BlockState blockState) {
        int lowestNeighboringFluidLevel = blockState.getBlock() instanceof HoneyFluidBlock ? blockState.get(BOTTOM_LEVEL) : HoneyFluidBlock.maxBottomLayer;
        int currentFluidLevel = blockState.getBlock() instanceof HoneyFluidBlock ? blockState.get(HoneyFluidBlock.LEVEL) : 0;
        int highestNeighboringFluidLevel = currentFluidLevel;
        int neighboringFluidSource = 0;
        boolean hadAboveFluid = blockState.getBlock() instanceof HoneyFluidBlock ? blockState.get(ABOVE_FLUID) : false;

        for(Direction direction : Direction.Type.HORIZONTAL) {
            BlockPos sideBlockPos = blockPos.offset(direction);
            BlockState sideBlockState = worldReader.getBlockState(sideBlockPos);
            FluidState sideFluidState = sideBlockState.getFluidState();
            if (sideFluidState.getFluid().matchesType(this) && ((FlowingFluidAccessor)this).thebumblezone_callCanPassThroughWall(direction, worldReader, blockPos, blockState, sideBlockPos, sideBlockState)) {
                if (sideFluidState.isStill()) {
                    ++neighboringFluidSource;
                }

                highestNeighboringFluidLevel = Math.max(highestNeighboringFluidLevel, sideFluidState.getLevel());
                if(sideFluidState.getFluid() instanceof HoneyFluid){
                    lowestNeighboringFluidLevel = Math.min(lowestNeighboringFluidLevel, sideFluidState.isStill() ? 0 : sideFluidState.get(BOTTOM_LEVEL));
                }
            }
        }

        BlockPos aboveBlockPos = blockPos.up();
        BlockState aboveBlockState = worldReader.getBlockState(aboveBlockPos);
        FluidState aboveFluidState = aboveBlockState.getFluidState();
        boolean aboveFluidIsThisFluid = !aboveFluidState.isEmpty() && aboveFluidState.getFluid().matchesType(this);
        int newBottomFluidLevel = Math.max(lowestNeighboringFluidLevel - 1, 0);
        boolean isFalling = true;
        int newFluidLevel = 8;
        int dropOffValue = this.getLevelDecreasePerBlock(worldReader);
        if(hadAboveFluid && !aboveFluidIsThisFluid) {
            dropOffValue = 0;
        }

        if (aboveFluidIsThisFluid && ((FlowingFluidAccessor)this).thebumblezone_callCanPassThroughWall(Direction.UP, worldReader, blockPos, blockState, aboveBlockPos, aboveBlockState)) {
            if(!aboveFluidState.isStill() && aboveFluidState.get(BOTTOM_LEVEL) != 0) {
                newFluidLevel = highestNeighboringFluidLevel - dropOffValue;
            }
        }
        else {
            BlockState belowBlockState = worldReader.getBlockState(blockPos.down());
            isFalling = aboveFluidState.isEmpty() && neighboringFluidSource == 0 && highestNeighboringFluidLevel <= currentFluidLevel && ((FlowingFluidAccessor)this).thebumblezone_callCanPassThroughWall(Direction.DOWN, worldReader, blockPos, blockState, blockPos.down(), belowBlockState);
            newFluidLevel = highestNeighboringFluidLevel - dropOffValue;
        }

        return newFluidLevel <= 0 ?
                Fluids.EMPTY.getDefaultState() :
                this.getFlowing(newFluidLevel, isFalling)
                        .with(BOTTOM_LEVEL, newBottomFluidLevel)
                        .with(ABOVE_FLUID, aboveFluidIsThisFluid && (aboveFluidState.isStill() || aboveFluidState.get(BOTTOM_LEVEL) == 0));
    }

    @Override
    public float getHeight(FluidState fluidState, BlockView world, BlockPos blockPos) {
        BlockPos aboveBlockPos = blockPos.up();
        BlockState aboveBlockState = world.getBlockState(aboveBlockPos);
        FluidState aboveFluidState = aboveBlockState.getFluidState();
        boolean aboveFluidIsThisFluid =
                    !aboveFluidState.isEmpty() &&
                    aboveFluidState.getFluid().matchesType(this) &&
                    (aboveFluidState.isStill() || aboveFluidState.get(BOTTOM_LEVEL) == 0);

        return fluidState.get(ABOVE_FLUID) || aboveFluidIsThisFluid ? 1.0f : fluidState.getHeight();
    }

    public static float getHoneyFluidHeight(BlockView world, BlockPos blockPos, Fluid fluid) {
        float totalHeight = 0.0F;
        int checkedSides = 0;
        int fluidSides = 0;
        FluidState currentMatchingFluidState = null;

        // Checks in a square. One spot will be the current fluid
        for(int xOffset = -2; xOffset <= 1; xOffset++) {
            for(int zOffset = -2; zOffset <= 1; zOffset++) {
                BlockPos currentBlockPos = blockPos.add(xOffset, 0, zOffset);

                if(xOffset == -2 || zOffset == -2 || xOffset == 1 || zOffset == 1) {
                    if (world.getFluidState(currentBlockPos).getFluid().matchesType(fluid)) {
                        fluidSides++;
                    }
                    continue;
                }

                FluidState aboveFluidState = world.getFluidState(currentBlockPos.up());
                if (aboveFluidState.getFluid().matchesType(fluid) && (aboveFluidState.isStill() || aboveFluidState.get(BOTTOM_LEVEL) == 0)) {
                    return 1.0F;
                }

                FluidState currentFluidState = world.getFluidState(currentBlockPos);
                if (currentFluidState.getFluid().matchesType(fluid)) {
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
            currentMatchingFluidState.getFluid().matchesType(fluid) &&
            !currentMatchingFluidState.isStill() &&
            currentMatchingFluidState.get(FALLING))
        {
            return currentMatchingFluidState.getHeight(world, blockPos);
        }

        return totalHeight / (float)checkedSides;
    }

    public static boolean shouldNotCullSide(BlockView world, BlockPos blockPos, Direction direction, FluidState currentFluidState) {
        if(direction == Direction.UP) {
            FluidState aboveFluidState = world.getBlockState(blockPos.up()).getFluidState();
            return aboveFluidState.isIn(BzFluidTags.BZ_HONEY_FLUID) && !aboveFluidState.isStill() &&
                    (aboveFluidState.get(BOTTOM_LEVEL) != 0 || currentFluidState.getLevel() != 8);
        }
        else if(direction == Direction.DOWN) {
            FluidState belowFluidState = world.getBlockState(blockPos.down()).getFluidState();
            return belowFluidState.isIn(BzFluidTags.BZ_HONEY_FLUID) && !currentFluidState.isStill() &&
                    (belowFluidState.getLevel() != 8 || currentFluidState.get(BOTTOM_LEVEL) != 0);
        }
        else {
            FluidState sideFluidState = world.getBlockState(blockPos.offset(direction)).getFluidState();
            if(sideFluidState.isIn(BzFluidTags.BZ_HONEY_FLUID)) {
                int bottomLayerCurrent = currentFluidState.isStill() ? 0 : currentFluidState.get(BOTTOM_LEVEL);
                int bottomLayerSide = sideFluidState.isStill() ? 0 : sideFluidState.get(BOTTOM_LEVEL);
                return bottomLayerCurrent < bottomLayerSide;
            }
        }

        return false;
    }

    public static void breathing(LivingEntity thisEntity) {
        boolean invulnerable = thisEntity instanceof PlayerEntity && ((PlayerEntity)thisEntity).getAbilities().invulnerable;
        if (thisEntity.isAlive()) {
            if (thisEntity.isSubmergedIn(BzFluidTags.BZ_HONEY_FLUID)) {
                if (!thisEntity.canBreatheInWater() && !StatusEffectUtil.hasWaterBreathing(thisEntity) && !invulnerable) {
                    thisEntity.setAir(
                        decreaseAirSupply(
                            thisEntity.getAir() - 4, // -4 to counteract the +4 for rebreathing as vanilla thinks the honey fluid is air
                            thisEntity,
                            thisEntity.world.random)
                    );
                    if (thisEntity.getAir() == -20) {
                        thisEntity.setAir(0);
                        Vec3d vector3d = thisEntity.getVelocity();

                        for(int i = 0; i < 8; ++i) {
                            double d2 = thisEntity.world.random.nextDouble() - thisEntity.world.random.nextDouble();
                            double d3 = thisEntity.world.random.nextDouble() - thisEntity.world.random.nextDouble();
                            double d4 = thisEntity.world.random.nextDouble() - thisEntity.world.random.nextDouble();
                            thisEntity.world.addParticle(BzParticles.HONEY_PARTICLE, thisEntity.getX() + d2, thisEntity.getY() + d3, thisEntity.getZ() + d4, vector3d.x, vector3d.y, vector3d.z);
                        }

                        thisEntity.damage(DamageSource.DROWN, 2.0F);
                    }
                }

                if (!thisEntity.world.isClient() && thisEntity.hasVehicle() && thisEntity.getVehicle() != null && !thisEntity.getVehicle().canBeRiddenInWater()) {
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
            setDefaultState(getStateManager().getDefaultState()
                    .with(LEVEL, 8)
                    .with(BOTTOM_LEVEL, 0)
                    .with(ABOVE_FLUID, false)
            );
        }

        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(LEVEL);
            builder.add(BOTTOM_LEVEL);
            builder.add(ABOVE_FLUID);
        }

        @Override
        public int getLevel(FluidState state) {
            return state.get(LEVEL);
        }

        @Override
        public boolean isStill(FluidState state) {
            return false;
        }

        @Override
        protected boolean isInfinite() {
            return true;
        }
    }

    public static class Source extends HoneyFluid {

        public Source() {
            setDefaultState(getStateManager().getDefaultState().with(ABOVE_FLUID, false));
        }

        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(ABOVE_FLUID);
        }

        @Override
        public int getLevel(FluidState state) {
            return 8;
        }

        @Override
        public boolean isStill(FluidState state) {
            return true;
        }

        @Override
        protected boolean isInfinite() {
            return false;
        }
    }
}
