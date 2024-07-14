package com.telepathicgrunt.the_bumblezone.packets.handlers;

import com.telepathicgrunt.the_bumblezone.datacomponents.BumbleBeeChestplateData;
import com.telepathicgrunt.the_bumblezone.items.BumbleBeeChestplate;
import com.telepathicgrunt.the_bumblezone.modinit.BzDataComponents;
import com.telepathicgrunt.the_bumblezone.packets.BumbleBeeChestplateFlyingPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class BumbleBeeChestplateFlyingPacketHandleBody {
    public static void handle(BumbleBeeChestplateFlyingPacket message, Player player) {
        ItemStack itemStack = BumbleBeeChestplate.getEntityBeeChestplate(player);
        if(!itemStack.isEmpty()) {
            BumbleBeeChestplateData bumbleBeeChestplateData = itemStack.get(BzDataComponents.BUMBLEBEE_CHESTPLATE_DATA.get());
            itemStack.set(BzDataComponents.BUMBLEBEE_CHESTPLATE_DATA.get(), new BumbleBeeChestplateData(
                    message.isFlying() != 0,
                    bumbleBeeChestplateData.flyCounter(),
                    bumbleBeeChestplateData.forcedMaxFlyingTickTime(),
                    bumbleBeeChestplateData.requiredWearablesCountForForcedFlyingTime()
            ));
        }
    }
}