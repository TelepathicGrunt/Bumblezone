package com.telepathicgrunt.the_bumblezone.packets.networking.fabric;

import com.telepathicgrunt.the_bumblezone.packets.networking.base.Packet;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

@ClientOnly
public class QuiltClientPacketHelper {

    @ClientOnly
    public static <T extends Packet<T>> void clientOnlyRegister(ResourceLocation location, PacketHandler<T> handler) {
        ClientPlayNetworking.registerGlobalReceiver(location, (client, handler1, buf, responseSender) -> {
            T decode = handler.decode(buf);
            client.execute(() -> handler.handle(decode).apply(client.player, client.level));
        });
    }

    @ClientOnly
    public static <T extends Packet<T>> void sendToServerClientOnly(ResourceLocation channel, T packet) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        packet.getHandler().encode(packet, buf);
        ClientPlayNetworking.send(createChannelLocation(channel, packet.getID()), buf);
    }

    private static ResourceLocation createChannelLocation(ResourceLocation channel, ResourceLocation id) {
        return new ResourceLocation(channel.getNamespace(), channel.getPath() + "/" + id.getNamespace() + "/" + id.getPath());
    }
}