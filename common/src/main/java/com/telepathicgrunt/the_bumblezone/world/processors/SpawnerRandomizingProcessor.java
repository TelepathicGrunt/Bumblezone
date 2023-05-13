package com.telepathicgrunt.the_bumblezone.world.processors;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.modinit.BzProcessors;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SpawnerBlock;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.List;
import java.util.Optional;

public class SpawnerRandomizingProcessor extends StructureProcessor {

    public static final Codec<SpawnerRandomizingProcessor> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.mapPair(BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("resourcelocation"), Codec.intRange(1, Integer.MAX_VALUE).fieldOf("weight")).codec().listOf().fieldOf("spawner_mob_entries").forGetter(spawnerRandomizingProcessor -> spawnerRandomizingProcessor.mainSpawnerData),
            RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE, BuiltInRegistries.ENTITY_TYPE.byNameCodec()).optionalFieldOf("override_mobs_to_pick_from").forGetter(spawnerRandomizingProcessor -> spawnerRandomizingProcessor.overrideMobsToPickFrom),
            Codec.floatRange(0, 1).fieldOf("chance_to_override_with_tagged_mobs").orElse(0F).forGetter(spawnerRandomizingProcessor -> spawnerRandomizingProcessor.chanceToOverrideWithTaggedMobs)
    ).apply(instance, instance.stable(SpawnerRandomizingProcessor::new)));

    public final List<Pair<EntityType<?>, Integer>> mainSpawnerData;
    public final Optional<HolderSet<EntityType<?>>> overrideMobsToPickFrom;
    public final float chanceToOverrideWithTaggedMobs;

    private SpawnerRandomizingProcessor(List<Pair<EntityType<?>, Integer>> mainSpawnerData, Optional<HolderSet<EntityType<?>>> overrideMobsToPickFrom, float chanceToOverrideWithTaggedMobs) {
        this.mainSpawnerData = mainSpawnerData;
        this.overrideMobsToPickFrom = overrideMobsToPickFrom;
        this.chanceToOverrideWithTaggedMobs = chanceToOverrideWithTaggedMobs;
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldView, BlockPos pos, BlockPos blockPos, StructureTemplate.StructureBlockInfo structureBlockInfoLocal, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings structurePlacementData) {
        if (structureBlockInfoWorld.state().getBlock() instanceof SpawnerBlock) {
            BlockPos worldPos = structureBlockInfoWorld.pos();
            RandomSource randomSource = structurePlacementData.getRandom(worldPos);

            CompoundTag newSpawnerData;
            if (overrideMobsToPickFrom.isPresent() && overrideMobsToPickFrom.get().size() > 0 && randomSource.nextFloat() < chanceToOverrideWithTaggedMobs) {
                newSpawnerData = SetMobSpawnerEntity(overrideMobsToPickFrom.get().get(randomSource.nextInt(overrideMobsToPickFrom.get().size())).value(), structureBlockInfoWorld.nbt());
            }
            else if (mainSpawnerData.size() > 0) {
                newSpawnerData = SetMobSpawnerEntity(GeneralUtils.getRandomEntry(mainSpawnerData, randomSource), structureBlockInfoWorld.nbt());
            }
            else {
                return structureBlockInfoWorld;
            }

            return new StructureTemplate.StructureBlockInfo(
                    worldPos,
                    structureBlockInfoWorld.state(),
                    newSpawnerData);
        }
        return structureBlockInfoWorld;
    }

    /**
     * Makes the given block entity now have the correct spawner mob
     */
    private CompoundTag SetMobSpawnerEntity(EntityType<?> entity, CompoundTag nbt) {
        if(entity != null) {
            if(nbt != null) {
                CompoundTag spawnDataTag = nbt.getCompound("SpawnData");
                if(spawnDataTag.isEmpty()) {
                    spawnDataTag = new CompoundTag();
                    nbt.put("SpawnData", spawnDataTag);
                }
                CompoundTag entityTag = nbt.getCompound("entity");
                if(entityTag.isEmpty()) {
                    entityTag = new CompoundTag();
                    spawnDataTag.put("entity", entityTag);
                }
                entityTag.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(entity).toString());

                CompoundTag spawnEntityDataTag = new CompoundTag();
                spawnEntityDataTag.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(entity).toString());
                CompoundTag spawnPotentialDataEntryTag = new CompoundTag();
                spawnPotentialDataEntryTag.put("entity", spawnEntityDataTag);
                CompoundTag spawnPotentialEntryTag = new CompoundTag();
                spawnPotentialEntryTag.put("data", spawnPotentialDataEntryTag);
                spawnPotentialEntryTag.put("weight", IntTag.valueOf(1));
                nbt.put("SpawnPotentials", new ListTag());
                return nbt;
            }
            else {
                CompoundTag compound = new CompoundTag();
                compound.putShort("Delay", (short) 20);
                compound.putShort("MinSpawnDelay", (short) 200);
                compound.putShort("MaxSpawnDelay", (short) 800);
                compound.putShort("SpawnCount", (short) 4);
                compound.putShort("MaxNearbyEntities", (short) 6);
                compound.putShort("RequiredPlayerRange", (short) 16);
                compound.putShort("SpawnRange", (short) 4);

                CompoundTag spawnDataEntity = new CompoundTag();
                spawnDataEntity.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(entity).toString());
                CompoundTag spawnData = new CompoundTag();
                spawnData.put("entity", spawnDataEntity);
                compound.put("SpawnData", spawnData);

                CompoundTag entityData = new CompoundTag();
                entityData.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(entity).toString());

                CompoundTag spawnPotentialData = new CompoundTag();
                spawnPotentialData.put("entity", entityData);
                CompoundTag listEntry = new CompoundTag();
                listEntry.put("data", spawnPotentialData);
                listEntry.putInt("weight", 1);
                compound.put("SpawnPotentials", new ListTag());

                return compound;
            }
        }

        return nbt;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return BzProcessors.SPAWNER_RANDOMIZING_PROCESSOR.get();
    }
}
