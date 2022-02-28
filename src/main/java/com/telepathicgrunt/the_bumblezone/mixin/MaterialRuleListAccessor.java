package com.telepathicgrunt.the_bumblezone.mixin;

import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.material.MaterialRuleList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(MaterialRuleList.class)
public interface MaterialRuleListAccessor {
    @Accessor("materialRuleList")
    List<NoiseChunk.BlockStateFiller> getMaterialRuleList();
}
