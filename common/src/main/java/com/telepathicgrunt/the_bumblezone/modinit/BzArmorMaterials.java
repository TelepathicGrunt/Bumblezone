package com.telepathicgrunt.the_bumblezone.modinit;

import com.google.common.collect.Maps;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.Map;

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
