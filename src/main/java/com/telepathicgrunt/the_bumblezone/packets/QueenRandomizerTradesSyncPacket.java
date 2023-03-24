package com.telepathicgrunt.the_bumblezone.packets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.serialization.DataResult;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.QueensTradeManager;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public record QueenRandomizerTradesSyncPacket(List<QueensTradeManager.TradeWantEntry> recipeViewerRandomizerTrades) {
    public static Gson gson = new GsonBuilder().create();
    public static final ResourceLocation PACKET_ID = new ResourceLocation(Bumblezone.MODID, "queen_randomize_trades_sync_packet");

    public static void registerPacket() {
        ClientPlayNetworking.registerGlobalReceiver(PACKET_ID,
                (client, handler, buf, responseSender) -> {
                    QueenRandomizerTradesSyncPacket queenRandomizeTradesSyncPacket = parse(buf);

                    client.execute(() -> {
                        QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerRandomizerTrades = queenRandomizeTradesSyncPacket.recipeViewerRandomizerTrades();
                    });
                });
    }

    public static void sendToClient(final ServerPlayer serverPlayer, List<QueensTradeManager.TradeWantEntry> dataToCompose) {
        FriendlyByteBuf passedData = new FriendlyByteBuf(Unpooled.buffer());
        compose(dataToCompose, passedData);
        ServerPlayNetworking.send(serverPlayer, QueenRandomizerTradesSyncPacket.PACKET_ID, passedData);
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
    public static void compose(final List<QueensTradeManager.TradeWantEntry> dataToCompose, final FriendlyByteBuf buf) {
        CompoundTag data = new CompoundTag();
        ListTag listTag = new ListTag();
        for (QueensTradeManager.TradeWantEntry tradeWantEntry : dataToCompose) {
            DataResult<Tag> dataResult = QueensTradeManager.TradeWantEntry.CODEC.encodeStart(NbtOps.INSTANCE, tradeWantEntry);
            dataResult.error().ifPresent(e -> Bumblezone.LOGGER.error("Failed to encode Queen Randomizer Trade packet entry: {}", e.toString()));
            dataResult.result().ifPresent(listTag::add);
        }
        data.put("randomize_trades", listTag);
        buf.writeNbt(data);
    }
}
