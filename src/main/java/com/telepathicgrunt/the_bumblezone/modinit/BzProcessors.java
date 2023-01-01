package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.world.processors.BeeDungeonProcessor;
import com.telepathicgrunt.the_bumblezone.world.processors.CloseOffOutsideFluidsProcessor;
import com.telepathicgrunt.the_bumblezone.world.processors.FluidTickProcessor;
import com.telepathicgrunt.the_bumblezone.world.processors.HoneycombBroodRandomizeProcessor;
import com.telepathicgrunt.the_bumblezone.world.processors.HoneycombHoleProcessor;
import com.telepathicgrunt.the_bumblezone.world.processors.NoiseReplaceWithPropertiesProcessor;
import com.telepathicgrunt.the_bumblezone.world.processors.PillarProcessor;
import com.telepathicgrunt.the_bumblezone.world.processors.PollenPilingProcessor;
import com.telepathicgrunt.the_bumblezone.world.processors.RandomReplaceWithPropertiesProcessor;
import com.telepathicgrunt.the_bumblezone.world.processors.RemoveFloatingBlocksProcessor;
import com.telepathicgrunt.the_bumblezone.world.processors.ReplaceAirOnlyProcessor;
import com.telepathicgrunt.the_bumblezone.world.processors.ReplaceNotAirProcessor;
import com.telepathicgrunt.the_bumblezone.world.processors.SpawnerRandomizingProcessor;
import com.telepathicgrunt.the_bumblezone.world.processors.SpiderInfestedBeeDungeonProcessor;
import com.telepathicgrunt.the_bumblezone.world.processors.TagReplaceProcessor;
import com.telepathicgrunt.the_bumblezone.world.processors.WaterloggingFixProcessor;
import net.minecraft.core.Registry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class BzProcessors {

    public static final StructureProcessorType<BeeDungeonProcessor> BEE_DUNGEON_PROCESSOR = () -> BeeDungeonProcessor.CODEC;
    public static final StructureProcessorType<SpiderInfestedBeeDungeonProcessor> SPIDER_INFESTED_BEE_DUNGEON_PROCESSOR = () -> SpiderInfestedBeeDungeonProcessor.CODEC;
    public static final StructureProcessorType<ReplaceNotAirProcessor> REPLACE_NOT_AIR_PROCESSOR = () -> ReplaceNotAirProcessor.CODEC;
    public static final StructureProcessorType<CloseOffOutsideFluidsProcessor> CLOSE_OFF_OUTSIDE_FLUIDS_PROCESSOR = () -> CloseOffOutsideFluidsProcessor.CODEC;
    public static final StructureProcessorType<RemoveFloatingBlocksProcessor> REMOVE_FLOATING_BLOCKS_PROCESSOR = () -> RemoveFloatingBlocksProcessor.CODEC;
    public static final StructureProcessorType<SpawnerRandomizingProcessor> SPAWNER_RANDOMIZING_PROCESSOR = () -> SpawnerRandomizingProcessor.CODEC;
    public static final StructureProcessorType<HoneycombHoleProcessor> HONEYCOMB_HOLE_PROCESSOR = () -> HoneycombHoleProcessor.CODEC;
    public static final StructureProcessorType<PollenPilingProcessor> POLLEN_PILING_PROCESSOR = () -> PollenPilingProcessor.CODEC;
    public static final StructureProcessorType<FluidTickProcessor> FLUID_TICK_PROCESSOR = () -> FluidTickProcessor.CODEC;
    public static final StructureProcessorType<ReplaceAirOnlyProcessor> REPLACE_AIR_PROCESSOR = () -> ReplaceAirOnlyProcessor.CODEC;
    public static final StructureProcessorType<NoiseReplaceWithPropertiesProcessor> NOISE_REPLACE_WITH_PROPERTIES_PROCESSOR = () -> NoiseReplaceWithPropertiesProcessor.CODEC;
    public static final StructureProcessorType<HoneycombBroodRandomizeProcessor> HONEYCOMB_BROOD_RANDOMIZE_PROCESSOR = () -> HoneycombBroodRandomizeProcessor.CODEC;
    public static final StructureProcessorType<RandomReplaceWithPropertiesProcessor> RANDOM_REPLACE_WITH_PROPERTIES_PROCESSOR = () -> RandomReplaceWithPropertiesProcessor.CODEC;
    public static final StructureProcessorType<PillarProcessor> PILLAR_PROCESSOR = () -> PillarProcessor.CODEC;
    public static final StructureProcessorType<TagReplaceProcessor> TAG_REPLACE_PROCESSOR = () -> TagReplaceProcessor.CODEC;
    public static final StructureProcessorType<WaterloggingFixProcessor> WATERLOGGING_FIX_PROCESSOR = () -> WaterloggingFixProcessor.CODEC;

    public static void registerProcessors() {
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(Bumblezone.MODID, "bee_dungeon_processor"), BEE_DUNGEON_PROCESSOR);
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(Bumblezone.MODID, "spider_infested_bee_dungeon_processor"), SPIDER_INFESTED_BEE_DUNGEON_PROCESSOR);
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(Bumblezone.MODID, "replace_not_air_processor"), REPLACE_NOT_AIR_PROCESSOR);
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(Bumblezone.MODID, "close_off_outside_fluids_processor"), CLOSE_OFF_OUTSIDE_FLUIDS_PROCESSOR);
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(Bumblezone.MODID, "remove_floating_blocks_processor"), REMOVE_FLOATING_BLOCKS_PROCESSOR);
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(Bumblezone.MODID, "spawner_randomizing_processor"), SPAWNER_RANDOMIZING_PROCESSOR);
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(Bumblezone.MODID, "honeycomb_hole_processor"), HONEYCOMB_HOLE_PROCESSOR);
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(Bumblezone.MODID, "pollen_piling_processor"), POLLEN_PILING_PROCESSOR);
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(Bumblezone.MODID, "fluid_tick_processor"), FLUID_TICK_PROCESSOR);
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(Bumblezone.MODID, "replace_air_processor"), REPLACE_AIR_PROCESSOR);
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(Bumblezone.MODID, "noise_replace_with_properties_processor"), NOISE_REPLACE_WITH_PROPERTIES_PROCESSOR);
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(Bumblezone.MODID, "honeycomb_brood_randomize_processor"), HONEYCOMB_BROOD_RANDOMIZE_PROCESSOR);
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(Bumblezone.MODID, "random_replace_with_properties_processor"), RANDOM_REPLACE_WITH_PROPERTIES_PROCESSOR);
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(Bumblezone.MODID, "pillar_processor"), PILLAR_PROCESSOR);
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(Bumblezone.MODID, "tag_replace_processor"), TAG_REPLACE_PROCESSOR);
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(Bumblezone.MODID, "waterlogging_fix_processor"), WATERLOGGING_FIX_PROCESSOR);
    }
}
