package net.telepathicgrunt.bumblezone.generation;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.NoiseSampler;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;


public abstract class BzNoiseChunkGenerator<T extends ChunkGeneratorConfig> extends ChunkGenerator<T>
{

	private static final BlockState STONE = Blocks.STONE.getDefaultState();
	private static final BlockState WATER = Blocks.WATER.getDefaultState();
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


	public BzNoiseChunkGenerator(IWorld world, BiomeSource biomeProvider, int horizontalNoiseGranularityIn, int verticalNoiseGranularityIn, int maxHeight, T settings)
	{
		super(world, biomeProvider, settings);
		this.verticalNoiseGranularity = verticalNoiseGranularityIn;
		this.horizontalNoiseGranularity = horizontalNoiseGranularityIn;
		this.defaultBlock = STONE;
		this.defaultFluid = WATER;
		this.noiseSizeX = 16 / this.horizontalNoiseGranularity;
		this.noiseSizeY = maxHeight / this.verticalNoiseGranularity;
		this.noiseSizeZ = 16 / this.horizontalNoiseGranularity;
		this.randomSeed = new ChunkRandom(this.seed);
		this.minNoise = new OctavePerlinNoiseSampler(this.randomSeed, 15, 0);
		this.maxNoise = new OctavePerlinNoiseSampler(this.randomSeed, 15, 0);
		this.mainNoise = new OctavePerlinNoiseSampler(this.randomSeed, 7, 0);
		this.surfaceDepthNoise = (NoiseSampler) (new OctaveSimplexNoiseSampler(this.randomSeed, 3, 0));
	}


	/*
	 * Implement these on the chunk generator that extends this class. This allows each chunk generator to be more unique in
	 * their own way.
	 */
	protected abstract double[] computeNoiseRange(int noiseX, int noiseZ);


	protected abstract double computeNoiseFalloff(double depth, double scale, int y);


	protected abstract void sampleNoiseColumn(double[] buffer, int x, int z);


