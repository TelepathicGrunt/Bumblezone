package com.telepathicgrunt.the_bumblezone.packets;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.MusicHandler;
import com.telepathicgrunt.the_bumblezone.configs.BzClientConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public record MusicPacketFromServer(ResourceLocation musicRL, boolean play) implements Packet<MusicPacketFromServer> {

    public static final ResourceLocation ID = new ResourceLocation(Bumblezone.MODID, "music_packet_from_server");
    public static final ClientboundPacketType<MusicPacketFromServer> TYPE = new MusicPacketFromServer.Handler();

    public static void sendToClient(Player player, ResourceLocation musicRL, boolean play) {
        MessageHandler.DEFAULT_CHANNEL.sendToPlayer(new MusicPacketFromServer(musicRL, play), player);
    }

    @Override
    public PacketType<MusicPacketFromServer> type() {
        return TYPE;
    }

    private static final class Handler implements ClientboundPacketType<MusicPacketFromServer> {

        @Override
        public void encode(MusicPacketFromServer message, RegistryFriendlyByteBuf buffer) {
            buffer.writeResourceLocation(message.musicRL());
            buffer.writeBoolean(message.play());
        }

        @Override
        public MusicPacketFromServer decode(RegistryFriendlyByteBuf buffer) {
            return new MusicPacketFromServer(buffer.readResourceLocation(), buffer.readBoolean());
        }

        @Override
        public Runnable handle(MusicPacketFromServer message) {
            return () -> {
                Player player = Minecraft.getInstance().player;
                MusicHandler.playStopSempiternalSanctumMusic(player, message.musicRL(), message.play() && BzClientConfigs.playSempiternalSanctumMusic);
                MusicHandler.playStopEssenceEventMusic(player, message.musicRL(), message.play() && BzClientConfigs.playSempiternalSanctumMusic);
            };
        }

        @Override
        public Class<MusicPacketFromServer> type() {
            return MusicPacketFromServer.class;
        }

        @Override
        public ResourceLocation id() {
            return ID;
        }
    }
}
