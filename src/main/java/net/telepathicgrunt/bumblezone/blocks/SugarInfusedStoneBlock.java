package net.telepathicgrunt.bumblezone.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;


public class SugarInfusedStoneBlock extends Block
{

	public SugarInfusedStoneBlock()
	{
		super(Block.Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(1.5F, 6.0F));
	}
}
