package com.telepathicgrunt.the_bumblezone.packets;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.QueensTradeManager;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.DatapackSyncEvent;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.Packet;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketContext;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.Objects;

public record SyncBeehemothSpeedConfigFromServer(double newBeehemothSpeed) implements Packet<SyncBeehemothSpeedConfigFromServer> {

    public static final ResourceLocation ID = new ResourceLocation(Bumblezone.MODID, "sync_beehemoth_speed_config_from_server");
    static final Handler HANDLER = new Handler();

    public static void sendToClient(Entity entity, double beehemothSpeedConfigValue) {
        MessageHandler.DEFAULT_CHANNEL.sendToAllPlayers(new SyncBeehemothSpeedConfigFromServer(beehemothSpeedConfigValue), Objects.requireNonNull(entity.level().getServer()));
    }

    public static void sendToClient(DatapackSyncEvent event) {
        MessageHandler.DEFAULT_CHANNEL.sendToPlayer(new SyncBeehemothSpeedConfigFromServer(BzGeneralConfigs.beehemothSpeed), event.player());
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<SyncBeehemothSpeedConfigFromServer> getHandler() {
        return HANDLER;
    }

    private static final class Handler implements PacketHandler<SyncBeehemothSpeedConfigFromServer> {

        @Override
        public void encode(SyncBeehemothSpeedConfigFromServer message, FriendlyByteBuf buffer) {
            buffer.writeDouble(message.newBeehemothSpeed());
        }

        @Override
        public SyncBeehemothSpeedConfigFromServer decode(FriendlyByteBuf buffer) {
            return new SyncBeehemothSpeedConfigFromServer(buffer.readDouble());
        }

        @Override
        public PacketContext handle(SyncBeehemothSpeedConfigFromServer message) {
            return (player, level) -> {
                BeehemothEntity.beehemothSpeedConfigValue = message.newBeehemothSpeed();
            };
        }
    }
}
