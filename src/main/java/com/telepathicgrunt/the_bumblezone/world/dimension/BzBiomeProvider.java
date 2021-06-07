package com.telepathicgrunt.the_bumblezone.world.dimension;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.mixin.world.LayerAccessor;
import com.telepathicgrunt.the_bumblezone.world.dimension.layer.*;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.LongFunction;
import java.util.stream.Collectors;

public class BzBiomeProvider extends BiomeProvider {
    public static void registerBiomeProvider() {
        Registry.register(Registry.BIOME_SOURCE, new ResourceLocation(Bumblezone.MODID, "biome_source"), BzBiomeProvider.CODEC);
    }

    public static final Codec<BzBiomeProvider> CODEC =
            RecordCodecBuilder.create((instance) -> instance.group(
                    RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter((vanillaLayeredBiomeSource) -> vanillaLayeredBiomeSource.BIOME_REGISTRY))
            .apply(instance, instance.stable(BzBiomeProvider::new)));

    public static ResourceLocation HIVE_WALL = new ResourceLocation(Bumblezone.MODID, "hive_wall");
    public static ResourceLocation HIVE_PILLAR = new ResourceLocation(Bumblezone.MODID, "hive_pillar");
    public static ResourceLocation SUGAR_WATER_FLOOR = new ResourceLocation(Bumblezone.MODID, "sugar_water_floor");

    private final Layer BIOME_SAMPLER;
    private final Registry<Biome> BIOME_REGISTRY;
    public static Registry<Biome> LAYERS_BIOME_REGISTRY;
    public static List<Biome> NONSTANDARD_BIOME = new ArrayList<>();

    public BzBiomeProvider(Registry<Biome> biomeRegistry) {
        this(0, biomeRegistry);
    }
    public BzBiomeProvider(long seed, Registry<Biome> biomeRegistry) {
        super(biomeRegistry.entrySet().stream()
                .filter(entry -> entry.getKey().location().getNamespace().equals(Bumblezone.MODID))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList()));

        NONSTANDARD_BIOME = this.possibleBiomes.stream()
                .filter(biome -> biomeRegistry.getKey(biome) != HIVE_WALL &&
                                biomeRegistry.getKey(biome) != HIVE_PILLAR &&
                                biomeRegistry.getKey(biome) != SUGAR_WATER_FLOOR)
                .collect(Collectors.toList());

        BzBiomeLayer.setSeed(seed);
        this.BIOME_REGISTRY = biomeRegistry;
        BzBiomeProvider.LAYERS_BIOME_REGISTRY = biomeRegistry;
        this.BIOME_SAMPLER = buildWorldProcedure(seed);
    }

    public static Layer buildWorldProcedure(long seed) {
        IAreaFactory<LazyArea> layerFactory = build((salt) ->
                new LazyAreaLayerContext(25, seed, salt));
        return new Layer(layerFactory);
    }


    public static <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> build(LongFunction<C> contextFactory) {
        IAreaFactory<T> layer = BzBiomeLayer.INSTANCE.run(contextFactory.apply(200L));
        layer = BzBiomePillarLayer.INSTANCE.run(contextFactory.apply(1008L), layer);
        layer = BzBiomeScalePillarLayer.INSTANCE.run(contextFactory.apply(1055L), layer);
        layer = ZoomLayer.FUZZY.run(contextFactory.apply(2003L), layer);
        layer = ZoomLayer.FUZZY.run(contextFactory.apply(2523L), layer);

        if(!NONSTANDARD_BIOME.isEmpty()){
            IAreaFactory<T> layerOverlay = BzBiomeNonstandardLayer.INSTANCE.run(contextFactory.apply(204L));
            layerOverlay = ZoomLayer.NORMAL.run(contextFactory.apply(2423L), layerOverlay);
            layerOverlay = ZoomLayer.NORMAL.run(contextFactory.apply(2503L), layerOverlay);
            layerOverlay = ZoomLayer.NORMAL.run(contextFactory.apply(2603L), layerOverlay);
            layerOverlay = ZoomLayer.FUZZY.run(contextFactory.apply(2853L), layerOverlay);
            layerOverlay = ZoomLayer.FUZZY.run(contextFactory.apply(3583L), layerOverlay);
            layer = BzBiomeMergeLayer.INSTANCE.run(contextFactory.apply(5583L), layerOverlay, layer);
        }

        return layer;
    }

    @Override
    public Biome getNoiseBiome(int x, int y, int z) {
        return this.sample(this.BIOME_REGISTRY, x, z);
    }

    public Biome sample(Registry<Biome> dynamicBiomeRegistry, int x, int z) {
        int resultBiomeID = ((LayerAccessor)this.BIOME_SAMPLER).thebumblezone_getSampler().get(x, z);
        Biome biome = dynamicBiomeRegistry.byId(resultBiomeID);
        if (biome == null) {
            if (SharedConstants.IS_RUNNING_IN_IDE) {
                throw Util.pauseInIde(new IllegalStateException("Unknown biome id: " + resultBiomeID));
            } else {
                // Spawn ocean if we can't resolve the biome from the layers.
                RegistryKey<Biome> backupBiomeKey = BiomeRegistry.byId(0);
                Bumblezone.LOGGER.warn("Unknown biome id: ${}. Will spawn ${} instead.", resultBiomeID, backupBiomeKey.location());
                return dynamicBiomeRegistry.get(backupBiomeKey);
            }
        } else {
            return biome;
        }
    }

    @Override
    protected Codec<? extends BiomeProvider> codec() {
        return CODEC;
    }

    @Override
    // CLIENT-SIDED
    public BiomeProvider withSeed(long seed) {
        return new BzBiomeProvider(seed, this.BIOME_REGISTRY);
    }
}
