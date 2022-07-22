package com.telepathicgrunt.the_bumblezone.packets;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.items.BumbleBeeChestplate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

public class BumbleBeeChestplateFlyingPacket {

    public static ResourceLocation PACKET_ID = new ResourceLocation(Bumblezone.MODID, "bumble_bee_chestplate_flying_packet");

    public static void registerPacket() {
        ServerPlayNetworking.registerGlobalReceiver(PACKET_ID,
            (minecraftServer, serverPlayer, packetListener, buf, packetSender) -> {
                boolean isFlying = buf.readByte() != 0;
                minecraftServer.execute(() -> {
                    if(serverPlayer == null) {
                        return;
                    }

                    ItemStack itemStack = BumbleBeeChestplate.getEntityBeeChestplate(serverPlayer);
                    if(!itemStack.isEmpty()) {
                        CompoundTag tag = itemStack.getOrCreateTag();
                        tag.putBoolean("isFlying", isFlying);
                    }
                });
            });
    }
}
