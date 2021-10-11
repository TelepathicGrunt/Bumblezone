package com.telepathicgrunt.bumblezone.world.processors;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.modinit.BzProcessors;
import com.telepathicgrunt.bumblezone.utils.GeneralUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SpawnerBlock;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.List;
import java.util.Random;

public class SpawnerRandomizingProcessor extends StructureProcessor {

    public static final Codec<SpawnerRandomizingProcessor> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.mapPair(Registry.ENTITY_TYPE.fieldOf("resourcelocation"), Codec.intRange(1, Integer.MAX_VALUE).fieldOf("weight")).codec().listOf().fieldOf("spawner_mob_entries").forGetter(spawnerRandomizingProcessor -> spawnerRandomizingProcessor.spawnerRandomizingProcessor)
    ).apply(instance, instance.stable(SpawnerRandomizingProcessor::new)));

    public final List<Pair<EntityType<?>, Integer>> spawnerRandomizingProcessor;

    private SpawnerRandomizingProcessor(List<Pair<EntityType<?>, Integer>> spawnerRandomizingProcessor) {
        this.spawnerRandomizingProcessor = spawnerRandomizingProcessor;
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldView, BlockPos pos, BlockPos blockPos, StructureTemplate.StructureBlockInfo structureBlockInfoLocal, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings structurePlacementData) {
        if (structureBlockInfoWorld.state.getBlock() instanceof SpawnerBlock) {
            BlockPos worldPos = structureBlockInfoWorld.pos;
            Random random = new WorldgenRandom();
            random.setSeed(worldPos.asLong() * worldPos.getY());
            return new StructureTemplate.StructureBlockInfo(
                    worldPos,
                    structureBlockInfoWorld.state,
                    SetMobSpawnerEntity(random, structureBlockInfoWorld.nbt));
        }
        return structureBlockInfoWorld;
    }

    /**
     * Makes the given block entity now have the correct spawner mob
     */
    private CompoundTag SetMobSpawnerEntity(Random random, CompoundTag nbt) {
        EntityType<?> entity = GeneralUtils.getRandomEntry(spawnerRandomizingProcessor, random);
        if (entity != null) {
            CompoundTag compound = new CompoundTag();
            compound.putShort("Delay", (short) 20);
            compound.putShort("MinSpawnDelay", (short) 200);
            compound.putShort("MaxSpawnDelay", (short) 800);
            compound.putShort("SpawnCount", (short) 4);
            compound.putShort("MaxNearbyEntities", (short) 6);
            compound.putShort("RequiredPlayerRange", (short) 16);
            compound.putShort("SpawnRange", (short) 4);

            CompoundTag spawnData = new CompoundTag();
            spawnData.putString("id", Registry.ENTITY_TYPE.getKey(entity).toString());
            compound.put("SpawnData", spawnData);

            CompoundTag entityData = new CompoundTag();
            entityData.putString("id", Registry.ENTITY_TYPE.getKey(entity).toString());

            CompoundTag listEntry = new CompoundTag();
            listEntry.put("Entity", entityData);
            listEntry.putInt("Weight", 1);

            ListTag listnbt = new ListTag();
            listnbt.add(listEntry);

            compound.put("SpawnPotentials", listnbt);

            return compound;
        }
        else {
            Bumblezone.LOGGER.warn("EntityType in a dungeon does not exist in registry! : {}", spawnerRandomizingProcessor);
        }

        return nbt;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return BzProcessors.SPAWNER_RANDOMIZING_PROCESSOR;
    }
}
