package com.telepathicgrunt.the_bumblezone.items.materials;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class BeeArmorMaterial implements ArmorMaterial {
    public static final ArmorMaterial BEE_MATERIAL =
            new BeeArmorMaterial(Bumblezone.MODID + ":bee_material",
                    24,
                    new int[]{1, 2, 4, 2},
                    25,
                    SoundEvents.ARMOR_EQUIP_LEATHER,
                    0.5F,
                    0.0F);

    private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};
    private final String name;
    private final int durabilityMultiplier;
    private final int[] slotProtections;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;

    private BeeArmorMaterial(String name, int durabilityMultipiler, int[] slotProtections, int enchantmentValue, SoundEvent soundEvent, float toughness, float knockbackResistance) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultipiler;
        this.slotProtections = slotProtections;
        this.enchantmentValue = enchantmentValue;
        this.sound = soundEvent;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
    }

    public int getDurabilityForSlot(EquipmentSlot pSlot) {
        return HEALTH_PER_SLOT[pSlot.getIndex()] * this.durabilityMultiplier;
    }

    public int getDefenseForSlot(EquipmentSlot pSlot) {
        return this.slotProtections[pSlot.getIndex()];
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
