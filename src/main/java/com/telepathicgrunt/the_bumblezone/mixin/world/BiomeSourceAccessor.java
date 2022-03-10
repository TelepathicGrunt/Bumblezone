package com.telepathicgrunt.the_bumblezone.mixin.world;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(BiomeSource.class)
public interface BiomeSourceAccessor {
    @Accessor("possibleBiomes")
    Set<Holder<Biome>> getPossibleBiomes();
}
