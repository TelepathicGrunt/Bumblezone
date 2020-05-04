package net.telepathicgrunt.bumblezone.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;


public class HoneyCrystalBlock extends Block
{

	public HoneyCrystalBlock()
	{
		super(Block.Properties.create(Material.GLASS, MaterialColor.ADOBE).hardnessAndResistance(0.1F, 0.1F));
	}
}
