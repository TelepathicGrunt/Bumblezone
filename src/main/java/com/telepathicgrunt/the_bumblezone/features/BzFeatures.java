package com.telepathicgrunt.the_bumblezone.features;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.generation.BzBiomeProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.BiomeMaker;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.event.RegistryEvent;

public class BzFeatures {
    public static Feature<NoFeatureConfig> HONEYCOMB_HOLE = new HoneycombHole(NoFeatureConfig.CODEC);
    public static Feature<NoFeatureConfig> HONEYCOMB_CAVES = new HoneycombCaves(NoFeatureConfig.CODEC);
    public static Feature<NoFeatureConfig> CAVE_SUGAR_WATERFALL = new CaveSugarWaterfall(NoFeatureConfig.CODEC);
    public static Feature<NoFeatureConfig> BEE_DUNGEON = new BeeDungeon(NoFeatureConfig.CODEC);
    public static Feature<NoFeatureConfig> SPIDER_INFESTED_BEE_DUNGEON = new SpiderInfestedBeeDungeon(NoFeatureConfig.CODEC);
    public static Feature<NoFeatureConfig> HONEY_CRYSTAL_FEATURE = new HoneyCrystalFeature(NoFeatureConfig.CODEC);
    public static Feature<BzBEOreFeatureConfig> BZ_BE_ORE_FEATURE = new BzBEOreFeature(BzBEOreFeatureConfig.CODEC);
    public static Feature<NoFeatureConfig> BZ_BEES_WAX_PILLAR_FEATURE = new BzBeesWaxPillarFeature(NoFeatureConfig.CODEC);

    public static void registerFeatures(final RegistryEvent.Register<Feature<?>> event) {
        event.getRegistry().register(HONEYCOMB_HOLE.setRegistryName(new ResourceLocation(Bumblezone.MODID, "honeycomb_holes")));
        event.getRegistry().register(HONEYCOMB_CAVES.setRegistryName(new ResourceLocation(Bumblezone.MODID, "honeycomb_caves")));
        event.getRegistry().register(CAVE_SUGAR_WATERFALL.setRegistryName(new ResourceLocation(Bumblezone.MODID, "cave_sugar_waterfall")));
        event.getRegistry().register(BEE_DUNGEON.setRegistryName(new ResourceLocation(Bumblezone.MODID, "bee_dungeon")));
        event.getRegistry().register(SPIDER_INFESTED_BEE_DUNGEON.setRegistryName(new ResourceLocation(Bumblezone.MODID, "spider_infested_bee_dungeon")));
        event.getRegistry().register(HONEY_CRYSTAL_FEATURE.setRegistryName(new ResourceLocation(Bumblezone.MODID, "honey_crystal_feature")));
        event.getRegistry().register(BZ_BE_ORE_FEATURE.setRegistryName(new ResourceLocation(Bumblezone.MODID, "bz_be_ore_feature")));
        event.getRegistry().register(BZ_BEES_WAX_PILLAR_FEATURE.setRegistryName(new ResourceLocation(Bumblezone.MODID, "bz_bees_wax_pillar_feature")));
    }
}
