package net.telepathicgrunt.bumblezone.features.carvers;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.Dynamic;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.world.gen.carver.CaveWorldCarver;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;


public class HoneycombCaves extends CaveWorldCarver
{
	public HoneycombCaves(Function<Dynamic<?>, ? extends ProbabilityConfig> config, int height)
	{
		super(config, height);
		Set<Block> blockSet = new HashSet<Block>();
		blockSet.add(Blocks.HONEY_BLOCK);
		blockSet.add(Blocks.HONEYCOMB_BLOCK);
		blockSet.add(BzBlocks.FILLED_POROUS_HONEYCOMB.get());
		blockSet.add(BzBlocks.POROUS_HONEYCOMB.get());
		this.carvableBlocks = blockSet;
		this.carvableFluids = ImmutableSet.of(Fluids.LAVA, Fluids.WATER, BzBlocks.SUGAR_WATER_FLUID.get());
	}

}
