package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.world.biome.layer.util.CachingLayerSampler;
import net.minecraft.world.biome.source.BiomeLayerSampler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BiomeLayerSampler.class)
public interface BiomeLayerSamplerAccessor {

    @Accessor("sampler")
    CachingLayerSampler getSampler();
}
