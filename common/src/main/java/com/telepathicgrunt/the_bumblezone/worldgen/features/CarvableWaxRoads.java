package com.telepathicgrunt.the_bumblezone.worldgen.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.blocks.CarvableWax;
import com.telepathicgrunt.the_bumblezone.fluids.base.BzFluid;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import com.telepathicgrunt.the_bumblezone.utils.OpenSimplex2F;
import com.telepathicgrunt.the_bumblezone.utils.UnsafeBulkSectionAccess;
import com.telepathicgrunt.the_bumblezone.worldgen.features.configs.BiomeBasedConfig;
import com.telepathicgrunt.the_bumblezone.worldgen.features.configs.BiomeBasedLayerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;

import java.util.List;


public class CarvableWaxRoads extends Feature<BiomeBasedConfig> {
    protected long seed;
    protected static OpenSimplex2F noiseGen;

    public void setSeed(long seed) {
        if (this.seed != seed || noiseGen == null) {
            noiseGen = new OpenSimplex2F(seed);
            this.seed = seed;
        }
    }

    public CarvableWaxRoads(Codec<BiomeBasedConfig> configFactory) {
        super(configFactory);
    }

    @Override
    public boolean place(FeaturePlaceContext<BiomeBasedConfig> context) {
        WorldGenLevel level = context.level();
        setSeed(level.getSeed());
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos().set(context.origin());
        Holder<Biome> targetBiome = context.config().biome;

        int orgX = context.origin().getX();
        int orgZ = context.origin().getZ();

        UnsafeBulkSectionAccess bulkSectionAccess = new UnsafeBulkSectionAccess(context.level());
        fillChunkWithRoads(context, mutable, orgX, orgZ, bulkSectionAccess, targetBiome);

        return true;
    }

    private void fillChunkWithRoads(FeaturePlaceContext<BiomeBasedConfig> context, BlockPos.MutableBlockPos mutable, int orgX, int orgZ, UnsafeBulkSectionAccess bulkSectionAccess, Holder<Biome> targetBiome) {
        BlockState currentBlockState;
        BlockState previousBlockState;
        int maxY = 70;
        for (int xOffset = 0; xOffset <= 15; xOffset++) {
            for (int zOffset = 0; zOffset <= 15; zOffset++) {
                mutable.set(orgX + xOffset, maxY, orgZ + zOffset);
                if (!bulkSectionAccess.getBiome(mutable, context.level()).is(targetBiome)) {
                    continue;
                }

                previousBlockState = null;
                while (mutable.getY() >= 20) {
                    currentBlockState = bulkSectionAccess.getBlockState(mutable);

                    if (!currentBlockState.is(BlockTags.DIRT) ||
                        previousBlockState == null ||
                        !previousBlockState.getCollisionShape(context.level(), mutable).isEmpty())
                    {
                        previousBlockState = currentBlockState;
                        mutable.move(Direction.DOWN);
                        continue;
                    }

                    double noise1 = noiseGen.noise3_Classic(
                            mutable.getX() * 0.02225D,
                            mutable.getZ() * 0.02225D,
                            0);

                    double thresholdBorder = 0.0125f;
                    double thresholdInside = 0.0025f;
                    double distanceFromThreshold = noise1 - 0.12f;
                    double finalNoise = noise1 * noise1;

                    if (distanceFromThreshold > 0) {
                        zOffset = zSkipping(zOffset, distanceFromThreshold);
                        break;
                    }

                    if (finalNoise < thresholdInside) {
                        bulkSectionAccess.setBlockState(
                                mutable,
                                BzBlocks.CARVABLE_WAX.get().defaultBlockState().setValue(CarvableWax.CARVING, CarvableWax.Carving.BRICKS),
                                false);
                        break;
                    }
                    else if (finalNoise < thresholdBorder) {
                        bulkSectionAccess.setBlockState(
                                mutable,
                                BzBlocks.CARVABLE_WAX.get().defaultBlockState().setValue(CarvableWax.CARVING, CarvableWax.Carving.FLOWER),
                                false);
                        break;
                    }

                    if (!previousBlockState.getFluidState().isEmpty()) {
                        double noise2 = noiseGen.noise3_Classic(
                                mutable.getX() * -0.0011D,
                                mutable.getZ() * -0.0011D,
                                0);

                        double thresholdRootedDirt = 0.2f;

                        if (noise2 < thresholdRootedDirt) {
                            bulkSectionAccess.setBlockState(
                                    mutable,
                                    Blocks.ROOTED_DIRT.defaultBlockState(),
                                    false);
                        }
                        break;
                    }

                    break;
                }
            }
        }
    }

    /// Noise generators giving a value very far from our threshold means there a large area where the noise value will remain too far.
    /// This attempts to skip those area in hopes we land into a spot that is much closer to our threshold where we can then be checking every block.
    /// Noise generators can be expensive to run so this is a neat small optimization. Values were chosen based on visual testing.
    private int zSkipping(int z, double noiseDistanceFromThreshold) {
        if (noiseDistanceFromThreshold >= 0.8) {
            z += 5;
        }
        else if (noiseDistanceFromThreshold >= 0.7) {
            z += 4;
        }
        else if (noiseDistanceFromThreshold >= 0.6) {
            z += 3;
        }
        else if (noiseDistanceFromThreshold >= 0.5) {
            z += 2;
        }
        else if (noiseDistanceFromThreshold >= 0.4) {
            z += 1;
        }
        return z;
    }
}