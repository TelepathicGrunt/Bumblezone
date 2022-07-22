package com.telepathicgrunt.the_bumblezone.blocks;

import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;


public class SugarInfusedCobblestone extends Block {

    public SugarInfusedCobblestone() {
        super(QuiltBlockSettings.of(Material.STONE).requiresTool().strength(2.0F, 6.0F));
    }
}
