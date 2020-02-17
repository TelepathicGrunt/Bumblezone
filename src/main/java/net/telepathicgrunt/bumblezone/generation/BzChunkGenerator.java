package net.telepathicgrunt.bumblezone.generation;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.OverworldChunkGeneratorConfig;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.features.decorators.BzPlacingUtils;


public class BzChunkGenerator extends BzNoiseChunkGenerator<OverworldChunkGeneratorConfig>
{
	/*
	 * Used in getBiomeNoiseColumn to get the height of land by sampling the height of all biomes around it
	 */
	private static final float[] field_222576_h = Util.make(new float[25], (p_222575_0_) ->
	{
		for (int i = -2; i <= 2; ++i)
		{
			for (int j = -2; j <= 2; ++j)
			{
				float f = 10.0F / MathHelper.sqrt((float) ((i * i) + (j * j)) + 0.2F);
				p_222575_0_[(i + 2) + (j + 2) * 5] = f;
			}
		}

	});
	private final OctavePerlinNoiseSampler depthNoise;


	public BzChunkGenerator(IWorld world, BiomeSource provider, OverworldChunkGeneratorConfig settingsIn)
	{
		super(world, provider, 4, 8, 256, settingsIn);
		this.randomSeed.consume(2620); 
		this.depthNoise = new OctavePerlinNoiseSampler(this.randomSeed, 15, 0);
	}
	

	private static final Biome.SpawnEntry INITIAL_SLIME_ENTRY = new Biome.SpawnEntry(EntityType.SLIME, 1, 1, 1);
	private static final Biome.SpawnEntry INITIAL_BEE_ENTRY = new Biome.SpawnEntry(EntityType.BEE, 1, 4, 4);


	/**
	 * Dedicated to spawning slimes/bees when generating chunks initially.
	 * This is so there's lots of bees and the slime can spawn despite the 
	 * slime having extremely restrictive spawning mechanics.
	 * 
	 * This is mainly vanilla code but with biome$spawnlistentry changed to 
	 * use bee/slime and the restrictive terrain check called on the entity removed.
	 * The height is also restricted so the mob cannot spawn on the ceiling of this
	 * dimension as well.
	 */
	public void populateEntities(ChunkRegion region)
	{
		int xChunk = region.getCenterChunkX();
		int zChunk = region.getCenterChunkZ();
		int xCord = xChunk << 4;
		int zCord = zChunk << 4;
		Biome biome = region.getBiome((new ChunkPos(xChunk, zChunk)).getCenterBlockPos());
		ChunkRandom sharedseedrandom = new ChunkRandom();
		sharedseedrandom.setSeed(region.getSeed(), xCord, zCord);

		while (sharedseedrandom.nextFloat() < biome.getMaxSpawnLimit())
		{
			//30% of time, spawn slime. Otherwise, spawn bees.
			Biome.SpawnEntry biome$spawnlistentry = sharedseedrandom.nextFloat() < 0.4f ? INITIAL_SLIME_ENTRY : INITIAL_BEE_ENTRY;
			
			
			EntityData ilivingentitydata = null;
			int startingX = xCord + sharedseedrandom.nextInt(16);
			int startingZ = zCord + sharedseedrandom.nextInt(16);
			int currentX = startingX;
			int currentZ = startingZ;

			BlockPos.Mutable blockpos = new BlockPos.Mutable(currentX, 0, currentZ);
			int height = BzPlacingUtils.topOfSurfaceBelowHeight(region, sharedseedrandom.nextInt(255), 0, blockpos) + 1;

			if (biome$spawnlistentry.type.isSummonable() && height > 0 && height < 255)
			{
				float width = biome$spawnlistentry.type.getWidth();
				double xLength = MathHelper.clamp((double) startingX, (double) xCord + (double) width, (double) xCord + 16.0D - (double) width);
				double zLength = MathHelper.clamp((double) startingZ, (double) zCord + (double) width, (double) zCord + 16.0D - (double) width);

				Entity entity;
				try
				{
					entity = biome$spawnlistentry.type.create(region.getWorld());
				}
				catch (Exception exception)
				{
					Bumblezone.LOGGER.warn("Failed to create mob", (Throwable) exception);
					continue;
				}

				entity.refreshPositionAndAngles(xLength, (double) height, zLength, sharedseedrandom.nextFloat() * 360.0F, 0.0F);
				if (entity instanceof MobEntity)
				{
					MobEntity mobentity = (MobEntity) entity;
					if (mobentity.canSpawn(region, SpawnType.CHUNK_GENERATION) && mobentity.canSpawn(region))
					{
						ilivingentitydata = mobentity.initialize(region, region.getLocalDifficulty(new BlockPos(mobentity)), SpawnType.CHUNK_GENERATION, ilivingentitydata, (CompoundTag) null);
						region.spawnEntity(mobentity);
					}
				}
			}
		}
	}

	
	/**
	 * For spawning specific mobs in certain places like structures.
	 */
	public List<Biome.SpawnEntry> getEntitySpawnList(EntityCategory creatureType, BlockPos pos)
	{
		return super.getEntitySpawnList(creatureType, pos);
	}

	
	/**
	 * This is what kicks start the actual terrain shaping of the biomes.
	 * 
	 * The parameters passed in setupPerlinNoiseGenerators is used to 
	 * manipulate the perlin noise generator. This is extremely important.
	 * The values used is carefully tailored for this dimension.
	 */
	protected void sampleNoiseColumn(double[] areaArrayIn, int x, int z)
	{
		//We step fast in x, slower in z, and neutralish in y.
		this.setupNoiseGenerators(areaArrayIn, x, z, 2600D, 250D, 600D, 8D, 4D, 3D, -10, -10);
	}


