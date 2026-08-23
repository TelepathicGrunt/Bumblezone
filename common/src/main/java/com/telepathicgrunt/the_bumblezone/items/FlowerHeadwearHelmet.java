package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.mixin.effects.MobEffectInstanceAccessor;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.platform.ItemExtension;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.Level;

import java.util.concurrent.atomic.AtomicReference;

public class FlowerHeadwearHelmet extends BzArmor implements ItemExtension {
    public static final ResourceKey<EquipmentAsset> FLOWER_HEADWEAR_EQUIPMENT_ASSET_ID = ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(Bumblezone.MODID, "flower_headwear"));

    public FlowerHeadwearHelmet(Properties properties) {
        super(properties
                .stacksTo(1)
                .durability(55)
                .repairable(BzTags.FLOWER_HEADWEAR_REPAIR_ITEMS)
                .component(
                        DataComponents.EQUIPPABLE,
                        Equippable.builder(EquipmentSlot.HEAD).setAsset(FLOWER_HEADWEAR_EQUIPMENT_ASSET_ID).build()
                ));
    }

    @Override
    public void bz$onArmorTick(ItemStack itemstack, Level level, Player player) {
        if (player.getCooldowns().isOnCooldown(itemstack)) {
            return;
        }

        int beeWearablesCount = BeeArmor.getBeeThemedWearablesCount(player);

        MobEffectInstance wrath = player.getEffect(BzEffects.WRATH_OF_THE_HIVE.holder());
        if (wrath != null) {
            if (wrath.getDuration() > 0) {
                ((MobEffectInstanceAccessor) wrath).bumblezone$callTickDownDuration();
                if (beeWearablesCount > 3) {
                    ((MobEffectInstanceAccessor) wrath).bumblezone$callTickDownDuration();
                }
            }

            if (!level.isClientSide() &&
                player.getRandom().nextFloat() < 0.002f &&
                itemstack.getMaxDamage() - itemstack.getDamageValue() > 1)
            {
                itemstack.hurtAndBreak(1, player, EquipmentSlot.HEAD);
            }
        }
    }

    public static ItemStack getFlowerHeadwear(LivingEntity entity) {

        for (EquipmentSlot equipmentSlot : EquipmentSlotGroup.ARMOR) {
            if (equipmentSlot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                ItemStack armor = entity.getItemBySlot(equipmentSlot);
                if (armor.getItem() instanceof FlowerHeadwearHelmet) {
                    if (entity instanceof Player player && player.getCooldowns().isOnCooldown(armor)) {
                        continue;
                    }

                    return armor;
                }
            }
        }

        AtomicReference<ItemStack> flowerStack = new AtomicReference<>(ItemStack.EMPTY);
        for (ModCompat compat : ModChecker.CUSTOM_EQUIPMENT_SLOTS_COMPATS) {
            compat.getNumberOfMatchingEquippedItemsInCustomSlots(entity, (itemStack) -> {
                if (itemStack.is(BzItems.FLOWER_HEADWEAR.get())) {
                    if (entity instanceof Player player && player.getCooldowns().isOnCooldown(itemStack)) {
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