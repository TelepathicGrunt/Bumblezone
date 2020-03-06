package net.telepathicgrunt.bumblezone.dimension;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.generation.BzBiomeProvider;
import net.telepathicgrunt.bumblezone.generation.BzChunkGenerator;

import javax.annotation.Nullable;


public class BzDimension extends Dimension
{
	public static boolean ACTIVE_WRATH = false;
	public static float REDDISH_FOG_TINT = 0;


	public BzDimension(World world, DimensionType typeIn)
	{
		super(world, typeIn, 1.0f); //set 1.0f. I think it has to do with maximum brightness?

		/**
		 * Creates the light to brightness table. It changes how light levels looks to the players but does not change the
		 * actual values of the light levels.
		 */
		for (int i = 0; i <= 15; ++i)
		{
			this.lightLevelToBrightness[i] = (float) i / 20.0F + 0.25F;
		}
	}

	@Override
	public DimensionType getType()
	{
		return BzDimensionType.BUMBLEZONE_TYPE;
	}
	
	
	/**
	 * Use our own biome provider and chunk generator for this dimension
	 */
	@Override
	public ChunkGenerator<?> createChunkGenerator()
	{
		return new BzChunkGenerator(world, new BzBiomeProvider(world), ChunkGeneratorType.SURFACE.createSettings());
	}

	@Override
	public boolean canPlayersSleep()
	{
		return false; // no sleeping 
	}


	@Override
	public BlockPos getSpawningBlockInChunk(ChunkPos chunkPos, boolean b) {
		return null;
	}

	@Override
	public BlockPos getTopSpawningBlockPosition(int i, int i1, boolean b) {
		return null;
	}
	

	public static double getMovementFactor()
	{
		return Bumblezone.BZ_CONFIG.movementFactor;
	}


	@Override
	public boolean isNether()
	{
		return false;
	}


	@Override
	public boolean hasGround()
	{
		return true;
	}

	/**
	 * Keep sky to noon always so sunset/sunrise color doesn't bleed into our fog
	 */
	@Override
	public float getSkyAngle(long worldTime, float partialTicks)
	{
		if (Bumblezone.BZ_CONFIG.dayNightCycle || Bumblezone.BZ_CONFIG.fogBrightnessPercentage > 50)
		{
			return 0f;
		}
		else
		{
			return 0.5f;
		}
	}

	@Override
	public boolean hasVisibleSky()
	{
		return false;
	}


	@Override
	public boolean hasSkyLight()
	{
		return false;
	}


	/**
	 * the y level at which clouds are rendered.
	 */
	@Environment(EnvType.CLIENT)
	@Override
	public float getCloudHeight()
	{
		return 280;
	}


	/**
	 * mimics vanilla Overworld sky timer
	 * 
	 * Returns a value between 0 and 1. .25 is dusk and .75 is dawn 0 is noon. 0.5 is midnight
	 */
	public float calculateVanillaSkyPositioning(long worldTime, float partialTicks)
	{
		double fractionComponent = MathHelper.fractionalPart((double) worldTime / 24000.0D - 0.25D);
		double d1 = 0.5D - Math.cos(fractionComponent * Math.PI) / 2.0D;
		return (float) (fractionComponent * 2.0D + d1) / 3.0F;
	}

	/**
	 * Returns fog color
	 * 
	 * What I done is made it be based on the day/night cycle so the fog will darken at night but brighten during day.
	 * calculateVanillaSkyPositioning returns a value which is between 0 and 1 for day/night and fogChangeSpeed is the range
	 * that the fog color will cycle between.
	 */
	@Environment(EnvType.CLIENT)
	public Vec3d modifyFogColor(Vec3d vec3d, float tickDelta) {
		float colorFactor = 1;

		if (Bumblezone.BZ_CONFIG.dayNightCycle)
		{
			// Modifies the sky angle to be in range of 0 to 1 with 0 as night and 1 as day.
			float scaledAngle = Math.abs(0.5f - calculateVanillaSkyPositioning(this.world.getTime(), 1.0F)) * 2;

			// Limits angle between 0 to 1 and sharply changes color between 0.333... and 0.666...‬
			colorFactor = Math.min(Math.max(scaledAngle * 3 - 1f, 0), 1);

			// Scales the returned factor by user chosen brightness.
			colorFactor *= (Bumblezone.BZ_CONFIG.fogBrightnessPercentage / 100);
		}
		else
		{
			/*
			 * The sky will be turned to midnight when brightness is below 50 and the day/night cycle is off. This lets us get the
			 * full range of brightness by utilizing the default brightness that the current celestial time gives.
			 */
			if (Bumblezone.BZ_CONFIG.fogBrightnessPercentage <= 50)
			{
				colorFactor *= (Bumblezone.BZ_CONFIG.fogBrightnessPercentage / 50);
			}
			else
			{
				colorFactor *= (Bumblezone.BZ_CONFIG.fogBrightnessPercentage / 100);
			}
		}

		if (ACTIVE_WRATH && REDDISH_FOG_TINT < 0.5f)
		{
			REDDISH_FOG_TINT += 0.00004f;
		}
		else if (REDDISH_FOG_TINT > 0)
		{
			REDDISH_FOG_TINT -= 0.00004f;
		}

		return new Vec3d(
				Math.min(0.9f * colorFactor, 1.1f + REDDISH_FOG_TINT),
					Math.min(0.63f * colorFactor, 1.1f) - REDDISH_FOG_TINT * 0.4f,
				Math.min(0.0015f * colorFactor, 1.1f) - REDDISH_FOG_TINT * 1.75f);
	}

	/**
	 * Returns a double value representing the Y value relative to the top of the map at which void fog is at its maximum.
	 */
	@Environment(EnvType.CLIENT)
	@Override
	public double getHorizonShadingRatio()
	{
		return 256;
	}


	/**
	 * Show fog at all?
	 */
	@Override
	public boolean isFogThick(int x, int z)
	{
		return true;
	}


}
