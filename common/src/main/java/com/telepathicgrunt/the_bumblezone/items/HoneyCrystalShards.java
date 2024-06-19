package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.entities.nonliving.HoneyCrystalShardEntity;
import com.telepathicgrunt.the_bumblezone.platform.BzArrowItem;
import com.telepathicgrunt.the_bumblezone.utils.EnchantmentUtils;
import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class HoneyCrystalShards extends BzArrowItem {
    public HoneyCrystalShards(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity livingEntity, ItemStack weapon) {
        return new HoneyCrystalShardEntity(level, livingEntity, stack.copyWithCount(1), weapon);
    }

    @Override
    public OptionalBoolean bz$isInfinite(ItemStack stack, ItemStack bow, LivingEntity player) {
        if (player == null && player.level().isClientSide()) {
            return OptionalBoolean.EMPTY;
        }

        int enchantLevel = EnchantmentHelper.processAmmoUse((ServerLevel) player.level(), stack, bow, 1);
        return (enchantLevel == 0 || EnchantmentHelper.getItemEnchantmentLevel(EnchantmentUtils.getEnchantmentHolder(Enchantments.INFINITY, player.level()), bow) > 0)
                ? OptionalBoolean.of(true) : OptionalBoolean.EMPTY;
    }
}
