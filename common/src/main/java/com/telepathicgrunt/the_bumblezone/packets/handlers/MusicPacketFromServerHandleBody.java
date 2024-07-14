package com.telepathicgrunt.the_bumblezone.packets.handlers;

import com.telepathicgrunt.the_bumblezone.client.MusicHandler;
import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import com.telepathicgrunt.the_bumblezone.configs.BzClientConfigs;
import com.telepathicgrunt.the_bumblezone.packets.MusicPacketFromServer;
import net.minecraft.world.entity.player.Player;

public class MusicPacketFromServerHandleBody {
    public static void handle(MusicPacketFromServer message) {
        Player player = GeneralUtilsClient.getClientPlayer();
        MusicHandler.playStopSempiternalSanctumMusic(player, message.musicRL(), message.play() && BzClientConfigs.playSempiternalSanctumMusic);
        MusicHandler.playStopEssenceEventMusic(player, message.musicRL(), message.play() && BzClientConfigs.playSempiternalSanctumMusic);
    }
}