	/**
	 * Handles the smoothing between biomes of different heights.
	 * 
	 * Lower the number after d1 *=  to make the transition sharper and
	 * increase it to make it even smoother. Note, by decreasing the number,
	 * some biomes might lower in overall height so compensate for that.
	 */
	protected double computeNoiseFalloff(double depth, double scale, int y)
	{
		double d1 = ((double) y - (8.5D + depth * 8.5D / 8.0D * 4.0D)) * 12.0D * 128.0D / 256.0D / scale;
		if (d1 < 0.0D)
		{
			d1 *= 2.0D;
		}

		return d1;
	}


	/**
	 * Using the biome's depth and scale + nearby biome heights,
	 * it gets what the final height should be at each location.
	 */
	protected double[] computeNoiseRange(int x, int z)
	{
		double[] adouble = new double[2];
		float f = 0.0F;
		float f1 = 0.0F;
		float f2 = 0.0F;
		int y = this.getSeaLevel();
		float f3 = this.biomeSource.getBiomeForNoiseGen(x, y, z).getDepth();

		for (int j = -2; j <= 2; ++j)
		{
			for (int k = -2; k <= 2; ++k)
			{
				Biome biome = this.biomeSource.getBiomeForNoiseGen(x + j, y, z + k);
				float depthWeight = biome.getDepth();
				float scaleWeight = biome.getScale();

				depthWeight = 1.0F + depthWeight * 1.10F; // overall height of biome
				scaleWeight = 1.0F + scaleWeight * 10.00F; // sharp variation in land height

				float f6 = field_222576_h[j + 2 + (k + 2) * 5] / (depthWeight + 5.0F); // smooth out height by sampling nearby heights
				if (biome.getDepth() > f3)
				{
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
	private double getNoiseDepthAt(int x, int z)
	{
		double noise = this.depthNoise.sample((double) (x * 200), 10.0D, (double) (z * 200), 1.0D, 0.0D, true) * 65535.0D / 8000.0D;
		if (noise < 0.0D)
		{
			noise = -noise * 0.3D;
		}

		noise = noise * 3.0D - 2.0D;
		if (noise < 0.0D)
		{
			noise = noise / 28.0D;
		}
		else
		{
			if (noise > 1.0D)
			{
				noise = 1.0D;
			}

			noise = noise / 40.0D;
		}

		return noise;
	}


	/**
	 * For spawning mobs that uses specific spawners.
	 * 
	 * Think Phantoms for tired players, Pillagers for
	 * Pillager outposts, cats in Witch Huts, and Zombie
	 * Sieges.
	 */
	public void spawnEntities(ServerWorld world, boolean spawnHostileMobs, boolean spawnPeacefulMobs)
	{
	}

	/**
	 * Ground height duh. For spawning purposes which isn't really needed for our dimension.
	 */
	public int getSpawnHeight()
	{
		return getSeaLevel() + 1;
	}
}