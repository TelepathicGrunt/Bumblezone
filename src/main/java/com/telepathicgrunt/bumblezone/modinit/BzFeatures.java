package com.telepathicgrunt.bumblezone.modinit;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.world.features.BeeDungeon;
import com.telepathicgrunt.bumblezone.world.features.CaveSugarWaterfall;
import com.telepathicgrunt.bumblezone.world.features.HoneyCrystalFeature;
import com.telepathicgrunt.bumblezone.world.features.HoneycombCaves;
import com.telepathicgrunt.bumblezone.world.features.HoneycombHole;
import com.telepathicgrunt.bumblezone.world.features.NbtFeature;
import com.telepathicgrunt.bumblezone.world.features.PollinatedCaves;
import com.telepathicgrunt.bumblezone.world.features.SpiderInfestedBeeDungeon;
import com.telepathicgrunt.bumblezone.world.features.configs.NbtFeatureConfig;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class BzFeatures {
    public static Feature<NbtFeatureConfig> HONEYCOMB_HOLE = new HoneycombHole(NbtFeatureConfig.CODEC);
    public static Feature<DefaultFeatureConfig> HONEYCOMB_CAVES = new HoneycombCaves(DefaultFeatureConfig.CODEC);
    public static Feature<DefaultFeatureConfig> POLLINATED_CAVES = new PollinatedCaves(DefaultFeatureConfig.CODEC);
    public static Feature<DefaultFeatureConfig> CAVE_SUGAR_WATERFALL = new CaveSugarWaterfall(DefaultFeatureConfig.CODEC);
    public static Feature<DefaultFeatureConfig> HONEY_CRYSTAL_FEATURE = new HoneyCrystalFeature(DefaultFeatureConfig.CODEC);
    public static Feature<NbtFeatureConfig> NBT_FEATURE = new NbtFeature(NbtFeatureConfig.CODEC);
    public static Feature<NbtFeatureConfig> BEE_DUNGEON = new BeeDungeon(NbtFeatureConfig.CODEC);
    public static Feature<NbtFeatureConfig> SPIDER_INFESTED_BEE_DUNGEON = new SpiderInfestedBeeDungeon(NbtFeatureConfig.CODEC);

    public static void registerFeatures() {
        Registry.register(Registry.FEATURE, new Identifier(Bumblezone.MODID, "honeycomb_holes"), HONEYCOMB_HOLE);
        Registry.register(Registry.FEATURE, new Identifier(Bumblezone.MODID, "honeycomb_caves"), HONEYCOMB_CAVES);
        Registry.register(Registry.FEATURE, new Identifier(Bumblezone.MODID, "pollinated_caves"), POLLINATED_CAVES);
        Registry.register(Registry.FEATURE, new Identifier(Bumblezone.MODID, "cave_sugar_waterfall"), CAVE_SUGAR_WATERFALL);
        Registry.register(Registry.FEATURE, new Identifier(Bumblezone.MODID, "honey_crystal_feature"), HONEY_CRYSTAL_FEATURE);
        Registry.register(Registry.FEATURE, new Identifier(Bumblezone.MODID, "nbt_feature"), NBT_FEATURE);
        Registry.register(Registry.FEATURE, new Identifier(Bumblezone.MODID, "bee_dungeon"), BEE_DUNGEON);
        Registry.register(Registry.FEATURE, new Identifier(Bumblezone.MODID, "spider_infested_bee_dungeon"), SPIDER_INFESTED_BEE_DUNGEON);
    }
}
