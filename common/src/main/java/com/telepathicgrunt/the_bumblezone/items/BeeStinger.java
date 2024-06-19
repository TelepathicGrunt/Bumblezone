package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.entities.nonliving.BeeStingerEntity;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modules.PlayerDataHandler;
import com.telepathicgrunt.the_bumblezone.platform.BzArrowItem;
import com.telepathicgrunt.the_bumblezone.utils.EnchantmentUtils;
import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BeeStinger extends BzArrowItem {
    public BeeStinger(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(@NotNull Level level, ItemStack stack, LivingEntity livingEntity, @Nullable ItemStack itemStack2) {
        if (itemStack2 != null && !itemStack2.is(BzItems.CRYSTAL_CANNON.get()) && livingEntity instanceof ServerPlayer serverPlayer) {
            PlayerDataHandler.onBeeStingerFired(serverPlayer);
        }
        return new BeeStingerEntity(level, livingEntity, stack, itemStack2);
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
