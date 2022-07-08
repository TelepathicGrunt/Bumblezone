package com.telepathicgrunt.the_bumblezone.items;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class HoneyCrystalShards extends ArrowItem {
    public HoneyCrystalShards(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity livingEntity) {
        Arrow arrow = new Arrow(level, livingEntity); //TODO: create custom crystald shards entity to spawn
        arrow.setEffectsFromItem(stack);
        return arrow;
    }

    @Override
    public boolean isInfinite(ItemStack stack, ItemStack bow, Player player) {
        int enchantLevel = bow.getEnchantmentLevel(Enchantments.INFINITY_ARROWS);
        return enchantLevel > 0;
    }
}
