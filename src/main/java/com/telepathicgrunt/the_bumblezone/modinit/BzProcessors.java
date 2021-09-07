package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.world.processors.BeeDungeonProcessor;
import com.telepathicgrunt.the_bumblezone.world.processors.FluidTickProcessor;
import com.telepathicgrunt.the_bumblezone.world.processors.HoneycombHoleProcessor;
import com.telepathicgrunt.the_bumblezone.world.processors.PollenPilingProcessor;
import com.telepathicgrunt.the_bumblezone.world.processors.RemoveFloatingBlocksProcessor;
import com.telepathicgrunt.the_bumblezone.world.processors.ReplaceNotAirProcessor;
import com.telepathicgrunt.the_bumblezone.world.processors.SpawnerRandomizingProcessor;
import com.telepathicgrunt.the_bumblezone.world.processors.SpiderInfestedBeeDungeonProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;

public class BzProcessors {

    public static IStructureProcessorType<BeeDungeonProcessor> BEE_DUNGEON_PROCESSOR = () -> BeeDungeonProcessor.CODEC;
    public static IStructureProcessorType<SpiderInfestedBeeDungeonProcessor> SPIDER_INFESTED_BEE_DUNGEON_PROCESSOR = () -> SpiderInfestedBeeDungeonProcessor.CODEC;
    public static IStructureProcessorType<ReplaceNotAirProcessor> REPLACE_NOT_AIR_PROCESSOR = () -> ReplaceNotAirProcessor.CODEC;
    public static IStructureProcessorType<RemoveFloatingBlocksProcessor> REMOVE_FLOATING_BLOCKS_PROCESSOR = () -> RemoveFloatingBlocksProcessor.CODEC;
    public static IStructureProcessorType<SpawnerRandomizingProcessor> SPAWNER_RANDOMIZING_PROCESSOR = () -> SpawnerRandomizingProcessor.CODEC;
    public static IStructureProcessorType<HoneycombHoleProcessor> HONEYCOMB_HOLE_PROCESSOR = () -> HoneycombHoleProcessor.CODEC;
    public static IStructureProcessorType<PollenPilingProcessor> POLLEN_PILING_PROCESSOR = () -> PollenPilingProcessor.CODEC;
    public static IStructureProcessorType<FluidTickProcessor> FLUID_TICK_PROCESSOR = () -> FluidTickProcessor.CODEC;

    public static void registerProcessors() {
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(Bumblezone.MODID, "bee_dungeon_processor"), BEE_DUNGEON_PROCESSOR);
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(Bumblezone.MODID, "spider_infested_bee_dungeon_processor"), SPIDER_INFESTED_BEE_DUNGEON_PROCESSOR);
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(Bumblezone.MODID, "replace_not_air_processor"), REPLACE_NOT_AIR_PROCESSOR);
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(Bumblezone.MODID, "remove_floating_blocks_processor"), REMOVE_FLOATING_BLOCKS_PROCESSOR);
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(Bumblezone.MODID, "spawner_randomizing_processor"), SPAWNER_RANDOMIZING_PROCESSOR);
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(Bumblezone.MODID, "honeycomb_hole_processor"), HONEYCOMB_HOLE_PROCESSOR);
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(Bumblezone.MODID, "pollen_piling_processor"), POLLEN_PILING_PROCESSOR);
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(Bumblezone.MODID, "fluid_tick_processor"), FLUID_TICK_PROCESSOR);
    }
}
