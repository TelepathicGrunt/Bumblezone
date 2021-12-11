package com.telepathicgrunt.the_bumblezone.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;


public class SugarInfusedCobblestone extends Block {

    public SugarInfusedCobblestone() {
        super(BlockBehaviour.Properties.of(Material.STONE).strength(2.0F, 6.0F));
    }
}
