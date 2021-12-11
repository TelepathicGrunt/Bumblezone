package com.telepathicgrunt.the_bumblezone.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;


public class SugarInfusedCobblestone extends Block {

    public SugarInfusedCobblestone() {
        super(FabricBlockSettings.of(Material.STONE).strength(2.0F, 6.0F));
    }
}
