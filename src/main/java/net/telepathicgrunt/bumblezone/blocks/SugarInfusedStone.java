package net.telepathicgrunt.bumblezone.blocks;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;


public class SugarInfusedStone extends Block
{

	public SugarInfusedStone()
	{
		super(FabricBlockSettings.of(Material.STONE, MaterialColor.STONE).strength(1.5F, 6.0F).build());
	}
}
