package com.telepathicgrunt.bumblezone.modinit;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.world.processors.*;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BzProcessors {

    public static StructureProcessorType<BeeDungeonProcessor> BEE_DUNGEON_PROCESSOR = () -> BeeDungeonProcessor.CODEC;
    public static StructureProcessorType<SpiderInfestedBeeDungeonProcessor> SPIDER_INFESTED_BEE_DUNGEON_PROCESSOR = () -> SpiderInfestedBeeDungeonProcessor.CODEC;
    public static StructureProcessorType<ReplaceNotAirProcessor> REPLACE_NOT_AIR_PROCESSOR = () -> ReplaceNotAirProcessor.CODEC;
    public static StructureProcessorType<RemoveFloatingBlocksProcessor> REMOVE_FLOATING_BLOCKS_PROCESSOR = () -> RemoveFloatingBlocksProcessor.CODEC;
    public static StructureProcessorType<SpawnerRandomizingProcessor> SPAWNER_RANDOMIZING_PROCESSOR = () -> SpawnerRandomizingProcessor.CODEC;

    public static void registerProcessors() {
        Registry.register(Registry.STRUCTURE_PROCESSOR, new Identifier(Bumblezone.MODID, "bee_dungeon_processor"), BEE_DUNGEON_PROCESSOR);
        Registry.register(Registry.STRUCTURE_PROCESSOR, new Identifier(Bumblezone.MODID, "spider_infested_bee_dungeon_processor"), SPIDER_INFESTED_BEE_DUNGEON_PROCESSOR);
        Registry.register(Registry.STRUCTURE_PROCESSOR, new Identifier(Bumblezone.MODID, "replace_not_air_processor"), REPLACE_NOT_AIR_PROCESSOR);
        Registry.register(Registry.STRUCTURE_PROCESSOR, new Identifier(Bumblezone.MODID, "remove_floating_blocks_processor"), REMOVE_FLOATING_BLOCKS_PROCESSOR);
        Registry.register(Registry.STRUCTURE_PROCESSOR, new Identifier(Bumblezone.MODID, "spawner_randomizing_processor"), SPAWNER_RANDOMIZING_PROCESSOR);
    }
}
