package com.telepathicgrunt.the_bumblezone.mixin.fabric.fabricapi;

import net.fabricmc.fabric.impl.biome.modification.BiomeModificationContextImpl;
import net.minecraft.core.RegistryAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BiomeModificationContextImpl.class)
public interface BiomeModificationContextImplMixin {

    @Accessor("registries")
    RegistryAccess getRegistries();
}