package com.telepathicgrunt.bumblezone.world.dimension;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.mixin.world.BiomeLayerAccessor;
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
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.biome.Biomes;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.newbiome.area.Area;
import net.minecraft.world.level.newbiome.area.AreaFactory;
import net.minecraft.world.level.newbiome.area.LazyArea;
import net.minecraft.world.level.newbiome.context.BigContext;
import net.minecraft.world.level.newbiome.context.LazyAreaContext;
import net.minecraft.world.level.newbiome.layer.Layer;
import net.minecraft.world.level.newbiome.layer.ZoomLayer;
import net.minecraft.world.level.newbiome.layer.traits.AreaTransformer1;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.LongFunction;
import java.util.stream.Collectors;

public class BzBiomeProvider extends BiomeSource {
    public static void registerBiomeProvider() {
        Registry.register(Registry.BIOME_SOURCE, new ResourceLocation(Bumblezone.MODID, "biome_source"), BzBiomeProvider.CODEC);
    }

    public static final Codec<BzBiomeProvider> CODEC =
            RecordCodecBuilder.create((instance) -> instance.group(
                    RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter((biomeSource) -> biomeSource.BIOME_REGISTRY))
            .apply(instance, instance.stable(BzBiomeProvider::new)));


    public static ResourceLocation HIVE_WALL = new ResourceLocation(Bumblezone.MODID, "hive_wall");
    public static ResourceLocation HIVE_PILLAR = new ResourceLocation(Bumblezone.MODID, "hive_pillar");
    public static ResourceLocation SUGAR_WATER_FLOOR = new ResourceLocation(Bumblezone.MODID, "sugar_water_floor");
    public static ResourceLocation POLLINATED_FIELDS = new ResourceLocation(Bumblezone.MODID, "pollinated_fields");
    public static ResourceLocation POLLINATED_PILLAR = new ResourceLocation(Bumblezone.MODID, "pollinated_pillar");

    private final Layer BIOME_SAMPLER;
    private final Registry<Biome> BIOME_REGISTRY;
    public static Registry<Biome> LAYERS_BIOME_REGISTRY;
    public static List<Biome> NONSTANDARD_BIOME = new ArrayList<>();

    public BzBiomeProvider(Registry<Biome> biomeRegistry) {
        // Need world seed passed here
        this(0, biomeRegistry);
    }

    public BzBiomeProvider(long seed, Registry<Biome> biomeRegistry) {
        super(biomeRegistry.entrySet().stream()
                .filter(entry -> entry.getKey().location().getNamespace().equals(Bumblezone.MODID))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList()));

        NONSTANDARD_BIOME = this.possibleBiomes.stream()
                .filter(biome ->  {
                    ResourceLocation rlKey = biomeRegistry.getKey(biome);
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



    public static <T extends Area, C extends BigContext<T>> AreaFactory<T> stack(long seed, AreaTransformer1 parent, AreaFactory<T> incomingArea, int count, LongFunction<C> contextFactory) {
        AreaFactory<T> LayerFactory = incomingArea;

        for (int i = 0; i < count; ++i) {
            LayerFactory = parent.run(contextFactory.apply(seed + (long) i), LayerFactory);
        }

        return LayerFactory;
    }


    public static Layer buildWorldProcedure(long seed) {
        AreaFactory<LazyArea> layerFactory = build((salt) ->
                new LazyAreaContext(25, seed, salt));
        return new Layer(layerFactory);
    }


    public static <T extends Area, C extends BigContext<T>> AreaFactory<T> build(LongFunction<C> contextFactory) {
        AreaFactory<T> layer = BzBiomeLayer.INSTANCE.run(contextFactory.apply(200L));
        layer = BzBiomePillarLayer.INSTANCE.run(contextFactory.apply(1008L), layer);
        layer = new BzBiomeScaleLayer(HIVE_PILLAR).run(contextFactory.apply(1055L), layer);
        layer = ZoomLayer.FUZZY.run(contextFactory.apply(2003L), layer);
        layer = ZoomLayer.FUZZY.run(contextFactory.apply(2523L), layer);

        AreaFactory<T> layerOverlay = BzBiomeNonstandardLayer.INSTANCE.run(contextFactory.apply(204L));
        layerOverlay = ZoomLayer.NORMAL.run(contextFactory.apply(2423L), layerOverlay);
        layerOverlay = BzBiomePollinatedPillarLayer.INSTANCE.run(contextFactory.apply(3008L), layerOverlay);
        layerOverlay = new BzBiomeScaleLayer(POLLINATED_PILLAR).run(contextFactory.apply(4455L), layerOverlay);
        layerOverlay = ZoomLayer.NORMAL.run(contextFactory.apply(2503L), layerOverlay);
        layerOverlay = ZoomLayer.NORMAL.run(contextFactory.apply(2603L), layerOverlay);
        layerOverlay = BzBiomePollinatedFieldsLayer.INSTANCE.run(contextFactory.apply(3578L), layerOverlay);
        layerOverlay = new BzBiomeScaleLayer(POLLINATED_FIELDS).run(contextFactory.apply(4055L), layerOverlay);
        layerOverlay = ZoomLayer.FUZZY.run(contextFactory.apply(2853L), layerOverlay);
        layerOverlay = ZoomLayer.FUZZY.run(contextFactory.apply(3583L), layerOverlay);
        layer = BzBiomeMergeLayer.INSTANCE.run(contextFactory.apply(5583L), layerOverlay, layer);

        return layer;
    }


    public Biome getNoiseBiome(int x, int y, int z) {
        return this.sample(this.BIOME_REGISTRY, x, z);
    }

    public Biome sample(Registry<Biome> dynamicBiomeRegistry, int x, int z) {
        int resultBiomeID = ((BiomeLayerAccessor)this.BIOME_SAMPLER).thebumblezone_getSampler().get(x, z);
        Biome biome = dynamicBiomeRegistry.byId(resultBiomeID);
        if (biome == null) {
            if (SharedConstants.IS_RUNNING_IN_IDE) {
                throw Util.pauseInIde(new IllegalStateException("Unknown biome id: " + resultBiomeID));
            } else {
                // Spawn ocean if we can't resolve the biome from the layers.
                ResourceKey<Biome> backupBiomeKey = Biomes.byId(0);
                Bumblezone.LOGGER.warn("Unknown biome id: ${}. Will spawn ${} instead.", resultBiomeID, backupBiomeKey.location());
                return dynamicBiomeRegistry.get(backupBiomeKey);
            }
        } else {
            return biome;
        }
    }

    @Override
    protected Codec<? extends BiomeSource> codec() {
        return CODEC;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public BiomeSource withSeed(long seed) {
        return new BzBiomeProvider(seed, this.BIOME_REGISTRY);
    }
}
