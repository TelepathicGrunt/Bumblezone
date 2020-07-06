package net.telepathicgrunt.bumblezone.generation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.NoiseSampler;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import net.telepathicgrunt.bumblezone.mixin.BeeEntityInvoker;
import net.telepathicgrunt.bumblezone.utils.BzPlacingUtils;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.IntStream;


public class BzChunkGenerator extends ChunkGenerator {
    public static void registerChunkgenerator() {
        Registry.register(Registry.CHUNK_GENERATOR, Bumblezone.MOD_FULL_ID, BzChunkGenerator.CODEC);
    }

    public static final Codec<BzChunkGenerator> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(BiomeSource.field_24713.fieldOf("biome_source").forGetter((surfaceChunkGenerator) -> {
            return surfaceChunkGenerator.biomeSource;
        }), Codec.LONG.fieldOf("seed").stable().forGetter((surfaceChunkGenerator) -> {
            return surfaceChunkGenerator.seed;
        }), ChunkGeneratorType.field_24781.fieldOf("settings").forGetter((surfaceChunkGenerator) -> {
            return surfaceChunkGenerator.settings;
        })).apply(instance, instance.stable(BzChunkGenerator::new));
    });

    /*
     * Used in getBiomeNoiseColumn to get the height of land by sampling the height of all biomes around it
     */
    private static final float[] field_222576_h = Util.make(new float[25], (p_222575_0_) ->
    {
        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                float f = 10.0F / MathHelper.sqrt((float) ((i * i) + (j * j)) + 0.2F);
                p_222575_0_[(i + 2) + (j + 2) * 5] = f;
            }
        }

    });
    private final OctavePerlinNoiseSampler depthNoise;
    private static final BlockState STONE = Blocks.STONE.getDefaultState();
    private static final BlockState CAVE_AIR = Blocks.CAVE_AIR.getDefaultState();
    private final int verticalNoiseGranularity;
    private final int horizontalNoiseGranularity;
    private final int noiseSizeX;
    private final int noiseSizeY;
    private final int noiseSizeZ;
    protected final ChunkRandom randomSeed;
    private final OctavePerlinNoiseSampler minNoise;
    private final OctavePerlinNoiseSampler maxNoise;
    private final OctavePerlinNoiseSampler mainNoise;
    private final NoiseSampler surfaceDepthNoise;
    protected final BlockState defaultBlock;
    protected final BlockState defaultFluid;
    protected final ChunkGeneratorType settings;
    private final long seed;
    private final int height;


    public BzChunkGenerator(BiomeSource biomeSource, long l, ChunkGeneratorType chunkGeneratorType) {
        this(biomeSource, biomeSource, l, chunkGeneratorType);
    }

    public BzChunkGenerator(BiomeSource biomeSource, BiomeSource biomeSource2, long seed, ChunkGeneratorType chunkGeneratorType) {
        super(biomeSource, biomeSource2, chunkGeneratorType.getConfig(), seed);
        this.settings = chunkGeneratorType;
        this.randomSeed = new ChunkRandom(seed);
        this.randomSeed.consume(2620);
        this.seed = seed;
        this.height = 256;
        this.depthNoise = new OctavePerlinNoiseSampler(this.randomSeed, IntStream.rangeClosed(-15, 0));
        this.verticalNoiseGranularity = 8;
        this.horizontalNoiseGranularity = 8;
        this.defaultBlock = STONE;
        this.defaultFluid = BzBlocks.SUGAR_WATER_BLOCK.getDefaultState();
        this.noiseSizeX = 16 / this.horizontalNoiseGranularity;
        this.noiseSizeY = this.height / this.verticalNoiseGranularity;
        this.noiseSizeZ = 16 / this.horizontalNoiseGranularity;
        this.minNoise = new OctavePerlinNoiseSampler(this.randomSeed, IntStream.rangeClosed(-15, 0));
        this.maxNoise = new OctavePerlinNoiseSampler(this.randomSeed, IntStream.rangeClosed(-15, 0));
        this.mainNoise = new OctavePerlinNoiseSampler(this.randomSeed, IntStream.rangeClosed(-7, 0));
        this.surfaceDepthNoise = new OctaveSimplexNoiseSampler(this.randomSeed, IntStream.rangeClosed(-3, 0));
    }

    private static final Biome.SpawnEntry INITIAL_SLIME_ENTRY = new Biome.SpawnEntry(EntityType.SLIME, 1, 1, 1);
    private static final Biome.SpawnEntry INITIAL_BEE_ENTRY = new Biome.SpawnEntry(EntityType.BEE, 1, 4, 4);

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
    public void populateEntities(ChunkRegion region) {
        int xChunk = region.getCenterChunkX();
        int zChunk = region.getCenterChunkZ();
        int xCord = xChunk << 4;
        int zCord = zChunk << 4;
        Biome biome = region.getBiome((new ChunkPos(xChunk, zChunk)).getCenterBlockPos());
        ChunkRandom sharedseedrandom = new ChunkRandom();
        sharedseedrandom.setSeed(region.getSeed());

        while (sharedseedrandom.nextFloat() < biome.getMaxSpawnChance()) {
            //30% of time, spawn slime. Otherwise, spawn bees.
            Biome.SpawnEntry biome$spawnlistentry = sharedseedrandom.nextFloat() < 0.4f ? INITIAL_SLIME_ENTRY : INITIAL_BEE_ENTRY;

            int startingX = xCord + sharedseedrandom.nextInt(16);
            int startingZ = zCord + sharedseedrandom.nextInt(16);
            int currentX = startingX;
            int currentZ = startingZ;

            BlockPos.Mutable blockpos = new BlockPos.Mutable(currentX, 0, currentZ);
            int height = BzPlacingUtils.topOfSurfaceBelowHeight(region, sharedseedrandom.nextInt(255), -1, blockpos) + 1;

            if (biome$spawnlistentry.type.isSummonable() && height > 0 && height < 255) {
                float width = biome$spawnlistentry.type.getWidth();
                double xLength = MathHelper.clamp((double) startingX, (double) xCord + (double) width, (double) xCord + 16.0D - (double) width);
                double zLength = MathHelper.clamp((double) startingZ, (double) zCord + (double) width, (double) zCord + 16.0D - (double) width);

                Entity entity;
                try {
                    entity = biome$spawnlistentry.type.create(region.getWorld());

                    if (biome$spawnlistentry.type == EntityType.BEE) {
                        //20% chance of being full of pollen
                        if (randomSeed.nextFloat() < 0.2f) {
                            ((BeeEntityInvoker) entity).callSetBeeFlag(8, true);
                        }

                        //Bumblezone.LOGGER.log(Level.INFO, " outside beeproductive check");
//						if(FabricLoader.getInstance().isModLoaded("beeproductive")) {
//							//Bumblezone.LOGGER.log(Level.INFO, " inside beeproductive check. passed with flying colors");
//							entity = BeeProductiveIntegration.spawnBeeProductiveBee(region.getRandom(), entity);
//						}
                    }
                } catch (Exception exception) {
                    Bumblezone.LOGGER.warn("Failed to create mob", (Throwable) exception);
                    continue;
                }

                entity.refreshPositionAndAngles(xLength, (double) height, zLength, sharedseedrandom.nextFloat() * 360.0F, 0.0F);
                if (entity instanceof MobEntity) {
                    MobEntity mobentity = (MobEntity) entity;
                    if (mobentity.canSpawn(region, SpawnReason.CHUNK_GENERATION) && mobentity.canSpawn(region)) {
                        mobentity.initialize(region, region.getLocalDifficulty(new BlockPos(mobentity.getPos())), SpawnReason.CHUNK_GENERATION, null, (CompoundTag) null);
                        region.spawnEntity(mobentity);
                    }
                }
            }
        }
    }


    /**
     * For spawning specific mobs in certain places like structures.
     */
    @Override
    public List<Biome.SpawnEntry> getEntitySpawnList(Biome biome, StructureAccessor accessor, SpawnGroup group, BlockPos pos) {
        return super.getEntitySpawnList(biome, accessor, group, pos);
    }


    /**
     * This is what kicks start the actual terrain shaping of the biomes.
     * <p>
     * The parameters passed in setupPerlinNoiseGenerators is used to
     * manipulate the perlin noise generator. This is extremely important.
     * The values used is carefully tailored for this dimension.
     */
    protected void sampleNoiseColumn(double[] areaArrayIn, int x, int z) {
        //We step fast in x, slower in z, and neutralish in y.
        this.setupNoiseGenerators(areaArrayIn, x, z, 2600D, 250D, 600D, 8D, 4D, 3D, -10, -10);
    }


    /**
     * Handles the smoothing between biomes of different heights.
     * <p>
     * Lower the number after d1 *=  to make the transition sharper and
     * increase it to make it even smoother. Note, by decreasing the number,
     * some biomes might lower in overall height so compensate for that.
     */
    protected double computeNoiseFalloff(double depth, double scale, int y) {
        double d1 = ((double) y - (8.5D + depth * 8.5D / 8.0D * 4.0D)) * 12.0D * 128.0D / this.height / scale;
        if (d1 < 0.0D) {
            d1 *= 1.75D;
        }

        return d1;
    }


    /**
     * Using the biome's depth and scale + nearby biome heights,
     * it gets what the final height should be at each location.
     */
    protected double[] computeNoiseRange(int x, int z) {
        double[] adouble = new double[2];
        float f = 0.0F;
        float f1 = 0.0F;
        float f2 = 0.0F;
        int y = this.getSeaLevel();
        float f3 = this.biomeSource.getBiomeForNoiseGen(x, y, z).getDepth();

        for (int j = -2; j <= 2; ++j) {
            for (int k = -2; k <= 2; ++k) {
                Biome biome = this.biomeSource.getBiomeForNoiseGen(x + j, y, z + k);
                float depthWeight = biome.getDepth();
                float scaleWeight = biome.getScale();

                depthWeight = 1.0F + depthWeight * 1.10F; // overall height of biome
                scaleWeight = 1.0F + scaleWeight * 10.00F; // sharp variation in land height

                float f6 = field_222576_h[j + 2 + (k + 2) * 5] / (depthWeight + 5.0F); // smooth out height by sampling nearby heights
                if (biome.getDepth() > f3) {
                    f6 /= 2.0F; // I think this curbs the height if it is too high?
                }

                f += scaleWeight * f6;
                f1 += depthWeight * f6;
                f2 += f6;
            }
        }

        //Some spooky vanilla magic going on down here.
        f = f / f2;
        f1 = f1 / f2;
        f = f * 0.9F + 0.1F;
        f1 = (f1 * 4.0F - 1.0F) / 8.0F;
        adouble[0] = (double) f1 + this.getNoiseDepthAt(x, z);
        adouble[1] = (double) f;
        return adouble;
    }

    /**
     * creates noise in the terrain's height so it isn't fully smooth
     */
    private double getNoiseDepthAt(int x, int z) {
        double noise = this.depthNoise.sample((double) (x * 200), 10.0D, (double) (z * 200), 1.0D, 0.0D, true) * 65535.0D / 8000.0D;
        if (noise < 0.0D) {
            noise = -noise * 0.3D;
        }

        noise = noise * 3.0D - 2.0D;
        if (noise < 0.0D) {
            noise = noise / 28.0D;
        } else {
            if (noise > 1.0D) {
                noise = 1.0D;
            }

            noise = noise / 40.0D;
        }

        return noise;
    }

    /**
     * Ground height duh. For spawning purposes which isn't really needed for our dimension.
     */
    @Override
    public int getSpawnHeight() {
        return getSeaLevel() + 1;
    }

    /**
     * This sets up all the perlin stuff using the passed in values to know how much to step in x, y, and z plus some more
     * options.
     * <p>
     * Mainly vanilla code but I did move the mainX and stuff out of sample parameters so it is much cleaner and also gave
     * it separate parameters to use so I can manipulate them separately from the limitX and stuff to see what terrain I can
     * create.
     */
    private double sampleNoise(int x, int y, int z, double getXCoordinateScale, double getZCoordinateScale, double getHeightScale, double getMainCoordinateScale, double getMainHeightScale, double p_222552_10_) {
        double d0 = 0.0D;
        double d1 = 0.0D;
        double d2 = 0.0D;
        double d3 = 1.0D;

        for (int i = 0; i < 16; ++i) {
            double limitX = OctavePerlinNoiseSampler.maintainPrecision((double) x * getXCoordinateScale * d3);
            double limitY = OctavePerlinNoiseSampler.maintainPrecision((double) y * getHeightScale * d3);
            double limitZ = OctavePerlinNoiseSampler.maintainPrecision((double) z * getZCoordinateScale * d3);

            double mainX = OctavePerlinNoiseSampler.maintainPrecision((double) x * getMainCoordinateScale * d3);
            double mainY = OctavePerlinNoiseSampler.maintainPrecision((double) y * getMainHeightScale * d3);
            double mainZ = OctavePerlinNoiseSampler.maintainPrecision((double) z * getMainCoordinateScale * d3);

            double d7 = getHeightScale * d3;
            d0 += this.minNoise.getOctave(i).sample(limitX, limitY, limitZ, d7, (double) y * d7) / d3;
            d1 += this.maxNoise.getOctave(i).sample(limitX, limitY, limitZ, d7, (double) y * d7) / d3;
            if (i < 8) {
                d2 += this.mainNoise.getOctave(i).sample(mainX, mainY, mainZ, p_222552_10_ * d3, (double) y * p_222552_10_ * d3) / d3;
            }

            d3 /= 2.0D;
        }

        return MathHelper.clampedLerp(d0 / 512.0D, d1 / 512.0D, (d2 / 10.0D + 1.0D) / 2.0D);
    }

    /**
     * vanilla voodoo. Don't question it
     */
    protected void setupNoiseGenerators(double[] areaArrayIn, int x, int z, double getXCoordinateScale, double getZCoordinateScale, double getHeightScale, double getMainCoordinateScale, double getMainHeightScale, double p_222546_10_, int p_222546_12_, int p_222546_13_) {
        double[] localAreaArray = this.computeNoiseRange(x, z);
        double d0 = localAreaArray[0];
        double d1 = localAreaArray[1];
        double d2 = this.noiseSizeY - 3;
        double d3 = 0;

        for (int y = 0; y < this.noiseSizeY + 1; ++y) {
            double d4 = this.sampleNoise(x, y, z, getXCoordinateScale, getZCoordinateScale, getHeightScale, getMainCoordinateScale, getMainHeightScale, p_222546_10_);
            d4 = d4 - this.computeNoiseFalloff(d0, d1, y);
            if ((double) y > d2) {
                d4 = MathHelper.clampedLerp(d4, (double) p_222546_13_, ((double) y - d2) / (double) p_222546_12_);
            } else if ((double) y < d3) {
                d4 = MathHelper.clampedLerp(d4, -30.0D, (d3 - (double) y) / (d3 - 1.0D));
            }

            areaArrayIn[y] = d4;
        }

    }

    /**
     * Cursed. I didn't touched this. Leave it as is.
     */
    protected double[] sampleNoiseColumn(int x, int z) {
        double[] ds = new double[this.noiseSizeY + 1];
        this.sampleNoiseColumn(ds, x, z);
        return ds;
    }


    /**
     * Cursed. I didn't touched this. Leave it as is.
     */
    @Override
    public int getHeightOnGround(int chunkX, int chunkZ, Heightmap.Type heightmapType) {
        int minX = Math.floorDiv(chunkX, this.horizontalNoiseGranularity);
        int minZ = Math.floorDiv(chunkZ, this.horizontalNoiseGranularity);
        int modX = Math.floorMod(chunkX, this.horizontalNoiseGranularity);
        int modZ = Math.floorMod(chunkZ, this.horizontalNoiseGranularity);
        double xFactor = (double) modX / (double) this.horizontalNoiseGranularity;
        double zFactor = (double) modZ / (double) this.horizontalNoiseGranularity;
        double[][] terrain2DArray = new double[][]{this.sampleNoiseColumn(minX, minZ), this.sampleNoiseColumn(minX, minZ + 1), this.sampleNoiseColumn(minX + 1, minZ), this.sampleNoiseColumn(minX + 1, minZ + 1)};

        for (int noiseY = this.noiseSizeY - 1; noiseY >= 0; --noiseY) {
            double d2 = terrain2DArray[0][noiseY];
            double d3 = terrain2DArray[1][noiseY];
            double d4 = terrain2DArray[2][noiseY];
            double d5 = terrain2DArray[3][noiseY];
            double d6 = terrain2DArray[0][noiseY + 1];
            double d7 = terrain2DArray[1][noiseY + 1];
            double d8 = terrain2DArray[2][noiseY + 1];
            double d9 = terrain2DArray[3][noiseY + 1];

            for (int yOffset = this.verticalNoiseGranularity - 1; yOffset >= 0; --yOffset) {
                double yFactor = (double) yOffset / (double) this.verticalNoiseGranularity;
                double finalNoise = MathHelper.lerp3(yFactor, xFactor, zFactor, d2, d6, d4, d8, d3, d7, d5, d9);
                int y = noiseY * this.verticalNoiseGranularity + yOffset;
                if (finalNoise > 0.0D) {
                    BlockState blockstate;
                    if (finalNoise > 0.0D) {
                        blockstate = this.defaultBlock;
                    } else {
                        blockstate = this.defaultFluid;
                    }

                    if (heightmapType.getBlockPredicate().test(blockstate)) {
                        return y + 1;
                    }
                }
            }
        }

        return 0;
    }


    /**
     * Self-explanatory. After terrain is made, this comes in to place the surface blocks by calling the biome's surface
     * builder for each position in the chunk
     */
    @Override
    public void buildSurface(ChunkRegion region, Chunk chunk) {
        ChunkPos chunkpos = chunk.getPos();
        int chunkXSeed = chunkpos.x;
        int chunkZSeed = chunkpos.z;
        ChunkRandom sharedseedrandom = new ChunkRandom();
        sharedseedrandom.setTerrainSeed(chunkXSeed, chunkZSeed);
        ChunkPos chunkpos1 = chunk.getPos();
        int chunkX = chunkpos1.getStartX();
        int chunkZ = chunkpos1.getStartZ();
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                int xPos = chunkX + x;
                int zPos = chunkZ + z;
                int ySurface = chunk.sampleHeightmap(Heightmap.Type.WORLD_SURFACE_WG, x, z) + 1;
                double noise = this.surfaceDepthNoise.sample((double) xPos * 0.0625D, (double) zPos * 0.0625D, 0.0625D, (double) x * 0.0625D) * 10.0D;
                region.getBiome(blockpos$mutable.set(chunkX + x, ySurface, chunkZ + z)).buildSurface(sharedseedrandom, chunk, xPos, zPos, ySurface, noise, this.defaultBlock, this.defaultFluid, this.getSeaLevel(), region.getSeed());
            }
        }

        this.makeCeilingAndFloor(chunk, sharedseedrandom);
    }


    /**
     * Creates the ceiling and floor that separates the dimension from the emptiness above y = 256 and below y = 0.
     * <p>
     * We use honeycomb blocks instead of Bedrock.
     */
    protected void makeCeilingAndFloor(Chunk chunk, Random random) {
        BlockPos.Mutable blockpos$Mutable = new BlockPos.Mutable();
        int xStart = chunk.getPos().getStartX();
        int zStart = chunk.getPos().getStartZ();
        int roofHeight = 255;
        int floorHeight = 0;

        for (BlockPos blockpos : BlockPos.iterate(xStart, 0, zStart, xStart + 15, 0, zStart + 15)) {
            //fills in gap between top of terrain gen and y = 255 with solid blocks
            for (int ceilingY = roofHeight; ceilingY >= roofHeight - 7; --ceilingY) {
                chunk.setBlockState(blockpos$Mutable.set(blockpos.getX(), ceilingY, blockpos.getZ()), Blocks.HONEYCOMB_BLOCK.getDefaultState(), false);
            }

            //single layer of solid blocks
            for (int floorY = floorHeight; floorY <= floorHeight; ++floorY) {
                chunk.setBlockState(blockpos$Mutable.set(blockpos.getX(), floorY, blockpos.getZ()), Blocks.HONEYCOMB_BLOCK.getDefaultState(), false);
            }
        }

    }


    /**
     * Creates the actual terrain itself. It seems to go by cubic chunk-like when generating terrain?
     */
    @Override
    public void populateNoise(WorldAccess world, StructureAccessor accessor, Chunk chunk)  {
        ChunkPos chunkpos = chunk.getPos();
        int chunkX = chunkpos.x;
        int chunkZ = chunkpos.z;
        int coordinateX = chunkX << 4;
        int coordinateZ = chunkZ << 4;

        //my additions to make top half of dimension mirror bottom half
        int yNoiseMod;
        int yChunk;

        //vanilla stuff. Messy and dunno what to call them
        double d16;
        double d17;
        double d18;
        double d0;
        double d1;
        double d2;
        double d3;
        double d4;

        double[][][] terrainNoise2DArray = new double[2][this.noiseSizeZ + 1][this.noiseSizeY + 1];

        for (int index = 0; index < this.noiseSizeZ + 1; ++index) {
            terrainNoise2DArray[0][index] = new double[this.noiseSizeY + 1];
            this.sampleNoiseColumn(terrainNoise2DArray[0][index], chunkX * this.noiseSizeX, chunkZ * this.noiseSizeZ + index);
            terrainNoise2DArray[1][index] = new double[this.noiseSizeY + 1];
        }

        ProtoChunk protoChunk = (ProtoChunk) chunk;
        Heightmap heightmap = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmap1 = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);
        BlockPos.Mutable blockpos$Mutable = new BlockPos.Mutable();

        for (int xNoise = 0; xNoise < this.noiseSizeX; ++xNoise) {
            for (int zNoise = 0; zNoise < this.noiseSizeZ + 1; ++zNoise) {
                this.sampleNoiseColumn(terrainNoise2DArray[1][zNoise], chunkX * this.noiseSizeX + xNoise + 1, chunkZ * this.noiseSizeZ + zNoise);
            }

            for (int zNoise = 0; zNoise < this.noiseSizeZ; ++zNoise) {
                ChunkSection chunksection = protoChunk.getSection(15);
                chunksection.lock();

                for (int yNoise = this.noiseSizeY - 1; yNoise >= 0; --yNoise) {

                    /*
                     * When the noise is greater than 16 (chunks), begin the mirroring effect
                     */
                    if (yNoise > 16) {
                        /*
                         * Move down one because we ended on that y chunk before and by mirroring that chunk, the transition between the lower
                         * half and upper half is smoother.
                         */
                        yChunk = yNoise - 1;
                        yNoiseMod = 31 - yChunk;

                        d16 = terrainNoise2DArray[0][zNoise][yNoiseMod + 1];
                        d17 = terrainNoise2DArray[0][zNoise + 1][yNoiseMod + 1];
                        d18 = terrainNoise2DArray[1][zNoise][yNoiseMod + 1];
                        d0 = terrainNoise2DArray[1][zNoise + 1][yNoiseMod + 1];
                        d1 = terrainNoise2DArray[0][zNoise][yNoiseMod];
                        d2 = terrainNoise2DArray[0][zNoise + 1][yNoiseMod];
                        d3 = terrainNoise2DArray[1][zNoise][yNoiseMod];
                        d4 = terrainNoise2DArray[1][zNoise + 1][yNoiseMod];
                    } else {
                        /*
                         * Generate the y chunk as normal for y chunks 16 and below
                         */
                        yChunk = yNoise;
                        yNoiseMod = yChunk;

                        d16 = terrainNoise2DArray[0][zNoise][yNoiseMod];
                        d17 = terrainNoise2DArray[0][zNoise + 1][yNoiseMod];
                        d18 = terrainNoise2DArray[1][zNoise][yNoiseMod];
                        d0 = terrainNoise2DArray[1][zNoise + 1][yNoiseMod];
                        d1 = terrainNoise2DArray[0][zNoise][yNoiseMod + 1];
                        d2 = terrainNoise2DArray[0][zNoise + 1][yNoiseMod + 1];
                        d3 = terrainNoise2DArray[1][zNoise][yNoiseMod + 1];
                        d4 = terrainNoise2DArray[1][zNoise + 1][yNoiseMod + 1];
                    }

                    for (int yInfluence = this.verticalNoiseGranularity - 1; yInfluence >= 0; --yInfluence) {

                        //This is where the actual y position of the block that is going to be added to land.
                        //Very important stuff. Handle with delicate care OR ELSE.
                        int currentY = yChunk * this.verticalNoiseGranularity + yInfluence;

                        int y = currentY & 15;
                        int yChunkFinal = currentY >> 4;
                        if (chunksection.getYOffset() >> 4 != yChunkFinal) {
                            chunksection.unlock();
                            chunksection = protoChunk.getSection(yChunkFinal);
                            chunksection.lock();
                        }

                        double d5 = (double) yInfluence / (double) this.verticalNoiseGranularity;
                        double d6 = MathHelper.lerp(d5, d16, d1);
                        double d7 = MathHelper.lerp(d5, d18, d3);
                        double d8 = MathHelper.lerp(d5, d17, d2);
                        double d9 = MathHelper.lerp(d5, d0, d4);

                        for (int horizontalNoiseX = 0; horizontalNoiseX < this.horizontalNoiseGranularity; ++horizontalNoiseX) {
                            int xCoordinate = coordinateX + xNoise * this.horizontalNoiseGranularity + horizontalNoiseX;
                            int xInChunk = xCoordinate & 15;
                            double d10 = (double) horizontalNoiseX / (double) this.horizontalNoiseGranularity;
                            double d11 = MathHelper.lerp(d10, d6, d7);
                            double d12 = MathHelper.lerp(d10, d8, d9);

                            for (int horizontalNoiseZ = 0; horizontalNoiseZ < this.horizontalNoiseGranularity; ++horizontalNoiseZ) {
                                int zCoordinate = coordinateZ + zNoise * this.horizontalNoiseGranularity + horizontalNoiseZ;
                                int zInChunk = zCoordinate & 15;
                                double d13 = (double) horizontalNoiseZ / (double) this.horizontalNoiseGranularity;
                                double d14 = MathHelper.lerp(d13, d11, d12);
                                double finalTerrainNoise = MathHelper.clamp(d14 / 200.0D, -1.0D, 1.0D);

                                BlockState blockstate;

                                if (finalTerrainNoise > 0.0D) {
                                    //place the biome's solid block
                                    blockstate = this.defaultBlock;
                                } else if (currentY < 40) {
                                    //The sea
                                    blockstate = this.defaultFluid;
                                } else {
                                    //The air
                                    blockstate = CAVE_AIR;
                                }

                                if (blockstate != CAVE_AIR) {
                                    if (blockstate.getLuminance() != 0) {
                                        blockpos$Mutable.set(xCoordinate, currentY, zCoordinate);
                                        protoChunk.addLightSource(blockpos$Mutable);
                                    }

                                    chunksection.setBlockState(xInChunk, y, zInChunk, blockstate, false);
                                    heightmap.trackUpdate(xInChunk, currentY, zInChunk, blockstate);
                                    heightmap1.trackUpdate(xInChunk, currentY, zInChunk, blockstate);
                                }
                            }
                        }
                    }
                }

                chunksection.unlock();
            }

            double[][] adouble1 = terrainNoise2DArray[0];
            terrainNoise2DArray[0] = terrainNoise2DArray[1];
            terrainNoise2DArray[1] = adouble1;
        }

    }


    @Override
    protected Codec<? extends ChunkGenerator> method_28506() {
        return CODEC;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public ChunkGenerator withSeed(long seed) {
        return new BzChunkGenerator(this.biomeSource.withSeed(seed), seed, this.settings);
    }

    @Override
    public int getHeight(int x, int z, Heightmap.Type heightmapType) {
        return this.sampleHeightmap(x, z, (BlockState[])null, heightmapType.getBlockPredicate());
    }

    private int sampleHeightmap(int x, int z, BlockState[] states, Predicate<BlockState> predicate) {
        int i = Math.floorDiv(x, this.horizontalNoiseGranularity);
        int j = Math.floorDiv(z, this.horizontalNoiseGranularity);
        int k = Math.floorMod(x, this.horizontalNoiseGranularity);
        int l = Math.floorMod(z, this.horizontalNoiseGranularity);
        double d = (double)k / (double)this.horizontalNoiseGranularity;
        double e = (double)l / (double)this.horizontalNoiseGranularity;
        double[][] ds = new double[][]{this.sampleNoiseColumn(i, j), this.sampleNoiseColumn(i, j + 1), this.sampleNoiseColumn(i + 1, j), this.sampleNoiseColumn(i + 1, j + 1)};

        for(int m = this.noiseSizeY - 1; m >= 0; --m) {
            double f = ds[0][m];
            double g = ds[1][m];
            double h = ds[2][m];
            double n = ds[3][m];
            double o = ds[0][m + 1];
            double p = ds[1][m + 1];
            double q = ds[2][m + 1];
            double r = ds[3][m + 1];

            for(int s = this.verticalNoiseGranularity - 1; s >= 0; --s) {
                double t = (double)s / (double)this.verticalNoiseGranularity;
                double u = MathHelper.lerp3(t, d, e, f, o, h, q, g, p, n, r);
                int v = m * this.verticalNoiseGranularity + s;
                BlockState blockState = this.getBlockState(u, v);
                if (states != null) {
                    states[v] = blockState;
                }

                if (predicate != null && predicate.test(blockState)) {
                    return v + 1;
                }
            }
        }

        return 0;
    }

    protected BlockState getBlockState(double density, int y) {
        BlockState blockState3;
        if (density > 0.0D) {
            blockState3 = this.defaultBlock;
        } else if (y < this.getSeaLevel()) {
            blockState3 = this.defaultFluid;
        } else {
            blockState3 = CAVE_AIR;
        }

        return blockState3;
    }


    @Override
    public BlockView getColumnSample(int x, int z) {
        BlockState[] blockStates = new BlockState[this.noiseSizeY * this.verticalNoiseGranularity];
        this.sampleHeightmap(x, z, blockStates, (Predicate)null);
        return new VerticalBlockSample(blockStates);
    }


    public int getSeaLevel() {
        return 40;
    }
}