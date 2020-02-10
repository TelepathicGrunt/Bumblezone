package net.telepathicgrunt.bumblezone.generation;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.LongFunction;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.gen.layer.ZoomLayer;
import net.minecraft.world.gen.layer.traits.IAreaTransformer1;
import net.telepathicgrunt.bumblezone.biome.BiomeInit;
import net.telepathicgrunt.bumblezone.generation.layer.BiomeLayer;


public class BumblezoneBiomeProvider extends BiomeProvider
{

	private final Layer genBiomes;


	public BumblezoneBiomeProvider(long seed, WorldType worldType)
	{
		super(BiomeInit.biomes);

		//generates the world and biome layouts
		Layer[] agenlayer = buildOverworldProcedure(seed, worldType);
		this.genBiomes = agenlayer[0];
	}


	public BumblezoneBiomeProvider(World world)
	{
		this(world.getSeed(), world.getWorldInfo().getGenerator());
		BiomeLayer.setSeed(world.getSeed());
	}


	public static <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> repeat(long seed, IAreaTransformer1 parent, IAreaFactory<T> incomingArea, int count, LongFunction<C> contextFactory)
	{
		IAreaFactory<T> iareafactory = incomingArea;

		for (int i = 0; i < count; ++i)
		{
			iareafactory = parent.apply(contextFactory.apply(seed + (long) i), iareafactory);
		}
		
		return iareafactory;
	}


	public static Layer[] buildOverworldProcedure(long seed, WorldType typeIn)
	{
		ImmutableList<IAreaFactory<LazyArea>> immutablelist = buildOverworldProcedure(typeIn, (p_215737_2_) ->
		{
			return new LazyAreaLayerContext(25, seed, p_215737_2_);
		});
		Layer genlayer = new Layer(immutablelist.get(0));
		Layer genlayer1 = new Layer(immutablelist.get(1));
		Layer genlayer2 = new Layer(immutablelist.get(2));
		return new Layer[] { genlayer, genlayer1, genlayer2 };
	}


	public static <T extends IArea, C extends IExtendedNoiseRandom<T>> ImmutableList<IAreaFactory<T>> buildOverworldProcedure(WorldType worldTypeIn, LongFunction<C> contextFactory)
	{
	    IAreaFactory<T> layer = BiomeLayer.INSTANCE.apply(contextFactory.apply(200L));
		layer = ZoomLayer.FUZZY.apply(contextFactory.apply(2000L), layer);
		layer = ZoomLayer.NORMAL.apply((IExtendedNoiseRandom<T>) contextFactory.apply(1001L), layer);
		layer = ZoomLayer.NORMAL.apply((IExtendedNoiseRandom<T>) contextFactory.apply(1001L), layer);
		return ImmutableList.of(layer, layer, layer);
	}


	public Set<Biome> getBiomesInArea(int centerX, int centerY, int centerZ, int sideLength)
	{
		int i = centerX - sideLength >> 2;
		int j = centerY - sideLength >> 2;
		int k = centerZ - sideLength >> 2;
		int l = centerX + sideLength >> 2;
		int i1 = centerY + sideLength >> 2;
		int j1 = centerZ + sideLength >> 2;
		int k1 = l - i + 1;
		int l1 = i1 - j + 1;
		int i2 = j1 - k + 1;
		Set<Biome> set = Sets.newHashSet();

		for (int j2 = 0; j2 < i2; ++j2)
		{
			for (int k2 = 0; k2 < k1; ++k2)
			{
				for (int l2 = 0; l2 < l1; ++l2)
				{
					int xPos = i + k2;
					int yPos = j + l2;
					int zPos = k + j2;
					set.add(this.getBiomeForNoiseGen(xPos, yPos, zPos));
				}
			}
		}
		return set;
	}


	@Nullable
	public BlockPos locateBiome(int x, int z, int range, List<Biome> biomes, Random random)
	{
		int i = x - range >> 2;
		int j = z - range >> 2;
		int k = x + range >> 2;
		int l = z + range >> 2;
		int i1 = k - i + 1;
		int j1 = l - j + 1;
		BlockPos blockpos = null;
		int k1 = 0;

		for (int l1 = 0; l1 < i1 * j1; ++l1)
		{
			int i2 = i + l1 % i1 << 2;
			int j2 = j + l1 / i1 << 2;
			if (biomes.contains(this.getBiomeForNoiseGen(i2, k1, j2)))
			{
				if (blockpos == null || random.nextInt(k1 + 1) == 0)
				{
					blockpos = new BlockPos(i2, 0, j2);
				}

				++k1;
			}
		}

		return blockpos;
	}


	public boolean hasStructure(Structure<?> structureIn)
	{
		return this.hasStructureCache.computeIfAbsent(structureIn, (structure) ->
		{
			for (Biome biome : this.biomes)
			{
				if (biome.hasStructure(structure))
				{
					return true;
				}
			}

			return false;
		});
	}


	public Set<BlockState> getSurfaceBlocks()
	{
		if (this.topBlocksCache.isEmpty())
		{
			for (Biome biome : this.biomes)
			{
				this.topBlocksCache.add(biome.getSurfaceBuilderConfig().getTop());
			}
		}

		return this.topBlocksCache;
	}


	public Biome getBiomeForNoiseGen(int x, int y, int z)
	{
		return this.genBiomes.func_215738_a(x, z);
	}

}
