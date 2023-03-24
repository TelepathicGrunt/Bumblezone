package com.telepathicgrunt.the_bumblezone.packets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.serialization.DataResult;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.QueensTradeManager;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.DatapackSyncEvent;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.Packet;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketContext;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public record QueenRandomizerTradesSyncPacket(List<QueensTradeManager.TradeWantEntry> recipeViewerRandomizerTrades) implements Packet<QueenRandomizerTradesSyncPacket> {
    public static Gson gson = new GsonBuilder().create();

    public static final ResourceLocation ID = new ResourceLocation(Bumblezone.MODID, "queen_randomize_trades_sync_packet");
    public static final QueenRandomizerTradesSyncPacket.Handler HANDLER = new QueenRandomizerTradesSyncPacket.Handler();

    public static void sendToClient(DatapackSyncEvent event) {
        MessageHandler.DEFAULT_CHANNEL.sendToPlayer(new QueenRandomizerTradesSyncPacket(QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerRandomizerTrades), event.player());
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<QueenRandomizerTradesSyncPacket> getHandler() {
        return HANDLER;
    }

    /*
     * What the client will do with the packet
     */
    public static class Handler implements PacketHandler<QueenRandomizerTradesSyncPacket> {
        //this is what gets run on the client
        public PacketContext handle(final QueenRandomizerTradesSyncPacket pkt) {
            return (player, level) -> {
                QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerRandomizerTrades = pkt.recipeViewerRandomizerTrades();
            };
        }

        /*
         * How the client will read the packet.
         */
        public QueenRandomizerTradesSyncPacket decode(final FriendlyByteBuf buf) {
            List<QueensTradeManager.TradeWantEntry> parsedData = new ArrayList<>();

            CompoundTag data = buf.readAnySizeNbt();
            if (data == null) {
                Bumblezone.LOGGER.error("Queen Randomizer Trade packet is empty??? Wtf???");
                return new QueenRandomizerTradesSyncPacket(parsedData);
            }

            ListTag tagList = data.getList("randomize_trades", Tag.TAG_COMPOUND);
            for (int i = 0; i < tagList.size(); i++) {
                CompoundTag tradeCompound = tagList.getCompound(i);
                DataResult<QueensTradeManager.TradeWantEntry> dataResult = QueensTradeManager.TradeWantEntry.CODEC.parse(NbtOps.INSTANCE, tradeCompound);
                dataResult.error().ifPresent(e -> Bumblezone.LOGGER.error("Failed to parse Queen Randomizer Trade packet entry: {}", e.toString()));
                dataResult.result().ifPresent(parsedData::add);
            }

            return new QueenRandomizerTradesSyncPacket(parsedData);
        }

        /*
         * creates the packet buffer and sets its values
         */
        public void encode(final QueenRandomizerTradesSyncPacket pkt, final FriendlyByteBuf buf) {
            CompoundTag data = new CompoundTag();
            ListTag listTag = new ListTag();
            for (int i = 0; i < pkt.recipeViewerRandomizerTrades().size(); i++) {
                DataResult<Tag> dataResult = QueensTradeManager.TradeWantEntry.CODEC.encodeStart(NbtOps.INSTANCE, pkt.recipeViewerRandomizerTrades().get(i));
                dataResult.error().ifPresent(e -> Bumblezone.LOGGER.error("Failed to encode Queen Randomizer Trade packet entry: {}", e.toString()));
                dataResult.result().ifPresent(listTag::add);
            }
            data.put("randomize_trades", listTag);
            buf.writeNbt(data);
        }
    }
}
