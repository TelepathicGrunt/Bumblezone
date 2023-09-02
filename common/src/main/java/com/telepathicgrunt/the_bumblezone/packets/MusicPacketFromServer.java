package com.telepathicgrunt.the_bumblezone.packets;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.MusicHandler;
import com.telepathicgrunt.the_bumblezone.configs.BzClientConfigs;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.Packet;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketContext;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public record MusicPacketFromServer(ResourceLocation musicRL, boolean play) implements Packet<MusicPacketFromServer> {

    public static final ResourceLocation ID = new ResourceLocation(Bumblezone.MODID, "music_packet_from_server");
    public static final Handler HANDLER = new Handler();

    public static void sendToClient(Player player, ResourceLocation musicRL, boolean play) {
        MessageHandler.DEFAULT_CHANNEL.sendToPlayer(new MusicPacketFromServer(musicRL, play), player);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<MusicPacketFromServer> getHandler() {
        return HANDLER;
    }

    private static final class Handler implements PacketHandler<MusicPacketFromServer> {

        @Override
        public void encode(MusicPacketFromServer message, FriendlyByteBuf buffer) {
            buffer.writeResourceLocation(message.musicRL());
            buffer.writeBoolean(message.play());
        }

        @Override
        public MusicPacketFromServer decode(FriendlyByteBuf buffer) {
            return new MusicPacketFromServer(buffer.readResourceLocation(), buffer.readBoolean());
        }

        @Override
        public PacketContext handle(MusicPacketFromServer message) {
            return (player, level) -> {
                MusicHandler.playStopSempiternalSanctumMusic(player, message.musicRL(), message.play() && BzClientConfigs.playSempiternalSanctumMusic);
                MusicHandler.playStopEssenceEventMusic(player, message.musicRL(), message.play() && BzClientConfigs.playSempiternalSanctumMusic);
            };
        }
    }
}
