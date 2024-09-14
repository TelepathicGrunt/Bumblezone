package com.telepathicgrunt.the_bumblezone.worldgen.structures.placements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.modinit.BzStructurePlacementType;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

import java.util.List;
import java.util.Optional;

public class AdvancedRandomSpread extends RandomSpreadStructurePlacement {
    public static final Codec<AdvancedRandomSpread> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Vec3i.offsetCodec(16).optionalFieldOf("locate_offset", Vec3i.ZERO).forGetter(AdvancedRandomSpread::locateOffset),
            StructurePlacement.FrequencyReductionMethod.CODEC.optionalFieldOf("frequency_reduction_method", StructurePlacement.FrequencyReductionMethod.DEFAULT).forGetter(AdvancedRandomSpread::frequencyReductionMethod),
            Codec.floatRange(0.0F, 1.0F).optionalFieldOf("frequency", 1.0F).forGetter(AdvancedRandomSpread::frequency),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("salt").forGetter(AdvancedRandomSpread::salt),
            StructurePlacement.ExclusionZone.CODEC.optionalFieldOf("exclusion_zone").forGetter(AdvancedRandomSpread::exclusionZone),
            SuperExclusionZone.CODEC.optionalFieldOf("super_exclusion_zone").forGetter(AdvancedRandomSpread::superExclusionZone),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("spacing").forGetter(AdvancedRandomSpread::spacing),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("separation").forGetter(AdvancedRandomSpread::separation),
            RandomSpreadType.CODEC.optionalFieldOf("spread_type", RandomSpreadType.LINEAR).forGetter(AdvancedRandomSpread::spreadType),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("min_distance_from_world_origin").forGetter(AdvancedRandomSpread::minDistanceFromWorldOrigin)
    ).apply(instance, instance.stable(AdvancedRandomSpread::new)));

    private final int spacing;
    private final int separation;
    private final RandomSpreadType spreadType;
    private final Optional<Integer> minDistanceFromWorldOrigin;
    private final Optional<SuperExclusionZone> superExclusionZone;

    public AdvancedRandomSpread(Vec3i locationOffset,
                                StructurePlacement.FrequencyReductionMethod frequencyReductionMethod,
                                float frequency,
                                int salt,
                                Optional<ExclusionZone> exclusionZone,
                                Optional<SuperExclusionZone> superExclusionZone,
                                int spacing,
                                int separation,
                                RandomSpreadType spreadType,
                                Optional<Integer> minDistanceFromWorldOrigin
    ) {
        super(locationOffset, frequencyReductionMethod, frequency, salt, exclusionZone, spacing, separation, spreadType);
        this.spacing = spacing;
        this.separation = separation;
        this.spreadType = spreadType;
        this.minDistanceFromWorldOrigin = minDistanceFromWorldOrigin;
        this.superExclusionZone = superExclusionZone;

        if (spacing <= separation) {
            throw new RuntimeException("""
                Repurposed Structures: Spacing cannot be less or equal to separation.
                Please correct this error as there's no way to spawn this structure properly
                    Spacing: %s
                    Separation: %s.
            """.formatted(spacing, separation));
        }
    }

    @Override
    public int spacing() {
        return this.spacing;
    }

    @Override
    public int separation() {
        return this.separation;
    }

    @Override
    public RandomSpreadType spreadType() {
        return this.spreadType;
    }

    public Optional<Integer> minDistanceFromWorldOrigin() {
        return this.minDistanceFromWorldOrigin;
    }

    public Optional<SuperExclusionZone> superExclusionZone() {
        return this.superExclusionZone;
    }

    @Override
    public boolean isStructureChunk(ChunkGeneratorStructureState chunkGeneratorStructureState, int i, int j) {
        if (!super.isStructureChunk(chunkGeneratorStructureState, i, j)) {
            return false;
        }
        else {
            return this.superExclusionZone.isEmpty() || !this.superExclusionZone.get().isPlacementForbidden(chunkGeneratorStructureState, i, j);
        }
    }

    @Override
    public ChunkPos getPotentialStructureChunk(long seed, int x, int z) {
        int regionX = Math.floorDiv(x, this.spacing);
        int regionZ = Math.floorDiv(z, this.spacing);
        WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(0L));
        worldgenrandom.setLargeFeatureWithSalt(seed, regionX, regionZ, this.salt());
        int diff = this.spacing - this.separation;
        int offsetX = this.spreadType.evaluate(worldgenrandom, diff);
        int offsetZ = this.spreadType.evaluate(worldgenrandom, diff);
        return new ChunkPos(regionX * this.spacing + offsetX, regionZ * this.spacing + offsetZ);
    }

    @Override
    protected boolean isPlacementChunk(ChunkGeneratorStructureState chunkGeneratorStructureState, int x, int z) {
        if (minDistanceFromWorldOrigin.isPresent()) {
            long xBlockPos = x * 16L;
            long zBlockPos = z * 16L;
            if ((xBlockPos * xBlockPos) + (zBlockPos * zBlockPos) <
                (((long) minDistanceFromWorldOrigin.get()) * minDistanceFromWorldOrigin.get()))
            {
                return false;
            }
        }

        ChunkPos chunkpos = this.getPotentialStructureChunk(chunkGeneratorStructureState.getLevelSeed(), x, z);
        return chunkpos.x == x && chunkpos.z == z;
    }

    @Override
    public StructurePlacementType<?> type() {
        return BzStructurePlacementType.ADVANCED_RANDOM_SPREAD.get();
    }

    public record SuperExclusionZone(List<AvoidData> avoidStructureData) {
        public static final Codec<AdvancedRandomSpread.SuperExclusionZone> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                Codec.list(AvoidData.CODEC).fieldOf("list_of_avoids").forGetter(AdvancedRandomSpread.SuperExclusionZone::avoidStructureData)
        ).apply(builder, AdvancedRandomSpread.SuperExclusionZone::new));


        boolean isPlacementForbidden(ChunkGeneratorStructureState chunkGeneratorStructureState, int l, int j) {
            for (AvoidData avoidData : this.avoidStructureData) {
                for (Holder<StructureSet> holder : avoidData.otherSet()) {
                    if (chunkGeneratorStructureState.hasStructureChunkInRange(holder, l, j, avoidData.chunkCount())) {
                        return true;
                    }
                }

                if (avoidData.allowedChunkCount().isPresent() && avoidData.allowedChunkCount().get() > avoidData.chunkCount()) {
                    boolean isAnyInRange = false;
                    for (Holder<StructureSet> holder : avoidData.otherSet()) {
                        if (chunkGeneratorStructureState.hasStructureChunkInRange(holder, l, j, avoidData.allowedChunkCount().get())) {
                            isAnyInRange = true;
                        }
                    }
                    if (!isAnyInRange) {
                        return false;
                    }
                }
            }

            return false;
        }

        private record AvoidData(HolderSet<StructureSet> otherSet, int chunkCount, Optional<Integer> allowedChunkCount) {
            public static final Codec<AvoidData> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                    RegistryCodecs.homogeneousList(Registries.STRUCTURE_SET, StructureSet.DIRECT_CODEC).fieldOf("other_set").forGetter(AvoidData::otherSet),
                    ExtraCodecs.POSITIVE_INT.fieldOf("chunk_count").forGetter(AvoidData::chunkCount),
                    ExtraCodecs.POSITIVE_INT.optionalFieldOf("allowed_chunk_count").forGetter(AvoidData::allowedChunkCount)
            ).apply(builder, AvoidData::new));
        }
    }
}