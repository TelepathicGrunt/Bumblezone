package com.telepathicgrunt.the_bumblezone.world.dimension;

import com.telepathicgrunt.the_bumblezone.mixin.world.BiomeManagerAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;

public class NoVerticalBlendBiomeManager extends BiomeManager{
    private final BiomeManager biomeManager;

    public NoVerticalBlendBiomeManager(BiomeManager biomeManager) {
        super(((BiomeManagerAccessor)biomeManager).getNoiseBiomeSource(), ((BiomeManagerAccessor)biomeManager).getBiomeZoomSeed());
        this.biomeManager = biomeManager;
    }

    @Override
    public Holder<Biome> getBiome(BlockPos arg) {
        int xMinus2 = arg.getX() - 2;
        int zMinus2 = arg.getZ() - 2;
        int xShifted = xMinus2 >> 2;
        int zShifted = zMinus2 >> 2;
        double xMagic = (double)(xMinus2 & 3) / 4.0;
        double zMagic = (double)(zMinus2 & 3) / 4.0;
        int lastIteration = 0;
        double currentDistance = Double.POSITIVE_INFINITY;

        for(int iteration = 0; iteration < 8; ++iteration) {

            boolean flag4 = (iteration & 4) == 0;
            boolean flag1 = (iteration & 1) == 0;
            int xShiftedFlagged = flag4 ? xShifted : xShifted + 1;
            int zShiftedFlagged = flag1 ? zShifted : zShifted + 1;
            double xMagicFlagged = flag4 ? xMagic : xMagic - 1.0;
            double zMagicFlagged = flag1 ? zMagic : zMagic - 1.0;

            double fiddledDistance = BiomeManagerAccessor.callGetFiddledDistance(
                    ((BiomeManagerAccessor)biomeManager).getBiomeZoomSeed(),
                    xShiftedFlagged,
                    0,
                    zShiftedFlagged,
                    xMagicFlagged,
                    0,
                    zMagicFlagged);

            if (currentDistance > fiddledDistance) {
                lastIteration = iteration;
                currentDistance = fiddledDistance;
            }
        }

        int finalX = (lastIteration & 4) == 0 ? xShifted : xShifted + 1;
        int finalZ = (lastIteration & 1) == 0 ? zShifted : zShifted + 1;
        return ((BiomeManagerAccessor)biomeManager).getNoiseBiomeSource().getNoiseBiome(finalX, 0, finalZ);
    }
}
