package com.telepathicgrunt.the_bumblezone.world.dimension;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.mixin.world.NoiseChunkAccessor;
import com.telepathicgrunt.the_bumblezone.mixin.world.NoiseGeneratorSettingsInvoker;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.utils.BzPlacingUtils;
import com.telepathicgrunt.the_bumblezone.utils.OpenSimplex2F;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Predicate;


public class BzChunkGenerator extends ChunkGenerator {

    public static final Codec<BzChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryOps.retrieveRegistry(Registry.STRUCTURE_SET_REGISTRY).forGetter(bzChunkGenerator -> bzChunkGenerator.structureSets),
            RegistryOps.retrieveRegistry(Registry.NOISE_REGISTRY).forGetter(bzChunkGenerator -> bzChunkGenerator.noises),
            BiomeSource.CODEC.fieldOf("biome_source").forGetter(bzChunkGenerator -> bzChunkGenerator.biomeSource),
            NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(bzChunkGenerator -> bzChunkGenerator.settings),
            RegistryOps.retrieveRegistry(Registry.BIOME_REGISTRY).forGetter((bzChunkGenerator) -> bzChunkGenerator.biomeRegistry))
    .apply(instance, instance.stable(BzChunkGenerator::new)));

    private static final BlockState[] EMPTY_COLUMN = new BlockState[0];
    protected final BlockState defaultBlock;
    protected final BlockState defaultFluid;
    private final Registry<NormalNoise.NoiseParameters> noises;
    protected final Holder<NoiseGeneratorSettings> settings;
    private final NoiseRouter router;
    private final Climate.Sampler sampler;
    private final Registry<Biome> biomeRegistry;
    private final Aquifer.FluidPicker globalFluidPicker;
    private static final MobSpawnSettings.SpawnerData INITIAL_HONEY_SLIME_ENTRY = new MobSpawnSettings.SpawnerData(BzEntities.HONEY_SLIME, 1, 1, 3);
    private static final MobSpawnSettings.SpawnerData INITIAL_BEE_ENTRY = new MobSpawnSettings.SpawnerData(EntityType.BEE, 1, 1, 4);
    private static final MobSpawnSettings.SpawnerData INITIAL_BEEHEMOTH_ENTRY = new MobSpawnSettings.SpawnerData(BzEntities.BEEHEMOTH, 1, 1, 1);

    public BzChunkGenerator(Registry<StructureSet> structureSetRegistry, Registry<NormalNoise.NoiseParameters> parametersRegistry, BiomeSource biomeSource, Holder<NoiseGeneratorSettings> supplier, Registry<Biome> biomeRegistry) {
        this(structureSetRegistry, parametersRegistry, biomeSource, biomeSource, supplier, biomeRegistry);
    }

    private BzChunkGenerator(Registry<StructureSet> structureSetRegistry, Registry<NormalNoise.NoiseParameters> parametersRegistry, BiomeSource biomeSource, BiomeSource biomeSource2, Holder<NoiseGeneratorSettings> supplier, Registry<Biome> biomeRegistry) {
        super(structureSetRegistry, Optional.empty(), biomeSource);
        this.noises = parametersRegistry;
        this.settings = supplier;
        this.biomeRegistry = biomeRegistry;
        NoiseGeneratorSettings noiseGeneratorSettings = this.settings.value();
        this.defaultBlock = noiseGeneratorSettings.defaultBlock();
        this.defaultFluid = noiseGeneratorSettings.defaultFluid();
        NoiseRouter noiseRouter = noiseGeneratorSettings.noiseRouter();
        this.router = noiseRouter;

        this.sampler = new Climate.Sampler(
                noiseRouter.temperature(),
                noiseRouter.vegetation(),
                noiseRouter.continents(),
                noiseRouter.erosion(),
                noiseRouter.depth(),
                noiseRouter.ridges(),
                noiseGeneratorSettings.spawnTarget());

//        DensityFunction newFinalDensity = DensityFunctions.add(
//                new BiomeNoise(this.sampler, this.biomeRegistry, this.getBiomeSource()),
//                noiseRouter.finalDensity()
//        );
//        newFinalDensity = DensityFunctions.interpolated(newFinalDensity);
//        newFinalDensity = DensityFunctions.add(
//                new RoughSurfaceNoise(),
//                newFinalDensity
//        );
//        newFinalDensity = DensityFunctions.interpolated(newFinalDensity);
//
//        this.router = new NoiseRouter(
//                noiseRouter.barrierNoise(),
//                noiseRouter.fluidLevelFloodednessNoise(),
//                noiseRouter.fluidLevelSpreadNoise(),
//                noiseRouter.lavaNoise(),
//                noiseRouter.temperature(),
//                noiseRouter.vegetation(),
//                noiseRouter.continents(),
//                noiseRouter.erosion(),
//                noiseRouter.depth(),
//                noiseRouter.ridges(),
//                noiseRouter.initialDensityWithoutJaggedness(),
//                newFinalDensity,
//                noiseRouter.veinToggle(),
//                noiseRouter.veinRidged(),
//                noiseRouter.veinGap()
//        );

        int seaLevel = noiseGeneratorSettings.seaLevel();
        Aquifer.FluidStatus fluidStatus = new Aquifer.FluidStatus(seaLevel, noiseGeneratorSettings.defaultFluid());
        this.globalFluidPicker = (x, y, z) -> fluidStatus;
    }

    record RoughSurfaceNoise() implements DensityFunction.SimpleFunction {
        private static final OpenSimplex2F noiseGen = new OpenSimplex2F(0);

        @Override
        public double compute(FunctionContext functionContext) {
            return (noiseGen.noise3_Classic(functionContext.blockX(), functionContext.blockY(), functionContext.blockZ()) / 100d) - 0.01d;
        }

        @Override
        public double minValue() {
            return 0;
        }

        @Override
        public double maxValue() {
            return 2;
        }

        @Override
        public KeyDispatchDataCodec<? extends DensityFunction> codec() {
            return null;
        }
    }

    record BiomeNoise(Climate.Sampler sampler, Registry<Biome> biomeRegistry, BiomeSource biomeSource) implements DensityFunction.SimpleFunction {
        @Override
        public double compute(FunctionContext functionContext) {
            return BiomeInfluencedNoiseSampler.calculateBaseNoise(
                    functionContext.blockX(),
                    functionContext.blockZ(),
                    this.sampler,
                    this.biomeSource,
                    this.biomeRegistry);
        }

        @Override
        public double minValue() {
            return 0;
        }

        @Override
        public double maxValue() {
            return 2;
        }

        @Override
        public KeyDispatchDataCodec<? extends DensityFunction> codec() {
            return null;
        }
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    public boolean stable(ResourceKey<NoiseGeneratorSettings> resourceKey) {
        return this.settings.is(resourceKey);
    }

    @Override
    public void applyCarvers(WorldGenRegion worldGenRegion, long seed, RandomState randomState, BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunkAccess, GenerationStep.Carving carving) {}

    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types types, LevelHeightAccessor levelHeightAccessor, RandomState randomState) {
        NoiseSettings noiseSettings = this.settings.value().noiseSettings();
        int maxY = Math.max(noiseSettings.minY(), levelHeightAccessor.getMinBuildHeight());
        int minY = Math.min(noiseSettings.minY() + noiseSettings.height(), levelHeightAccessor.getMaxBuildHeight());
        int maxYCell = Mth.intFloorDiv(maxY, noiseSettings.getCellHeight());
        int minYCell = Mth.intFloorDiv(minY - maxY, noiseSettings.getCellHeight());
        return this.iterateNoiseColumn(levelHeightAccessor, x, z, randomState, null, types.isOpaque(), maxYCell, minYCell).orElse(levelHeightAccessor.getMinBuildHeight());
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor levelHeightAccessor, RandomState randomState) {
        NoiseSettings noiseSettings = this.settings.value().noiseSettings();
        int minY = Math.max(noiseSettings.minY(), levelHeightAccessor.getMinBuildHeight());
        int maxY = Math.min(noiseSettings.minY() + noiseSettings.height(), levelHeightAccessor.getMaxBuildHeight());
        int minYCell = Mth.intFloorDiv(minY, noiseSettings.getCellHeight());
        int maxYCell = Mth.intFloorDiv(maxY - minY, noiseSettings.getCellHeight());
        if (maxYCell <= 0) {
            return new NoiseColumn(minY, EMPTY_COLUMN);
        }
        else {
            BlockState[] blockStates = new BlockState[maxYCell * noiseSettings.getCellHeight()];
            this.iterateNoiseColumn(levelHeightAccessor, x, z, randomState, blockStates, null, minYCell, maxYCell);
            return new NoiseColumn(minY, blockStates);
        }
    }

    @Override
    public void addDebugScreenInfo(List<String> list, RandomState randomState, BlockPos blockPos) {}

    private OptionalInt iterateNoiseColumn(LevelHeightAccessor levelHeightAccessor, int x, int z, RandomState randomState, @Nullable BlockState[] blockStates, @Nullable Predicate<BlockState> predicate, int minYCell, int maxYCell) {
        NoiseSettings noiseSettings = this.settings.value().noiseSettings().clampToHeightAccessor(levelHeightAccessor);
        int cellWidth = noiseSettings.getCellWidth();
        int cellHeight = noiseSettings.getCellHeight();
        int o = Math.floorDiv(x, cellWidth);
        int p = Math.floorDiv(z, cellWidth);
        int q = Math.floorMod(x, cellWidth);
        int r = Math.floorMod(z, cellWidth);
        int s = o * cellWidth;
        int t = p * cellWidth;
        double d = (double)q / (double)cellWidth;
        double e = (double)r / (double)cellWidth;
        NoiseChunk noiseChunk = new NoiseChunk(1, randomState, s, t, noiseSettings, DensityFunctions.BeardifierMarker.INSTANCE, this.settings.value(), this.globalFluidPicker, Blender.empty());
        noiseChunk.initializeForFirstCellX();
        noiseChunk.advanceCellX(0);

        for(int currentYCell = maxYCell - 1; currentYCell >= 0; --currentYCell) {
            noiseChunk.selectCellYZ(currentYCell, 0);

            for(int yInCell = cellHeight - 1; yInCell >= 0; --yInCell) {
                int y = (minYCell + currentYCell) * cellHeight + yInCell;
                double f = (double)yInCell / (double)cellHeight;
                noiseChunk.updateForY(y, f);
                noiseChunk.updateForX(x, d);
                noiseChunk.updateForZ(z, e);
                BlockState blockState = ((NoiseChunkAccessor)noiseChunk).callGetInterpolatedState();
                BlockState blockState2 = blockState == null ? this.defaultBlock : blockState;
                if((blockState == null || blockState.isAir()) && y < getSeaLevel()) {
                    blockState2 = this.defaultFluid;
                }

                if (blockStates != null) {
                    int index = currentYCell * cellHeight + yInCell;
                    blockStates[index] = blockState2;
                }

                if (predicate != null && predicate.test(blockState2)) {
                    noiseChunk.stopInterpolation();
                    return OptionalInt.of(y + 1);
                }
            }
        }

        return OptionalInt.empty();
    }

    @Override
    public void buildSurface(WorldGenRegion worldGenRegion, StructureManager structureManager, RandomState randomState, ChunkAccess chunkAccess) {
        if (!SharedConstants.debugVoidTerrain(chunkAccess.getPos())) {
            WorldGenerationContext worldgenerationcontext = new WorldGenerationContext(this, worldGenRegion);
            this.buildSurface(chunkAccess, worldgenerationcontext, randomState, structureManager, worldGenRegion.getBiomeManager(), worldGenRegion.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY), Blender.of(worldGenRegion));
        }
    }

    public void buildSurface(ChunkAccess chunkAccess, WorldGenerationContext worldGenerationContext, RandomState randomState, StructureManager structureManager, BiomeManager biomeManager, Registry<Biome> biomeRegistry, Blender blender) {
        NoiseChunk noisechunk = chunkAccess.getOrCreateNoiseChunk((noiseChunk) -> this.createNoiseChunk(noiseChunk, structureManager, blender, randomState));
        NoiseGeneratorSettings noisegeneratorsettings = this.settings.value();
        randomState.surfaceSystem().buildSurface(randomState, biomeManager, biomeRegistry, noisegeneratorsettings.useLegacyRandomSource(), worldGenerationContext, chunkAccess, noisechunk, noisegeneratorsettings.surfaceRule());
    }

    private NoiseChunk createNoiseChunk(ChunkAccess chunkAccess, StructureManager structureManager, Blender blender, RandomState randomState) {
        return NoiseChunk.forChunk(chunkAccess, randomState, Beardifier.forStructuresInChunk(structureManager, chunkAccess.getPos()), this.settings.value(), this.globalFluidPicker, blender);
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunkAccess) {
        NoiseSettings noiseSettings = this.settings.value().noiseSettings();
        LevelHeightAccessor levelHeightAccessor = chunkAccess.getHeightAccessorForGeneration();
        int minY = Math.max(noiseSettings.minY(), levelHeightAccessor.getMinBuildHeight());
        int maxY = Math.min(noiseSettings.minY() + noiseSettings.height(), levelHeightAccessor.getMaxBuildHeight());
        int minYCell = Mth.intFloorDiv(minY, noiseSettings.getCellHeight());
        int maxYCell = Mth.intFloorDiv(maxY - minY, noiseSettings.getCellHeight());
        if (maxYCell <= 0) {
            return CompletableFuture.completedFuture(chunkAccess);
        }
        else {
            int maxChunkSection = chunkAccess.getSectionIndex(maxYCell * noiseSettings.getCellHeight() - 1 + minY);
            int minChunkSection = chunkAccess.getSectionIndex(minY);
            Set<LevelChunkSection> set = Sets.newHashSet();

            for(int currentChunkSection = maxChunkSection; currentChunkSection >= minChunkSection; --currentChunkSection) {
                LevelChunkSection levelChunkSection = chunkAccess.getSection(currentChunkSection);
                levelChunkSection.acquire();
                set.add(levelChunkSection);
            }

            return CompletableFuture.supplyAsync(Util.wrapThreadWithTaskName("wgen_fill_noise", () -> this.doFill(blender, structureManager, randomState, chunkAccess, minYCell, maxYCell)), Util.backgroundExecutor()).whenCompleteAsync((chunkAccessx, throwable) -> {
                for(LevelChunkSection levelChunkSectionx : set) {
                    levelChunkSectionx.release();
                }

            }, executor);
        }
    }

    private ChunkAccess doFill(Blender blender, StructureManager structureManager, RandomState randomState, ChunkAccess chunkAccess, int minYCell, int maxYCell) {
        NoiseGeneratorSettings noiseGeneratorSettings = this.settings.value();
        NoiseChunk noiseChunk = chunkAccess.getOrCreateNoiseChunk((chunkAccess1) -> this.createNoiseChunk(chunkAccess1, structureManager,  blender, randomState));
        Heightmap heightmap = chunkAccess.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
        Heightmap heightmap2 = chunkAccess.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);
        ChunkPos chunkPos = chunkAccess.getPos();
        int k = chunkPos.getMinBlockX();
        int l = chunkPos.getMinBlockZ();
        Aquifer aquifer = noiseChunk.aquifer();
        noiseChunk.initializeForFirstCellX();
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        NoiseSettings noiseSettings = noiseGeneratorSettings.noiseSettings();
        int m = noiseSettings.getCellWidth();
        int n = noiseSettings.getCellHeight();
        int o = 16 / m;
        int p = 16 / m;

        for(int q = 0; q < o; ++q) {
            noiseChunk.advanceCellX(q);

            for(int r = 0; r < p; ++r) {
                LevelChunkSection levelChunkSection = chunkAccess.getSection(chunkAccess.getSectionsCount() - 1);

                for(int s = maxYCell - 1; s >= 0; --s) {
                    noiseChunk.selectCellYZ(s, r);

                    for(int t = n - 1; t >= 0; --t) {
                        int yy = (minYCell + s) * n + t;
                        int v = yy & 15;
                        int w = chunkAccess.getSectionIndex(yy);
                        if (chunkAccess.getSectionIndex(levelChunkSection.bottomBlockY()) != w) {
                            levelChunkSection = chunkAccess.getSection(w);
                        }

                        double d = (double)t / (double)n;

                        for(int x = 0; x < m; ++x) {
                            int xx = k + q * m + x;
                            int z = xx & 15;
                            double e = (double)x / (double)m;
                            noiseChunk.updateForX(xx, e);

                            for(int aa = 0; aa < m; ++aa) {
                                int zz = l + r * m + aa;
                                int ac = zz & 15;
                                double f = (double)aa / (double)m;
                                noiseChunk.updateForZ(zz, f);
                                noiseChunk.updateForY(yy, d);

                                BlockState blockState = ((NoiseChunkAccessor)noiseChunk).callGetInterpolatedState();
                                if (blockState == null) {
                                    blockState = this.defaultBlock;
                                }

                                if (blockState.isAir() && yy < this.getSeaLevel()) {
                                    blockState = this.defaultFluid;
                                }

                                if (!blockState.isAir() && !SharedConstants.debugVoidTerrain(chunkAccess.getPos())) {
                                    if (blockState.getLightEmission() != 0 && chunkAccess instanceof ProtoChunk) {
                                        mutableBlockPos.set(xx, yy, zz);
                                        ((ProtoChunk)chunkAccess).addLight(mutableBlockPos);
                                    }

                                    levelChunkSection.setBlockState(z, v, ac, blockState, false);
                                    heightmap.update(z, yy, ac, blockState);
                                    heightmap2.update(z, yy, ac, blockState);
                                    if (aquifer.shouldScheduleFluidUpdate() && !blockState.getFluidState().isEmpty()) {
                                        mutableBlockPos.set(xx, yy, zz);
                                        chunkAccess.markPosForPostprocessing(mutableBlockPos);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            noiseChunk.swapSlices();
        }

        noiseChunk.stopInterpolation();
        return chunkAccess;
    }

    @Override
    public int getGenDepth() {
        return this.settings.value().noiseSettings().height();
    }

    @Override
    public int getSeaLevel() {
        return this.settings.value().seaLevel();
    }

    @Override
    public int getMinY() {
        return this.settings.value().noiseSettings().minY();
    }

    /**
     * For spawning specific mobs in certain places like structures.
     */
    @Override
    public WeightedRandomList<MobSpawnSettings.SpawnerData> getMobsAt(Holder<Biome> biome, StructureManager accessor, MobCategory group, BlockPos pos) {
        return super.getMobsAt(biome, accessor, group, pos);
    }

    /**
     * Dedicated to spawning slimes/bees when generating chunks initially.
     * This is so there's lots of bees and the slime can spawn despite the
     * slime having extremely restrictive spawning mechanics.
     * <p>
     * Also spawns bees with chance to bee full of pollen
     * <p>
     * This is mainly vanilla code but with biome$spawnlistentry changed to
     * use bee/slime and the restrictive terrain check called on the entity removed.
     * The height is also restricted so the mob cannot spawn on the ceiling of this
     * dimension as well.
     */
    @Override
    @SuppressWarnings("deprecation")
    public void spawnOriginalMobs(WorldGenRegion region) {
        NoiseGeneratorSettings noiseGeneratorSettings = this.settings.value();
        if (!((NoiseGeneratorSettingsInvoker)(Object)noiseGeneratorSettings).thebumblezone_callDisableMobGeneration()) {
            ChunkPos chunkPos = region.getCenter();
            Biome biome = region.getBiome(chunkPos.getWorldPosition()).value();
            WorldgenRandom sharedseedrandom = new WorldgenRandom(new LegacyRandomSource(RandomSupport.generateUniqueSeed()));
            sharedseedrandom.setDecorationSeed(region.getSeed(), chunkPos.getMinBlockX(), chunkPos.getMinBlockZ());
            while (sharedseedrandom.nextFloat() < biome.getMobSettings().getCreatureProbability()) {
                //15% of time, spawn honey slime. Otherwise, spawn bees.
                MobSpawnSettings.SpawnerData biome$spawnlistentry;
                float threshold = sharedseedrandom.nextFloat();
                if(threshold < 0.15f) {
                    biome$spawnlistentry = INITIAL_HONEY_SLIME_ENTRY;
                }
                else if (threshold < 0.95f) {
                    biome$spawnlistentry = INITIAL_BEE_ENTRY;
                }
                else {
                    biome$spawnlistentry = INITIAL_BEEHEMOTH_ENTRY;
                }

                int startingX = chunkPos.getMinBlockX() + sharedseedrandom.nextInt(16);
                int startingZ = chunkPos.getMinBlockZ() + sharedseedrandom.nextInt(16);

                BlockPos.MutableBlockPos blockpos = new BlockPos.MutableBlockPos(startingX, 0, startingZ);
                int height = BzPlacingUtils.topOfSurfaceBelowHeight(region, sharedseedrandom.nextInt(255), -1, blockpos) + 1;

                if (biome$spawnlistentry.type.canSummon() && height > 0 && height < 255) {
                    float width = biome$spawnlistentry.type.getWidth();
                    double xLength = Mth.clamp(startingX, (double) chunkPos.getMinBlockX() + (double) width, (double) chunkPos.getMinBlockX() + 16.0D - (double) width);
                    double zLength = Mth.clamp(startingZ, (double) chunkPos.getMinBlockZ() + (double) width, (double) chunkPos.getMinBlockZ() + 16.0D - (double) width);

                    Entity entity = biome$spawnlistentry.type.create(region.getLevel());
                    if(entity == null)
                        continue;

                    entity.moveTo(xLength, height, zLength, sharedseedrandom.nextFloat() * 360.0F, 0.0F);
                    if (entity instanceof Mob mobEntity) {
                        if (mobEntity.checkSpawnRules(region, MobSpawnType.CHUNK_GENERATION) && mobEntity.checkSpawnObstruction(region)) {
                            mobEntity.finalizeSpawn(region, region.getCurrentDifficultyAt(new BlockPos(mobEntity.position())), MobSpawnType.CHUNK_GENERATION, null, null);
                            region.addFreshEntity(mobEntity);
                        }
                    }
                }
            }
        }
    }
}