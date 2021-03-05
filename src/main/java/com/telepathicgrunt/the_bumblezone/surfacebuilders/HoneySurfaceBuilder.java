package com.telepathicgrunt.the_bumblezone.surfacebuilders;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modCompat.BuzzierBeesRedirection;
import com.telepathicgrunt.the_bumblezone.modCompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modCompat.ResourcefulBeesRedirection;
import net.minecraft.block.BlockState;
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
        //creates the default surface normally
        SurfaceBuilder.DEFAULT.buildSurface(random, chunkIn, biomeIn, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, seed, config);

        int xpos = x & 15;
        int zpos = z & 15;
        BlockPos.Mutable blockpos$Mutable = new BlockPos.Mutable();
        BlockState prevBlockState = defaultBlock;
        int depth = 0;

        // Adds underwater surface blocks and modded surface blocks dynamically that default surface builder cant do.
        for (int ypos = startHeight; ypos >= 0; --ypos) {
            blockpos$Mutable.setPos(xpos, ypos, zpos);
            BlockState currentBlockState = chunkIn.getBlockState(blockpos$Mutable);

            if (currentBlockState.getMaterial() != Material.AIR && currentBlockState.getFluidState().isEmpty()) {

                if (ypos <= seaLevel + 2 + Math.max(noise, 0) + random.nextInt(2)) {
                    if (depth == 0 &&
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
                else if(ModChecker.buzzierBeesPresent && Bumblezone.BzModCompatibilityConfig.crystallizedHoneyWorldgen.get() &&
                        !prevBlockState.isAir() && prevBlockState.getFluidState().isEmpty())
                {
                    if(depth > 0 && depth < 1 + Math.abs(noise - 1) + random.nextInt(2) &&
                        blockpos$Mutable.getY() > (seaLevel + 24) + (noise * 1.5d) + random.nextInt(5))
                    {
                        chunkIn.setBlockState(blockpos$Mutable, BuzzierBeesRedirection.getCrystallizedHoneyBlock(), false);
                    }
                    else if(depth < 7 && blockpos$Mutable.getY() > (seaLevel + 19)){
                        chunkIn.setBlockState(blockpos$Mutable, defaultBlock, false);
                    }
                }

                depth++;
            }
            else{
                depth = 0;
            }

            prevBlockState = currentBlockState;
        }

    }
}