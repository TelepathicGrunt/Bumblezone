package com.telepathicgrunt.the_bumblezone.packets;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.datamanagers.queentrades.QueensTradeManager;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzDatapackSyncEvent;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.RandomizeTradeRowInput;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record QueenRandomizerTradesSyncPacket(List<RandomizeTradeRowInput> recipeViewerRandomizerTrades) implements Packet<QueenRandomizerTradesSyncPacket> {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "queen_randomize_trades_sync_packet");
    public static final ClientboundPacketType<QueenRandomizerTradesSyncPacket> TYPE = new QueenRandomizerTradesSyncPacket.Handler();

    public static void sendToClient(BzDatapackSyncEvent event) {
        MessageHandler.DEFAULT_CHANNEL.sendToPlayer(new QueenRandomizerTradesSyncPacket(QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerRandomizerTrades), event.player());
    }

    @Override
    public PacketType<QueenRandomizerTradesSyncPacket> type() {
        return TYPE;
    }

    /*
     * What the client will do with the packet
     */
    public static class Handler implements ClientboundPacketType<QueenRandomizerTradesSyncPacket> {

        @Override
        public QueenRandomizerTradesSyncPacket decode(final RegistryFriendlyByteBuf buf) {
            List<RandomizeTradeRowInput> parsedData = new ArrayList<>();

            CompoundTag data = buf.readNbt();
            if (data == null) {
                Bumblezone.LOGGER.error("Queen Randomizer Trade packet is empty??? Wtf???");
                return new QueenRandomizerTradesSyncPacket(parsedData);
            }

            ListTag tagList = data.getList("randomize_trades", Tag.TAG_STRING);
            for (int i = 0; i < tagList.size(); i++) {
                TagKey<Item> tagKey = TagKey.create(Registries.ITEM, ResourceLocation.tryParse(tagList.getString(i)));
                RandomizeTradeRowInput wantEntry = new RandomizeTradeRowInput(Optional.of(tagKey));
                parsedData.add(wantEntry);
            }

            return new QueenRandomizerTradesSyncPacket(parsedData);
        }

        @Override
        public void encode(final QueenRandomizerTradesSyncPacket pkt, final RegistryFriendlyByteBuf buf) {
            CompoundTag data = new CompoundTag();
            ListTag listTag = new ListTag();
            for (int i = 0; i < pkt.recipeViewerRandomizerTrades().size(); i++) {
                listTag.add(StringTag.valueOf(pkt.recipeViewerRandomizerTrades().get(i).tagKey().get().location().toString()));
            }
            data.put("randomize_trades", listTag);
            buf.writeNbt(data);
        }

        @Override
        public Runnable handle(final QueenRandomizerTradesSyncPacket pkt) {
            return () -> QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerRandomizerTrades = pkt.recipeViewerRandomizerTrades();
        }

        @Override
        public Class<QueenRandomizerTradesSyncPacket> type() {
            return QueenRandomizerTradesSyncPacket.class;
        }

        @Override
        public ResourceLocation id() {
            return ID;
        }
    }
}
