package com.telepathicgrunt.the_bumblezone.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.event.InputEvent;

public class StingerSpearCooldownClient {
    public static void isOnCooldown(InputEvent.ClickInputEvent event) {
        Player player = Minecraft.getInstance().player;
        HitResult hitResult = Minecraft.getInstance().hitResult;
        if(player != null && hitResult != null && hitResult.getType() == HitResult.Type.ENTITY) {
            ItemStack itemStack = player.getItemInHand(event.getHand());
            if(player.getCooldowns().isOnCooldown(itemStack.getItem())) {
                event.setSwingHand(false);
            }
        }
    }
}
