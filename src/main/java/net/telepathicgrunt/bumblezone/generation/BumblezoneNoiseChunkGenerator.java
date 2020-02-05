package net.telepathicgrunt.bumblezone.generation;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.INoiseGenerator;
import net.minecraft.world.gen.OctavesNoiseGenerator;
import net.minecraft.world.gen.PerlinNoiseGenerator;
import net.minecraft.world.gen.WorldGenRegion;

public abstract class BumblezoneNoiseChunkGenerator<T extends GenerationSettings> extends ChunkGenerator<T> {

	private static final BlockState STONE = Blocks.STONE.getDefaultState();
    private static final BlockState WATER = Blocks.WATER.getDefaultState();
    private static final BlockState AIR = Blocks.AIR.getDefaultState();
	
    
	private final int verticalNoiseGranularity;
	private final int horizontalNoiseGranularity;
	private final int noiseSizeX;
	private final int noiseSizeY;
	private final int noiseSizeZ;
	protected final SharedSeedRandom randomSeed;
	private final OctavesNoiseGenerator minNoise;
	private final OctavesNoiseGenerator maxNoise;
	private final OctavesNoiseGenerator mainNoise;
	private final INoiseGenerator surfaceDepthNoise;
	protected final BlockState defaultBlock;
	protected final BlockState defaultFluid;

	public BumblezoneNoiseChunkGenerator(IWorld world, BiomeProvider biomeProvider, int horizontalNoiseGranularityIn, int verticalNoiseGranularityIn, int maxHeight, T settings) {
		super(world, biomeProvider, settings);
		this.verticalNoiseGranularity = verticalNoiseGranularityIn;
		this.horizontalNoiseGranularity = horizontalNoiseGranularityIn;
		this.defaultBlock = STONE;
		this.defaultFluid = WATER;
		this.noiseSizeX = 16 / this.horizontalNoiseGranularity;
		this.noiseSizeY = maxHeight / this.verticalNoiseGranularity;
		this.noiseSizeZ = 16 / this.horizontalNoiseGranularity;
		this.randomSeed = new SharedSeedRandom(this.seed);
		this.minNoise = new OctavesNoiseGenerator(this.randomSeed, 15, 0);
		this.maxNoise = new OctavesNoiseGenerator(this.randomSeed, 15, 0);
		this.mainNoise = new OctavesNoiseGenerator(this.randomSeed, 7, 0);
		this.surfaceDepthNoise = (INoiseGenerator) (new PerlinNoiseGenerator(this.randomSeed, 3, 0));
	}

	private double internalSetupPerlinNoiseGenerators(int x, int y, int z, double getCoordinateScale, double getHeightScale, double getMainCoordinateScale, double getMainHeightScale, double p_222552_10_) {
		double d0 = 0.0D;
		double d1 = 0.0D;
		double d2 = 0.0D;
		double d3 = 1.0D;

		for (int i = 0; i < 16; ++i) {
			double limitX = OctavesNoiseGenerator.maintainPrecision((double) x * getCoordinateScale * d3);
			double limitY = OctavesNoiseGenerator.maintainPrecision((double) y * getHeightScale * d3);
			double limitZ = OctavesNoiseGenerator.maintainPrecision((double) z * getCoordinateScale * d3);

			double mainX = OctavesNoiseGenerator.maintainPrecision((double) x * getMainCoordinateScale * d3);
			double mainY = OctavesNoiseGenerator.maintainPrecision((double) y * getMainHeightScale * d3);
			double mainZ = OctavesNoiseGenerator.maintainPrecision((double) z * getMainCoordinateScale * d3);
			
			double d7 = 684.412F * d3;
			d0 += this.minNoise.getOctave(i).func_215456_a(limitX, limitY, limitZ, d7, (double) y * d7) / d3;
			d1 += this.maxNoise.getOctave(i).func_215456_a(limitX, limitY, limitZ, d7, (double) y * d7) / d3;
			if (i < 8) {
				d2 += this.mainNoise.getOctave(i).func_215456_a(mainX, mainY, mainZ, p_222552_10_ * d3, (double) y * p_222552_10_ * d3) / d3;
			}

			d3 /= 2.0D;
		}

		return MathHelper.clampedLerp(d0 / 512.0D, d1 / 512.0D, (d2 / 10.0D + 1.0D) / 2.0D);
	}

	protected double[] func_222547_b(int p_222547_1_, int p_222547_2_) {
		double[] adouble = new double[this.noiseSizeY + 1];
		this.fillNoiseColumn(adouble, p_222547_1_, p_222547_2_);
		return adouble;
	}

