package com.telepathicgrunt.the_bumblezone.features;

import com.mojang.serialization.Codec;
import cy.jdkdigital.productivebees.common.tileentity.CombBlockTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;

import java.util.BitSet;
import java.util.Random;

public class BzBEOreFeature extends Feature<BzBEOreFeatureConfig> {
    public BzBEOreFeature(Codec<BzBEOreFeatureConfig> codec) {
        super(codec);
    }

    public boolean generate(ISeedReader iSeedReader, ChunkGenerator chunkGenerator, Random random, BlockPos pos, BzBEOreFeatureConfig bzBEOreFeatureConfig) {
        if(bzBEOreFeatureConfig.type == null) return false;

        float f = random.nextFloat() * (float) Math.PI;
        float f1 = (float) bzBEOreFeatureConfig.size / 8.0F;
        int i = MathHelper.ceil(((float) bzBEOreFeatureConfig.size / 16.0F * 2.0F + 1.0F) / 2.0F);
        double d0 = (double) pos.getX() + Math.sin(f) * (double) f1;
        double d1 = (double) pos.getX() - Math.sin(f) * (double) f1;
        double d2 = (double) pos.getZ() + Math.cos(f) * (double) f1;
        double d3 = (double) pos.getZ() - Math.cos(f) * (double) f1;
        double d4 = pos.getY() + random.nextInt(3) - 2;
        double d5 = pos.getY() + random.nextInt(3) - 2;
        int k = pos.getX() - MathHelper.ceil(f1) - i;
        int l = pos.getY() - 2 - i;
        int i1 = pos.getZ() - MathHelper.ceil(f1) - i;
        int j1 = 2 * (MathHelper.ceil(f1) + i);
        int k1 = 2 * (2 + i);

        for (int l1 = k; l1 <= k + j1; ++l1) {
            for (int i2 = i1; i2 <= i1 + j1; ++i2) {
                if (l <= iSeedReader.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, l1, i2)) {
                    return this.func_207803_a(iSeedReader, random, bzBEOreFeatureConfig, d0, d1, d2, d3, d4, d5, k, l, i1, j1, k1);
                }
            }
        }

        return false;
    }

    protected boolean func_207803_a(IWorld world, Random random, BzBEOreFeatureConfig bzBEOreFeatureConfig, double v, double v1, double v2, double v3, double v4, double v5, int i4, int i5, int i6, int i7, int i8) {
        int i = 0;
        BitSet bitset = new BitSet(i7 * i8 * i7);
        BlockPos.Mutable blockPos = new BlockPos.Mutable();
        int j = bzBEOreFeatureConfig.size;
        double[] adouble = new double[j * 4];

        for (int k = 0; k < j; ++k) {
            float f = (float) k / (float) j;
            double d0 = MathHelper.lerp(f, v, v1);
            double d2 = MathHelper.lerp(f, v4, v5);
            double d4 = MathHelper.lerp(f, v2, v3);
            double d6 = random.nextDouble() * (double) j / 16.0D;
            double d7 = ((double) (MathHelper.sin((float) Math.PI * f) + 1.0F) * d6 + 1.0D) / 2.0D;
            adouble[k * 4] = d0;
            adouble[k * 4 + 1] = d2;
            adouble[k * 4 + 2] = d4;
            adouble[k * 4 + 3] = d7;
        }

        for (int i3 = 0; i3 < j - 1; ++i3) {
            if (!(adouble[i3 * 4 + 3] <= 0.0D)) {
                for (int k3 = i3 + 1; k3 < j; ++k3) {
                    if (!(adouble[k3 * 4 + 3] <= 0.0D)) {
                        double d12 = adouble[i3 * 4] - adouble[k3 * 4];
                        double d13 = adouble[i3 * 4 + 1] - adouble[k3 * 4 + 1];
                        double d14 = adouble[i3 * 4 + 2] - adouble[k3 * 4 + 2];
                        double d15 = adouble[i3 * 4 + 3] - adouble[k3 * 4 + 3];
                        if (d15 * d15 > d12 * d12 + d13 * d13 + d14 * d14) {
                            if (d15 > 0.0D) {
                                adouble[k3 * 4 + 3] = -1.0D;
                            }
                            else {
                                adouble[i3 * 4 + 3] = -1.0D;
                            }
                        }
                    }
                }
            }
        }

        for (int j3 = 0; j3 < j; ++j3) {
            double d11 = adouble[j3 * 4 + 3];
            if (!(d11 < 0.0D)) {
                double d1 = adouble[j3 * 4];
                double d3 = adouble[j3 * 4 + 1];
                double d5 = adouble[j3 * 4 + 2];
                int l = Math.max(MathHelper.floor(d1 - d11), i4);
                int l3 = Math.max(MathHelper.floor(d3 - d11), i5);
                int i1 = Math.max(MathHelper.floor(d5 - d11), i6);
                int j1 = Math.max(MathHelper.floor(d1 + d11), l);
                int k1 = Math.max(MathHelper.floor(d3 + d11), l3);
                int l1 = Math.max(MathHelper.floor(d5 + d11), i1);

                for (int i2 = l; i2 <= j1; ++i2) {
                    double d8 = ((double) i2 + 0.5D - d1) / d11;
                    if (d8 * d8 < 1.0D) {
                        for (int j2 = l3; j2 <= k1; ++j2) {
                            double d9 = ((double) j2 + 0.5D - d3) / d11;
                            if (d8 * d8 + d9 * d9 < 1.0D) {
                                for (int k2 = i1; k2 <= l1; ++k2) {
                                    double d10 = ((double) k2 + 0.5D - d5) / d11;
                                    if (d8 * d8 + d9 * d9 + d10 * d10 < 1.0D) {
                                        int l2 = i2 - i4 + (j2 - i5) * i7 + (k2 - i6) * i7 * i8;
                                        if (!bitset.get(l2)) {
                                            bitset.set(l2);
                                            blockPos.setPos(i2, j2, k2);
                                            if (blockPos.getY() < world.getDimensionHeight() && blockPos.getY() > 0 && bzBEOreFeatureConfig.target.test(world.getBlockState(blockPos), random)) {

                                                world.setBlockState(blockPos, bzBEOreFeatureConfig.state, 2);

                                                TileEntity tileentity = world.getTileEntity(blockPos);
                                                if(tileentity instanceof CombBlockTileEntity){
                                                    ((CombBlockTileEntity) tileentity).setType(bzBEOreFeatureConfig.type);
                                                    tileentity.markDirty();
                                                }

                                                ++i;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return i > 0;
    }
}
