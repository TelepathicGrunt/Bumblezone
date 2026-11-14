package com.telepathicgrunt.the_bumblezone.packets;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.datamanagers.queentrades.QueensTradeManager;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzDatapackSyncEvent;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.util.HashSet;
import java.util.Set;

public record QueenClientHandTradesSyncPacket(Set<Item> clientAllowedMainTradeItems, Set<Item> clientAllowedSpecialTradeItems) implements Packet<QueenClientHandTradesSyncPacket> {
    public static final Identifier ID = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "queen_client_hand_trades_sync_packet");
    public static final ClientboundPacketType<QueenClientHandTradesSyncPacket> TYPE = new QueenClientHandTradesSyncPacket.Handler();

    public static void sendToClient(BzDatapackSyncEvent event) {
        MessageHandler.DEFAULT_CHANNEL.sendToPlayer(new QueenClientHandTradesSyncPacket(QueensTradeManager.QUEENS_TRADE_MANAGER.queenTrades.keySet(), QueensTradeManager.QUEENS_TRADE_MANAGER.specialDayQueenTrades.keySet()), event.player());
    }

    @Override
    public PacketType<QueenClientHandTradesSyncPacket> type() {
        return TYPE;
    }

    /*
     * What the client will do with the packet
     */
    public static class Handler implements ClientboundPacketType<QueenClientHandTradesSyncPacket> {

        @Override
        public QueenClientHandTradesSyncPacket decode(final RegistryFriendlyByteBuf buf) {
            HashSet<Item> clientMainItems = new HashSet<>(ByteBufCodecs.<RegistryFriendlyByteBuf, Item>list().apply(ByteBufCodecs.registry(Registries.ITEM)).decode(buf));
            HashSet<Item> clientSpecialItems = new HashSet<>(ByteBufCodecs.<RegistryFriendlyByteBuf, Item>list().apply(ByteBufCodecs.registry(Registries.ITEM)).decode(buf));
            return new QueenClientHandTradesSyncPacket(clientMainItems, clientSpecialItems);
        }

        @Override
        public void encode(final QueenClientHandTradesSyncPacket pkt, final RegistryFriendlyByteBuf buf) {
            ByteBufCodecs.<RegistryFriendlyByteBuf, Item>list().apply(ByteBufCodecs.registry(Registries.ITEM)).encode(buf, pkt.clientAllowedMainTradeItems().stream().toList());
            ByteBufCodecs.<RegistryFriendlyByteBuf, Item>list().apply(ByteBufCodecs.registry(Registries.ITEM)).encode(buf, pkt.clientAllowedSpecialTradeItems().stream().toList());
        }

        @Override
        public Runnable handle(final QueenClientHandTradesSyncPacket pkt) {
            return () -> {
                QueensTradeManager.CLIENT_ALLOWED_MAIN_TRADE_ITEMS = pkt.clientAllowedMainTradeItems();
                QueensTradeManager.CLIENT_ALLOWED_SPECIAL_TRADE_ITEMS = pkt.clientAllowedSpecialTradeItems();
            };
        }

        @Override
        public Identifier id() {
            return ID;
        }
    }
}
