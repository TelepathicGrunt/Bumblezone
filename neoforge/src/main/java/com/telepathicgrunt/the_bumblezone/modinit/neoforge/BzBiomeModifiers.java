package com.telepathicgrunt.the_bumblezone.modinit.neoforge;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.worldgen.neoforge.BzModCompatBiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class BzBiomeModifiers {
    public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIERS = DeferredRegister.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Bumblezone.MODID);

    public static final DeferredHolder<Codec<? extends BiomeModifier>, Codec<BzModCompatBiomeModifier>> BIOME_MODIFIER = BIOME_MODIFIERS.register("mod_compat_additions_modifier", () -> BzModCompatBiomeModifier.CODEC);
}
