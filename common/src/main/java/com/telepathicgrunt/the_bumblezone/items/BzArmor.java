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

    // Fired on serverside only
    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity entity, EquipmentSlot equipmentSlot) {
        this.bz$onArmorTickWrapper(itemStack, level, entity, equipmentSlot);
    }

    // Called in inventoryTick for serverside and in ItemStackMixin for clientside ticks.
    public void bz$onArmorTickWrapper(ItemStack itemStack, Level level, Entity entity, EquipmentSlot equipmentSlot) {
        if (entity instanceof Player player &&
                equipmentSlot != EquipmentSlot.MAINHAND &&
                equipmentSlot != EquipmentSlot.OFFHAND &&
                player.getItemBySlot(equipmentSlot) == itemStack)
        {
            this.bz$onArmorTick(itemStack, level, player);
        }
    }

    public void bz$onArmorTick(ItemStack itemstack, Level level, Player player) { }
}
