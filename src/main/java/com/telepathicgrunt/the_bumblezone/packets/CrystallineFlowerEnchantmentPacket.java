package com.telepathicgrunt.the_bumblezone.packets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.telepathicgrunt.the_bumblezone.screens.CrystallineFlowerScreen;
import com.telepathicgrunt.the_bumblezone.screens.EnchantmentSkeleton;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public record CrystallineFlowerEnchantmentPacket(List<EnchantmentSkeleton> enchantmentSkeletons) {
    public static Gson gson = new GsonBuilder().create();

    public static void sendToClient(ServerPlayer entity, List<EnchantmentSkeleton> enchantmentSkeletons) {
        MessageHandler.DEFAULT_CHANNEL.send(PacketDistributor.PLAYER.with(() -> entity),
                new CrystallineFlowerEnchantmentPacket(enchantmentSkeletons));
    }

    /*
     * How the client will read the packet.
     */
    public static CrystallineFlowerEnchantmentPacket parse(final FriendlyByteBuf buf) {
        List<EnchantmentSkeleton> enchantmentSkeletons = new ArrayList<>();
        int elements = buf.readInt();
        for (int i = 0; i < elements; i++) {
            String jsonData = buf.readUtf();
            enchantmentSkeletons.add(gson.fromJson(jsonData, EnchantmentSkeleton.class));
        }
        return new CrystallineFlowerEnchantmentPacket(enchantmentSkeletons);
    }

    /*
     * creates the packet buffer and sets its values
     */
    public static void compose(final CrystallineFlowerEnchantmentPacket pkt, final FriendlyByteBuf buf) {
        buf.writeInt(pkt.enchantmentSkeletons().size());
        for (EnchantmentSkeleton enchantmentSkeleton : pkt.enchantmentSkeletons()) {
            buf.writeUtf(gson.toJson(enchantmentSkeleton));
        }
    }

    /*
     * What the client will do with the packet
     */
    public static class Handler {
        //this is what gets run on the client
        public static void handle(final CrystallineFlowerEnchantmentPacket pkt, final Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                CrystallineFlowerScreen.enchantmentsAvailable = pkt.enchantmentSkeletons;
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
