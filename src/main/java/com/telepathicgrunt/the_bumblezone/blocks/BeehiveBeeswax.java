package com.telepathicgrunt.the_bumblezone.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;


public class BeehiveBeeswax extends Block {
    public BeehiveBeeswax() {
        super(QuiltBlockSettings.of(Material.WOOD, MaterialColor.COLOR_YELLOW).strength(0.3F, 0.3F).sound(SoundType.WOOD));
    }
}
