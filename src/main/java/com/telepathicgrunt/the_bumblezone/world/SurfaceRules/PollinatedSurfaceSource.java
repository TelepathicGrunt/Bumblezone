package com.telepathicgrunt.the_bumblezone.world.surfacerules;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.utils.OpenSimplex2F;
import com.telepathicgrunt.the_bumblezone.utils.WorldSeedHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.SurfaceRules;

public record PollinatedSurfaceSource (BlockState resultState, RandomLayerStateRule rule) implements SurfaceRules.RuleSource {
    public static final Codec<PollinatedSurfaceSource> CODEC = BlockState.CODEC.xmap(PollinatedSurfaceSource::new, PollinatedSurfaceSource::resultState).fieldOf("result_state").codec();

    PollinatedSurfaceSource(BlockState blockState) {
        this(blockState, new RandomLayerStateRule(blockState));
    }

    @Override
    public Codec<? extends SurfaceRules.RuleSource> codec() {
        return CODEC;
    }

    public SurfaceRules.SurfaceRule apply(SurfaceRules.Context context) {
        return this.rule;
    }

    public static class RandomLayerStateRule implements SurfaceRules.SurfaceRule {
        protected BlockState blockState;
        protected long seed;
        private OpenSimplex2F noiseGenerator = null;
        private final boolean haslayer;
        private final float xzScale = 0.035f;
        private final float yScale = 0.015f;

        public RandomLayerStateRule(BlockState blockState) {
            this.blockState = blockState;
            this.haslayer = this.blockState.hasProperty(BlockStateProperties.LAYERS);
            initNoise();
        }

        public void initNoise() {
            if (this.seed != WorldSeedHolder.getSeed() || noiseGenerator == null) {
                noiseGenerator = new OpenSimplex2F(WorldSeedHolder.getSeed());
                this.seed = WorldSeedHolder.getSeed();
            }
        }

        @Override
        public BlockState tryApply(int x, int y, int z) {
            if (haslayer) {
                double noiseVal = noiseGenerator.noise3_Classic(x * xzScale, y * yScale, z * xzScale);
                int layerHeight = Math.max(0, (int) (((noiseVal / 2D) + 0.5D) * 8D));
                layerHeight = Math.min(8, layerHeight + this.blockState.getValue(BlockStateProperties.LAYERS));
                return this.blockState.setValue(BlockStateProperties.LAYERS, layerHeight);
            }
            return this.blockState;
        }

    }
}
