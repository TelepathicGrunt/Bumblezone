package com.telepathicgrunt.the_bumblezone.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;


import net.minecraft.block.AbstractBlock;

public class BeeswaxPlanks extends Block {
    public BeeswaxPlanks() {
        super(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.COLOR_YELLOW).strength(0.3F, 0.3F).sound(SoundType.WOOD));
    }
}
