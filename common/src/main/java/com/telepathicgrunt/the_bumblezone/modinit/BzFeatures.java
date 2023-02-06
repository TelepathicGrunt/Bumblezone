package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistries;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistry;
import com.telepathicgrunt.the_bumblezone.world.features.BeeDungeon;
import com.telepathicgrunt.the_bumblezone.world.features.BlockEntityCombOre;
import com.telepathicgrunt.the_bumblezone.world.features.CaveSugarWaterfall;
import com.telepathicgrunt.the_bumblezone.world.features.GiantHoneyCrystalFeature;
import com.telepathicgrunt.the_bumblezone.world.features.HangingGardenMob;
import com.telepathicgrunt.the_bumblezone.world.features.HoneyCrystalFeature;
import com.telepathicgrunt.the_bumblezone.world.features.HoneycombCaves;
import com.telepathicgrunt.the_bumblezone.world.features.HoneycombHole;
import com.telepathicgrunt.the_bumblezone.world.features.LayeredBlockSurface;
import com.telepathicgrunt.the_bumblezone.world.features.NbtFeature;
import com.telepathicgrunt.the_bumblezone.world.features.PollinatedCaves;
import com.telepathicgrunt.the_bumblezone.world.features.SpiderInfestedBeeDungeon;
import com.telepathicgrunt.the_bumblezone.world.features.StickyHoneyResidueFeature;
import com.telepathicgrunt.the_bumblezone.world.features.WebWall;
import com.telepathicgrunt.the_bumblezone.world.features.configs.BiomeBasedLayerConfig;
import com.telepathicgrunt.the_bumblezone.world.features.configs.HoneyCrystalFeatureConfig;
import com.telepathicgrunt.the_bumblezone.world.features.configs.NbtFeatureConfig;
import com.telepathicgrunt.the_bumblezone.world.features.configs.NbtOreConfiguration;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class BzFeatures {
    public static final ResourcefulRegistry<Feature<?>> FEATURES = ResourcefulRegistries.create(BuiltInRegistries.FEATURE, Bumblezone.MODID);

    public static final RegistryEntry<Feature<NbtFeatureConfig>> HONEYCOMB_HOLE = FEATURES.register("honeycomb_holes", () -> new HoneycombHole(NbtFeatureConfig.CODEC));
    public static final RegistryEntry<Feature<NoneFeatureConfiguration>> HONEYCOMB_CAVES = FEATURES.register("honeycomb_caves", () -> new HoneycombCaves(NoneFeatureConfiguration.CODEC));
    public static final RegistryEntry<Feature<NoneFeatureConfiguration>> POLLINATED_CAVES = FEATURES.register("pollinated_caves", () -> new PollinatedCaves(NoneFeatureConfiguration.CODEC));
    public static final RegistryEntry<Feature<NoneFeatureConfiguration>> CAVE_SUGAR_WATERFALL = FEATURES.register("cave_sugar_waterfall", () -> new CaveSugarWaterfall(NoneFeatureConfiguration.CODEC));
    public static final RegistryEntry<Feature<HoneyCrystalFeatureConfig>> HONEY_CRYSTAL_FEATURE = FEATURES.register("honey_crystals_feature", () -> new HoneyCrystalFeature(HoneyCrystalFeatureConfig.CODEC));
    public static final RegistryEntry<Feature<NoneFeatureConfiguration>> GIANT_HONEY_CRYSTAL_FEATURE = FEATURES.register("giant_honey_crystal_feature", () -> new GiantHoneyCrystalFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryEntry<Feature<NoneFeatureConfiguration>> STICKY_HONEY_RESIDUE_FEATURE = FEATURES.register("sticky_honey_residue_feature", () -> new StickyHoneyResidueFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryEntry<Feature<NbtFeatureConfig>> NBT_FEATURE = FEATURES.register("nbt_feature", () -> new NbtFeature(NbtFeatureConfig.CODEC));
    public static final RegistryEntry<Feature<NbtFeatureConfig>> BEE_DUNGEON = FEATURES.register("bee_dungeon", () -> new BeeDungeon(NbtFeatureConfig.CODEC));
    public static final RegistryEntry<Feature<NbtFeatureConfig>> SPIDER_INFESTED_BEE_DUNGEON = FEATURES.register("spider_infested_bee_dungeon", () -> new SpiderInfestedBeeDungeon(NbtFeatureConfig.CODEC));
    public static final RegistryEntry<Feature<BiomeBasedLayerConfig>> LAYERED_BLOCK_SURFACE = FEATURES.register("layered_block_surface", () -> new LayeredBlockSurface(BiomeBasedLayerConfig.CODEC));
    public static final RegistryEntry<Feature<NoneFeatureConfiguration>> WEB_WALL = FEATURES.register("web_wall", () -> new WebWall(NoneFeatureConfiguration.CODEC));
    public static final RegistryEntry<Feature<NoneFeatureConfiguration>> HANGING_GARDEN_MOB = FEATURES.register("hanging_garden_mob", () -> new HangingGardenMob(NoneFeatureConfiguration.CODEC));

    public static final RegistryEntry<Feature<NbtOreConfiguration>> BLOCKENTITY_COMBS_FEATURE = FEATURES.register("blockentity_combs", () -> new BlockEntityCombOre(NbtOreConfiguration.CODEC));
}
