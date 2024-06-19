package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.mixin.effects.MobEffectInstanceAccessor;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.platform.ItemExtension;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.concurrent.atomic.AtomicReference;

public class FlowerHeadwearHelmet extends BzArmor implements ItemExtension {
    public FlowerHeadwearHelmet(Holder<ArmorMaterial> material, Type armorType, Properties properties) {
        super(material, armorType, properties);
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return repair.is(BzTags.FLOWER_HEADWEAR_REPAIR_ITEMS);
    }

    @Override
    public void bz$onArmorTick(ItemStack itemstack, Level world, Player player) {
        if (player.getCooldowns().isOnCooldown(itemstack.getItem())) {
            return;
        }

        int beeWearablesCount = BeeArmor.getBeeThemedWearablesCount(player);

        MobEffectInstance wrath = player.getEffect(BzEffects.WRATH_OF_THE_HIVE.holder());
        if (wrath != null) {
            if (wrath.getDuration() > 0) {
                ((MobEffectInstanceAccessor) wrath).callTickDownDuration();
                if (beeWearablesCount > 3) {
                    ((MobEffectInstanceAccessor) wrath).callTickDownDuration();
                }
            }

            if (!world.isClientSide() &&
                player.getRandom().nextFloat() < 0.002f &&
                itemstack.getMaxDamage() - itemstack.getDamageValue() > 1)
            {
                itemstack.hurtAndBreak(1, player, EquipmentSlot.HEAD);
            }
        }
    }

    public static ItemStack getFlowerHeadwear(LivingEntity entity) {

        for (ItemStack armor : entity.getArmorSlots()) {
            if (armor.getItem() instanceof FlowerHeadwearHelmet flowerHeadwearHelmet) {
                if (entity instanceof Player player && player.getCooldowns().isOnCooldown(flowerHeadwearHelmet)) {
                    continue;
                }

                return armor;
            }
        }

        AtomicReference<ItemStack> flowerStack = new AtomicReference<>(ItemStack.EMPTY);
        for (ModCompat compat : ModChecker.CUSTOM_EQUIPMENT_SLOTS_COMPATS) {
            compat.getNumberOfMatchingEquippedItemsInCustomSlots(entity, (itemStack) -> {
                if (itemStack.is(BzItems.FLOWER_HEADWEAR.get())) {
                    if (entity instanceof Player player && player.getCooldowns().isOnCooldown(itemStack.getItem())) {
                        return false;
                    }

                    flowerStack.set(itemStack);
                    return true;
                }
                return false;
            });
        }

        return flowerStack.get();
    }
}