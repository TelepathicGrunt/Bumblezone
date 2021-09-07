package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.tags.BzBlockTags;
import com.telepathicgrunt.the_bumblezone.world.features.BeeDungeon;
import com.telepathicgrunt.the_bumblezone.world.features.BzBeesWaxPillarFeature;
import com.telepathicgrunt.the_bumblezone.world.features.CaveSugarWaterfall;
import com.telepathicgrunt.the_bumblezone.world.features.HoneyCrystalFeature;
import com.telepathicgrunt.the_bumblezone.world.features.HoneycombCaves;
import com.telepathicgrunt.the_bumblezone.world.features.HoneycombHole;
import com.telepathicgrunt.the_bumblezone.world.features.NbtFeature;
import com.telepathicgrunt.the_bumblezone.world.features.PollinatedCaves;
import com.telepathicgrunt.the_bumblezone.world.features.SpiderInfestedBeeDungeon;
import com.telepathicgrunt.the_bumblezone.world.features.configs.NbtFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.feature.template.TagMatchRuleTest;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BzFeatures {
    // For making ore features replace combs
    // Init at mod startup when adding FEATURES to event bus
    public static final RuleTest HONEYCOMB_BUMBLEZONE = new TagMatchRuleTest(BzBlockTags.HONEYCOMBS_THAT_FEATURES_CAN_CARVE);

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Bumblezone.MODID);

    public static final RegistryObject<Feature<NbtFeatureConfig>> HONEYCOMB_HOLE = FEATURES.register("honeycomb_holes", () -> new HoneycombHole(NbtFeatureConfig.CODEC));
    public static final RegistryObject<Feature<NoFeatureConfig>> HONEYCOMB_CAVES = FEATURES.register("honeycomb_caves", () -> new HoneycombCaves(NoFeatureConfig.CODEC));
    public static final RegistryObject<Feature<NoFeatureConfig>> POLLINATED_CAVES = FEATURES.register("pollinated_caves", () -> new PollinatedCaves(NoFeatureConfig.CODEC));
    public static final RegistryObject<Feature<NoFeatureConfig>> CAVE_SUGAR_WATERFALL = FEATURES.register("cave_sugar_waterfall", () -> new CaveSugarWaterfall(NoFeatureConfig.CODEC));
    public static final RegistryObject<Feature<NoFeatureConfig>> HONEY_CRYSTAL_FEATURE = FEATURES.register("honey_crystal_feature", () -> new HoneyCrystalFeature(NoFeatureConfig.CODEC));
    public static final RegistryObject<Feature<NoFeatureConfig>> BZ_BEES_WAX_PILLAR_FEATURE = FEATURES.register("bz_bees_wax_pillar_feature", () -> new BzBeesWaxPillarFeature(NoFeatureConfig.CODEC));
    public static final RegistryObject<Feature<NbtFeatureConfig>> NBT_FEATURE = FEATURES.register("nbt_feature", () -> new NbtFeature(NbtFeatureConfig.CODEC));
    public static final RegistryObject<Feature<NbtFeatureConfig>> BEE_DUNGEON = FEATURES.register("bee_dungeon", () -> new BeeDungeon(NbtFeatureConfig.CODEC));
    public static final RegistryObject<Feature<NbtFeatureConfig>> SPIDER_INFESTED_BEE_DUNGEON = FEATURES.register("spider_infested_bee_dungeon", () -> new SpiderInfestedBeeDungeon(NbtFeatureConfig.CODEC));
}
