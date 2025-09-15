package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.platform.ItemExtension;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BzArmor extends Item implements ItemExtension {
    public BzArmor(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity entity, EquipmentSlot equipmentSlot) {
        if (entity instanceof Player player && player.getItemBySlot(equipmentSlot) == itemStack) {
            this.bz$onArmorTick(itemStack, level, player);
        }
    }

    public void bz$onArmorTick(ItemStack itemstack, Level world, Player player) { }
}
