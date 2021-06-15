package com.telepathicgrunt.bumblezone.world.processors;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.modinit.BzProcessors;
import com.telepathicgrunt.bumblezone.utils.GeneralUtils;
import net.minecraft.block.SpawnerBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.ChunkRandom;

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
    public Structure.StructureBlockInfo process(WorldView worldView, BlockPos pos, BlockPos blockPos, Structure.StructureBlockInfo structureBlockInfoLocal, Structure.StructureBlockInfo structureBlockInfoWorld, StructurePlacementData structurePlacementData) {
        if (structureBlockInfoWorld.state.getBlock() instanceof SpawnerBlock) {
            BlockPos worldPos = structureBlockInfoWorld.pos;
            Random random = new ChunkRandom();
            random.setSeed(worldPos.asLong() * worldPos.getY());
            return new Structure.StructureBlockInfo(
                    worldPos,
                    structureBlockInfoWorld.state,
                    SetMobSpawnerEntity(random, structureBlockInfoWorld.nbt));
        }
        return structureBlockInfoWorld;
    }

    /**
     * Makes the given block entity now have the correct spawner mob
     */
    private NbtCompound SetMobSpawnerEntity(Random random, NbtCompound nbt) {
        EntityType<?> entity = GeneralUtils.getRandomEntry(spawnerRandomizingProcessor, random);
        if (entity != null) {
            NbtCompound compound = new NbtCompound();
            compound.putShort("Delay", (short) 20);
            compound.putShort("MinSpawnDelay", (short) 200);
            compound.putShort("MaxSpawnDelay", (short) 800);
            compound.putShort("SpawnCount", (short) 4);
            compound.putShort("MaxNearbyEntities", (short) 6);
            compound.putShort("RequiredPlayerRange", (short) 16);
            compound.putShort("SpawnRange", (short) 4);

            NbtCompound spawnData = new NbtCompound();
            spawnData.putString("id", Registry.ENTITY_TYPE.getId(entity).toString());
            compound.put("SpawnData", spawnData);

            NbtCompound entityData = new NbtCompound();
            entityData.putString("id", Registry.ENTITY_TYPE.getId(entity).toString());

            NbtCompound listEntry = new NbtCompound();
            listEntry.put("Entity", entityData);
            listEntry.putInt("Weight", 1);

            NbtList listnbt = new NbtList();
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
