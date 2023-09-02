package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.mixin.effects.MobEffectInstanceAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FlowerHeadwearHelmet extends BzDyeableArmor implements DyeableLeatherItem {
    public FlowerHeadwearHelmet(ArmorMaterial material, Type armorType, Properties properties) {
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
        int beeWearablesCount = BeeArmor.getBeeThemedWearablesCount(player);

        MobEffectInstance wrath = player.getEffect(BzEffects.WRATH_OF_THE_HIVE.get());
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
                itemstack.hurtAndBreak(1, player, (playerEntity) -> playerEntity.broadcastBreakEvent(EquipmentSlot.HEAD));
            }
        }
    }

    public static ItemStack getFlowerHeadware(Entity entity) {
        for(ItemStack armor : entity.getArmorSlots()) {
            if(armor.getItem() instanceof FlowerHeadwearHelmet) {
                return armor;
            }
        }
        return ItemStack.EMPTY;
    }
}