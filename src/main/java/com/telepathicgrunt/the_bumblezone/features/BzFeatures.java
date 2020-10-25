package com.telepathicgrunt.the_bumblezone.features;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class BzFeatures {
    public static Feature<NoFeatureConfig> HONEYCOMB_HOLE = new HoneycombHole(NoFeatureConfig.CODEC);
    public static Feature<NoFeatureConfig> HONEYCOMB_CAVES = new HoneycombCaves(NoFeatureConfig.CODEC);
    public static Feature<NoFeatureConfig> CAVE_SUGAR_WATERFALL = new CaveSugarWaterfall(NoFeatureConfig.CODEC);
    public static Feature<NoFeatureConfig> BEE_DUNGEON = new BeeDungeon(NoFeatureConfig.CODEC);
    public static Feature<NoFeatureConfig> SPIDER_INFESTED_BEE_DUNGEON = new SpiderInfestedBeeDungeon(NoFeatureConfig.CODEC);
    public static Feature<NoFeatureConfig> HONEY_CRYSTAL_FEATURE = new HoneyCrystalFeature(NoFeatureConfig.CODEC);
    public static Feature<BzBEOreFeatureConfig> BZ_BE_ORE_FEATURE = new BzBEOreFeature(BzBEOreFeatureConfig.CODEC);
    public static Feature<NoFeatureConfig> BZ_BEES_WAX_PILLAR_FEATURE = new BzBeesWaxPillarFeature(NoFeatureConfig.CODEC);

    public static void registerFeatures() {
        Registry.register(Registry.FEATURE, new ResourceLocation(Bumblezone.MODID, "honeycomb_holes"), HONEYCOMB_HOLE);
        Registry.register(Registry.FEATURE, new ResourceLocation(Bumblezone.MODID, "honeycomb_caves"), HONEYCOMB_CAVES);
        Registry.register(Registry.FEATURE, new ResourceLocation(Bumblezone.MODID, "cave_sugar_waterfall"), CAVE_SUGAR_WATERFALL);
        Registry.register(Registry.FEATURE, new ResourceLocation(Bumblezone.MODID, "bee_dungeon"), BEE_DUNGEON);
        Registry.register(Registry.FEATURE, new ResourceLocation(Bumblezone.MODID, "spider_infested_bee_dungeon"), SPIDER_INFESTED_BEE_DUNGEON);
        Registry.register(Registry.FEATURE, new ResourceLocation(Bumblezone.MODID, "honey_crystal_feature"), HONEY_CRYSTAL_FEATURE);
        Registry.register(Registry.FEATURE, new ResourceLocation(Bumblezone.MODID, "bz_be_ore_feature"), BZ_BE_ORE_FEATURE);
        Registry.register(Registry.FEATURE, new ResourceLocation(Bumblezone.MODID, "bz_bees_wax_pillar_feature"), BZ_BEES_WAX_PILLAR_FEATURE);
    }
}
