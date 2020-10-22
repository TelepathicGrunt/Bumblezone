package com.telepathicgrunt.the_bumblezone.generation;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.generation.layer.BzBiomeLayer;
import com.telepathicgrunt.the_bumblezone.generation.layer.BzBiomePillarLayer;
import com.telepathicgrunt.the_bumblezone.generation.layer.BzBiomeScalePillarLayer;
import com.telepathicgrunt.the_bumblezone.mixin.LayerAccessor;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeRegistry;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.gen.layer.ZoomLayer;
import net.minecraft.world.gen.layer.traits.IAreaTransformer1;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.LongFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class BzBiomeProvider extends BiomeProvider {
    public static void registerBiomeProvider() {
        Registry.register(Registry.BIOME_SOURCE, new ResourceLocation(Bumblezone.MODID, "biome_source"), BzBiomeProvider.CODEC);
    }

    public static final Codec<BzBiomeProvider> CODEC =
            RecordCodecBuilder.create((instance) -> instance.group(
                    RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter((vanillaLayeredBiomeSource) -> vanillaLayeredBiomeSource.BIOME_REGISTRY))
            .apply(instance, instance.stable(BzBiomeProvider::new)));

    private final Layer BIOME_SAMPLER;
    private final Registry<Biome> BIOME_REGISTRY;
    public static Registry<Biome> layersBiomeRegistry;

    public BzBiomeProvider(Registry<Biome> biomeRegistry) {
        this(0, biomeRegistry);
    }
    public BzBiomeProvider(long seed, Registry<Biome> biomeRegistry) {
        super(biomeRegistry.getEntries().stream()
                .filter(entry -> entry.getKey().getValue().getNamespace().equals(Bumblezone.MODID))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList()));

        BzBiomeLayer.setSeed(seed);
        this.BIOME_REGISTRY = biomeRegistry;
        BzBiomeProvider.layersBiomeRegistry = biomeRegistry;
        this.BIOME_SAMPLER = buildWorldProcedure(seed);
    }



    public static <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> stack(long seed, IAreaTransformer1 parent, IAreaFactory<T> incomingArea, int count, LongFunction<C> contextFactory) {
        IAreaFactory<T> IAreaFactory = incomingArea;

        for (int i = 0; i < count; ++i) {
            IAreaFactory = parent.apply(contextFactory.apply(seed + (long) i), IAreaFactory);
        }

        return IAreaFactory;
    }


    public static Layer buildWorldProcedure(long seed) {
        IAreaFactory<LazyArea> layerFactory = build((salt) ->
                new LazyAreaLayerContext(25, seed, salt));
        return new Layer(layerFactory);
    }


    public static <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> build(LongFunction<C> contextFactory) {
        IAreaFactory<T> layer = BzBiomeLayer.INSTANCE.apply(contextFactory.apply(200L));
        layer = BzBiomePillarLayer.INSTANCE.apply(contextFactory.apply(1008L), layer);
        layer = BzBiomeScalePillarLayer.INSTANCE.apply(contextFactory.apply(1055L), layer);
        layer = ZoomLayer.FUZZY.apply(contextFactory.apply(2003L), layer);
        layer = ZoomLayer.FUZZY.apply(contextFactory.apply(2523L), layer);
        return layer;
    }


    public Biome getBiomeForNoiseGen(int x, int y, int z) {
        return this.sample(this.BIOME_REGISTRY, x, z);
    }

    public Biome sample(Registry<Biome> registry, int i, int j) {
        int k = ((LayerAccessor)this.BIOME_SAMPLER).getSampler().getValue(i, j);
        Biome biome = registry.getByValue(k);
        if (biome == null) {
            if (SharedConstants.developmentMode) {
                throw Util.throwOrPause(new IllegalStateException("Unknown biome id: " + k));
            } else {
                return registry.get(BiomeRegistry.fromRawId(0));
            }
        } else {
            return biome;
        }
    }

    @Override
    protected Codec<? extends BiomeProvider> getCodec() {
        return CODEC;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public BiomeProvider withSeed(long seed) {
        return new BzBiomeProvider(seed, this.BIOME_REGISTRY);
    }
}
