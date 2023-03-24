package com.telepathicgrunt.the_bumblezone.packets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.QueensTradeManager;
import com.telepathicgrunt.the_bumblezone.screens.CrystallineFlowerScreen;
import com.telepathicgrunt.the_bumblezone.screens.EnchantmentSkeleton;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public record QueenRandomizerTradesSyncPacket(List<QueensTradeManager.TradeWantEntry> recipeViewerRandomizerTrades) {
    public static Gson gson = new GsonBuilder().create();

    public static void sendToClient(ServerPlayer entity, List<QueensTradeManager.TradeWantEntry> recipeViewerRandomizerTrades) {
        MessageHandler.DEFAULT_CHANNEL.send(PacketDistributor.PLAYER.with(() -> entity),
                new QueenRandomizerTradesSyncPacket(recipeViewerRandomizerTrades));
    }

    /*
     * How the client will read the packet.
     */
    public static QueenRandomizerTradesSyncPacket parse(final FriendlyByteBuf buf) {
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
    public static void compose(final QueenRandomizerTradesSyncPacket pkt, final FriendlyByteBuf buf) {
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

    /*
     * What the client will do with the packet
     */
    public static class Handler {
        //this is what gets run on the client
        public static void handle(final QueenRandomizerTradesSyncPacket pkt, final Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerRandomizerTrades = pkt.recipeViewerRandomizerTrades();
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
