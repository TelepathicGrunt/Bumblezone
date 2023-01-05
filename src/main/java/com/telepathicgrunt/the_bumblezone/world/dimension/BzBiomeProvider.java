package com.telepathicgrunt.the_bumblezone.world.dimension;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import com.telepathicgrunt.the_bumblezone.world.dimension.layer.BzBiomeBlobLayer;
import com.telepathicgrunt.the_bumblezone.world.dimension.layer.BzBiomeLayer;
import com.telepathicgrunt.the_bumblezone.world.dimension.layer.BzBiomeMergeLayer;
import com.telepathicgrunt.the_bumblezone.world.dimension.layer.BzBiomePillarLayer;
import com.telepathicgrunt.the_bumblezone.world.dimension.layer.BzBiomePollinatedFieldsLayer;
import com.telepathicgrunt.the_bumblezone.world.dimension.layer.BzBiomePollinatedPillarLayer;
import com.telepathicgrunt.the_bumblezone.world.dimension.layer.BzBiomeScaleLayer;
import com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla.Area;
import com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla.AreaFactory;
import com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla.AreaTransformer1;
import com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla.BigContext;
import com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla.Layer;
import com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla.LazyArea;
import com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla.LazyAreaContext;
import com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla.ZoomLayer;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;

import java.util.Set;
import java.util.function.LongFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BzBiomeProvider extends BiomeSource implements BiomeManager.NoiseBiomeSource {

    public static final Codec<BzBiomeProvider> CODEC =
            RecordCodecBuilder.create((instance) -> instance.group(
                Codec.LONG.fieldOf("seed").orElse(0L).stable().forGetter(bzBiomeProvider -> bzBiomeProvider.seed),
                Biome.LIST_CODEC.fieldOf("blob_biomes").orElse(HolderSet.direct()).forGetter((biomeSource) -> biomeSource.blobBiomes),
                Biome.LIST_CODEC.fieldOf("main_biomes").orElse(HolderSet.direct()).forGetter((biomeSource) -> biomeSource.mainBiomes))
            .apply(instance, instance.stable(BzBiomeProvider::new)));

    public static ResourceLocation HIVE_WALL = new ResourceLocation(Bumblezone.MODID, "hive_wall");
    public static ResourceLocation HIVE_PILLAR = new ResourceLocation(Bumblezone.MODID, "hive_pillar");
    public static ResourceLocation SUGAR_WATER_FLOOR = new ResourceLocation(Bumblezone.MODID, "sugar_water_floor");
    public static ResourceLocation POLLINATED_FIELDS = new ResourceLocation(Bumblezone.MODID, "pollinated_fields");
    public static ResourceLocation POLLINATED_PILLAR = new ResourceLocation(Bumblezone.MODID, "pollinated_pillar");
    public static ResourceLocation CRYSTAL_CANYON = new ResourceLocation(Bumblezone.MODID, "crystal_canyon");

    private final long seed;
    private final Layer biomeSampler;
    public final HolderSet<Biome> blobBiomes;
    public final HolderSet<Biome> mainBiomes;
    public final GeneralUtils.Lazy<Set<Holder<Biome>>> lazyPossibleBiomes = new GeneralUtils.Lazy<>();

    public BzBiomeProvider(long seed, HolderSet<Biome> blobBiomes, HolderSet<Biome> mainBiomes) {
        super(Stream.empty());

        this.seed = seed;
        this.blobBiomes = blobBiomes;
        this.mainBiomes = mainBiomes;
        this.biomeSampler = buildWorldProcedure(seed, this.blobBiomes);
    }

    @Override
    protected Codec<? extends BiomeSource> codec() {
        return CODEC;
    }

    @Override
    public Set<Holder<Biome>> possibleBiomes() {
        return this.lazyPossibleBiomes.getOrCompute(() -> Stream.concat(blobBiomes.stream(), mainBiomes.stream()).collect(Collectors.toSet()));
    }

    public static <T extends Area, C extends BigContext<T>> AreaFactory<T> stack(long seed, AreaTransformer1 parent, AreaFactory<T> incomingArea, int count, LongFunction<C> contextFactory) {
        AreaFactory<T> LayerFactory = incomingArea;

        for (int i = 0; i < count; ++i) {
            LayerFactory = parent.run(contextFactory.apply(seed + (long) i), LayerFactory);
        }

        return LayerFactory;
    }

    public static Layer buildWorldProcedure(long seed, HolderSet<Biome> blobBiomes) {
        AreaFactory<LazyArea> layerFactory = build((salt) -> new LazyAreaContext(25, seed, salt), seed, blobBiomes);
        return new Layer(layerFactory);
    }

    public static <T extends Area, C extends BigContext<T>> AreaFactory<T> build(LongFunction<C> contextFactory, long seed, HolderSet<Biome> blobBiomes) {
        AreaFactory<T> layer = new BzBiomeLayer(seed).run(contextFactory.apply(200L));
        layer = new BzBiomePillarLayer().run(contextFactory.apply(1008L), layer);
        layer = new BzBiomeScaleLayer(Set.of(HIVE_PILLAR)).run(contextFactory.apply(1055L), layer);
        layer = ZoomLayer.FUZZY.run(contextFactory.apply(2003L), layer);
        layer = ZoomLayer.FUZZY.run(contextFactory.apply(2523L), layer);
        layer = new BzBiomeScaleLayer(Set.of(CRYSTAL_CANYON, SUGAR_WATER_FLOOR)).run(contextFactory.apply(54088L), layer);
        AreaFactory<T> layerOverlay = new BzBiomeBlobLayer(blobBiomes).run(contextFactory.apply(204L));
        layerOverlay = ZoomLayer.NORMAL.run(contextFactory.apply(2423L), layerOverlay);
        layerOverlay = new BzBiomePollinatedPillarLayer().run(contextFactory.apply(3008L), layerOverlay);
        layerOverlay = new BzBiomeScaleLayer(Set.of(POLLINATED_PILLAR)).run(contextFactory.apply(4455L), layerOverlay);
        layerOverlay = ZoomLayer.NORMAL.run(contextFactory.apply(2503L), layerOverlay);
        layerOverlay = ZoomLayer.NORMAL.run(contextFactory.apply(2603L), layerOverlay);
        layerOverlay = new BzBiomePollinatedFieldsLayer().run(contextFactory.apply(3578L), layerOverlay);
        layerOverlay = new BzBiomeScaleLayer(Set.of(POLLINATED_FIELDS)).run(contextFactory.apply(4055L), layerOverlay);
        layerOverlay = ZoomLayer.FUZZY.run(contextFactory.apply(2853L), layerOverlay);
        layerOverlay = ZoomLayer.FUZZY.run(contextFactory.apply(3583L), layerOverlay);
        layerOverlay = ZoomLayer.NORMAL.run(contextFactory.apply(4583L), layerOverlay);
        layer = new BzBiomeMergeLayer().run(contextFactory.apply(5583L), layerOverlay, layer);

        return layer;
    }

    @Override
    public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler sampler) {
        return biomeSampler.sample(x, z);
    }

    @Override
    public Holder<Biome> getNoiseBiome(int x, int y, int z) {
        return biomeSampler.sample(x, z);
    }
}
