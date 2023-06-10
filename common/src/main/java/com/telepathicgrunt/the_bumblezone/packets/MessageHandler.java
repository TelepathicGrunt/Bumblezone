package com.telepathicgrunt.the_bumblezone.packets;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.packets.networking.NetworkChannel;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.NetworkDirection;

public class MessageHandler {

    //setup channel to send packages through
    public static final NetworkChannel DEFAULT_CHANNEL = new NetworkChannel(Bumblezone.MODID, 1, "networking");

    /*
     * Register the channel so it exists
     */
    public static void init() {
        DEFAULT_CHANNEL.registerPacket(NetworkDirection.CLIENT_TO_SERVER, BeehemothControlsPacket.ID, BeehemothControlsPacket.HANDLER, BeehemothControlsPacket.class);
        DEFAULT_CHANNEL.registerPacket(NetworkDirection.CLIENT_TO_SERVER, BumbleBeeChestplateFlyingPacket.ID, BumbleBeeChestplateFlyingPacket.HANDLER, BumbleBeeChestplateFlyingPacket.class);
        DEFAULT_CHANNEL.registerPacket(NetworkDirection.CLIENT_TO_SERVER, StinglessBeeHelmetSightPacket.ID, StinglessBeeHelmetSightPacket.HANDLER, StinglessBeeHelmetSightPacket.class);
        DEFAULT_CHANNEL.registerPacket(NetworkDirection.CLIENT_TO_SERVER, CrystallineFlowerClickedEnchantmentButtonPacket.ID, CrystallineFlowerClickedEnchantmentButtonPacket.HANDLER, CrystallineFlowerClickedEnchantmentButtonPacket.class);
        DEFAULT_CHANNEL.registerPacket(NetworkDirection.CLIENT_TO_SERVER, SyncHorseOwnerUUIDPacketToServer.ID, SyncHorseOwnerUUIDPacketToServer.HANDLER, SyncHorseOwnerUUIDPacketToServer.class);

        DEFAULT_CHANNEL.registerPacket(NetworkDirection.SERVER_TO_CLIENT, CrystallineFlowerEnchantmentPacket.ID, CrystallineFlowerEnchantmentPacket.HANDLER, CrystallineFlowerEnchantmentPacket.class);
        DEFAULT_CHANNEL.registerPacket(NetworkDirection.SERVER_TO_CLIENT, MobEffectClientSyncPacket.ID, MobEffectClientSyncPacket.HANDLER, MobEffectClientSyncPacket.class);
        DEFAULT_CHANNEL.registerPacket(NetworkDirection.SERVER_TO_CLIENT, UpdateFallingBlockPacket.ID, UpdateFallingBlockPacket.HANDLER, UpdateFallingBlockPacket.class);
        DEFAULT_CHANNEL.registerPacket(NetworkDirection.SERVER_TO_CLIENT, QueenMainTradesSyncPacket.ID, QueenMainTradesSyncPacket.HANDLER, QueenMainTradesSyncPacket.class);
        DEFAULT_CHANNEL.registerPacket(NetworkDirection.SERVER_TO_CLIENT, QueenRandomizerTradesSyncPacket.ID, QueenRandomizerTradesSyncPacket.HANDLER, QueenRandomizerTradesSyncPacket.class);
        DEFAULT_CHANNEL.registerPacket(NetworkDirection.SERVER_TO_CLIENT, SyncHorseOwnerUUIDPacketFromServer.ID, SyncHorseOwnerUUIDPacketFromServer.HANDLER, SyncHorseOwnerUUIDPacketFromServer.class);
    }
}
