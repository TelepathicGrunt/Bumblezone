package com.telepathicgrunt.bumblezone.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;


public class BeeswaxPlanks extends Block {
    public BeeswaxPlanks() {
        super(FabricBlockSettings.of(Material.WOOD, MapColor.YELLOW).strength(0.3F, 0.3F).sounds(BlockSoundGroup.WOOD));
    }
}
