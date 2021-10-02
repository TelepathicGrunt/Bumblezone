package com.telepathicgrunt.bumblezone.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;


public class SugarInfusedStone extends Block {

    public SugarInfusedStone() {
        super(FabricBlockSettings.of(Material.STONE, MapColor.STONE_GRAY).strength(1.5F, 6.0F));
    }
}
