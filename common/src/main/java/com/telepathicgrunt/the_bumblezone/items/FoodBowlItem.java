package com.telepathicgrunt.the_bumblezone.items;

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
        ItemStack itemStack2 = super.finishUsingItem(itemStack, level, livingEntity);
        return livingEntity instanceof Player && ((Player)livingEntity).getAbilities().instabuild ? itemStack2 : new ItemStack(Items.BOWL);
    }
}