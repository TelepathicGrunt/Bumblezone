package com.telepathicgrunt.the_bumblezone.packets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.QueensTradeManager;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.WeightedTradeResult;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.MainTradeRowInput;
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
import net.minecraft.util.random.WeightedRandomList;

import java.util.ArrayList;
import java.util.List;

public record QueenMainTradesSyncPacket(List<Pair<MainTradeRowInput, WeightedRandomList<WeightedTradeResult>>> recipeViewerMainTrades) {
    public static Gson gson = new GsonBuilder().create();
    public static final ResourceLocation PACKET_ID = new ResourceLocation(Bumblezone.MODID, "queen_main_trades_sync_packet");

    public static void registerPacket() {
        ClientPlayNetworking.registerGlobalReceiver(PACKET_ID,
                (client, handler, buf, responseSender) -> {
                    QueenMainTradesSyncPacket queenMainTradesSyncPacket = parse(buf);

                    client.execute(() -> {
                        QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerMainTrades = queenMainTradesSyncPacket.recipeViewerMainTrades();
                    });
                });
    }

    public static void sendToClient(final ServerPlayer serverPlayer, List<Pair<MainTradeRowInput, WeightedRandomList<WeightedTradeResult>>> dataToCompose) {
        FriendlyByteBuf passedData = new FriendlyByteBuf(Unpooled.buffer());
        compose(dataToCompose, passedData);
        ServerPlayNetworking.send(serverPlayer, QueenMainTradesSyncPacket.PACKET_ID, passedData);
    }


    /*
     * How the client will read the packet.
     */
    public static QueenMainTradesSyncPacket parse(final FriendlyByteBuf buf) {
        List<Pair<MainTradeRowInput, WeightedRandomList<WeightedTradeResult>>> parsedData = new ArrayList<>();

        CompoundTag data = buf.readAnySizeNbt();
        if (data == null) {
            Bumblezone.LOGGER.error("Queen Main Trade packet is empty??? Wtf???");
            return new QueenMainTradesSyncPacket(parsedData);
        }

        ListTag tagList = data.getList("main_trades", Tag.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++) {
            CompoundTag tradeCompound = tagList.getCompound(i);
            CompoundTag firstHalf = tradeCompound.getCompound("input");
            ListTag secondHalf = tradeCompound.getList("output", Tag.TAG_COMPOUND);

            DataResult<MainTradeRowInput> dataResult1 = MainTradeRowInput.CODEC.parse(NbtOps.INSTANCE, firstHalf);
            dataResult1.error().ifPresent(e -> Bumblezone.LOGGER.error("Failed to parse Queen Main Trade packet entry (first half): {}", e.toString()));

            DataResult<WeightedRandomList<WeightedTradeResult>> dataResult2 = WeightedRandomList.codec(WeightedTradeResult.CODEC).parse(NbtOps.INSTANCE, secondHalf);
            dataResult2.error().ifPresent(e -> Bumblezone.LOGGER.error("Failed to parse Queen Main Trade packet entry (second half): {}", e.toString()));


            dataResult1.result().ifPresent(input -> dataResult2.result().ifPresent(output -> parsedData.add(Pair.of(input, output))));
        }

        return new QueenMainTradesSyncPacket(parsedData);
    }

    /*
     * creates the packet buffer and sets its values
     */
    public static void compose(final List<Pair<MainTradeRowInput, WeightedRandomList<WeightedTradeResult>>> dataToCompose, final FriendlyByteBuf buf) {
        CompoundTag data = new CompoundTag();
        ListTag listTag = new ListTag();
        for (Pair<MainTradeRowInput, WeightedRandomList<WeightedTradeResult>> tradeRow : dataToCompose) {

            CompoundTag pairData = new CompoundTag();
            DataResult<Tag> dataResult1 = MainTradeRowInput.CODEC.encodeStart(NbtOps.INSTANCE, tradeRow.getFirst());
            dataResult1.error().ifPresent(e -> Bumblezone.LOGGER.error("Failed to encode Queen Main Trade packet entry (first half): {}", e.toString()));
            dataResult1.result().ifPresent(r -> pairData.put("input", r));

            DataResult<Tag> dataResult = WeightedRandomList.codec(WeightedTradeResult.CODEC).encodeStart(NbtOps.INSTANCE, tradeRow.getSecond());
            dataResult.error().ifPresent(e -> Bumblezone.LOGGER.error("Failed to encode Queen Main Trade packet entry (second half): {}", e.toString()));
            dataResult.result().ifPresent(r -> pairData.put("output", r));

            listTag.add(pairData);
        }
        data.put("main_trades", listTag);
        buf.writeNbt(data);
    }
}
