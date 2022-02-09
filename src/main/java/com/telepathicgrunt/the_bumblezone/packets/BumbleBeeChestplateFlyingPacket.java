package com.telepathicgrunt.the_bumblezone.packets;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.PileOfPollen;
import com.telepathicgrunt.the_bumblezone.items.BumbleBeeChestplate;
import com.telepathicgrunt.the_bumblezone.mixin.blocks.FallingBlockEntityAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;

public class BumbleBeeChestplateFlyingPacket {

    public static ResourceLocation PACKET_ID = new ResourceLocation(Bumblezone.MODID, "bumble_bee_chestplate_flying_packet");

    public static void registerPacket() {
        ServerPlayNetworking.registerGlobalReceiver(PACKET_ID,
            (minecraftServer, serverPlayer, packetListener, buf, packetSender) -> {
                minecraftServer.execute(() -> {
                    if(serverPlayer == null) {
                        return;
                    }

                    ItemStack itemStack = BumbleBeeChestplate.getEntityBeeChestplate(serverPlayer);
                    if(!itemStack.isEmpty()) {
                        boolean isFlying = buf.readByte() != 0;
                        CompoundTag tag = itemStack.getOrCreateTag();
                        tag.putBoolean("isFlying", isFlying);
                    }
                });
            });
    }
}
