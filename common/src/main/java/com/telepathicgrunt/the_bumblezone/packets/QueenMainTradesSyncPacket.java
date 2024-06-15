package com.telepathicgrunt.the_bumblezone.packets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.QueensTradeManager;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.WeightedTradeResult;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.DatapackSyncEvent;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.MainTradeRowInput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.WeightedRandomList;

import java.util.ArrayList;
import java.util.List;

public record QueenMainTradesSyncPacket(List<Pair<MainTradeRowInput, WeightedRandomList<WeightedTradeResult>>> recipeViewerMainTrades) implements Packet<QueenMainTradesSyncPacket> {
    public static Gson gson = new GsonBuilder().create();

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "queen_main_trades_sync_packet");
    public static final ClientboundPacketType<QueenMainTradesSyncPacket> TYPE = new QueenMainTradesSyncPacket.Handler();

    public static void sendToClient(DatapackSyncEvent event) {
        MessageHandler.DEFAULT_CHANNEL.sendToPlayer(new QueenMainTradesSyncPacket(QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerMainTrades), event.player());
    }

    @Override
    public PacketType<QueenMainTradesSyncPacket> type() {
        return TYPE;
    }

    /*
     * What the client will do with the packet
     */
    public static class Handler implements ClientboundPacketType<QueenMainTradesSyncPacket> {

        @Override
        public QueenMainTradesSyncPacket decode(final RegistryFriendlyByteBuf buf) {
            List<Pair<MainTradeRowInput, WeightedRandomList<WeightedTradeResult>>> parsedData = new ArrayList<>();

            CompoundTag data = buf.readNbt();
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
                dataResult1.error().ifPresent(e -> Bumblezone.LOGGER.error("Failed to parse Queen Main Trade packet entry (first half): {}", e));

                DataResult<WeightedRandomList<WeightedTradeResult>> dataResult2 = WeightedRandomList.codec(WeightedTradeResult.CODEC).parse(NbtOps.INSTANCE, secondHalf);
                dataResult2.error().ifPresent(e -> Bumblezone.LOGGER.error("Failed to parse Queen Main Trade packet entry (second half): {}", e));


                dataResult1.result().ifPresent(input -> dataResult2.result().ifPresent(output -> parsedData.add(Pair.of(input, output))));
            }

            return new QueenMainTradesSyncPacket(parsedData);
        }

        @Override
        public void encode(final QueenMainTradesSyncPacket pkt, final RegistryFriendlyByteBuf buf) {
            CompoundTag data = new CompoundTag();
            ListTag listTag = new ListTag();
            for (Pair<MainTradeRowInput, WeightedRandomList<WeightedTradeResult>> tradeRow : pkt.recipeViewerMainTrades()) {

                CompoundTag pairData = new CompoundTag();
                DataResult<Tag> dataResult1 = MainTradeRowInput.CODEC.encodeStart(NbtOps.INSTANCE, tradeRow.getFirst());
                dataResult1.error().ifPresent(e -> Bumblezone.LOGGER.error("Failed to encode Queen Main Trade packet entry (first half): {}", e));
                dataResult1.result().ifPresent(r -> pairData.put("input", r));

                DataResult<Tag> dataResult = WeightedRandomList.codec(WeightedTradeResult.CODEC).encodeStart(NbtOps.INSTANCE, tradeRow.getSecond());
                dataResult.error().ifPresent(e -> Bumblezone.LOGGER.error("Failed to encode Queen Main Trade packet entry (second half): {}", e));
                dataResult.result().ifPresent(r -> pairData.put("output", r));

                listTag.add(pairData);
            }
            data.put("main_trades", listTag);
            buf.writeNbt(data);
        }

        @Override
        public Runnable handle(final QueenMainTradesSyncPacket pkt) {
            return () -> QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerMainTrades = pkt.recipeViewerMainTrades();
        }

        @Override
        public Class<QueenMainTradesSyncPacket> type() {
            return QueenMainTradesSyncPacket.class;
        }

        @Override
        public ResourceLocation id() {
            return ID;
        }
    }
}