	protected void setupPerlinNoiseGenerators(double[] areaArrayIn, int x, int z, double getCoordinateScale, double getHeightScale, double getMainCoordinateScale, double getMainHeightScale, double p_222546_8_, double p_222546_10_, int p_222546_12_, int p_222546_13_) {
		double[] localAreaArray = this.getBiomeNoiseColumn(x, z);
		double d0 = localAreaArray[0];
		double d1 = localAreaArray[1];
		double d2 = this.func_222551_g();
		double d3 = this.func_222553_h();

		for (int y = 0; y < this.noiseSizeY(); ++y) {
			double d4 = this.internalSetupPerlinNoiseGenerators(x, y, z, getCoordinateScale, getHeightScale, getMainCoordinateScale, getMainHeightScale, p_222546_10_);
			d4 = d4 - this.func_222545_a(d0, d1, y);
			if ((double) y > d2) {
				d4 = MathHelper.clampedLerp(d4, (double) p_222546_13_, ((double) y - d2) / (double) p_222546_12_);
			} else if ((double) y < d3) {
				d4 = MathHelper.clampedLerp(d4, -30.0D, (d3 - (double) y) / (d3 - 1.0D));
			}

			areaArrayIn[y] = d4;
		}

	}

	protected abstract double[] getBiomeNoiseColumn(int noiseX, int noiseZ);

	protected abstract double func_222545_a(double p_222545_1_, double p_222545_3_, int p_222545_5_);

	protected double func_222551_g() {
		return (double) (this.noiseSizeY() - 4);
	}

	protected double func_222553_h() {
		return 0.0D;
	}

