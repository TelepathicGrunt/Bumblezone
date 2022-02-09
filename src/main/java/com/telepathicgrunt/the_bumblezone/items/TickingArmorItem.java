package com.telepathicgrunt.the_bumblezone.items;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class TickingArmorItem extends ArmorItem {
    public TickingArmorItem(ArmorMaterial material, EquipmentSlot slot, Properties properties) {
        super(material, slot, properties);
    }

    public abstract void onArmorTick(ItemStack itemstack, Level world, Player entity);
}
