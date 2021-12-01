package com.telepathicgrunt.bumblezone.mixin;

import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(JigsawConfiguration.class)
public interface JigsawConfigurationAccessor {
    @Mutable
    @Accessor("maxDepth")
    void setMaxDepth(int maxDepth);
}