	public int func_222529_a(int chunkX, int chunkZ, Heightmap.Type heightmapType) {
		int minX = Math.floorDiv(chunkX, this.horizontalNoiseGranularity);
		int minZ = Math.floorDiv(chunkZ, this.horizontalNoiseGranularity);
		int modX = Math.floorMod(chunkX, this.horizontalNoiseGranularity);
		int modZ = Math.floorMod(chunkZ, this.horizontalNoiseGranularity);
		double xFactor = (double) modX / (double) this.horizontalNoiseGranularity;
		double zFactor = (double) modZ / (double) this.horizontalNoiseGranularity;
		double[][] terrain2DArray = new double[][] { this.func_222547_b(minX, minZ), this.func_222547_b(minX, minZ + 1), this.func_222547_b(minX + 1, minZ), this.func_222547_b(minX + 1, minZ + 1) };
		
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

					if (heightmapType.getHeightLimitPredicate().test(blockstate)) {
						return y + 1;
					}
				}
			}
		}

		return 0;
	}

	protected abstract void fillNoiseColumn(double[] p_222548_1_, int p_222548_2_, int p_222548_3_);

	public int noiseSizeY() {
		return this.noiseSizeY + 1;
	}

	public void buildSurface(WorldGenRegion region, IChunk chunk) {
		ChunkPos chunkpos = chunk.getPos();
		int i = chunkpos.x;
		int j = chunkpos.z;
		SharedSeedRandom sharedseedrandom = new SharedSeedRandom();
		sharedseedrandom.setBaseChunkSeed(i, j);
		ChunkPos chunkpos1 = chunk.getPos();
		int chunkX = chunkpos1.getXStart();
		int chunkZ = chunkpos1.getZStart();
	    BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

		for (int x = 0; x < 16; ++x) {
			for (int z = 0; z < 16; ++z) {
				int xPos = chunkX + x;
				int zPos = chunkZ + z;
				int ySurface = chunk.getTopBlockY(Heightmap.Type.WORLD_SURFACE_WG, x, z) + 1;
				double noise = this.surfaceDepthNoise.noiseAt((double) xPos * 0.0625D, (double) zPos * 0.0625D, 0.0625D, (double) x * 0.0625D) * 10.0D;
				region.getBiome(blockpos$mutable.setPos(chunkX + x, ySurface, chunkZ + z)).buildSurface(sharedseedrandom, chunk, xPos, zPos, ySurface, noise, this.defaultBlock, this.defaultFluid, 5, this.world.getSeed());
			}
		}

		this.makeBedrock(chunk, sharedseedrandom);
	}

	protected void makeBedrock(IChunk chunk, Random random) {
		BlockPos.Mutable blockpos$Mutable = new BlockPos.Mutable();
		int xStart = chunk.getPos().getXStart();
		int zStart = chunk.getPos().getZStart();
		int floorHeight = 0;
		int roofHeight = 255;

		for (BlockPos blockpos : BlockPos.getAllInBoxMutable(xStart, 0, zStart, xStart + 15, 0, zStart + 15)) {
			if (roofHeight > 0) {
				for (int floorY = roofHeight; floorY >= roofHeight - 4; --floorY) {
					if (floorY >= roofHeight - random.nextInt(5)) {
						chunk.setBlockState(blockpos$Mutable.setPos(blockpos.getX(), floorY, blockpos.getZ()), Blocks.field_226908_md_.getDefaultState(), false);
					}
				}
			}

			if (floorHeight < 256) {
				for (int ceilingY = floorHeight + 4; ceilingY >= floorHeight; --ceilingY) {
					if (ceilingY <= floorHeight + random.nextInt(5)) {
						chunk.setBlockState(blockpos$Mutable.setPos(blockpos.getX(), ceilingY, blockpos.getZ()), Blocks.field_226908_md_.getDefaultState(), false);
					}
				}
			}
		}

	}

	public void makeBase(IWorld world, IChunk chunk) {
		ChunkPos chunkpos = chunk.getPos();
		int chunkX = chunkpos.x;
		int chunkZ = chunkpos.z;
		int coordinateX = chunkX << 4;
		int coordinateZ = chunkZ << 4;
		int yNoiseMod;
		int yInfluenceMod;
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
			this.fillNoiseColumn(terrainNoise2DArray[0][index], chunkX * this.noiseSizeX, chunkZ * this.noiseSizeZ + index);
			terrainNoise2DArray[1][index] = new double[this.noiseSizeY + 1];
		}

		ChunkPrimer chunkprimer = (ChunkPrimer) chunk;
		Heightmap heightmap = chunkprimer.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
		Heightmap heightmap1 = chunkprimer.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);
		BlockPos.Mutable blockpos$Mutable = new BlockPos.Mutable();

		for (int xNoise = 0; xNoise < this.noiseSizeX; ++xNoise) {
			for (int zNoise = 0; zNoise < this.noiseSizeZ + 1; ++zNoise) {
				this.fillNoiseColumn(terrainNoise2DArray[1][zNoise], chunkX * this.noiseSizeX + xNoise + 1, chunkZ * this.noiseSizeZ + zNoise);
			}

			for (int zNoise = 0; zNoise < this.noiseSizeZ; ++zNoise) {
				ChunkSection chunksection = chunkprimer.func_217332_a(15);
				chunksection.lock();

				for (int yNoise = this.noiseSizeY - 1; yNoise >= 0; --yNoise) {
					if(yNoise > 16) 
					{
						yNoiseMod = 31 - yNoise;

						d16 = terrainNoise2DArray[0][zNoise][yNoiseMod + 1];
						d17 = terrainNoise2DArray[0][zNoise + 1][yNoiseMod +1];
						d18 = terrainNoise2DArray[1][zNoise][yNoiseMod + 1];
						d0 = terrainNoise2DArray[1][zNoise + 1][yNoiseMod + 1];
						d1 = terrainNoise2DArray[0][zNoise][yNoiseMod];
						d2 = terrainNoise2DArray[0][zNoise + 1][yNoiseMod ];
						d3 = terrainNoise2DArray[1][zNoise][yNoiseMod];
						d4 = terrainNoise2DArray[1][zNoise + 1][yNoiseMod];
					}
					else 
					{
						yNoiseMod = yNoise;

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
						
						//yInfluence and yNoise is what makes terrain more solid towards ground and now, towards ceiling too
						
							yInfluenceMod = yInfluence;
						
						int currentY = yNoise * this.verticalNoiseGranularity + yInfluenceMod;
						int y = currentY & 15;
						int yChunk = currentY >> 4;
						if (chunksection.getYLocation() >> 4 != yChunk) {
							chunksection.unlock();
							chunksection = chunkprimer.func_217332_a(yChunk);
							chunksection.lock();
						}

						double d5 = (double) yInfluenceMod / (double) this.verticalNoiseGranularity;
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
								
                                if (finalTerrainNoise > 0.0D)
                                {
                                	//place the biome's solid block
                                	blockstate = this.defaultBlock;
                                }
                            	else if (currentY < getSeaLevel())
                                {
                            		blockstate = this.defaultFluid;
                                } else {
									blockstate = AIR;
								}

								if (blockstate != AIR) {
									if (blockstate.getLightValue() != 0) {
										blockpos$Mutable.setPos(xCoordinate, currentY, zCoordinate);
										chunkprimer.addLightPosition(blockpos$Mutable);
									}

									chunksection.setBlockState(xInChunk, y, zInChunk, blockstate, false);
									heightmap.update(xInChunk, currentY, zInChunk, blockstate);
									heightmap1.update(xInChunk, currentY, zInChunk, blockstate);
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