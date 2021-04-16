package net.telepathicgrunt.bumblezone.world.features;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.telepathicgrunt.bumblezone.blocks.HoneyCrystal;
import net.telepathicgrunt.bumblezone.modinit.BzBlocks;

import java.util.Random;

public class HoneyCrystalFeature extends Feature<DefaultFeatureConfig> {

    public HoneyCrystalFeature(Codec<DefaultFeatureConfig> configFactory) {
        super(configFactory);
    }

    private static final Block CAVE_AIR = Blocks.CAVE_AIR;
    private static final Block AIR = Blocks.AIR;

    /**
     * Place crystal block attached to a block if it is buried underground or underwater
     */
    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator generator, Random random, BlockPos position, DefaultFeatureConfig config) {

        BlockPos.Mutable blockpos$Mutable = new BlockPos.Mutable().set(position);
        BlockState originalBlockstate = world.getBlockState(blockpos$Mutable);
        BlockState blockstate;

        if (originalBlockstate.getBlock() == CAVE_AIR || originalBlockstate.getFluidState().isIn(FluidTags.WATER)) {

            for (Direction face : Direction.values()) {
                blockpos$Mutable.set(position);
                blockstate = world.getBlockState(blockpos$Mutable.move(face, 7));

                if (blockstate.getBlock() == AIR) {
                    return false; // too close to the outside. Refuse generation
                }
            }


            BlockState honeyCrystal = BzBlocks.HONEY_CRYSTAL.getDefaultState()
                    .with(HoneyCrystal.WATERLOGGED, originalBlockstate.getFluidState().isIn(FluidTags.WATER));

            //loop through all 6 directions
            blockpos$Mutable.set(position);
            for (Direction facing : Direction.values()) {

                honeyCrystal = honeyCrystal.with(HoneyCrystal.FACING, facing);

                // if the block is solid, place crystal on it
                if (honeyCrystal.canPlaceAt(world, blockpos$Mutable)) {

                    //if the spot is invalid, we get air back
                    BlockState result = HoneyCrystal.postProcessState(honeyCrystal, world, blockpos$Mutable);
                    if (result.getBlock() != AIR) {
                        //avoid placing crystal on block in other chunk as the cave hasn't carved it yet.
                        Direction directionProp = result.get(HoneyCrystal.FACING);
                        if( (directionProp == Direction.NORTH && blockpos$Mutable.getZ() % 16 == 15) ||
                                (directionProp == Direction.SOUTH && blockpos$Mutable.getZ() % 16 == 0) ||
                                (directionProp == Direction.WEST && blockpos$Mutable.getX() % 16 == 15) ||
                                (directionProp == Direction.EAST && blockpos$Mutable.getX() % 16 == 0))
                        {
                            return false;
                        }

                        world.setBlockState(blockpos$Mutable, result, 3);
                        return true; //crystal was placed
                    }
                }
            }
        }

        return false;
    }

}