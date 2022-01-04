package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Random;


public class HoneyWeb extends Block {

    public static final BooleanProperty NORTHSOUTH = BooleanProperty.create("northsouth");
    public static final BooleanProperty EASTWEST = BooleanProperty.create("eastwest");

    public HoneyWeb() {
        super(Properties.of(Material.WEB, MaterialColor.COLOR_ORANGE).noCollission().requiresCorrectToolForDrops().strength(4.0F));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTHSOUTH, true)
                .setValue(EASTWEST, true));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(NORTHSOUTH, EASTWEST);
    }

    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if(!(entity instanceof Bee || entity instanceof BeehemothEntity)) {
            entity.makeStuckInBlock(blockState, new Vec3(0.12D, 0.03F, 0.12D));
        }
    }

    public BlockState getStateForPlacement(BlockPlaceContext placeContext) {
        BlockGetter blockgetter = placeContext.getLevel();
        BlockPos blockpos = placeContext.getClickedPos();
        BlockState state = getStateForSpot(blockgetter, blockpos);
        if(state.getValue(NORTHSOUTH) || state.getValue(EASTWEST)) {
            return state;
        }
        else {
            Direction.Axis horizontalFacing = Direction.Axis.Z;
            for(Direction direction : placeContext.getNearestLookingDirections()) {
                if(direction.getAxis() != Direction.Axis.Y) {
                    horizontalFacing = direction.getAxis();
                    break;
                }
            }

            if(horizontalFacing == Direction.Axis.Z) {
                return this.defaultBlockState()
                        .setValue(NORTHSOUTH, true)
                        .setValue(EASTWEST, false);
            }
            else {
                return this.defaultBlockState()
                        .setValue(NORTHSOUTH, false)
                        .setValue(EASTWEST, true);
            }
        }
    }


    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        BlockState state = getStateForSpot(levelAccessor, blockPos);
        if(state.getValue(NORTHSOUTH) || state.getValue(EASTWEST)) {
            return state;
        }
        return blockState;
    }

    @NotNull
    private BlockState getStateForSpot(BlockGetter blockgetter, BlockPos blockpos) {
        BlockState blockstateN = blockgetter.getBlockState(blockpos.north());
        BlockState blockstateE = blockgetter.getBlockState(blockpos.east());
        BlockState blockstateS = blockgetter.getBlockState(blockpos.south());
        BlockState blockstateW = blockgetter.getBlockState(blockpos.west());

        boolean connectNS = blockstateN.getBlock() instanceof HoneyWeb || blockstateS.getBlock() instanceof HoneyWeb;
        boolean connectEW = blockstateE.getBlock() instanceof HoneyWeb || blockstateW.getBlock() instanceof HoneyWeb;

        return this.defaultBlockState()
                .setValue(NORTHSOUTH, connectNS)
                .setValue(EASTWEST, connectEW);
    }

    /**
     * Called periodically clientside on blocks near the player to show honey particles. 20% of attempting to spawn a
     * particle
     */
    @Override
    public void animateTick(BlockState blockState, Level world, BlockPos position, Random random) {
        //number of particles in this tick
        for (int i = 0; i < random.nextInt(5); ++i) {
            this.addHoneyParticle(world, position, blockState.getCollisionShape(world, position), random.nextFloat());
        }
    }

    /**
     * intermediary method to apply the blockshape and ranges that the particle can spawn in for the next addHoneyParticle
     * method
     */
    private void addHoneyParticle(Level world, BlockPos blockPos, VoxelShape blockShape, double height) {
        this.addHoneyParticle(
                world,
                blockPos.getX() + blockShape.min(Direction.Axis.X),
                blockPos.getX() + blockShape.max(Direction.Axis.X),
                blockPos.getZ() + blockShape.min(Direction.Axis.Z),
                blockPos.getZ() + blockShape.max(Direction.Axis.Z),
                height);
    }


    /**
     * Adds the actual honey particle into the world within the given range
     */
    private void addHoneyParticle(Level world, double xMin, double xMax, double zMax, double zMin, double yHeight) {
        world.addParticle(ParticleTypes.DRIPPING_HONEY, Mth.lerp(world.random.nextDouble(), xMin, xMax), yHeight, Mth.lerp(world.random.nextDouble(), zMax, zMin), 0.0D, 0.0D, 0.0D);
    }
}
