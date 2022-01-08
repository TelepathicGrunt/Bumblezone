package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.PollenPuffEntity;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Random;


public class HoneyWeb extends Block {

    public static final BooleanProperty NORTHSOUTH = BooleanProperty.create("northsouth");
    public static final BooleanProperty EASTWEST = BooleanProperty.create("eastwest");
    public static final BooleanProperty UPDOWN = BooleanProperty.create("updown");
    protected final VoxelShape[] collisionShapeByIndex;
    protected final VoxelShape[] shapeByIndex;
    private final Object2IntMap<BlockState> stateToIndex = new Object2IntOpenHashMap<>();

    public HoneyWeb() {
        super(Properties.of(Material.WEB, MaterialColor.COLOR_ORANGE).noCollission().requiresCorrectToolForDrops().strength(4.0F));
        this.collisionShapeByIndex = this.makeShapes();
        this.shapeByIndex = this.makeShapes();
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTHSOUTH, false)
                .setValue(EASTWEST, false)
                .setValue(UPDOWN, false));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(NORTHSOUTH, EASTWEST, UPDOWN);
    }

    protected VoxelShape[] makeShapes() {
        VoxelShape voxelshape1 = Block.box(7, 0, 0, 9, 16, 16);
        VoxelShape voxelshape2 = Block.box(0, 0, 7, 16, 16, 9);
        VoxelShape voxelshape3 = Block.box(0, 7, 0, 16, 9, 16);

        return new VoxelShape[]{
                Shapes.empty(),
                voxelshape1,
                voxelshape2,
                Shapes.or(voxelshape1, voxelshape2),
                voxelshape3,
                Shapes.or(voxelshape1, voxelshape3),
                Shapes.or(voxelshape2, voxelshape3),
                Shapes.or(Shapes.or(voxelshape1, voxelshape2), voxelshape3)
        };
    }

    protected int getAABBIndex(BlockState blockState) {
        return this.stateToIndex.computeIfAbsent(blockState, (a) -> {
            int bitFlag = 0;
            if (blockState.getValue(NORTHSOUTH)) {
                bitFlag |= 1;
            }

            if (blockState.getValue(EASTWEST)) {
                bitFlag |= (1 << 1);
            }

            if (blockState.getValue(UPDOWN)) {
                bitFlag |= (1 << 2);
            }

            return bitFlag;
        });
    }

    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return this.shapeByIndex[this.getAABBIndex(blockState)];
    }

    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if (!(entity instanceof Bee || entity instanceof BeehemothEntity)) {
            VoxelShape shape = this.shapeByIndex[this.getAABBIndex(blockState)];
            shape = shape.move(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            if (Shapes.joinIsNotEmpty(shape, Shapes.create(entity.getBoundingBox()), BooleanOp.AND)) {
                double speedReduction = 0.15f;

                if (entity instanceof ThrowableItemProjectile) {
                    Vec3 deltaMovement = entity.getDeltaMovement();
                    entity.setDeltaMovement(new Vec3(
                            deltaMovement.x * speedReduction,
                            deltaMovement.y * speedReduction * 2,
                            deltaMovement.z * speedReduction));
                }
                else {
                    entity.makeStuckInBlock(blockState, new Vec3(speedReduction, speedReduction * 2, speedReduction));
                    if (entity instanceof LivingEntity livingEntity) {
                        livingEntity.addEffect(new MobEffectInstance(
                                MobEffects.MOVEMENT_SLOWDOWN,
                                200,
                                1,
                                false,
                                false,
                                true));
                    }
                }
            }
        }
    }

    public BlockState getStateForPlacement(BlockPlaceContext placeContext) {
        Level level = placeContext.getLevel();
        BlockPos blockpos = placeContext.getClickedPos();
        BlockState state = getStateForSpot(level, blockpos);
        if(!state.getValue(NORTHSOUTH) && !state.getValue(EASTWEST) && !state.getValue(UPDOWN)) {
            if(placeContext.getClickedFace().getAxis() != Direction.Axis.Y) {
                state = this.defaultBlockState().setValue(UPDOWN, true);
            }
            else {
                Direction.Axis horizontalFacing = Direction.Axis.Z;
                for(Direction direction : placeContext.getNearestLookingDirections()) {
                    if(direction.getAxis() != Direction.Axis.Y) {
                        horizontalFacing = direction.getAxis();
                        break;
                    }
                }

                if(horizontalFacing == Direction.Axis.X) {
                    state = this.defaultBlockState()
                            .setValue(NORTHSOUTH, true)
                            .setValue(EASTWEST, false);
                }
                else {
                    state = this.defaultBlockState()
                            .setValue(NORTHSOUTH, false)
                            .setValue(EASTWEST, true);
                }
            }
        }

        updateNeighboringStates(level, state, blockpos);
        return state;
    }

    private void updateNeighboringStates(Level level, BlockState centerState, BlockPos blockpos) {
        for (Direction direction : Direction.values()) {
            BlockPos sidePos = blockpos.relative(direction);
            BlockState neighboringBlockstate = level.getBlockState(sidePos);
            if (neighboringBlockstate.getBlock() instanceof HoneyWeb) {
                boolean changedState = false;
                if (direction.getAxis() != Direction.Axis.X && centerState.getValue(NORTHSOUTH) && !neighboringBlockstate.getValue(NORTHSOUTH)) {
                    neighboringBlockstate = neighboringBlockstate.setValue(NORTHSOUTH, true);
                    changedState = true;
                }
                if (direction.getAxis() != Direction.Axis.Z && centerState.getValue(EASTWEST) && !neighboringBlockstate.getValue(EASTWEST)) {
                    neighboringBlockstate = neighboringBlockstate.setValue(EASTWEST, true);
                    changedState = true;
                }
                if (direction.getAxis() != Direction.Axis.Y && centerState.getValue(UPDOWN) && !neighboringBlockstate.getValue(UPDOWN)) {
                    neighboringBlockstate = neighboringBlockstate.setValue(UPDOWN, true);
                    changedState = true;
                }

                if (changedState) {
                    level.setBlock(sidePos, neighboringBlockstate, 3);
                }
            }
        }
    }

    @NotNull
    private BlockState getStateForSpot(BlockGetter blockgetter, BlockPos blockpos) {
        BlockState currentBlockstate = this.defaultBlockState();

        for (Direction direction : Direction.values()) {
            BlockState neighboringBlockstate = blockgetter.getBlockState(blockpos.relative(direction));
            if (neighboringBlockstate.getBlock() instanceof HoneyWeb) {
                if (direction.getAxis() == Direction.Axis.Y) {
                    if (neighboringBlockstate.getValue(NORTHSOUTH)) {
                        currentBlockstate = currentBlockstate.setValue(NORTHSOUTH, true);
                    }
                    if (neighboringBlockstate.getValue(EASTWEST)) {
                        currentBlockstate = currentBlockstate.setValue(EASTWEST, true);
                    }
                }
                else if (direction.getAxis() == Direction.Axis.X) {
                    if (neighboringBlockstate.getValue(UPDOWN)) {
                        currentBlockstate = currentBlockstate.setValue(UPDOWN, true);
                    }
                    if (neighboringBlockstate.getValue(EASTWEST)) {
                        currentBlockstate = currentBlockstate.setValue(EASTWEST, true);
                    }
                }
                else if (direction.getAxis() == Direction.Axis.Z) {
                    if (neighboringBlockstate.getValue(UPDOWN)) {
                        currentBlockstate = currentBlockstate.setValue(UPDOWN, true);
                    }
                    if (neighboringBlockstate.getValue(NORTHSOUTH)) {
                        currentBlockstate = currentBlockstate.setValue(NORTHSOUTH, true);
                    }
                }
            }
        }

        return currentBlockstate;
    }

    /**
     * Called periodically clientside on blocks near the player to show honey particles. 20% of attempting to spawn a
     * particle
     */
    @Override
    public void animateTick(BlockState blockState, Level world, BlockPos position, Random random) {
        //chance of particle in this tick
        for (int i = 0; i == random.nextInt(50); ++i) {
            this.addHoneyParticle(world, position, blockState.getShape(world, position));
        }
    }

    /**
     * intermediary method to apply the blockshape and ranges that the particle can spawn in for the next addHoneyParticle
     * method
     */
    private void addHoneyParticle(Level world, BlockPos blockPos, VoxelShape blockShape) {
        this.addHoneyParticle(
                world,
                blockPos.getX() + blockShape.min(Direction.Axis.X),
                blockPos.getX() + blockShape.max(Direction.Axis.X),
                blockPos.getY() + blockShape.min(Direction.Axis.Y),
                blockPos.getY() + blockShape.max(Direction.Axis.Y),
                blockPos.getZ() + blockShape.min(Direction.Axis.Z),
                blockPos.getZ() + blockShape.max(Direction.Axis.Z));
    }


    /**
     * Adds the actual honey particle into the world within the given range
     */
    private void addHoneyParticle(Level world, double xMin, double xMax, double yMin, double yMax, double zMax, double zMin) {
        world.addParticle(ParticleTypes.DRIPPING_HONEY, Mth.lerp(world.random.nextDouble(), xMin, xMax), Mth.lerp(world.random.nextDouble(), yMin, yMax), Mth.lerp(world.random.nextDouble(), zMin, zMax), 0.0D, 0.0D, 0.0D);
    }
}
