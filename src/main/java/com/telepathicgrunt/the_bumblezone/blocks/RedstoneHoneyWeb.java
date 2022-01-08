package com.telepathicgrunt.the_bumblezone.blocks;

import com.mojang.math.Vector3f;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;


public class RedstoneHoneyWeb extends HoneyWeb {

    public static final IntegerProperty POWER = BlockStateProperties.POWER;
    private static final Vec3[] COLORS = Util.make(new Vec3[16], (vec3s) -> {
        for(int powerLevel = 0; powerLevel <= 15; ++powerLevel) {
            float brightness = (float)powerLevel / 15.0F;
            float red = brightness * 0.6F + (brightness > 0.0F ? 0.4F : 0.3F);
            float green = Mth.clamp(brightness * brightness * 0.7F - 0.5F, 0.0F, 1.0F);
            float blue = Mth.clamp(brightness * brightness * 0.6F - 0.7F, 0.0F, 1.0F);
            vec3s[powerLevel] = new Vec3(red, green, blue);
        }
    });

    public RedstoneHoneyWeb() {
        super();
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTHSOUTH, false)
                .setValue(EASTWEST, false)
                .setValue(UPDOWN, false)
                .setValue(POWER, 0));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(NORTHSOUTH, EASTWEST, UPDOWN, POWER);
    }

    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        super.entityInside(blockState,level, blockPos, entity);
        //trigger power change
    }

    public BlockState getStateForPlacement(BlockPlaceContext placeContext) {
        BlockState state = super.getStateForPlacement(placeContext);
        //change power
        return state;
    }

    /**
     * Called periodically clientside on blocks near the player to show honey particles. 20% of attempting to spawn a
     * particle
     */
    @Override
    public void animateTick(BlockState blockState, Level world, BlockPos position, Random random) {
        super.animateTick(blockState, world, position, random);
        //number of redstone particles in this tick
        for (int i = 0; i < random.nextInt(5); ++i) {
            this.addRedstoneParticle(world, position, blockState.getCollisionShape(world, position), blockState.getValue(POWER), random.nextFloat());
        }
    }

    /**
     * intermediary method to apply the blockshape and ranges that the particle can spawn in for the next addHoneyParticle
     * method
     */
    private void addRedstoneParticle(Level world, BlockPos blockPos, VoxelShape blockShape, int power, double height) {
        this.addRedstoneParticle(
                world,
                power,
                blockPos.getX() + blockShape.min(Direction.Axis.X),
                blockPos.getX() + blockShape.max(Direction.Axis.X),
                blockPos.getZ() + blockShape.min(Direction.Axis.Z),
                blockPos.getZ() + blockShape.max(Direction.Axis.Z),
                height);
    }


    /**
     * Adds the actual honey particle into the world within the given range
     */
    private void addRedstoneParticle(Level world, int power, double xMin, double xMax, double zMax, double zMin, double yHeight) {
        world.addParticle(new DustParticleOptions(new Vector3f(COLORS[power]), 1.0F), Mth.lerp(world.random.nextDouble(), xMin, xMax), yHeight, Mth.lerp(world.random.nextDouble(), zMax, zMin), 0.0D, 0.0D, 0.0D);
    }
}
