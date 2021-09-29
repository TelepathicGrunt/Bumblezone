package com.telepathicgrunt.bumblezone.world.dimension;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.mixin.world.BiomeLayerSamplerAccessor;
import com.telepathicgrunt.bumblezone.world.dimension.layer.BzBiomeLayer;
import com.telepathicgrunt.bumblezone.world.dimension.layer.BzBiomeMergeLayer;
import com.telepathicgrunt.bumblezone.world.dimension.layer.BzBiomeNonstandardLayer;
import com.telepathicgrunt.bumblezone.world.dimension.layer.BzBiomePillarLayer;
import com.telepathicgrunt.bumblezone.world.dimension.layer.BzBiomePollinatedFieldsLayer;
import com.telepathicgrunt.bumblezone.world.dimension.layer.BzBiomePollinatedPillarLayer;
import com.telepathicgrunt.bumblezone.world.dimension.layer.BzBiomeScaleLayer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.dynamic.RegistryLookupCodec;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BuiltinBiomes;
import net.minecraft.world.biome.layer.ScaleLayer;
import net.minecraft.world.biome.layer.type.ParentedLayer;
import net.minecraft.world.biome.layer.util.CachingLayerContext;
import net.minecraft.world.biome.layer.util.CachingLayerSampler;
import net.minecraft.world.biome.layer.util.LayerFactory;
import net.minecraft.world.biome.layer.util.LayerSampleContext;
import net.minecraft.world.biome.layer.util.LayerSampler;
import net.minecraft.world.biome.source.BiomeLayerSampler;
import net.minecraft.world.biome.source.BiomeSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.LongFunction;
import java.util.stream.Collectors;

public class BzBiomeProvider extends BiomeSource {
    public static void registerBiomeProvider() {
        Registry.register(Registry.BIOME_SOURCE, new Identifier(Bumblezone.MODID, "biome_source"), BzBiomeProvider.CODEC);
    }

    public static final Codec<BzBiomeProvider> CODEC =
            RecordCodecBuilder.create((instance) -> instance.group(
                    RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter((biomeSource) -> biomeSource.BIOME_REGISTRY))
            .apply(instance, instance.stable(BzBiomeProvider::new)));


    public static Identifier HIVE_WALL = new Identifier(Bumblezone.MODID, "hive_wall");
    public static Identifier HIVE_PILLAR = new Identifier(Bumblezone.MODID, "hive_pillar");
    public static Identifier SUGAR_WATER_FLOOR = new Identifier(Bumblezone.MODID, "sugar_water_floor");
    public static Identifier POLLINATED_FIELDS = new Identifier(Bumblezone.MODID, "pollinated_fields");
    public static Identifier POLLINATED_PILLAR = new Identifier(Bumblezone.MODID, "pollinated_pillar");

    private final BiomeLayerSampler BIOME_SAMPLER;
    private final Registry<Biome> BIOME_REGISTRY;
    public static Registry<Biome> LAYERS_BIOME_REGISTRY;
    public static List<Biome> NONSTANDARD_BIOME = new ArrayList<>();

    public BzBiomeProvider(Registry<Biome> biomeRegistry) {
        // Need world seed passed here
        this(0, biomeRegistry);
    }

    public BzBiomeProvider(long seed, Registry<Biome> biomeRegistry) {
        super(biomeRegistry.getEntries().stream()
                .filter(entry -> entry.getKey().getValue().getNamespace().equals(Bumblezone.MODID))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList()));

        NONSTANDARD_BIOME = this.biomes.stream()
                .filter(biome ->  {
                    Identifier rlKey = biomeRegistry.getId(biome);
                    return !rlKey.equals(HIVE_WALL) &&
                            !rlKey.equals(HIVE_PILLAR) &&
                            !rlKey.equals(SUGAR_WATER_FLOOR) &&
                            !rlKey.equals(POLLINATED_FIELDS) &&
                            !rlKey.equals(POLLINATED_PILLAR);
                })
                .collect(Collectors.toList());

