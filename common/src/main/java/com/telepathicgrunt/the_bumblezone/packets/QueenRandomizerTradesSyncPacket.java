package com.telepathicgrunt.the_bumblezone.packets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.serialization.DataResult;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.QueensTradeManager;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.DatapackSyncEvent;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.RandomizeTradeRowInput;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.Packet;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketContext;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketHandler;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record QueenRandomizerTradesSyncPacket(List<RandomizeTradeRowInput> recipeViewerRandomizerTrades) implements Packet<QueenRandomizerTradesSyncPacket> {
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
            List<RandomizeTradeRowInput> parsedData = new ArrayList<>();

            CompoundTag data = buf.readAnySizeNbt();
            if (data == null) {
                Bumblezone.LOGGER.error("Queen Randomizer Trade packet is empty??? Wtf???");
                return new QueenRandomizerTradesSyncPacket(parsedData);
            }

            ListTag tagList = data.getList("randomize_trades", Tag.TAG_STRING);
            for (int i = 0; i < tagList.size(); i++) {
                TagKey<Item> tagKey = TagKey.create(Registries.ITEM, new ResourceLocation(tagList.getString(i)));
                RandomizeTradeRowInput wantEntry = new RandomizeTradeRowInput(Optional.of(tagKey));
                parsedData.add(wantEntry);
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
                listTag.add(StringTag.valueOf(pkt.recipeViewerRandomizerTrades().get(i).tagKey().get().location().toString()));
            }
            data.put("randomize_trades", listTag);
            buf.writeNbt(data);
        }
    }
}
