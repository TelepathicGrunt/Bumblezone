package com.telepathicgrunt.the_bumblezone.packets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.QueensTradeManager;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.RandomizeTradeRowInput;
import io.netty.buffer.Unpooled;
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
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record QueenRandomizerTradesSyncPacket(List<RandomizeTradeRowInput> recipeViewerRandomizerTrades) {
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

    public static void sendToClient(final ServerPlayer serverPlayer, List<RandomizeTradeRowInput> dataToCompose) {
        FriendlyByteBuf passedData = new FriendlyByteBuf(Unpooled.buffer());
        compose(dataToCompose, passedData);
        ServerPlayNetworking.send(serverPlayer, QueenRandomizerTradesSyncPacket.PACKET_ID, passedData);
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
    public static void compose(final List<RandomizeTradeRowInput> dataToCompose, final FriendlyByteBuf buf) {
        CompoundTag data = new CompoundTag();
        ListTag listTag = new ListTag();
        for (RandomizeTradeRowInput entry : dataToCompose) {
            listTag.add(StringTag.valueOf(entry.tagKey().get().location().toString()));
        }
        data.put("randomize_trades", listTag);
        buf.writeNbt(data);
    }
}
