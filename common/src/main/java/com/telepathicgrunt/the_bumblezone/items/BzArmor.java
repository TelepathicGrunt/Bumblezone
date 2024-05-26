package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.platform.ItemExtension;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BzArmor extends ArmorItem implements ItemExtension {
    public BzArmor(Holder<ArmorMaterial> armorMaterial, ArmorItem.Type armorType, Properties properties) {
        super(armorMaterial, armorType, properties);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slotIndex, boolean selected) {
        if (entity instanceof Player player &&
            (player.getItemBySlot(EquipmentSlot.HEAD) == itemStack ||
            player.getItemBySlot(EquipmentSlot.CHEST) == itemStack ||
            player.getItemBySlot(EquipmentSlot.LEGS) == itemStack ||
            player.getItemBySlot(EquipmentSlot.FEET) == itemStack))
        {
            this.bz$onArmorTick(itemStack, level, player);
        }
    }

    public void bz$onArmorTick(ItemStack itemstack, Level world, Player player) { }
}
