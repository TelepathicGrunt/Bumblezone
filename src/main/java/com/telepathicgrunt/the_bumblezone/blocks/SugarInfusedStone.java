package com.telepathicgrunt.the_bumblezone.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;


public class SugarInfusedStone extends Block {

    public SugarInfusedStone() {
        super(Block.Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(1.5F, 6.0F));
    }
}
