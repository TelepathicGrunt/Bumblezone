package net.telepathicgrunt.bumblezone.blocks;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;


public class SugarInfusedCobblestone extends Block {

    public SugarInfusedCobblestone() {
        super(Block.Properties.create(Material.STONE).hardnessAndResistance(2.0F, 6.0F)));
    }
}
