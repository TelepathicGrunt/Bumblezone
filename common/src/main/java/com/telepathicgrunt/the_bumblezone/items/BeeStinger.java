package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.entities.nonliving.BeeStingerEntity;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modules.PlayerDataHandler;
import com.telepathicgrunt.the_bumblezone.platform.BzArrowItem;
import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BeeStinger extends BzArrowItem {
    public BeeStinger(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(@NotNull Level level, ItemStack stack, @NotNull LivingEntity livingEntity) {
        if (!stack.is(BzItems.CRYSTAL_CANNON.get()) && livingEntity instanceof ServerPlayer serverPlayer) {
            PlayerDataHandler.onBeeStingerFired(serverPlayer);
        }
        return new BeeStingerEntity(level, livingEntity);
    }

    @Override
    public OptionalBoolean bz$isInfinite(ItemStack stack, ItemStack bow, Player player) {
        int enchantLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, bow);
        return OptionalBoolean.of(enchantLevel > 0);
    }
}
