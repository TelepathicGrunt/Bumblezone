package com.telepathicgrunt.the_bumblezone.packets;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.items.BumbleBeeChestplate;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class StinglessBeeHelmetSightPacket {

    public static ResourceLocation PACKET_ID = new ResourceLocation(Bumblezone.MODID, "stingless_bee_helmet_sight_packet");

    public static void registerPacket() {
        ServerPlayNetworking.registerGlobalReceiver(PACKET_ID,
            (minecraftServer, serverPlayer, packetListener, buf, packetSender) -> {
                minecraftServer.execute(() -> {
                    if(serverPlayer != null && buf.readByte() != 0) {
                        BzCriterias.STINGLESS_BEE_HELMET_SUPER_SIGHT_TRIGGER.trigger(serverPlayer);
                    }
                });
            });
    }
}
