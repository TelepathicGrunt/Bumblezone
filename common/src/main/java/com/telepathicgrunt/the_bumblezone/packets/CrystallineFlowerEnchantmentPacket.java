package com.telepathicgrunt.the_bumblezone.packets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.Packet;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketContext;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketHandler;
import com.telepathicgrunt.the_bumblezone.screens.CrystallineFlowerScreen;
import com.telepathicgrunt.the_bumblezone.screens.EnchantmentSkeleton;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtilsClient;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public record CrystallineFlowerEnchantmentPacket(int containerId, List<EnchantmentSkeleton> enchantmentSkeletons) implements Packet<CrystallineFlowerEnchantmentPacket> {
    public static final Gson GSON = new GsonBuilder().create();

    public static final ResourceLocation ID = new ResourceLocation(Bumblezone.MODID, "crystalline_flower_enchantment");
    public static final Handler HANDLER = new Handler();

    public static void sendToClient(ServerPlayer player, int containerId, List<EnchantmentSkeleton> enchantmentSkeletons) {
        MessageHandler.DEFAULT_CHANNEL.sendToPlayer(new CrystallineFlowerEnchantmentPacket(containerId, enchantmentSkeletons), player);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<CrystallineFlowerEnchantmentPacket> getHandler() {
        return HANDLER;
    }

    private static final class Handler implements PacketHandler<CrystallineFlowerEnchantmentPacket> {

        @Override
        public void encode(CrystallineFlowerEnchantmentPacket message, FriendlyByteBuf buffer) {
            buffer.writeInt(message.containerId());
            buffer.writeCollection(message.enchantmentSkeletons(), (buf, enchantmentSkeleton) -> buf.writeUtf(GSON.toJson(enchantmentSkeleton)));
        }

        @Override
        public CrystallineFlowerEnchantmentPacket decode(FriendlyByteBuf buffer) {
            return new CrystallineFlowerEnchantmentPacket(buffer.readInt(), buffer.readList(buf -> GSON.fromJson(buf.readUtf(), EnchantmentSkeleton.class)));
        }

        @Override
        public PacketContext handle(CrystallineFlowerEnchantmentPacket message) {
            return (player, level) -> {
                if(GeneralUtilsClient.getClientPlayer() != null && GeneralUtilsClient.getClientPlayer().containerMenu.containerId == message.containerId){
                    CrystallineFlowerScreen.enchantmentsAvailable = message.enchantmentSkeletons();
                }
            };
        }
    }
}
