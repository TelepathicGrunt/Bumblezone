package com.telepathicgrunt.the_bumblezone.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;


public class SugarInfusedStone extends Block {

    public SugarInfusedStone() {
        super(QuiltBlockSettings.of(Material.STONE, MaterialColor.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F));
    }
}
