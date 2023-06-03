package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.screens.BuzzingBreifcaseMenu;
import com.telepathicgrunt.the_bumblezone.screens.BuzzingBreifcaseMenuProvider;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BuzzingBriefcase extends Item {

    public BuzzingBriefcase(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if(!level.isClientSide()) {
            player.openMenu(new BuzzingBreifcaseMenuProvider(stack));
        }

        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }
}