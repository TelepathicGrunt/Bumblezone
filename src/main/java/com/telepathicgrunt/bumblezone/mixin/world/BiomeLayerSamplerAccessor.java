package com.telepathicgrunt.bumblezone.mixin.world;

import net.minecraft.world.biome.layer.util.CachingLayerSampler;
import net.minecraft.world.biome.source.BiomeLayerSampler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BiomeLayerSampler.class)
public interface BiomeLayerSamplerAccessor {

    @Accessor("sampler")
    CachingLayerSampler thebumblezone_getSampler();
}
