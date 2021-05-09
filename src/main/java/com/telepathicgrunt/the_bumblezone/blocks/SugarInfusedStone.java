package com.telepathicgrunt.the_bumblezone.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;


import net.minecraft.block.AbstractBlock;

public class SugarInfusedStone extends Block {

    public SugarInfusedStone() {
        super(AbstractBlock.Properties.of(Material.STONE, MaterialColor.STONE).strength(1.5F, 6.0F));
    }
}
