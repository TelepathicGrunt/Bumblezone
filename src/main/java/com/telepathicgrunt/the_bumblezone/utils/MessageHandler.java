package com.telepathicgrunt.the_bumblezone.utils;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.packets.MobEffectClientSyncPacket;
import com.telepathicgrunt.the_bumblezone.packets.UpdateFallingBlockPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class MessageHandler {

    //setup channel to send packages through
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel DEFAULT_CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Bumblezone.MODID, "networking"),
            () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals
    );

    /*
     * Register the channel so it exists
     */
    public static void init() {
        int channelID = -1;
        DEFAULT_CHANNEL.registerMessage(++channelID, UpdateFallingBlockPacket.class, UpdateFallingBlockPacket::compose, UpdateFallingBlockPacket::parse, UpdateFallingBlockPacket.Handler::handle);
        DEFAULT_CHANNEL.registerMessage(++channelID, MobEffectClientSyncPacket.class, MobEffectClientSyncPacket::compose, MobEffectClientSyncPacket::parse, MobEffectClientSyncPacket.Handler::handle);
    }
}
