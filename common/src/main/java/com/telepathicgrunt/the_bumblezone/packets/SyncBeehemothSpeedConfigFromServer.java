package com.telepathicgrunt.the_bumblezone.packets;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzDatapackSyncEvent;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.Objects;

public record SyncBeehemothSpeedConfigFromServer(double newBeehemothSpeed) implements Packet<SyncBeehemothSpeedConfigFromServer> {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "sync_beehemoth_speed_config_from_server");
    public static final ClientboundPacketType<SyncBeehemothSpeedConfigFromServer> TYPE = new SyncBeehemothSpeedConfigFromServer.Handler();

    public static void sendToClient(Entity entity, double beehemothSpeedConfigValue) {
        MessageHandler.DEFAULT_CHANNEL.sendToAllPlayers(new SyncBeehemothSpeedConfigFromServer(beehemothSpeedConfigValue), Objects.requireNonNull(entity.level().getServer()));
    }

    public static void sendToClient(BzDatapackSyncEvent event) {
        MessageHandler.DEFAULT_CHANNEL.sendToPlayer(new SyncBeehemothSpeedConfigFromServer(BzGeneralConfigs.beehemothSpeed), event.player());
    }

    @Override
    public PacketType<SyncBeehemothSpeedConfigFromServer> type() {
        return TYPE;
    }

    private static final class Handler implements ClientboundPacketType<SyncBeehemothSpeedConfigFromServer> {

        @Override
        public void encode(SyncBeehemothSpeedConfigFromServer message, RegistryFriendlyByteBuf buffer) {
            buffer.writeDouble(message.newBeehemothSpeed());
        }

        @Override
        public SyncBeehemothSpeedConfigFromServer decode(RegistryFriendlyByteBuf buffer) {
            return new SyncBeehemothSpeedConfigFromServer(buffer.readDouble());
        }

        @Override
        public Runnable handle(SyncBeehemothSpeedConfigFromServer message) {
            return () -> BeehemothEntity.beehemothSpeedConfigValue = message.newBeehemothSpeed();
        }

        @Override
        public Class<SyncBeehemothSpeedConfigFromServer> type() {
            return SyncBeehemothSpeedConfigFromServer.class;
        }

        @Override
        public ResourceLocation id() {
            return ID;
        }
    }
}
