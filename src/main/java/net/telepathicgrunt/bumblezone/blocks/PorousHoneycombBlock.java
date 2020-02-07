package net.telepathicgrunt.bumblezone.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;


public class PorousHoneycombBlock extends Block
{
	public PorousHoneycombBlock()
	{
		super(Block.Properties.create(Material.CLAY, MaterialColor.ADOBE).hardnessAndResistance(0.5F).sound(SoundType.CORAL));

		setRegistryName("porous_honeycomb_block");
	}
}
