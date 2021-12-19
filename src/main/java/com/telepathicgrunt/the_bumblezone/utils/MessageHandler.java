package com.telepathicgrunt.the_bumblezone.utils;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.packets.UpdateFallingBlockPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

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
    }
}
