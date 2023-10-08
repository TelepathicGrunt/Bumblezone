package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class FoodBowlItem extends Item {

    public FoodBowlItem(Properties properties) {
        super(properties);
    }

    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        if (level.isClientSide()) {
            return itemStack;
        }

        // Already shrunk itemstack
        ItemStack itemStack2 = super.finishUsingItem(itemStack, level, livingEntity);
        boolean instabuildPlayer = livingEntity instanceof Player && ((Player)livingEntity).getAbilities().instabuild;

        if (instabuildPlayer) {
            return itemStack2;
        }

        if (itemStack2.getCount() > 0) {
            if (livingEntity instanceof Player player) {
                GeneralUtils.givePlayerItem(player,
                        player.getUsedItemHand(),
                        new ItemStack(Items.BOWL),
                        false,
                        false);
            }
            else {
                livingEntity.spawnAtLocation(new ItemStack(Items.BOWL));
            }

            return itemStack2;
        }

        return new ItemStack(Items.BOWL);
    }
}