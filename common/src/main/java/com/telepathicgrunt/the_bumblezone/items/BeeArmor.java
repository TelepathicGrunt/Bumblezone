package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public abstract class BeeArmor extends BzArmor {
    private final int variant;
    private final boolean transTexture;

    public BeeArmor(Properties properties, int variant, boolean transTexture) {
        super(properties);
        this.variant = variant;
        this.transTexture = transTexture;
    }

    public boolean hasTransTexture() {
        return transTexture;
    }

    public int getVariant() {
        return variant;
    }

    public static int getBeeThemedWearablesCount(Entity entity) {
        int beeWearablesCount = 0;
        if (entity instanceof LivingEntity livingEntity) {
            for (EquipmentSlot equipmentSlot : EquipmentSlotGroup.ARMOR) {
                if (equipmentSlot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                    ItemStack armor = livingEntity.getItemBySlot(equipmentSlot);
                    if (armor.is(BzTags.BZ_ARMOR_ABILITY_ENHANCING_WEARABLES)) {
                        if (isAllowedBeeArmorBoosting(armor)) {
                            beeWearablesCount++;
                        }
                    }
                }
            }
        }
        for (ModCompat compat : ModChecker.CUSTOM_EQUIPMENT_SLOTS_COMPATS) {
            beeWearablesCount += compat.getNumberOfMatchingEquippedItemsInCustomSlots(entity, (itemStack) -> {
                // TODO: Re-enable when backpacked updates
//                if (itemStack.is(BzTags.BZ_ARMOR_ABILITY_ENHANCING_WEARABLES)) {
//                    return !ModChecker.backpackedPresent || BackpackedCompat.isBackpackedHoneyThemedOrOtherItem(itemStack);
//                }
                return false;
            });
        }
        return beeWearablesCount;
    }

    private static boolean isAllowedBeeArmorBoosting(ItemStack armor) {
        for (ModCompat compat : ModChecker.BEE_WEARABLES_BOOSTING_COMPATS) {
            if (compat.isItemExplicitlyDisallowedFromBeeWearablesBoosting(armor)) {
                return false;
            }
        }
        return true;
    }
}