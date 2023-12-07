package com.telepathicgrunt.the_bumblezone.mixin.blocks;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StemBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StemBlock.class)
public interface StemBlockAccessor {
    @Accessor("fruit")
    ResourceKey<Block> getFruit();
}