	/**
	 * This sets up all the perlin stuff using the passed in values to know how much to step in x, y, and z plus some more
	 * options.
	 * 
	 * Mainly vanilla code but I did move the mainX and stuff out of sample parameters so it is much cleaner and also gave
	 * it separate parameters to use so I can manipulate them separately from the limitX and stuff to see what terrain I can
	 * create.
	 */
	private double sampleNoise(int x, int y, int z, double getXCoordinateScale, double getZCoordinateScale, double getHeightScale, double getMainCoordinateScale, double getMainHeightScale, double p_222552_10_)
	{
		double d0 = 0.0D;
		double d1 = 0.0D;
		double d2 = 0.0D;
		double d3 = 1.0D;

		for (int i = 0; i < 16; ++i)
		{
			double limitX = OctavePerlinNoiseSampler.maintainPrecision((double) x * getXCoordinateScale * d3);
			double limitY = OctavePerlinNoiseSampler.maintainPrecision((double) y * getHeightScale * d3);
			double limitZ = OctavePerlinNoiseSampler.maintainPrecision((double) z * getZCoordinateScale * d3);

			double mainX = OctavePerlinNoiseSampler.maintainPrecision((double) x * getMainCoordinateScale * d3);
			double mainY = OctavePerlinNoiseSampler.maintainPrecision((double) y * getMainHeightScale * d3);
			double mainZ = OctavePerlinNoiseSampler.maintainPrecision((double) z * getMainCoordinateScale * d3);

			double d7 = getHeightScale * d3;
			d0 += this.minNoise.getOctave(i).sample(limitX, limitY, limitZ, d7, (double) y * d7) / d3;
			d1 += this.maxNoise.getOctave(i).sample(limitX, limitY, limitZ, d7, (double) y * d7) / d3;
			if (i < 8)
			{
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
	protected double[] sampleNoiseColumn(int x, int z)
	{
		double[] ds = new double[this.noiseSizeY + 1];
		this.sampleNoiseColumn(ds, x, z);
		return ds;
	}


	/**
	 * Cursed. I didn't touched this. Leave it as is.
	 */
	public int getHeightOnGround(int chunkX, int chunkZ, Heightmap.Type heightmapType)
	{
		int minX = Math.floorDiv(chunkX, this.horizontalNoiseGranularity);
		int minZ = Math.floorDiv(chunkZ, this.horizontalNoiseGranularity);
		int modX = Math.floorMod(chunkX, this.horizontalNoiseGranularity);
		int modZ = Math.floorMod(chunkZ, this.horizontalNoiseGranularity);
		double xFactor = (double) modX / (double) this.horizontalNoiseGranularity;
		double zFactor = (double) modZ / (double) this.horizontalNoiseGranularity;
		double[][] terrain2DArray = new double[][] { this.sampleNoiseColumn(minX, minZ), this.sampleNoiseColumn(minX, minZ + 1), this.sampleNoiseColumn(minX + 1, minZ), this.sampleNoiseColumn(minX + 1, minZ + 1) };

		for (int noiseY = this.noiseSizeY - 1; noiseY >= 0; --noiseY)
		{
			double d2 = terrain2DArray[0][noiseY];
			double d3 = terrain2DArray[1][noiseY];
			double d4 = terrain2DArray[2][noiseY];
			double d5 = terrain2DArray[3][noiseY];
			double d6 = terrain2DArray[0][noiseY + 1];
			double d7 = terrain2DArray[1][noiseY + 1];
			double d8 = terrain2DArray[2][noiseY + 1];
			double d9 = terrain2DArray[3][noiseY + 1];

			for (int yOffset = this.verticalNoiseGranularity - 1; yOffset >= 0; --yOffset)
			{
				double yFactor = (double) yOffset / (double) this.verticalNoiseGranularity;
				double finalNoise = MathHelper.lerp3(yFactor, xFactor, zFactor, d2, d6, d4, d8, d3, d7, d5, d9);
				int y = noiseY * this.verticalNoiseGranularity + yOffset;
				if (finalNoise > 0.0D)
				{
					BlockState blockstate;
					if (finalNoise > 0.0D)
					{
						blockstate = this.defaultBlock;
					}
					else
					{
						blockstate = this.defaultFluid;
					}

					if (heightmapType.getBlockPredicate().test(blockstate))
					{
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
	public void buildSurface(ChunkRegion region, Chunk chunk)
	{
		ChunkPos chunkpos = chunk.getPos();
		int chunkXSeed = chunkpos.x;
		int chunkZSeed = chunkpos.z;
		ChunkRandom sharedseedrandom = new ChunkRandom();
		sharedseedrandom.setSeed(chunkXSeed, chunkZSeed);
		ChunkPos chunkpos1 = chunk.getPos();
		int chunkX = chunkpos1.getStartX();
		int chunkZ = chunkpos1.getStartZ();
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

		for (int x = 0; x < 16; ++x)
		{
			for (int z = 0; z < 16; ++z)
			{
				int xPos = chunkX + x;
				int zPos = chunkZ + z;
				int ySurface = chunk.sampleHeightmap(Heightmap.Type.WORLD_SURFACE_WG, x, z) + 1;
				double noise = this.surfaceDepthNoise.sample((double) xPos * 0.0625D, (double) zPos * 0.0625D, 0.0625D, (double) x * 0.0625D) * 10.0D;
				region.getBiome(blockpos$mutable.set(chunkX + x, ySurface, chunkZ + z)).buildSurface(sharedseedrandom, chunk, xPos, zPos, ySurface, noise, this.defaultBlock, this.defaultFluid, region.getSeaLevel(), this.world.getSeed());
			}
		}

		this.makeCeilingAndFloor(chunk, sharedseedrandom);
	}


	/**
	 * Creates the ceiling and floor that separates the dimension from the emptiness above y = 256 and below y = 0.
	 * 
	 * We use honeycomb blocks instead of Bedrock.
	 */
	protected void makeCeilingAndFloor(Chunk chunk, Random random)
	{
		BlockPos.Mutable blockpos$Mutable = new BlockPos.Mutable();
		int xStart = chunk.getPos().getStartX();
		int zStart = chunk.getPos().getStartZ();
		int roofHeight = 255;
		int floorHeight = 0;

		for (BlockPos blockpos : BlockPos.iterate(xStart, 0, zStart, xStart + 15, 0, zStart + 15))
		{
			//fills in gap between top of terrain gen and y = 255 with solid blocks
			for (int ceilingY = roofHeight; ceilingY >= roofHeight - 7; --ceilingY)
			{
				chunk.setBlockState(blockpos$Mutable.set(blockpos.getX(), ceilingY, blockpos.getZ()), Blocks.HONEYCOMB_BLOCK.getDefaultState(), false);
			}

			//single layer of solid blocks
			for (int floorY = floorHeight; floorY <= floorHeight; ++floorY)
			{
				chunk.setBlockState(blockpos$Mutable.set(blockpos.getX(), floorY, blockpos.getZ()), Blocks.HONEYCOMB_BLOCK.getDefaultState(), false);
			}
		}

	}


	/**
	 * Creates the actual terrain itself. It seems to go by cubic chunk-like when generating terrain?
	 */
	public void populateNoise(IWorld world, Chunk chunk)
	{
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

		for (int index = 0; index < this.noiseSizeZ + 1; ++index)
		{
			terrainNoise2DArray[0][index] = new double[this.noiseSizeY + 1];
			this.sampleNoiseColumn(terrainNoise2DArray[0][index], chunkX * this.noiseSizeX, chunkZ * this.noiseSizeZ + index);
			terrainNoise2DArray[1][index] = new double[this.noiseSizeY + 1];
		}

		ProtoChunk protoChunk = (ProtoChunk) chunk;
		Heightmap heightmap = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
		Heightmap heightmap1 = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);
		BlockPos.Mutable blockpos$Mutable = new BlockPos.Mutable();

		for (int xNoise = 0; xNoise < this.noiseSizeX; ++xNoise)
		{
			for (int zNoise = 0; zNoise < this.noiseSizeZ + 1; ++zNoise)
			{
				this.sampleNoiseColumn(terrainNoise2DArray[1][zNoise], chunkX * this.noiseSizeX + xNoise + 1, chunkZ * this.noiseSizeZ + zNoise);
			}

			for (int zNoise = 0; zNoise < this.noiseSizeZ; ++zNoise)
			{
				ChunkSection chunksection = protoChunk.getSection(15);
				chunksection.lock();

				for (int yNoise = this.noiseSizeY - 1; yNoise >= 0; --yNoise)
				{

					/*
					 * When the noise is greater than 16 (chunks), begin the mirroring effect
					 */
					if (yNoise > 16)
					{
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
					}
					else
					{
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

					for (int yInfluence = this.verticalNoiseGranularity - 1; yInfluence >= 0; --yInfluence)
					{

						//This is where the actual y position of the block that is going to be added to land.
						//Very important stuff. Handle with delicate care OR ELSE.
						int currentY = yChunk * this.verticalNoiseGranularity + yInfluence;

						int y = currentY & 15;
						int yChunkFinal = currentY >> 4;
						if (chunksection.getYOffset() >> 4 != yChunkFinal)
						{
							chunksection.unlock();
							chunksection = protoChunk.getSection(yChunkFinal);
							chunksection.lock();
						}

						double d5 = (double) yInfluence / (double) this.verticalNoiseGranularity;
						double d6 = MathHelper.lerp(d5, d16, d1);
						double d7 = MathHelper.lerp(d5, d18, d3);
						double d8 = MathHelper.lerp(d5, d17, d2);
						double d9 = MathHelper.lerp(d5, d0, d4);

						for (int horizontalNoiseX = 0; horizontalNoiseX < this.horizontalNoiseGranularity; ++horizontalNoiseX)
						{
							int xCoordinate = coordinateX + xNoise * this.horizontalNoiseGranularity + horizontalNoiseX;
							int xInChunk = xCoordinate & 15;
							double d10 = (double) horizontalNoiseX / (double) this.horizontalNoiseGranularity;
							double d11 = MathHelper.lerp(d10, d6, d7);
							double d12 = MathHelper.lerp(d10, d8, d9);

							for (int horizontalNoiseZ = 0; horizontalNoiseZ < this.horizontalNoiseGranularity; ++horizontalNoiseZ)
							{
								int zCoordinate = coordinateZ + zNoise * this.horizontalNoiseGranularity + horizontalNoiseZ;
								int zInChunk = zCoordinate & 15;
								double d13 = (double) horizontalNoiseZ / (double) this.horizontalNoiseGranularity;
								double d14 = MathHelper.lerp(d13, d11, d12);
								double finalTerrainNoise = MathHelper.clamp(d14 / 200.0D, -1.0D, 1.0D);

								BlockState blockstate;

								if (finalTerrainNoise > 0.0D)
								{
									//place the biome's solid block
									blockstate = this.defaultBlock;
								}
								else if (currentY < getSeaLevel())
								{
									//The sea
									blockstate = this.defaultFluid;
								}
								else
								{
									//The air
									blockstate = CAVE_AIR;
								}

								if (blockstate != CAVE_AIR)
								{
									if (blockstate.getLuminance() != 0)
									{
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
}