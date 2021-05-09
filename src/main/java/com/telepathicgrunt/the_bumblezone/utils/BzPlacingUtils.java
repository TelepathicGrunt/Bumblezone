package com.telepathicgrunt.the_bumblezone.utils;

import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class BzPlacingUtils
{
	/**
	 * Finds the first non-air land below to given height
	 * @param world - world to check blocks in
	 * @param startHeight - starting height to go down from
	 * @param position - x/z position to use
	 * @return - height of the first non-air block
	 */
	public static int topOfSurfaceBelowHeight(IWorld world, int startHeight, int minHeight, BlockPos position)
	{
		BlockPos.Mutable blockpos$Mutable = new BlockPos.Mutable(position.getX(), startHeight, position.getZ());

		//if height is inside a non-air block, move down until we reached an air block
		while (blockpos$Mutable.getY() > minHeight)
		{
			if (world.isEmptyBlock(blockpos$Mutable))
			{
				break;
			}

			blockpos$Mutable.move(Direction.DOWN);
		}

		//if height is an air block, move down until we reached a solid block. We are now on the surface of a piece of land
		while (blockpos$Mutable.getY() > minHeight)
		{
			if (!world.isEmptyBlock(blockpos$Mutable))
			{
				break;
			}

			blockpos$Mutable.move(Direction.DOWN);
		}

		return blockpos$Mutable.getY();
	}
	

	/**
	 * Finds the first non-solid land below to given height even through water
	 * @param world - world to check blocks in
	 * @param startHeight - starting height to go down from
	 * @param position - x/z position to use
	 * @return - height of the first non-air block
	 */
	public static int topOfSurfaceBelowHeightThroughWater(IWorld world, int startHeight, int minHeight, BlockPos position)
	{
		BlockPos.Mutable blockpos$Mutable = new BlockPos.Mutable(position.getX(), startHeight, position.getZ());

		//if height is inside a non-solid block, move down until we reached an air block
		while (blockpos$Mutable.getY() > minHeight)
		{
			if (world.isEmptyBlock(blockpos$Mutable) || world.getBlockState(blockpos$Mutable).getMaterial() == Material.WATER)
			{
				break;
			}

			blockpos$Mutable.move(Direction.DOWN);
		}

		//if height is a non-solid block, move down until we reached a solid block. We are now on the surface of a piece of land even underwater
		while (blockpos$Mutable.getY() > minHeight)
		{
			if (!world.isEmptyBlock(blockpos$Mutable) && world.getBlockState(blockpos$Mutable).getMaterial() != Material.WATER)
			{
				break;
			}

			blockpos$Mutable.move(Direction.DOWN);
		}

		return blockpos$Mutable.getY();
	}

	/**
	 * Finds the first solid ceiling above given height
	 * @param world - world to check blocks in
	 * @param startHeight - starting height to go up from
	 * @param position - x/z position to use
	 * @return - height of the first solid block
	 */
	public static int topOfCeilingAboveHeight(IWorld world, int startHeight, BlockPos position)
	{
		BlockPos.Mutable blockpos$Mutable = new BlockPos.Mutable(position.getX(), startHeight, position.getZ());

		// if height is inside a non-air block, move up until we reached an air block
		while (blockpos$Mutable.getY() < 255)
		{
			if (world.isEmptyBlock(blockpos$Mutable))
			{
				break;
			}

			blockpos$Mutable.move(Direction.UP);
		}

		// if height is an air block, move up until we reached a solid block. We are now
		// on the bottom of a piece of land
		while (blockpos$Mutable.getY() > 255)
		{
			if (!world.isEmptyBlock(blockpos$Mutable))
			{
				break;
			}

			blockpos$Mutable.move(Direction.UP);
		}

		return Math.min(blockpos$Mutable.getY(), 255);
	}
	

	/**
	 * Finds the first solid land below to given height through water
	 * @param world - world to check blocks in
	 * @param startHeight - starting height to down from
	 * @param position - x/z position to use
	 * @return - height of the first solid block
	 */
	public static int topOfUnderwaterSurfaceBelowHeight(IWorld world, int startHeight, BlockPos position)
	{
		BlockPos.Mutable blockpos$Mutable = new BlockPos.Mutable(position.getX(), startHeight, position.getZ());

		//If height is inside a non-air/water block, move down until we reached an air/waterlogged block
		//Treats waterlogged wood as non-waterlogged blocks.
		while (blockpos$Mutable.getY() > 74)
		{
			if (world.isEmptyBlock(blockpos$Mutable) || (world.getBlockState(blockpos$Mutable).getMaterial() == Material.WATER && world.getBlockState(blockpos$Mutable).getMaterial() != Material.WOOD))
			{
				break;
			}

			blockpos$Mutable.move(Direction.DOWN);
		}

		//if height is an air/waterlogged block, move down until we reached a solid block. We are now on the surface of a piece of land
		while (blockpos$Mutable.getY() > 74)
		{
			if (!world.isEmptyBlock(blockpos$Mutable) && !(world.getBlockState(blockpos$Mutable).getMaterial() == Material.WATER && world.getBlockState(blockpos$Mutable).getMaterial() != Material.WOOD))
			{
				break;
			}

			blockpos$Mutable.move(Direction.DOWN);
		}

		return blockpos$Mutable.getY();
	}
}
