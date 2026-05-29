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
    protected static OpenSimplex2F noiseGen2;

    public void setSeed(long seed) {
        if (this.seed != seed || noiseGen == null) {
            noiseGen = new OpenSimplex2F(seed);
            noiseGen2 = new OpenSimplex2F(seed + 1000);
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
        BlockState currentBlockState;
        BlockState previousBlockState = Blocks.AIR.defaultBlockState();
        Holder<Biome> targetBiome = context.config().biome;

        int orgX = context.origin().getX();
        int orgZ = context.origin().getZ();

        UnsafeBulkSectionAccess bulkSectionAccess = new UnsafeBulkSectionAccess(context.level());
        int maxY = 80;
        for (int xOffset = 0; xOffset <= 15; xOffset++) {
            for (int zOffset = 0; zOffset <= 15; zOffset++) {
                mutable.set(orgX + xOffset, maxY, orgZ + zOffset);
                if (!bulkSectionAccess.getBiome(mutable, context.level()).is(targetBiome)) {
                    continue;
                }

                while (mutable.getY() >= 20) {
                    currentBlockState = bulkSectionAccess.getBlockState(mutable);

                    if (!currentBlockState.is(BlockTags.DIRT) ||
                        !previousBlockState.getCollisionShape(context.level(), mutable).isEmpty())
                    {
                        previousBlockState = currentBlockState;
                        mutable.move(Direction.DOWN);
                        continue;
                    }

                    double noise1 = noiseGen.noise3_Classic(
                            mutable.getX() * 0.0225D,
                            mutable.getZ() * 0.0225D,
                            0);

                    if (Math.abs(noise1) >= 0.12D) {
                        zOffset = zSkipping(zOffset, Math.abs(noise1));
                        break;
                    }

                    double finalNoise = noise1 * noise1;

                    if (finalNoise < 0.0025f) {
                        bulkSectionAccess.setBlockState(
                                mutable,
                                BzBlocks.CARVABLE_WAX.get().defaultBlockState().setValue(CarvableWax.CARVING, CarvableWax.Carving.BRICKS),
                                false);
                    }
                    else if (finalNoise < 0.0125f) {
                        bulkSectionAccess.setBlockState(
                                mutable,
                                BzBlocks.CARVABLE_WAX.get().defaultBlockState().setValue(CarvableWax.CARVING, CarvableWax.Carving.FLOWER),
                                false);
                    }

                    break;
                }
            }
        }
        return true;
    }

    /// Noise generators giving a value very far from our threshold means there a large area where the noise value will remain too far.
    /// This attempts to skip those area in hopes we land into a spot that is much closer to our threshold where we can then be checking every block.
    /// Noise generators can be expensive to run so this is a neat small optimization. Values were chosen based on visual testing.
    private int zSkipping(int z, double noise1) {
        if (noise1 >= 0.9) {
            z += 6;
        }
        else if (noise1 >= 0.8) {
            z += 5;
        }
        else if (noise1 >= 0.7) {
            z += 4;
        }
        else if (noise1 >= 0.6) {
            z += 3;
        }
        else if (noise1 >= 0.5) {
            z += 2;
        }
        else if (noise1 >= 0.4) {
            z += 1;
        }
        return z;
    }
}