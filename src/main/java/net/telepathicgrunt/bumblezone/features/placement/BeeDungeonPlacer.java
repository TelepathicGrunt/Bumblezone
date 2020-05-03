package net.telepathicgrunt.bumblezone.features.placement;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

import com.mojang.datafixers.Dynamic;

import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;


public class BeeDungeonPlacer extends Placement<NoPlacementConfig>
{
	public BeeDungeonPlacer(Function<Dynamic<?>, ? extends NoPlacementConfig> configFactory)
	{
		super(configFactory);
	}


	@Override
	public Stream<BlockPos> getPositions(IWorld world, ChunkGenerator<? extends GenerationSettings> chunkGenerator, Random random, NoPlacementConfig placementConfig, BlockPos pos)
	{
		ArrayList<BlockPos> validPositions = new ArrayList<BlockPos>();
		BlockPos.Mutable mutable = new BlockPos.Mutable(pos);
		boolean validSpot;

		for (int currentAttempt = 0; currentAttempt <= 8; currentAttempt++)
		{
			validSpot = true;
			int x = random.nextInt(16) + pos.getX();
			int z = random.nextInt(16) + pos.getZ();
			int y = random.nextInt(chunkGenerator.getMaxHeight() - chunkGenerator.getSeaLevel()) + chunkGenerator.getSeaLevel();

			for (Direction face : Direction.values())
			{
				mutable.setPos(x, y, z).move(face);

				if (world.getBlockState(mutable).getBlock() != Blocks.CAVE_AIR)
					validSpot = false;
			}

			if (validSpot)
				validPositions.add(mutable);
		}

		return validPositions.parallelStream();
	}
}
