package com.telepathicgrunt.the_bumblezone.packets;

import com.teamresourceful.resourcefullib.common.network.Network;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.resources.ResourceLocation;

public class MessageHandler {

    //setup channel to send packages through
    public static final Network DEFAULT_CHANNEL = new Network(ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "networking"), 1);

    /*
     * Register the channel so it exists
     */
    public static void init() {
        DEFAULT_CHANNEL.register(BeehemothControlsPacket.TYPE);
        DEFAULT_CHANNEL.register(BumbleBeeChestplateFlyingPacket.TYPE);
        DEFAULT_CHANNEL.register(StinglessBeeHelmetSightPacket.TYPE);
        DEFAULT_CHANNEL.register(CrystallineFlowerClickedEnchantmentButtonPacket.TYPE);
        DEFAULT_CHANNEL.register(SyncHorseOwnerUUIDPacketToServer.TYPE);

        DEFAULT_CHANNEL.register(CrystallineFlowerEnchantmentPacket.TYPE);
        DEFAULT_CHANNEL.register(MobEffectClientSyncPacket.TYPE);
        DEFAULT_CHANNEL.register(UpdateFallingBlockPacket.TYPE);
        DEFAULT_CHANNEL.register(QueenMainTradesSyncPacket.TYPE);
        DEFAULT_CHANNEL.register(QueenRandomizerTradesSyncPacket.TYPE);
        DEFAULT_CHANNEL.register(SyncHorseOwnerUUIDPacketFromServer.TYPE);
        DEFAULT_CHANNEL.register(SyncBeehemothSpeedConfigFromServer.TYPE);
        DEFAULT_CHANNEL.register(MusicPacketFromServer.TYPE);
    }
}
