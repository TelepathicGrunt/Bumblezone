package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.components.EssenceComponent;
import com.telepathicgrunt.the_bumblezone.components.MiscComponent;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class EssenceOfTheBees extends Item {

    public EssenceOfTheBees(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        super.finishUsingItem(itemStack, level, livingEntity);
        if (livingEntity instanceof ServerPlayer serverPlayer) {
            if (hasEssence(serverPlayer)) {
                serverPlayer.displayClientMessage(Component.translatable("item.the_bumblezone.essence_of_the_bees.already_essenced"), false);
                return itemStack;
            }

            setEssence(serverPlayer, true);
            itemStack.shrink(1);

            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, itemStack);
            serverPlayer.awardStat(Stats.ITEM_USED.get(this));

        }
        return itemStack;
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return true;
    }

    /**
     * How long it takes to use or consume an item
     */
    @Override
    public int getUseDuration(ItemStack itemStack) {
        return 80;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.DRINK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    public static void setEssence(ServerPlayer serverPlayer, boolean newValue) {
        EssenceComponent capability = Bumblezone.ESSENCE_COMPONENT.get(serverPlayer);
        capability.isBeeEssenced = newValue;
    }

    public static boolean hasEssence(ServerPlayer serverPlayer) {
        EssenceComponent capability = Bumblezone.ESSENCE_COMPONENT.get(serverPlayer);
        return capability.isBeeEssenced;
    }
}