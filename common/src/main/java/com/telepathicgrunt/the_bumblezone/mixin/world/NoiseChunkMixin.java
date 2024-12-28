package com.telepathicgrunt.the_bumblezone.mixin.world;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.telepathicgrunt.the_bumblezone.worldgen.dimension.BzChunkGenerator;
import com.telepathicgrunt.the_bumblezone.worldgen.dimension.NoiseChunkExtension;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NoiseChunk.class)
public class NoiseChunkMixin implements NoiseChunkExtension {

    @Unique
    private BiomeSource the_bumblezone$biomeSource;

    @Unique
    private Climate.Sampler the_bumblezone$cachedClimateSampler;

    @Override
    @Unique
    public void the_bumblezone$setBiomeSource(BiomeSource resolver) {
        this.the_bumblezone$biomeSource = resolver;
    }

    @Override
    @Unique
    public BiomeSource the_bumblezone$getBiomeSource() {
        return this.the_bumblezone$biomeSource;
    }

    @Override
    @Unique
    public void the_bumblezone$setCachedClimateSampler(Climate.Sampler sampler) {
        this.the_bumblezone$cachedClimateSampler = sampler;
    }

    @Override
    @Unique
    public Climate.Sampler the_bumblezone$getCachedClimateSampler() {
        return this.the_bumblezone$cachedClimateSampler;
    }

    @ModifyReturnValue(method = "wrapNew", at = @At("RETURN"))
    private DensityFunction injectWrapNew(DensityFunction original) {
        if (original instanceof BzChunkGenerator.BiomeNoise) {
            return new BzChunkGenerator.BiomeNoise(() -> this.the_bumblezone$biomeSource, () -> this.the_bumblezone$cachedClimateSampler);
        } else {
            return original;
        }
    }

}
