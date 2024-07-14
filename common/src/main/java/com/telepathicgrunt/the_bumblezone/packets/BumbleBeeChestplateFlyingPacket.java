package com.telepathicgrunt.the_bumblezone.packets;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.base.ServerboundPacketType;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.packets.handlers.BumbleBeeChestplateFlyingPacketHandleBody;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public record BumbleBeeChestplateFlyingPacket(byte isFlying) implements Packet<BumbleBeeChestplateFlyingPacket> {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "bumblebee_chestplate_flying");
    public static final ServerboundPacketType<BumbleBeeChestplateFlyingPacket> TYPE = new BumbleBeeChestplateFlyingPacket.Handler();

    public static void sendToServer(boolean isFlying) {
        MessageHandler.DEFAULT_CHANNEL.sendToServer(new BumbleBeeChestplateFlyingPacket((byte) (isFlying ? 1 : 0)));
    }

    @Override
    public PacketType<BumbleBeeChestplateFlyingPacket> type() {
        return TYPE;
    }

    private static class Handler implements ServerboundPacketType<BumbleBeeChestplateFlyingPacket> {

        @Override
        public void encode(BumbleBeeChestplateFlyingPacket message, RegistryFriendlyByteBuf buffer) {
            buffer.writeByte(message.isFlying);
        }

        @Override
        public BumbleBeeChestplateFlyingPacket decode(RegistryFriendlyByteBuf buffer) {
            return new BumbleBeeChestplateFlyingPacket(buffer.readByte());
        }

        @Override
        public Consumer<Player> handle(BumbleBeeChestplateFlyingPacket message) {
            return (player) -> BumbleBeeChestplateFlyingPacketHandleBody.handle(message, player);
        }

        @Override
        public Class<BumbleBeeChestplateFlyingPacket> type() {
            return BumbleBeeChestplateFlyingPacket.class;
        }

        @Override
        public ResourceLocation id() {
            return ID;
        }
    }
}
