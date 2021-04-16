package com.telepathicgrunt.the_bumblezone.world.processors;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzProcessors;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.block.SpawnerBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

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
    public Template.BlockInfo func_230386_a_(IWorldReader worldView, BlockPos pos, BlockPos blockPos, Template.BlockInfo structureBlockInfoLocal, Template.BlockInfo structureBlockInfoWorld, PlacementSettings structurePlacementData) {
        if (structureBlockInfoWorld.state.getBlock() instanceof SpawnerBlock) {
            BlockPos worldPos = structureBlockInfoWorld.pos;
            Random random = new SharedSeedRandom();
            random.setSeed(worldPos.toLong() * worldPos.getY());
            return new Template.BlockInfo(
                    worldPos,
                    structureBlockInfoWorld.state,
                    SetMobSpawnerEntity(random, structureBlockInfoWorld.nbt));
        }
        return structureBlockInfoWorld;
    }

    /**
     * Makes the given block entity now have the correct spawner mob
     */
    private CompoundNBT SetMobSpawnerEntity(Random random, CompoundNBT nbt) {
        EntityType<?> entity = GeneralUtils.getRandomEntry(spawnerRandomizingProcessor, random);
        if (entity != null) {
            CompoundNBT compound = new CompoundNBT();
            compound.putShort("Delay", (short) 20);
            compound.putShort("MinSpawnDelay", (short) 200);
            compound.putShort("MaxSpawnDelay", (short) 800);
            compound.putShort("SpawnCount", (short) 4);
            compound.putShort("MaxNearbyEntities", (short) 6);
            compound.putShort("RequiredPlayerRange", (short) 16);
            compound.putShort("SpawnRange", (short) 4);

            CompoundNBT spawnData = new CompoundNBT();
            spawnData.putString("id", Registry.ENTITY_TYPE.getKey(entity).toString());
            compound.put("SpawnData", spawnData);

            CompoundNBT entityData = new CompoundNBT();
            entityData.putString("id", Registry.ENTITY_TYPE.getKey(entity).toString());

            CompoundNBT listEntry = new CompoundNBT();
            listEntry.put("Entity", entityData);
            listEntry.putInt("Weight", 1);

            ListNBT listnbt = new ListNBT();
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
    protected IStructureProcessorType<?> getType() {
        return BzProcessors.SPAWNER_RANDOMIZING_PROCESSOR;
    }
}
