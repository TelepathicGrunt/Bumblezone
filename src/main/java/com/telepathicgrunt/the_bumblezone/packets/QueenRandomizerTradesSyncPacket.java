package com.telepathicgrunt.the_bumblezone.packets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.QueensTradeManager;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.RandomizeTradeRowInput;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public record QueenRandomizerTradesSyncPacket(List<RandomizeTradeRowInput> recipeViewerRandomizerTrades) {
    public static Gson gson = new GsonBuilder().create();

    public static void sendToClient(ServerPlayer entity, List<RandomizeTradeRowInput> recipeViewerRandomizerTrades) {
        MessageHandler.DEFAULT_CHANNEL.send(PacketDistributor.PLAYER.with(() -> entity),
                new QueenRandomizerTradesSyncPacket(recipeViewerRandomizerTrades));
    }

    /*
     * How the client will read the packet.
     */
    public static QueenRandomizerTradesSyncPacket parse(final FriendlyByteBuf buf) {
        List<RandomizeTradeRowInput> parsedData = new ArrayList<>();

        CompoundTag data = buf.readAnySizeNbt();
        if (data == null) {
            Bumblezone.LOGGER.error("Queen Randomizer Trade packet is empty??? Wtf???");
            return new QueenRandomizerTradesSyncPacket(parsedData);
        }

        ListTag tagList = data.getList("randomize_trades", Tag.TAG_STRING);
        for (int i = 0; i < tagList.size(); i++) {
            TagKey<Item> tagKey = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(tagList.getString(i)));
            RandomizeTradeRowInput wantEntry = new RandomizeTradeRowInput(Optional.of(tagKey));
            parsedData.add(wantEntry);
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
            listTag.add(StringTag.valueOf(pkt.recipeViewerRandomizerTrades().get(i).tagKey().get().location().toString()));
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
