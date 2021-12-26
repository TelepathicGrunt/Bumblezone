package com.telepathicgrunt.the_bumblezone.world.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.blocks.HoneyCrystal;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class HoneyCrystalFeature extends Feature<NoneFeatureConfiguration> {

    public HoneyCrystalFeature(Codec<NoneFeatureConfiguration> configFactory) {
        super(configFactory);
    }

    /**
     * Place crystal block attached to a block if it is buried underground or underwater
     */
    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {

        BlockPos.MutableBlockPos blockpos$Mutable = new BlockPos.MutableBlockPos().set(context.origin());
        BlockState originalBlockstate = context.level().getBlockState(blockpos$Mutable);
        BlockState blockstate;
        ChunkPos currentChunkPos = new ChunkPos(blockpos$Mutable);

        if (originalBlockstate.getBlock() == Blocks.CAVE_AIR || originalBlockstate.getFluidState().is(FluidTags.WATER)) {

            for (Direction face : Direction.values()) {
                blockpos$Mutable.set(context.origin());
                blockstate = context.level().getBlockState(blockpos$Mutable.move(face, 7));

                if (blockstate.getBlock() == Blocks.AIR) {
                    return false; // too close to the outside. Refuse generation
                }
            }


            BlockState honeyCrystal = BzBlocks.HONEY_CRYSTAL.get().defaultBlockState()
                    .setValue(HoneyCrystal.WATERLOGGED, originalBlockstate.getFluidState().is(FluidTags.WATER));

            //loop through all 6 directions
            blockpos$Mutable.set(context.origin());
            for (Direction facing : Direction.values()) {

                honeyCrystal = honeyCrystal.setValue(HoneyCrystal.FACING, facing);

                // if the block is solid, place crystal on it
                if (honeyCrystal.canSurvive(context.level(), blockpos$Mutable)) {

                    //if the spot is invalid, we get air back
                    BlockState result = HoneyCrystal.updateFromNeighbourShapes(honeyCrystal, context.level(), blockpos$Mutable);
                    if (result.getBlock() != Blocks.AIR) {
                        //avoid placing crystal on block in other chunk as the cave hasn't carved it yet.
                        Direction directionProp = result.getValue(HoneyCrystal.FACING);
                        blockpos$Mutable.move(directionProp.getOpposite());
                        if (blockpos$Mutable.getX() >> 4 != currentChunkPos.x || blockpos$Mutable.getZ() >> 4 != currentChunkPos.z) {
                            return false; // facing side chunk. cancel spawn
                        }
                        blockpos$Mutable.move(directionProp); // move back
                        context.level().setBlock(blockpos$Mutable, result, 3);
                        return true; //crystal was placed
                    }
                }
            }
        }

        return false;
    }

}