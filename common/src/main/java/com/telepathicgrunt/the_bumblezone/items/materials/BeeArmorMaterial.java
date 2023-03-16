package com.telepathicgrunt.the_bumblezone.items.materials;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;

public class BeeArmorMaterial implements ArmorMaterial {
    public static final ArmorMaterial BEE_MATERIAL =
            new BeeArmorMaterial(Bumblezone.MODID + ":bee_material",
                    24,
                    Util.make(new EnumMap<>(ArmorItem.Type.class), (enumMap) -> {
                        enumMap.put(ArmorItem.Type.BOOTS, 1);
                        enumMap.put(ArmorItem.Type.LEGGINGS, 2);
                        enumMap.put(ArmorItem.Type.CHESTPLATE, 4);
                        enumMap.put(ArmorItem.Type.HELMET, 2);
                    }),
                    25,
                    SoundEvents.ARMOR_EQUIP_LEATHER,
                    0.5F,
                    0.0F);

    private static final EnumMap<ArmorItem.Type, Integer> HEALTH_FUNCTION_FOR_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), (enumMap) -> {
        enumMap.put(ArmorItem.Type.BOOTS, 13);
        enumMap.put(ArmorItem.Type.LEGGINGS, 15);
        enumMap.put(ArmorItem.Type.CHESTPLATE, 16);
        enumMap.put(ArmorItem.Type.HELMET, 11);
    });
    
    private final String name;
    private final int durabilityMultiplier;
    private final EnumMap<ArmorItem.Type, Integer> slotProtections;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;

    private BeeArmorMaterial(String name, int durabilityMultipiler, EnumMap<ArmorItem.Type, Integer> slotProtections, int enchantmentValue, SoundEvent soundEvent, float toughness, float knockbackResistance) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultipiler;
        this.slotProtections = slotProtections;
        this.enchantmentValue = enchantmentValue;
        this.sound = soundEvent;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return HEALTH_FUNCTION_FOR_TYPE.get(type) * this.durabilityMultiplier;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return this.slotProtections.get(type);
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public SoundEvent getEquipSound() {
        return this.sound;
    }

    public Ingredient getRepairIngredient() {
        return Ingredient.of(Items.AIR);
    }

    public String getName() {
        return this.name;
    }

    public float getToughness() {
        return this.toughness;
    }

    /**
     * Gets the percentage of knockback resistance provided by armor of the material.
     */
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}
