package com.telepathicgrunt.the_bumblezone.modinit;

import com.google.common.collect.Maps;
import com.teamresourceful.resourcefullib.common.registry.HolderRegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Util;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class BzArmorMaterials {
    public static ArmorMaterial BEE_MATERIAL = new ArmorMaterial(
            24,
            makeDefense(1, 3, 4, 2, 4),
            25,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            0.5F,
            0.0F,
            BzTags.BEE_ARMOR_REPAIR_ITEMS,
            EquipmentAssets.LEATHER
    );

    private static Map<ArmorType, Integer> makeDefense(int boots, int legs, int chest, int helm, int body) {
        return Maps.newEnumMap(
                Map.of(ArmorType.BOOTS, boots, ArmorType.LEGGINGS, legs, ArmorType.CHESTPLATE, chest, ArmorType.HELMET, helm, ArmorType.BODY, body)
        );
    }
}
