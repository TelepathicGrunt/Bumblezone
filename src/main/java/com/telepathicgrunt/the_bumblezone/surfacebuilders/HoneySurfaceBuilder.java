package com.telepathicgrunt.the_bumblezone.surfacebuilders;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modCompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modCompat.ResourcefulBeesRedirection;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

import java.util.Random;


public class HoneySurfaceBuilder extends SurfaceBuilder<SurfaceBuilderConfig> {
    public HoneySurfaceBuilder(Codec<SurfaceBuilderConfig> codec) {
        super(codec);
    }

    //Commented out because these fields were unused - andrew
//    private static final BlockState STONE = Blocks.STONE.getDefaultState();
//    private static final BlockState FILLED_POROUS_HONEYCOMB = BzBlocks.FILLED_POROUS_HONEYCOMB.getDefaultState();
//    private static final BlockState POROUS_HONEYCOMB = BzBlocks.POROUS_HONEYCOMB.getDefaultState();
//    private static final BlockState HONEYCOMB_BLOCK = Blocks.HONEYCOMB_BLOCK.getDefaultState();


    public void buildSurface(Random random, IChunk chunkIn, Biome biomeIn, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, SurfaceBuilderConfig config) {
        //creates grass surface normally
        SurfaceBuilder.DEFAULT.buildSurface(random, chunkIn, biomeIn, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, seed, config);

        int xpos = x & 15;
        int zpos = z & 15;
        BlockPos.Mutable blockpos$Mutable = new BlockPos.Mutable();
        boolean isSurface = false;

        //makes stone below sea level into end stone
        for (int ypos = 255; ypos >= 0; --ypos) {
            blockpos$Mutable.setPos(xpos, ypos, zpos);
            BlockState currentBlockState = chunkIn.getBlockState(blockpos$Mutable);

            if (currentBlockState.getMaterial() != Material.AIR && currentBlockState.getFluidState().isEmpty()) {

                if (ypos <= seaLevel + 2 + Math.max(noise, 0) + random.nextInt(2)) {
                    if (isSurface &&
                        ModChecker.resourcefulBeesPresent &&
                        Bumblezone.BzModCompatibilityConfig.RBBeesWaxWorldgen.get() &&
                        noise + random.nextInt(2) < -1)
                    {
                        chunkIn.setBlockState(blockpos$Mutable, ResourcefulBeesRedirection.getRBBeesWaxBlock(), false);
                    }
                    else if (currentBlockState == config.getTop() || currentBlockState == config.getUnder()) {
                        chunkIn.setBlockState(blockpos$Mutable, config.getUnderWaterMaterial(), false);
                    }
                }

                isSurface = false;
            }
            else{
                isSurface = true;
            }
        }

    }
}