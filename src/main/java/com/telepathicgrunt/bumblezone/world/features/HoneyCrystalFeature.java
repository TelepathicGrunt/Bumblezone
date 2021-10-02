package com.telepathicgrunt.bumblezone.world.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.bumblezone.blocks.HoneyCrystal;
import com.telepathicgrunt.bumblezone.modinit.BzBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

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
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {

        BlockPos.Mutable blockpos$Mutable = new BlockPos.Mutable().set(context.getOrigin());
        BlockState originalBlockstate = context.getWorld().getBlockState(blockpos$Mutable);
        BlockState blockstate;
        ChunkPos currentChunkPos = new ChunkPos(blockpos$Mutable);

        if (originalBlockstate.getBlock() == CAVE_AIR || originalBlockstate.getFluidState().isIn(FluidTags.WATER)) {

            for (Direction face : Direction.values()) {
                blockpos$Mutable.set(context.getOrigin());
                blockstate = context.getWorld().getBlockState(blockpos$Mutable.move(face, 7));

                if (blockstate.getBlock() == AIR) {
                    return false; // too close to the outside. Refuse generation
                }
            }


            BlockState honeyCrystal = BzBlocks.HONEY_CRYSTAL.getDefaultState()
                    .with(HoneyCrystal.WATERLOGGED, originalBlockstate.getFluidState().isIn(FluidTags.WATER));

            //loop through all 6 directions
            blockpos$Mutable.set(context.getOrigin());
            for (Direction facing : Direction.values()) {

                honeyCrystal = honeyCrystal.with(HoneyCrystal.FACING, facing);

                // if the block is solid, place crystal on it
                if (honeyCrystal.canPlaceAt(context.getWorld(), blockpos$Mutable)) {

                    //if the spot is invalid, we get air back
                    BlockState result = HoneyCrystal.postProcessState(honeyCrystal, context.getWorld(), blockpos$Mutable);
                    if (result.getBlock() != AIR) {
                        //avoid placing crystal on block in other chunk as the cave hasn't carved it yet.
                        Direction directionProp = result.get(HoneyCrystal.FACING);
                        blockpos$Mutable.move(directionProp.getOpposite());
                        if (blockpos$Mutable.getX() >> 4 != currentChunkPos.x || blockpos$Mutable.getZ() >> 4 != currentChunkPos.z) {
                            return false; // facing side chunk. cancel spawn
                        }
                        blockpos$Mutable.move(directionProp); // move back
                        context.getWorld().setBlockState(blockpos$Mutable, result, 3);
                        return true; //crystal was placed
                    }
                }
            }
        }

        return false;
    }

}