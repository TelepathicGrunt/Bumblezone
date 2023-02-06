package com.telepathicgrunt.the_bumblezone.mixin.quilt.qsl;

import net.minecraft.core.RegistryAccess;
import org.quiltmc.qsl.worldgen.biome.impl.modification.BiomeModificationContextImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BiomeModificationContextImpl.class)
public interface BiomeModificationContextImplMixin {

    @Accessor("registries")
    RegistryAccess getRegistries();
}