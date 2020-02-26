package net.telepathicgrunt.bumblezone.dimension;

import javax.annotation.Nullable;

import net.minecraft.client.audio.MusicTicker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.config.BzConfig;
import net.telepathicgrunt.bumblezone.generation.BzBiomeProvider;
import net.telepathicgrunt.bumblezone.generation.BzChunkGenerator;


@Mod.EventBusSubscriber(modid = Bumblezone.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BzWorldProvider extends Dimension
{
	public static boolean ACTIVE_WRATH = false;
	public static float reddishFogTint = 0;

	public BzWorldProvider(World world, DimensionType typeIn)
	{
		super(world, typeIn, 1.0f); //set 1.0f. I think it has to do with maximum brightness?

		/**
		 * Creates the light to brightness table. It changes how light levels looks to the players but does not change the
		 * actual values of the light levels.
		 */
		for (int i = 0; i <= 15; ++i)
		{
			this.lightBrightnessTable[i] = (float) i / 20.0F + 0.25F;
		}
	}

	
	/**
	 * Use our own biome provider and chunk generator for this dimension
	 */
	@Override
	public ChunkGenerator<?> createChunkGenerator()
	{
		return new BzChunkGenerator(world, new BzBiomeProvider(world), ChunkGeneratorType.SURFACE.createSettings());
	}
	
    /**
     * Use this to play music in the dimension. 
     */
    @Nullable
    @OnlyIn(Dist.CLIENT)
	public MusicTicker.MusicType getMusicType()
    {
        return null;
    }

	
	@Override
    public SleepResult canSleepAt(net.minecraft.entity.player.PlayerEntity player, BlockPos pos)
    {
        return SleepResult.DENY; //NO EXPLODING BEDS! But no sleeping too.
    }
	
	@Override
	public boolean canRespawnHere()
	{
		return false; //The bees disallow sleeping! 
	}

	@Override
	public BlockPos findSpawn(ChunkPos chunkPosIn, boolean checkValid)
	{
		return null;
	}

	@Override
	public BlockPos findSpawn(int posX, int posZ, boolean checkValid)
	{
		return null;
	}
	

	@Override
	public boolean shouldMapSpin(String entity, double x, double z, double rotation)
    {
        return true; //SPINNY MAPS!
    }

	@Override
	public double getMovementFactor()
    {
        return BzConfig.movementFactor; 
    }

	
	@Override
	public boolean isNether()
	{
		return false;
	}

	@Override
	public boolean isSurfaceWorld()
	{
		return false;
	}
	
	
	@Override
	public int getSeaLevel()
	{
		return 40;
	}


	/**
	 * Keep sky to noon always so sunset/sunrise color doesn't bleed into our fog
	 */
	@Override
	public float calculateCelestialAngle(long worldTime, float partialTicks)
	{
		if(BzConfig.dayNightCycle || BzConfig.fogBrightnessPercentage > 50)
		{
			return 0f;
		}
		else
		{
			return 0.5f;
		}
	}


	@OnlyIn(Dist.CLIENT)
	@Override
	public boolean isSkyColored()
	{
		return false;
	}


	@Override
	public boolean hasSkyLight()
	{
		return false;
	}


    @OnlyIn(Dist.CLIENT)
    @Nullable
	@Override
	public net.minecraftforge.client.IRenderHandler getSkyRenderer(){
    	return null;
    }
    
    
	@Nullable
	@OnlyIn(Dist.CLIENT)
	@Override
	public net.minecraftforge.client.IRenderHandler getWeatherRenderer()
	{
		return null;
	}


	@Nullable
	@OnlyIn(Dist.CLIENT)
	@Override
	public net.minecraftforge.client.IRenderHandler getCloudRenderer()
	{
		return null;
	}


	/**
	 * the y level at which clouds are rendered.
	 */
	@OnlyIn(Dist.CLIENT)
	@Override
	public float getCloudHeight()
	{
		return 280;
	}
	

	@Override
	public boolean canDoLightning(Chunk chunk)
    {
        return false;
    }

	
	@Override
	public boolean canDoRainSnowIce(Chunk chunk)
    {
        return false;
    }


	/**
	 * mimics vanilla Overworld sky timer
	 * 
	 * Returns a value between 0 and 1. .25 is dusk and .75 is dawn 0 is noon. 0.5 is midnight
	 */
	private float calculateVanillaSkyPositioning(long worldTime, float partialTicks)
	{
		double fractionComponent = MathHelper.frac((double) worldTime / 24000.0D - 0.25D);
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
	@Override
	public Vec3d getFogColor(float celestialAngle, float partialTicks)
	{
		float colorFactor = 1;
		Vec3d fogColor = Vec3d.ZERO; 
		
		if(BzConfig.dayNightCycle)
		{
			// Modifies the sky angle to be in range of 0 to 1 with 0 as night and 1 as day.
			float scaledAngle = Math.abs(0.5f - calculateVanillaSkyPositioning(this.getWorld().getDayTime(), 1.0F)) * 2;

			// Limits angle between 0 to 1 and sharply changes color between 0.333... and 0.666...
			colorFactor = Math.min(Math.max(scaledAngle * 3 - 1f, 0), 1);
			
			// Scales the returned factor by user chosen brightness.
			colorFactor *= (BzConfig.fogBrightnessPercentage/100);
		}
		else
		{
			/* The sky will be turned to midnight when brightness is below 50 and
			 * the day/night cycle is off. This lets us get the full range of brightness
			 * by utilizing the default brightness that the current celestial time gives.
			 */
			if(BzConfig.fogBrightnessPercentage <= 50)
			{
				colorFactor *= (BzConfig.fogBrightnessPercentage/50);
			}
			else
			{
				colorFactor *= (BzConfig.fogBrightnessPercentage/100);
			}
		}
		fogColor.add(Math.min(0.9f * colorFactor, 1.5f), Math.min(0.63f * colorFactor, 1.5f), Math.min(0.0015f * colorFactor, 1.5f));
		
		
		
		if(ACTIVE_WRATH && reddishFogTint < 0.25f)
		{
			reddishFogTint += 0.005f;
		}
		else if(reddishFogTint > 0)
		{
			reddishFogTint -= 0.005f;
		}
		fogColor.add(reddishFogTint, -reddishFogTint, -reddishFogTint);

		
		
		
		return fogColor;
	}


	/**
	 * Returns a double value representing the Y value relative to the top of the map at which void fog is at its maximum.
	 */
	@OnlyIn(Dist.CLIENT)
	@Override
	public double getVoidFogYFactor()
	{
		return 256;
	}
	

	/**
	 * Show fog at all?
	 */
	@Override
	public boolean doesXZShowFog(int x, int z)
	{
		return true;
	}
}
