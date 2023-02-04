package com.telepathicgrunt.the_bumblezone.world.structures;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.modinit.BzStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.Optional;

public class HoneyCaveRoomStructure extends Structure {

    public static final Codec<HoneyCaveRoomStructure> CODEC = RecordCodecBuilder.<HoneyCaveRoomStructure>mapCodec(instance ->
            instance.group(PollinatedStreamStructure.settingsCodec(instance),
                    StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool),
                    ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter(structure -> structure.startJigsawName),
                    Codec.intRange(0, 30).fieldOf("size").forGetter(structure -> structure.size),
                    HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> structure.startHeight),
                    Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(structure -> structure.projectStartToHeightmap),
                    Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter(structure -> structure.maxDistanceFromCenter)
            ).apply(instance, HoneyCaveRoomStructure::new)).codec();

    private final Holder<StructureTemplatePool> startPool;
    private final Optional<ResourceLocation> startJigsawName;
    private final int size;
    private final HeightProvider startHeight;
    private final Optional<Heightmap.Types> projectStartToHeightmap;
    private final int maxDistanceFromCenter;

    public HoneyCaveRoomStructure(Structure.StructureSettings config,
                                     Holder<StructureTemplatePool> startPool,
                                     Optional<ResourceLocation> startJigsawName,
                                     int size,
                                     HeightProvider startHeight,
                                     Optional<Heightmap.Types> projectStartToHeightmap,
                                     int maxDistanceFromCenter)
    {
        super(config);
        this.startPool = startPool;
        this.startJigsawName = startJigsawName;
        this.size = size;
        this.startHeight = startHeight;
        this.projectStartToHeightmap = projectStartToHeightmap;
        this.maxDistanceFromCenter = maxDistanceFromCenter;
    }

    private static boolean validSpot(ChunkGenerator chunkGenerator, BlockPos centerPos, LevelHeightAccessor heightLimitView, RandomState randomState) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        int radius = 20;
        for(int x = -radius; x <= radius; x += radius*2) {
            for(int z = -radius; z <= radius; z += radius*2) {
                mutable.set(centerPos).move(x, 0, z);
                NoiseColumn columnOfBlocks = chunkGenerator.getBaseColumn(mutable.getX(), mutable.getZ(), heightLimitView, randomState);
                BlockState state = columnOfBlocks.getBlock(mutable.getY() + 2);
                BlockState aboveState = columnOfBlocks.getBlock(mutable.getY() + 17);
                if(state.isAir() || !state.getFluidState().isEmpty() ||
                    aboveState.isAir() || !aboveState.getFluidState().isEmpty())
                {
                    return false;
                }
            }
        }

        return true;
    }

    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        WorldgenRandom positionedRandom = new WorldgenRandom(new LegacyRandomSource(context.seed() + (context.chunkPos().x * (context.chunkPos().z * 17L))));
        int height = context.chunkGenerator().getSeaLevel() + positionedRandom.nextInt(Math.max(context.chunkGenerator().getGenDepth() - (context.chunkGenerator().getSeaLevel() + 50), 1));
        BlockPos centerPos = new BlockPos(context.chunkPos().getMinBlockX(), height, context.chunkPos().getMinBlockZ());

        if(!validSpot(context.chunkGenerator(), centerPos, context.heightAccessor(), context.randomState())) {
            return Optional.empty();
        }

        return OptimizedJigsawManager.assembleJigsawStructure(
                context,
                this.startPool,
                this.size,
                context.registryAccess().registryOrThrow(Registries.STRUCTURE).getKey(this),
                centerPos,
                false,
                this.projectStartToHeightmap,
                this.maxDistanceFromCenter,
                (structurePiecesBuilder, pieces) -> {});
    }

    @Override
    public StructureType<?> type() {
        return BzStructures.HONEY_CAVE_ROOM.get();
    }
}