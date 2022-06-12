package com.telepathicgrunt.the_bumblezone.modinit;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.world.biomemodifiers.AdditionsModifier;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class BzBiomeModifiers {
	public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Bumblezone.MODID);

	public static final RegistryObject<Codec<? extends BiomeModifier>> MODDED_ADDITIONS_MODIFIER = BIOME_MODIFIER_SERIALIZERS.register("additions_modifier", () -> AdditionsModifier.CODEC);
}
