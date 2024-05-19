package com.telepathicgrunt.the_bumblezone.modinit;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.worldgen.processors.ArenaSpecialBlockHandlerProcessor;
import com.telepathicgrunt.the_bumblezone.worldgen.processors.BeeDungeonProcessor;
import com.telepathicgrunt.the_bumblezone.worldgen.processors.BlockMergeOverridesProcessor;
import com.telepathicgrunt.the_bumblezone.worldgen.processors.CloseOffOutsideFluidsProcessor;
import com.telepathicgrunt.the_bumblezone.worldgen.processors.FluidTickProcessor;
import com.telepathicgrunt.the_bumblezone.worldgen.processors.HoneycombBroodRandomizeProcessor;
import com.telepathicgrunt.the_bumblezone.worldgen.processors.HoneycombHoleProcessor;
import com.telepathicgrunt.the_bumblezone.worldgen.processors.NoiseReplaceWithPropertiesProcessor;
import com.telepathicgrunt.the_bumblezone.worldgen.processors.PillarProcessor;
import com.telepathicgrunt.the_bumblezone.worldgen.processors.PollenPilingProcessor;
import com.telepathicgrunt.the_bumblezone.worldgen.processors.RandomReplaceWithPropertiesProcessor;
import com.telepathicgrunt.the_bumblezone.worldgen.processors.RemoveFloatingBlocksProcessor;
import com.telepathicgrunt.the_bumblezone.worldgen.processors.ReplaceAirOnlyProcessor;
import com.telepathicgrunt.the_bumblezone.worldgen.processors.ReplaceHoneyProcessor;
import com.telepathicgrunt.the_bumblezone.worldgen.processors.ReplaceNotAirProcessor;
import com.telepathicgrunt.the_bumblezone.worldgen.processors.SpawnerRandomizingProcessor;
import com.telepathicgrunt.the_bumblezone.worldgen.processors.SpiderInfestedBeeDungeonProcessor;
import com.telepathicgrunt.the_bumblezone.worldgen.processors.StrongerWaterloggingProcessor;
import com.telepathicgrunt.the_bumblezone.worldgen.processors.TagIgnoreProcessor;
import com.telepathicgrunt.the_bumblezone.worldgen.processors.TagReplaceProcessor;
import com.telepathicgrunt.the_bumblezone.worldgen.processors.WaterloggingFixProcessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class BzProcessors {
    public static final ResourcefulRegistry<StructureProcessorType<?>> STRUCTURE_PROCESSOR = ResourcefulRegistries.create(BuiltInRegistries.STRUCTURE_PROCESSOR, Bumblezone.MODID);

    public static final RegistryEntry<StructureProcessorType<BeeDungeonProcessor>> BEE_DUNGEON_PROCESSOR = STRUCTURE_PROCESSOR.register("bee_dungeon_processor", () -> () -> BeeDungeonProcessor.CODEC);
    public static final RegistryEntry<StructureProcessorType<SpiderInfestedBeeDungeonProcessor>> SPIDER_INFESTED_BEE_DUNGEON_PROCESSOR = STRUCTURE_PROCESSOR.register("spider_infested_bee_dungeon_processor", () -> () -> SpiderInfestedBeeDungeonProcessor.CODEC);
    public static final RegistryEntry<StructureProcessorType<ReplaceNotAirProcessor>> REPLACE_NOT_AIR_PROCESSOR = STRUCTURE_PROCESSOR.register("replace_not_air_processor", () -> () -> ReplaceNotAirProcessor.CODEC);
    public static final RegistryEntry<StructureProcessorType<ReplaceAirOnlyProcessor>> REPLACE_AIR_PROCESSOR = STRUCTURE_PROCESSOR.register("replace_air_processor", () -> () -> ReplaceAirOnlyProcessor.CODEC);
    public static final RegistryEntry<StructureProcessorType<CloseOffOutsideFluidsProcessor>> CLOSE_OFF_OUTSIDE_FLUIDS_PROCESSOR = STRUCTURE_PROCESSOR.register("close_off_outside_fluids_processor", () -> () -> CloseOffOutsideFluidsProcessor.CODEC);
    public static final RegistryEntry<StructureProcessorType<RemoveFloatingBlocksProcessor>> REMOVE_FLOATING_BLOCKS_PROCESSOR = STRUCTURE_PROCESSOR.register("remove_floating_blocks_processor", () -> () -> RemoveFloatingBlocksProcessor.CODEC);
    public static final RegistryEntry<StructureProcessorType<SpawnerRandomizingProcessor>> SPAWNER_RANDOMIZING_PROCESSOR = STRUCTURE_PROCESSOR.register("spawner_randomizing_processor", () -> () -> SpawnerRandomizingProcessor.CODEC);
    public static final RegistryEntry<StructureProcessorType<HoneycombHoleProcessor>> HONEYCOMB_HOLE_PROCESSOR = STRUCTURE_PROCESSOR.register("honeycomb_hole_processor", () -> () -> HoneycombHoleProcessor.CODEC);
    public static final RegistryEntry<StructureProcessorType<PollenPilingProcessor>> POLLEN_PILING_PROCESSOR = STRUCTURE_PROCESSOR.register("pollen_piling_processor", () -> () -> PollenPilingProcessor.CODEC);
    public static final RegistryEntry<StructureProcessorType<FluidTickProcessor>> FLUID_TICK_PROCESSOR = STRUCTURE_PROCESSOR.register("fluid_tick_processor", () -> () -> FluidTickProcessor.CODEC);
    public static final RegistryEntry<StructureProcessorType<NoiseReplaceWithPropertiesProcessor>> NOISE_REPLACE_WITH_PROPERTIES_PROCESSOR = STRUCTURE_PROCESSOR.register("noise_replace_with_properties_processor", () -> () -> NoiseReplaceWithPropertiesProcessor.CODEC);
    public static final RegistryEntry<StructureProcessorType<HoneycombBroodRandomizeProcessor>> HONEYCOMB_BROOD_RANDOMIZE_PROCESSOR = STRUCTURE_PROCESSOR.register("honeycomb_brood_randomize_processor", () -> () -> HoneycombBroodRandomizeProcessor.CODEC);
    public static final RegistryEntry<StructureProcessorType<RandomReplaceWithPropertiesProcessor>> RANDOM_REPLACE_WITH_PROPERTIES_PROCESSOR = STRUCTURE_PROCESSOR.register("random_replace_with_properties_processor", () -> () -> RandomReplaceWithPropertiesProcessor.CODEC);
    public static final RegistryEntry<StructureProcessorType<PillarProcessor>> PILLAR_PROCESSOR = STRUCTURE_PROCESSOR.register("pillar_processor", () -> () -> PillarProcessor.CODEC);
    public static final RegistryEntry<StructureProcessorType<TagReplaceProcessor>> TAG_REPLACE_PROCESSOR = STRUCTURE_PROCESSOR.register("tag_replace_processor", () -> () -> TagReplaceProcessor.CODEC);
    public static final RegistryEntry<StructureProcessorType<TagIgnoreProcessor>> TAG_IGNORE_PROCESSOR = STRUCTURE_PROCESSOR.register("tag_ignore_processor", () -> () -> TagIgnoreProcessor.CODEC);
    public static final RegistryEntry<StructureProcessorType<BlockMergeOverridesProcessor>> BLOCK_MERGE_OVERRIDES_PROCESSOR = STRUCTURE_PROCESSOR.register("block_merge_overrides_processor", () -> () -> BlockMergeOverridesProcessor.CODEC);
    public static final RegistryEntry<StructureProcessorType<StrongerWaterloggingProcessor>> STRONGER_WATERLOGGING_PROCESSOR = STRUCTURE_PROCESSOR.register("stronger_waterlogging_processor", () -> () -> StrongerWaterloggingProcessor.CODEC);
    public static final RegistryEntry<StructureProcessorType<WaterloggingFixProcessor>> WATERLOGGING_FIX_PROCESSOR = STRUCTURE_PROCESSOR.register("waterlogging_fix_processor", () -> () -> WaterloggingFixProcessor.CODEC);
    public static final RegistryEntry<StructureProcessorType<ArenaSpecialBlockHandlerProcessor>> ARENA_SPECIAL_BLOCK_HANDLER_PROCESSOR = STRUCTURE_PROCESSOR.register("arena_special_block_handler_processor", () -> () -> ArenaSpecialBlockHandlerProcessor.CODEC);
    public static final RegistryEntry<StructureProcessorType<ReplaceHoneyProcessor>> REPLACE_HONEY_PROCESSOR = STRUCTURE_PROCESSOR.register("replace_honey_processor", () -> () -> ReplaceHoneyProcessor.CODEC);
}
