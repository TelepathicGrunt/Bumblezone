package com.telepathicgrunt.bumblezone.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;


public class BeeswaxPlanks extends Block {
    public BeeswaxPlanks() {
        super(Block.Properties.create(Material.WOOD, MaterialColor.YELLOW).hardnessAndResistance(0.3F, 0.3F).sound(SoundType.WOOD));
    }
}
