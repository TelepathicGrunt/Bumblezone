package com.telepathicgrunt.the_bumblezone.packets;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.packets.handlers.TradeHintParticleSpawnPacketHandler;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public record TradeHintParticleSpawnPacket(int queenId, Item wantItem, List<ItemStack> rewardItemStacks) implements Packet<TradeHintParticleSpawnPacket> {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "trade_hint_particle_spawn");
    public static final ClientboundPacketType<TradeHintParticleSpawnPacket> TYPE = new TradeHintParticleSpawnPacket.Handler();

    public static void sendToClient(Entity queen, Item wantItem, List<ItemStack> rewardItems) {
        MessageHandler.DEFAULT_CHANNEL.sendToPlayersInLevel(new TradeHintParticleSpawnPacket(queen.getId(), wantItem, rewardItems), queen.level());
    }

    @Override
    public PacketType<TradeHintParticleSpawnPacket> type() {
        return TYPE;
    }

    private static final class Handler implements ClientboundPacketType<TradeHintParticleSpawnPacket> {

        @Override
        public void encode(TradeHintParticleSpawnPacket message, RegistryFriendlyByteBuf buffer) {
            buffer.writeInt(message.queenId());
            buffer.writeIdentifier(BuiltInRegistries.ITEM.getKey(message.wantItem));
            buffer.writeInt(message.rewardItemStacks().size());
            for (ItemStack rewardItem : message.rewardItemStacks()) {
                ItemStack.STREAM_CODEC.encode(buffer, rewardItem);
            }
        }

        @Override
        public TradeHintParticleSpawnPacket decode(RegistryFriendlyByteBuf buffer) {
            int queenId = buffer.readInt();
            Item wantItem = BuiltInRegistries.ITEM.get(buffer.readIdentifier()).get().value();

            int sizeOfRewards = buffer.readInt();
            List<ItemStack> rewardItems = new ArrayList<>(sizeOfRewards);
            for (int i = 0; i < sizeOfRewards; i++) {
                rewardItems.add(ItemStack.STREAM_CODEC.decode(buffer));
            }

            return new TradeHintParticleSpawnPacket(queenId, wantItem, rewardItems);
        }

        @Override
        public Runnable handle(TradeHintParticleSpawnPacket message) {
            return () -> TradeHintParticleSpawnPacketHandler.handle(message);
        }

        @Override
        public Identifier id() {
            return ID;
        }
    }
}
