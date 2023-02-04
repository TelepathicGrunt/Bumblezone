package com.telepathicgrunt.the_bumblezone.items;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class BeeArmor extends BzArmor {
    private final int variant;
    private final boolean transTexture;

    public BeeArmor(ArmorMaterial material, EquipmentSlot slot, Properties properties, int variant, boolean transTexture) {
        super(material, slot, properties);
        this.variant = variant;
        this.transTexture = transTexture;
    }

    public boolean hasTransTexture() {
        return transTexture;
    }

    public int getVariant() {
        return variant;
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slot, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slot, isSelected);
        if (entity instanceof Player player) {
            Inventory inventory = player.getInventory();
            if (slot < inventory.items.size()) return;
            if (slot >= inventory.items.size() + inventory.armor.size()) return;
            armorTick(stack, level, player);
        }
    }

    abstract void armorTick(ItemStack stack, Level level, Player player);
}