package com.telepathicgrunt.the_bumblezone.modinit;

import com.teamresourceful.resourcefullib.common.registry.HolderRegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class BzArmorMaterials {
    public static final ResourcefulRegistry<ArmorMaterial> ARMOR_MATERIAL = ResourcefulRegistries.create(BuiltInRegistries.ARMOR_MATERIAL, Bumblezone.MODID);

    public static final HolderRegistryEntry<ArmorMaterial> BEE_MATERIAL = ARMOR_MATERIAL.registerHolder(
            "bee_material", () -> createArmorMaterial(
                    "bee_material",
                    Util.make(new EnumMap<>(ArmorItem.Type.class), enumMap -> {
                        enumMap.put(ArmorItem.Type.BOOTS, 1);
                        enumMap.put(ArmorItem.Type.LEGGINGS, 3);
                        enumMap.put(ArmorItem.Type.CHESTPLATE, 4);
                        enumMap.put(ArmorItem.Type.HELMET, 2);
                        enumMap.put(ArmorItem.Type.BODY, 4);
                    }),
                    25,
                    SoundEvents.ARMOR_EQUIP_LEATHER,
                    0.5F,
                    0.0F,
                    () -> Ingredient.of(BzTags.BEE_ARMOR_REPAIR_ITEMS)));


    private static ArmorMaterial createArmorMaterial(
            String layerName,
            EnumMap<ArmorItem.Type, Integer> enumMap,
            int enchantmentValue,
            Holder<SoundEvent> equipSound,
            float toughness,
            float knockback,
            Supplier<Ingredient> repairIngredient
    ) {
        EnumMap<ArmorItem.Type, Integer> defenseMap = new EnumMap<>(ArmorItem.Type.class);

        for(ArmorItem.Type type : ArmorItem.Type.values()) {
            defenseMap.put(type, enumMap.get(type));
        }

        List<ArmorMaterial.Layer> layers = List.of(new ArmorMaterial.Layer(new ResourceLocation(layerName)));

        return new ArmorMaterial(defenseMap, enchantmentValue, equipSound, repairIngredient, layers, toughness, knockback);
    }
}
