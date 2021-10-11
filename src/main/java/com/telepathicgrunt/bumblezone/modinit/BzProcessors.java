package com.telepathicgrunt.bumblezone.modinit;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.world.processors.BeeDungeonProcessor;
import com.telepathicgrunt.bumblezone.world.processors.CloseOffOutsideFluidsProcessor;
import com.telepathicgrunt.bumblezone.world.processors.FluidTickProcessor;
import com.telepathicgrunt.bumblezone.world.processors.HoneycombHoleProcessor;
import com.telepathicgrunt.bumblezone.world.processors.PollenPilingProcessor;
import com.telepathicgrunt.bumblezone.world.processors.RemoveFloatingBlocksProcessor;
import com.telepathicgrunt.bumblezone.world.processors.ReplaceNotAirProcessor;
import com.telepathicgrunt.bumblezone.world.processors.SpawnerRandomizingProcessor;
import com.telepathicgrunt.bumblezone.world.processors.SpiderInfestedBeeDungeonProcessor;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class BzProcessors {

    public static StructureProcessorType<BeeDungeonProcessor> BEE_DUNGEON_PROCESSOR = () -> BeeDungeonProcessor.CODEC;
    public static StructureProcessorType<SpiderInfestedBeeDungeonProcessor> SPIDER_INFESTED_BEE_DUNGEON_PROCESSOR = () -> SpiderInfestedBeeDungeonProcessor.CODEC;
    public static StructureProcessorType<ReplaceNotAirProcessor> REPLACE_NOT_AIR_PROCESSOR = () -> ReplaceNotAirProcessor.CODEC;
    public static StructureProcessorType<CloseOffOutsideFluidsProcessor> CLOSE_OFF_OUTSIDE_FLUIDS_PROCESSOR = () -> CloseOffOutsideFluidsProcessor.CODEC;
    public static StructureProcessorType<RemoveFloatingBlocksProcessor> REMOVE_FLOATING_BLOCKS_PROCESSOR = () -> RemoveFloatingBlocksProcessor.CODEC;
    public static StructureProcessorType<SpawnerRandomizingProcessor> SPAWNER_RANDOMIZING_PROCESSOR = () -> SpawnerRandomizingProcessor.CODEC;
    public static StructureProcessorType<HoneycombHoleProcessor> HONEYCOMB_HOLE_PROCESSOR = () -> HoneycombHoleProcessor.CODEC;
    public static StructureProcessorType<PollenPilingProcessor> POLLEN_PILING_PROCESSOR = () -> PollenPilingProcessor.CODEC;
    public static StructureProcessorType<FluidTickProcessor> FLUID_TICK_PROCESSOR = () -> FluidTickProcessor.CODEC;

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
    }
}