        BzBiomeLayer.setSeed(seed);
        this.BIOME_REGISTRY = biomeRegistry;
        BzBiomeProvider.LAYERS_BIOME_REGISTRY = biomeRegistry;
        this.BIOME_SAMPLER = buildWorldProcedure(seed);
    }



    public static <T extends LayerSampler, C extends LayerSampleContext<T>> LayerFactory<T> stack(long seed, ParentedLayer parent, LayerFactory<T> incomingArea, int count, LongFunction<C> contextFactory) {
        LayerFactory<T> LayerFactory = incomingArea;

        for (int i = 0; i < count; ++i) {
            LayerFactory = parent.create(contextFactory.apply(seed + (long) i), LayerFactory);
        }

        return LayerFactory;
    }


    public static BiomeLayerSampler buildWorldProcedure(long seed) {
        LayerFactory<CachingLayerSampler> layerFactory = build((salt) ->
                new CachingLayerContext(25, seed, salt));
        return new BiomeLayerSampler(layerFactory);
    }


    public static <T extends LayerSampler, C extends LayerSampleContext<T>> LayerFactory<T> build(LongFunction<C> contextFactory) {
        LayerFactory<T> layer = BzBiomeLayer.INSTANCE.create(contextFactory.apply(200L));
        layer = BzBiomePillarLayer.INSTANCE.create(contextFactory.apply(1008L), layer);
        layer = new BzBiomeScaleLayer(HIVE_PILLAR).create(contextFactory.apply(1055L), layer);
        layer = ScaleLayer.FUZZY.create(contextFactory.apply(2003L), layer);
        layer = ScaleLayer.FUZZY.create(contextFactory.apply(2523L), layer);

        LayerFactory<T> layerOverlay = BzBiomeNonstandardLayer.INSTANCE.create(contextFactory.apply(204L));
        layerOverlay = ScaleLayer.NORMAL.create(contextFactory.apply(2423L), layerOverlay);
        layerOverlay = BzBiomePollinatedPillarLayer.INSTANCE.create(contextFactory.apply(3008L), layerOverlay);
        layerOverlay = new BzBiomeScaleLayer(POLLINATED_PILLAR).create(contextFactory.apply(4455L), layerOverlay);
        layerOverlay = ScaleLayer.NORMAL.create(contextFactory.apply(2503L), layerOverlay);
        layerOverlay = ScaleLayer.NORMAL.create(contextFactory.apply(2603L), layerOverlay);
        layerOverlay = BzBiomePollinatedFieldsLayer.INSTANCE.create(contextFactory.apply(3578L), layerOverlay);
        layerOverlay = new BzBiomeScaleLayer(POLLINATED_FIELDS).create(contextFactory.apply(4055L), layerOverlay);
        layerOverlay = ScaleLayer.FUZZY.create(contextFactory.apply(2853L), layerOverlay);
        layerOverlay = ScaleLayer.FUZZY.create(contextFactory.apply(3583L), layerOverlay);
        layer = BzBiomeMergeLayer.INSTANCE.create(contextFactory.apply(5583L), layerOverlay, layer);

        return layer;
    }


    public Biome getBiomeForNoiseGen(int x, int y, int z) {
        return this.sample(this.BIOME_REGISTRY, x, z);
    }

    public Biome sample(Registry<Biome> dynamicBiomeRegistry, int x, int z) {
        int resultBiomeID = ((BiomeLayerSamplerAccessor)this.BIOME_SAMPLER).thebumblezone_getSampler().sample(x, z);
        Biome biome = dynamicBiomeRegistry.get(resultBiomeID);
        if (biome == null) {
            if (SharedConstants.isDevelopment) {
                throw Util.throwOrPause(new IllegalStateException("Unknown biome id: " + resultBiomeID));
            } else {
                // Spawn ocean if we can't resolve the biome from the layers.
                RegistryKey<Biome> backupBiomeKey = BuiltinBiomes.fromRawId(0);
                Bumblezone.LOGGER.warn("Unknown biome id: ${}. Will spawn ${} instead.", resultBiomeID, backupBiomeKey.getValue());
                return dynamicBiomeRegistry.get(backupBiomeKey);
            }
        } else {
            return biome;
        }
    }

    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return CODEC;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public BiomeSource withSeed(long seed) {
        return new BzBiomeProvider(seed, this.BIOME_REGISTRY);
    }
}
