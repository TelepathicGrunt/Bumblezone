package com.telepathicgrunt.the_bumblezone.modinit.forge;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.world.forge.BzModCompatBiomeModifier;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BzBiomeModifiers {
    public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Bumblezone.MODID);

    public static final RegistryObject<Codec<BzModCompatBiomeModifier>> BIOME_MODIFIER = BIOME_MODIFIERS.register("mod_compat_additions_modifier", () -> BzModCompatBiomeModifier.CODEC);
}
