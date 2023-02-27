package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.components.MiscComponent;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.BeeStingerEntity;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class BeeStinger extends ArrowItem {
    public BeeStinger(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity livingEntity) {
        if (!stack.is(BzItems.CRYSTAL_CANNON) && livingEntity instanceof ServerPlayer serverPlayer) {
            MiscComponent.onBeeStingerFired(serverPlayer);
        }
        return new BeeStingerEntity(level, livingEntity);
    }
}
