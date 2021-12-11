package com.telepathicgrunt.the_bumblezone.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;


public class BeehiveBeeswax extends Block {
    public BeehiveBeeswax() {
        super(FabricBlockSettings.of(Material.WOOD, MaterialColor.COLOR_YELLOW).strength(0.3F, 0.3F).sound(SoundType.WOOD));
    }
}
