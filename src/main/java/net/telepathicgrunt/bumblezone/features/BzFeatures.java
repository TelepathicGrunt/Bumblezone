package net.telepathicgrunt.bumblezone.features;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class BzFeatures {
    public static Feature<DefaultFeatureConfig> HONEYCOMB_HOLE = new HoneycombHole(DefaultFeatureConfig::deserialize);
    public static Feature<DefaultFeatureConfig> HONEYCOMB_CAVES = new HoneycombCaves(DefaultFeatureConfig::deserialize);
    public static Feature<DefaultFeatureConfig> CAVE_SUGAR_WATERFALL = new CaveSugarWaterfall(DefaultFeatureConfig::deserialize);
    public static Feature<DefaultFeatureConfig> BEE_DUNGEON = new BeeDungeon(DefaultFeatureConfig::deserialize);
    public static Feature<DefaultFeatureConfig> SPIDER_INFESTED_BEE_DUNGEON = new SpiderInfestedBeeDungeon(DefaultFeatureConfig::deserialize);
    public static Feature<DefaultFeatureConfig> HONEY_CRYSTAL_FEATURE = new HoneyCrystalFeature(DefaultFeatureConfig::deserialize);

    public static void registerFeatures() {
        Registry.register(Registry.FEATURE, "honeycomb_hole", HONEYCOMB_HOLE);
        Registry.register(Registry.FEATURE, "honeycomb_caves", HONEYCOMB_CAVES);
        Registry.register(Registry.FEATURE, "cave_sugar_waterfall", CAVE_SUGAR_WATERFALL);
        Registry.register(Registry.FEATURE, "bee_dungeon", BEE_DUNGEON);
        Registry.register(Registry.FEATURE, "spider_infested_bee_dungeon", SPIDER_INFESTED_BEE_DUNGEON);
        Registry.register(Registry.FEATURE, "honey_crystal_feature", HONEY_CRYSTAL_FEATURE);
    }
}
