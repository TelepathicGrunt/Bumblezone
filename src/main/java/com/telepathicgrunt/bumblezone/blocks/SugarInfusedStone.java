package com.telepathicgrunt.bumblezone.blocks;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MapColor;


public class SugarInfusedStone extends Block {

    public SugarInfusedStone() {
        super(FabricBlockSettings.of(Material.STONE, MapColor.STONE_GRAY).strength(1.5F, 6.0F).build());
    }
}
