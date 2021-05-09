package com.telepathicgrunt.the_bumblezone.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;


import net.minecraft.block.AbstractBlock;

public class SugarInfusedCobblestone extends Block {

    public SugarInfusedCobblestone() {
        super(AbstractBlock.Properties.of(Material.STONE).strength(2.0F, 6.0F));
    }
}
