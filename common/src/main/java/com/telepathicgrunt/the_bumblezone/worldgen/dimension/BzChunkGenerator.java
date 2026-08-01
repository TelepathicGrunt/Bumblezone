package com.telepathicgrunt.the_bumblezone.worldgen.dimension;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.mixin.world.NoiseChunkAccessor;
import com.telepathicgrunt.the_bumblezone.mixin.world.NoiseGeneratorSettingsAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlockEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.services.PlatformService;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeResolver;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.NoiseRouterData;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.blending.Blender;
import org.apache.commons.lang3.mutable.MutableObject;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BzChunkGenerator extends NoiseBasedChunkGenerator {

    public static final MapCodec<BzChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    BiomeSource.CODEC.fieldOf("biome_source").forGetter(bzChunkGenerator -> bzChunkGenerator.biomeSource),
                    NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(bzChunkGenerator -> bzChunkGenerator.settings))
            .apply(instance, instance.stable(BzChunkGenerator::new)));

    protected final BlockState defaultBlock;
    protected final BlockState defaultFluid;
    private final Holder<NoiseGeneratorSettings> settings;
    private final Aquifer.FluidPicker globalFluidPicker;

    public BzChunkGenerator(BiomeSource biomeSource,
                            Holder<NoiseGeneratorSettings> supplier
    ) {
        super(biomeSource, supplier);
        NoiseGeneratorSettings noiseGeneratorSettings = supplier.value();
        this.defaultBlock = noiseGeneratorSettings.defaultBlock();
        this.defaultFluid = noiseGeneratorSettings.defaultFluid();

        NoiseRouter noiseRouter = noiseGeneratorSettings.noiseRouter();
        Climate.Sampler sampler = new Climate.Sampler(
                noiseRouter.temperature(),
                noiseRouter.vegetation(),
                noiseRouter.continents(),
                noiseRouter.erosion(),
                noiseRouter.depth(),
                noiseRouter.ridges(),
                noiseGeneratorSettings.spawnTarget()
        );
        ((NoiseGeneratorSettingsAccessor) (Object) noiseGeneratorSettings).bumblezone$setNoiseRouter(
                noiseRouter.mapAll(densityFunction -> {
                    if (densityFunction instanceof BiomeNoise) {
                        return new BiomeNoise(
                                this::getBiomeSource,
                                () -> sampler
                        );
                    } else {
                        return densityFunction;
                    }
                })
        );

        this.settings = supplier;

        int seaLevel = noiseGeneratorSettings.seaLevel();
        Aquifer.FluidStatus sea = new Aquifer.FluidStatus(seaLevel, noiseGeneratorSettings.defaultFluid());
        this.globalFluidPicker = (x, y, z) -> sea;
    }

    public record BiomeNoise(Supplier<BiomeSource> biomeSource,
                             Supplier<Climate.Sampler> sampler) implements DensityFunction.SimpleFunction
    {
        public static final KeyDispatchDataCodec<BiomeNoise> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(new BiomeNoise(null, null)));

        @Override
        public double compute(FunctionContext functionContext) {
            if (this.biomeSource == null || this.biomeSource.get() == null || this.sampler == null) {
                throw new IllegalStateException("Attempting to sample uninitialized BzChunkGenerator$BiomeNoise");
            }
            return BiomeInfluencedNoiseSampler.calculateBaseNoise(
                    functionContext.blockX(),
                    functionContext.blockZ(),
                    this.sampler.get(),
                    this.biomeSource.get(),
                    BiomeRegistryHolder.BIOME_REGISTRY);
        }

        @Override
        public double minValue() {
            return -10;
        }

        @Override
        public double maxValue() {
            return 10;
        }

        @Override
        public KeyDispatchDataCodec<? extends DensityFunction> codec() {
            return CODEC;
        }
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    protected void doCreateBiomes(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunkAccess) {
        NoiseChunk noiseChunk = chunkAccess.getOrCreateNoiseChunk((chunkAccess1) -> this.createNoiseChunk(chunkAccess1, structureManager, blender, randomState));
        Climate.Sampler sampler = ((NoiseChunkExtension) noiseChunk).the_bumblezone$getCachedClimateSampler();
        BiomeResolver biomeresolver = getBiomeResolver(((NoiseChunkExtension) noiseChunk).the_bumblezone$getBiomeSource());
        chunkAccess.fillBiomesFromNoise(biomeresolver, sampler);
    }

    private BiomeResolver getBiomeResolver(BiomeResolver biomeResolver) {
        return (x, y, z, biomeHolder) -> biomeResolver.getNoiseBiome(x, 0, z, biomeHolder);
    }

    @Override
    protected NoiseChunk createNoiseChunk(ChunkAccess chunkAccess, StructureManager structureManager, Blender blender, RandomState randomState) {
        NoiseChunk noiseChunk = super.createNoiseChunk(chunkAccess, structureManager, blender, randomState);
        postInitNoiseChunk(randomState, noiseChunk);
        return noiseChunk;
    }

    private void postInitNoiseChunk(RandomState randomState, NoiseChunk noiseChunk) {
        if (this.biomeSource instanceof BzBiomeSource bzBiomeSource) {
            ((NoiseChunkExtension) noiseChunk).the_bumblezone$setBiomeSource(new BzBiomeSource(bzBiomeSource));
        } else {
            ((NoiseChunkExtension) noiseChunk).the_bumblezone$setBiomeSource(this.biomeSource);
        }
        ((NoiseChunkExtension) noiseChunk).the_bumblezone$setCachedClimateSampler(((NoiseChunkAccessor) noiseChunk).bumblezone$callCachedClimateSampler(randomState.router(), this.settings.value().spawnTarget()));
    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types types, LevelHeightAccessor levelHeightAccessor, RandomState randomState) {
        return this.iterateNoiseColumn(levelHeightAccessor, randomState, x, z, null, types.isOpaque())
                .orElse(levelHeightAccessor.getMinY());
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor levelHeightAccessor, RandomState randomState) {
        MutableObject<NoiseColumn> mutableobject = new MutableObject<>();
        this.iterateNoiseColumn(levelHeightAccessor, randomState, x, z, mutableobject, null);
        return mutableobject.getValue();
    }

    @Override
    public void addDebugScreenInfo(List<String> strings, RandomState randomState, BlockPos blockPos) {
        DecimalFormat format = new DecimalFormat("0.000");
        NoiseRouter router = randomState.router();
        DensityFunction.SinglePointContext context = new DensityFunction.SinglePointContext(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        double weirdness = router.ridges().compute(context);
        strings.add(
                "NoiseRouter T: "
                        + format.format(router.temperature().compute(context))
                        + " V: "
                        + format.format(router.vegetation().compute(context))
                        + " C: "
                        + format.format(router.continents().compute(context))
                        + " E: "
                        + format.format(router.erosion().compute(context))
                        + " D: "
                        + format.format(router.depth().compute(context))
                        + " W: "
                        + format.format(weirdness)
                        + " PV: "
                        + format.format(NoiseRouterData.peaksAndValleys((float)weirdness))
                        + " PS: "
                        + format.format(router.preliminarySurfaceLevel().compute(context))
                        + " N: "
                        + format.format(router.finalDensity().compute(context))
        );
    }

    protected OptionalInt iterateNoiseColumn(LevelHeightAccessor levelHeightAccessor, RandomState randomState, int x, int z, MutableObject<NoiseColumn> mutableObject, Predicate<BlockState> blockStatePredicate) {
        NoiseSettings noisesettings = this.settings.value().noiseSettings().clampToHeightAccessor(levelHeightAccessor);
        int i = noisesettings.getCellHeight();
        int j = noisesettings.minY();
        int k = Mth.floorDiv(j, i);
        int l = Mth.floorDiv(noisesettings.height(), i);
        if (l > 0) {
            BlockState[] ablockstate;
            if (mutableObject == null) {
                ablockstate = null;
            }
            else {
                ablockstate = new BlockState[noisesettings.height()];
                mutableObject.setValue(new NoiseColumn(j, ablockstate));
            }

            int i1 = noisesettings.getCellWidth();
            int j1 = Math.floorDiv(x, i1);
            int k1 = Math.floorDiv(z, i1);
            int l1 = Math.floorMod(x, i1);
            int i2 = Math.floorMod(z, i1);
            int j2 = j1 * i1;
            int k2 = k1 * i1;
            double d0 = (double) l1 / (double) i1;
            double d1 = (double) i2 / (double) i1;
            NoiseChunk noiseChunk = new NoiseChunk(1, randomState, j2, k2, noisesettings, DensityFunctions.BeardifierMarker.INSTANCE, this.settings.value(), this.globalFluidPicker, Blender.empty());
            postInitNoiseChunk(randomState, noiseChunk);
            noiseChunk.initializeForFirstCellX();
            noiseChunk.advanceCellX(0);

            for (int l2 = l - 1; l2 >= 0; --l2) {
                noiseChunk.selectCellYZ(l2, 0);

                for (int i3 = i - 1; i3 >= 0; --i3) {
                    int j3 = (k + l2) * i + i3;
                    double d2 = (double) i3 / (double) i;
                    noiseChunk.updateForY(j3, d2);
                    noiseChunk.updateForX(x, d0);
                    noiseChunk.updateForZ(z, d1);
                    BlockState blockstate = ((NoiseChunkAccessor) noiseChunk).bumblezone$callGetInterpolatedState();
                    BlockState blockstate1 = blockstate == null ? this.defaultBlock : blockstate;
                    if (ablockstate != null) {
                        int k3 = l2 * i + i3;
                        ablockstate[k3] = blockstate1;
                    }

                    if (blockStatePredicate != null && blockStatePredicate.test(blockstate1)) {
                        noiseChunk.stopInterpolation();
                        return OptionalInt.of(j3 + 1);
                    }
                }
            }

            noiseChunk.stopInterpolation();
        }
        return OptionalInt.empty();
    }

    @Override
    public void buildSurface(WorldGenRegion worldGenRegion, StructureManager structureManager, RandomState randomState, ChunkAccess chunkAccess) {
        WorldGenerationContext worldgenerationcontext = new WorldGenerationContext(this, worldGenRegion);
        BiomeManager biomeManager;
        if (biomeSource instanceof BiomeManager.NoiseBiomeSource noiseBiomeSource) {
            biomeManager = new BiomeManager(noiseBiomeSource, worldGenRegion.getSeed());
        }
        else {
            biomeManager = worldGenRegion.getBiomeManager();
        }
        Blender blender = Blender.of(worldGenRegion);
        NoiseChunk noisechunk = chunkAccess.getOrCreateNoiseChunk((noiseChunk) -> this.createNoiseChunk(noiseChunk, structureManager, blender, randomState));
        biomeManager = new NoVerticalBlendBiomeManager(biomeManager, ((NoiseChunkExtension) noisechunk).the_bumblezone$getBiomeSource() instanceof BiomeManager.NoiseBiomeSource noiseBiomeSource ? noiseBiomeSource : null);
        NoiseGeneratorSettings noisegeneratorsettings = this.settings.value();
        randomState.surfaceSystem().buildSurface(randomState, biomeManager, worldGenRegion.registryAccess().lookupOrThrow(Registries.BIOME), noisegeneratorsettings.useLegacyRandomSource(), worldgenerationcontext, chunkAccess, noisechunk, noisegeneratorsettings.surfaceRule());
    }

    public void buildSurface(ChunkAccess chunkAccess, WorldGenerationContext worldGenerationContext, RandomState randomState, StructureManager structureManager, BiomeManager biomeManager, Registry<Biome> biomeRegistry, Blender blender) {
        NoiseChunk noisechunk = chunkAccess.getOrCreateNoiseChunk((noiseChunk) -> this.createNoiseChunk(noiseChunk, structureManager, blender, randomState));
        NoiseGeneratorSettings noisegeneratorsettings = this.settings.value();
        randomState.surfaceSystem().buildSurface(randomState, biomeManager, biomeRegistry, noisegeneratorsettings.useLegacyRandomSource(), worldGenerationContext, chunkAccess, noisechunk, noisegeneratorsettings.surfaceRule());
    }

    @Override
    public void applyCarvers(WorldGenRegion worldGenRegion, long seed, RandomState randomState, BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunkAccess) {}

    @Override
    protected ChunkAccess doFill(Blender blender, StructureManager structureManager, RandomState randomState, ChunkAccess chunkAccess, int chunkX, int chunkZ) {
        NoiseChunk noiseChunk = chunkAccess.getOrCreateNoiseChunk((chunkAccess1) -> this.createNoiseChunk(chunkAccess1, structureManager, blender, randomState));

        Heightmap oceanFloorHeightmap = chunkAccess.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
        Heightmap worldSurfaceHeightmap = chunkAccess.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);
        for(int x = 0; x < 16; x++) {
            for(int z = 0; z < 16; z++) {
                oceanFloorHeightmap.update(x, chunkAccess.getMaxY(), z, BzBlocks.BEEHIVE_BEESWAX.get().defaultBlockState());
                worldSurfaceHeightmap.update(x, chunkAccess.getMaxY(), z, BzBlocks.BEEHIVE_BEESWAX.get().defaultBlockState());
            }
        }

        ChunkPos chunkpos = chunkAccess.getPos();
        int minBlockPosX = chunkpos.getMinBlockX();
        int minBlockPosZ = chunkpos.getMinBlockZ();
        noiseChunk.initializeForFirstCellX();
        int cellWidth = this.settings.value().noiseSettings().getCellWidth();
        int cellHeight = this.settings.value().noiseSettings().getCellHeight();
        int cellWidthSection = 16 / cellWidth;
        int cellHeightSection = 16 / cellWidth;

        for(int currentCellWidthSection = 0; currentCellWidthSection < cellWidthSection; ++currentCellWidthSection) {
            noiseChunk.advanceCellX(currentCellWidthSection);

            for(int currentCellHeightSection = 0; currentCellHeightSection < cellHeightSection; ++currentCellHeightSection) {
                int lastChunkSectionIndex = chunkAccess.getSectionsCount() - 1;
                LevelChunkSection currentChunkSection = chunkAccess.getSection(chunkAccess.getSectionsCount() - 1);

                for(int magicYZCounter = chunkZ - 1; magicYZCounter >= 0; --magicYZCounter) {
                    noiseChunk.selectCellYZ(magicYZCounter, currentCellHeightSection);

                    for(int theOtherMagicCounter = cellHeight - 1; theOtherMagicCounter >= 0; --theOtherMagicCounter) {
                        int y = (chunkX + magicYZCounter) * cellHeight + theOtherMagicCounter;
                        int yOffset = y & 15;
                        int currentChunkSectionIndex = chunkAccess.getSectionIndex(y);
                        if (lastChunkSectionIndex != currentChunkSectionIndex) {
                            currentChunkSection = chunkAccess.getSection(currentChunkSectionIndex);
                        }
                        noiseChunk.updateForY(y, theOtherMagicCounter / (double)cellHeight);

                        for(int currentCellWidth = 0; currentCellWidth < cellWidth; ++currentCellWidth) {
                            int x = minBlockPosX + currentCellWidthSection * cellWidth + currentCellWidth;
                            int xOffset = x & 15;
                            noiseChunk.updateForX(x, currentCellWidth / (double)cellWidth);

                            for(int currentCellHeight = 0; currentCellHeight < cellWidth; ++currentCellHeight) {
                                int z = minBlockPosZ + currentCellHeightSection * cellWidth + currentCellHeight;
                                int zOffset = z & 15;
                                noiseChunk.updateForZ(z, currentCellHeight / (double)cellWidth);
                                BlockState blockstate = ((NoiseChunkAccessor)noiseChunk).bumblezone$callGetInterpolatedState();
                                if (blockstate == null) {
                                    blockstate = this.defaultBlock;
                                }

                                if (!blockstate.isAir()) {
                                    currentChunkSection.setBlockState(xOffset, yOffset, zOffset, blockstate, false);
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

    @Override
    public Holder<NoiseGeneratorSettings> generatorSettings() {
        return this.settings;
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion region) {
        if (!this.settings.value().disableMobGeneration()) {
            ChunkPos chunkpos = region.getCenter();
            Holder<Biome> holder = region.getBiome(chunkpos.getWorldPosition().atY(region.getMaxY() - 1));
            WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(RandomSupport.generateUniqueSeed()));
            worldgenrandom.setDecorationSeed(region.getSeed(), chunkpos.getMinBlockX(), chunkpos.getMinBlockZ());
            spawnNonBeeMobsForChunkGeneration(region, holder, chunkpos, worldgenrandom);
        }
    }

    public static void spawnNonBeeMobsForChunkGeneration(ServerLevelAccessor serverLevelAccessor, Holder<Biome> biomeHolder, ChunkPos chunkPos, RandomSource randomSource) {
        MobSpawnSettings mobspawnsettings = biomeHolder.value().getMobSettings();
        WeightedList<MobSpawnSettings.SpawnerData> weightedList = mobspawnsettings.getMobs(MobCategory.CREATURE);

        // Bees are spawned by different system if config is true. See BeeDedicatedSpawning class
        if (BzGeneralConfigs.specialBeeSpawning) {
            weightedList = WeightedList.of(weightedList.unwrap().stream().filter(e -> e.value().type() != EntityType.BEE).toList());
        }

        if (!weightedList.isEmpty()) {
            int minX = chunkPos.getMinBlockX();
            int minZ = chunkPos.getMinBlockZ();
            int seaLevel = ((ServerChunkCache)serverLevelAccessor.getChunkSource()).getGenerator().getSeaLevel();

            while(randomSource.nextFloat() < mobspawnsettings.getCreatureProbability() * 0.5) {
                Optional<MobSpawnSettings.SpawnerData> optional = weightedList.getRandom(randomSource);
                if (optional.isPresent()) {
                    MobSpawnSettings.SpawnerData mobspawnsettings$spawnerdata = optional.get();
                    int groupCount = mobspawnsettings$spawnerdata.minCount() + randomSource.nextInt(1 + mobspawnsettings$spawnerdata.maxCount() - mobspawnsettings$spawnerdata.minCount());
                    SpawnGroupData spawngroupdata = null;
                    int x = minX + randomSource.nextInt(16);
                    int z = minZ + randomSource.nextInt(16);
                    int tempX = x;
                    int tempZ = z;

                    for(int l1 = 0; l1 < groupCount; ++l1) {
                        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(x, randomSource.nextInt(250 - seaLevel) + seaLevel, z);
                        if (serverLevelAccessor.dimensionType().hasCeiling()) {
                            do {
                                mutableBlockPos.move(Direction.DOWN);
                            } while (!serverLevelAccessor.getBlockState(mutableBlockPos).isAir());

                            do {
                                mutableBlockPos.move(Direction.DOWN);
                            } while (serverLevelAccessor.getBlockState(mutableBlockPos).isAir() && mutableBlockPos.getY() > serverLevelAccessor.getMinY());
                        }

                        if (mobspawnsettings$spawnerdata.type().canSummon()) {
                            float mobWidth = mobspawnsettings$spawnerdata.type().getWidth();
                            double finalX = Mth.clamp(x + 0.5D, (double)minX + (double)mobWidth + 0.5D, (double)minX + 15.5D - (double)mobWidth);
                            double finalZ = Mth.clamp(z + 0.5D, (double)minZ + (double)mobWidth + 0.5D, (double)minZ + 15.5D - (double)mobWidth);

                            if (!serverLevelAccessor.getWorldBorder().isWithinBounds(finalX, finalZ) ||
                                (mutableBlockPos.getY() < serverLevelAccessor.getMinY() || mutableBlockPos.getY() >= serverLevelAccessor.getMaxY()))
                            {
                                continue;
                            }

                            Entity entity = null;
                            try {
                                entity = mobspawnsettings$spawnerdata.type().create(serverLevelAccessor.getLevel(), EntitySpawnReason.CHUNK_GENERATION);

                                entity.snapTo(finalX, mutableBlockPos.getY(), finalZ, randomSource.nextFloat() * 360.0F, 0.0F);
                                if (entity instanceof Mob mob) {
                                    PlatformService.INSTANCE.finalizeSpawn(mob, serverLevelAccessor, null, EntitySpawnReason.CHUNK_GENERATION);

                                    if (mob.checkSpawnObstruction(serverLevelAccessor)) {
                                        mob.snapTo(mob.getX(), mob.getY() + 1, mob.getZ());
                                        spawngroupdata = mob.finalizeSpawn(serverLevelAccessor, serverLevelAccessor.getCurrentDifficultyAt(mob.blockPosition()), EntitySpawnReason.CHUNK_GENERATION, spawngroupdata);
                                        serverLevelAccessor.addFreshEntityWithPassengers(mob);
                                    }
                                }
                            }
                            catch (Exception exception) {
                                Bumblezone.LOGGER.error("Failed to create mob: {}", entity);
                                exception.addSuppressed(new RuntimeException("Failed to create mob: " + entity));
                                throw exception;
                            }
                        }

                        x += randomSource.nextInt(5) - randomSource.nextInt(5);

                        for(z += randomSource.nextInt(5) - randomSource.nextInt(5); x < minX || x >= minX + 16 || z < minZ || z >= minZ + 16; z = tempZ + randomSource.nextInt(5) - randomSource.nextInt(5)) {
                            x = tempX + randomSource.nextInt(5) - randomSource.nextInt(5);
                        }
                    }
                }
            }
        }
    }
}