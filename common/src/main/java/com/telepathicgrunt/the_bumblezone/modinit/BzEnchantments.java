package com.telepathicgrunt.the_bumblezone.modinit;

import com.mojang.serialization.Codec;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.enchantments.datacomponents.CombCutterMarker;
import com.telepathicgrunt.the_bumblezone.enchantments.datacomponents.ParalyzeMarker;
import com.telepathicgrunt.the_bumblezone.enchantments.datacomponents.PoisonMarker;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;

public class BzEnchantments {
    public static final ResourceLocation COMB_CUTTER = ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "comb_cutter");
    public static final ResourceLocation POTENT_POISON = ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "potent_poison");
    public static final ResourceLocation NEUROTOXINS = ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "neurotoxins");

    public static final ResourcefulRegistry<DataComponentType<?>> ENCHANTMENT_EFFECT_COMPONENT_TYPE = ResourcefulRegistries.create(BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, Bumblezone.MODID);

    public static final RegistryEntry<DataComponentType<ParalyzeMarker>> PARALYZE_MARKER = ENCHANTMENT_EFFECT_COMPONENT_TYPE.register("paralyze", () -> buildPersistentComponent(ParalyzeMarker.CODEC));
    public static final RegistryEntry<DataComponentType<PoisonMarker>> POISON_MARKER = ENCHANTMENT_EFFECT_COMPONENT_TYPE.register("poison", () -> buildPersistentComponent(PoisonMarker.CODEC));
    public static final RegistryEntry<DataComponentType<CombCutterMarker>> COMB_CUTTER_MARKER = ENCHANTMENT_EFFECT_COMPONENT_TYPE.register("comb_cutter", () -> buildPersistentComponent(CombCutterMarker.CODEC));

    private static <T> DataComponentType<T> buildPersistentComponent(Codec<T> directCodec) {
        return DataComponentType.<T>builder().persistent(directCodec).cacheEncoding().build();
    }
}
