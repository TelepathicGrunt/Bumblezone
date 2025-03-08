package com.telepathicgrunt.the_bumblezone.packets;

import com.mojang.brigadier.Message;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.Packet;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketContext;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketHandler;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record TradeHintParticleSpawnPacket(int queenId, ResourceLocation wantItem, List<ResourceLocation> rewardItems) implements Packet<TradeHintParticleSpawnPacket> {

    public static final ResourceLocation ID = new ResourceLocation(Bumblezone.MODID, "trade_hint_particle_spawn");
    static final Handler HANDLER = new Handler();

    public static void sendToClient(Entity queen, Item wantItem, List<Item> rewardItems) {
        ResourceLocation wantItemRl = BuiltInRegistries.ITEM.getKey(wantItem);
        List<ResourceLocation> rewardItemRls = new ArrayList<>(rewardItems.size());
        for (Item item : rewardItems) {
            rewardItemRls.add(BuiltInRegistries.ITEM.getKey(item));
        }

        MessageHandler.DEFAULT_CHANNEL.sendToPlayersInLevel(new TradeHintParticleSpawnPacket(queen.getId(), wantItemRl, rewardItemRls), queen.level());
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<TradeHintParticleSpawnPacket> getHandler() {
        return HANDLER;
    }

    private static final class Handler implements PacketHandler<TradeHintParticleSpawnPacket> {

        @Override
        public void encode(TradeHintParticleSpawnPacket message, FriendlyByteBuf buffer) {
            buffer.writeInt(message.queenId());
            buffer.writeResourceLocation(message.wantItem());
            buffer.writeInt(message.rewardItems().size());
            for (ResourceLocation rewardItem : message.rewardItems()) {
                buffer.writeResourceLocation(rewardItem);
            }
        }

        @Override
        public TradeHintParticleSpawnPacket decode(FriendlyByteBuf buffer) {
            int queenId = buffer.readInt();
            ResourceLocation wantItem = buffer.readResourceLocation();

            int sizeOfRewards = buffer.readInt();
            List<ResourceLocation> rewardItems = new ArrayList<>(sizeOfRewards);
            for (int i = 0; i < sizeOfRewards; i++) {
                rewardItems.add(buffer.readResourceLocation());
            }

            return new TradeHintParticleSpawnPacket(queenId, wantItem, rewardItems);
        }

        @Override
        public PacketContext handle(TradeHintParticleSpawnPacket message) {
            return (player, level) -> {
                Entity queen = level.getEntity(message.queenId());
                if (queen != null) {
                    TradeHintParticleSpawnPacketHandler.handle(message, player, queen, level);
                }
            };
        }
    }
}
