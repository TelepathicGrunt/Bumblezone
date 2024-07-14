package com.telepathicgrunt.the_bumblezone.packets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.menus.EnchantmentSkeleton;
import com.telepathicgrunt.the_bumblezone.packets.handlers.CrystallineFlowerEnchantmentPacketHandleBody;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public record CrystallineFlowerEnchantmentPacket(int containerId, List<EnchantmentSkeleton> enchantmentSkeletons, ResourceLocation selectedResourceLocation) implements Packet<CrystallineFlowerEnchantmentPacket> {
    public static final Gson GSON = new GsonBuilder().create();

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "crystalline_flower_enchantment");
    public static final ClientboundPacketType<CrystallineFlowerEnchantmentPacket> TYPE = new CrystallineFlowerEnchantmentPacket.Handler();

    public static void sendToClient(ServerPlayer player, int containerId, List<EnchantmentSkeleton> enchantmentSkeletons, ResourceLocation selectedResourceLocation) {
        MessageHandler.DEFAULT_CHANNEL.sendToPlayer(new CrystallineFlowerEnchantmentPacket(containerId, enchantmentSkeletons, selectedResourceLocation), player);
    }
    
    @Override
    public PacketType<CrystallineFlowerEnchantmentPacket> type() {
        return TYPE;
    }

    private static final class Handler implements ClientboundPacketType<CrystallineFlowerEnchantmentPacket> {

        @Override
        public void encode(CrystallineFlowerEnchantmentPacket message, RegistryFriendlyByteBuf buffer) {
            buffer.writeInt(message.containerId());
            buffer.writeCollection(message.enchantmentSkeletons(), (buf, enchantmentSkeleton) -> buf.writeUtf(GSON.toJson(enchantmentSkeleton)));
            buffer.writeResourceLocation(message.selectedResourceLocation);
        }

        @Override
        public CrystallineFlowerEnchantmentPacket decode(RegistryFriendlyByteBuf buffer) {
            return new CrystallineFlowerEnchantmentPacket(
                    buffer.readInt(),
                    buffer.readList(buf -> GSON.fromJson(buf.readUtf(), EnchantmentSkeleton.class)),
                    buffer.readResourceLocation());
        }

        @Override
        public Runnable handle(CrystallineFlowerEnchantmentPacket message) {
            return () -> CrystallineFlowerEnchantmentPacketHandleBody.handle(message);
        }

        @Override
        public Class<CrystallineFlowerEnchantmentPacket> type() {
            return CrystallineFlowerEnchantmentPacket.class;
        }

        @Override
        public ResourceLocation id() {
            return ID;
        }
    }
}
