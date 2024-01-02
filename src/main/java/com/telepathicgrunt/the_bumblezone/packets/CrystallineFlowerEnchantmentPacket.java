package com.telepathicgrunt.the_bumblezone.packets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.telepathicgrunt.the_bumblezone.screens.CrystallineFlowerMenu;
import com.telepathicgrunt.the_bumblezone.screens.CrystallineFlowerScreen;
import com.telepathicgrunt.the_bumblezone.screens.EnchantmentSkeleton;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtilsClient;
import net.minecraft.client.Minecraft;
import net.minecraft.locale.Language;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public record CrystallineFlowerEnchantmentPacket(int containerId, List<EnchantmentSkeleton> enchantmentSkeletons, ResourceLocation selectedResourceLocation) {
    public static Gson gson = new GsonBuilder().create();

    public static void sendToClient(ServerPlayer entity, int containerId, List<EnchantmentSkeleton> enchantmentSkeletons, ResourceLocation selectedResourceLocation) {
        MessageHandler.DEFAULT_CHANNEL.send(PacketDistributor.PLAYER.with(() -> entity),
                new CrystallineFlowerEnchantmentPacket(containerId, enchantmentSkeletons, selectedResourceLocation));
    }

    /*
     * How the client will read the packet.
     */
    public static CrystallineFlowerEnchantmentPacket parse(final FriendlyByteBuf buf) {
        int containerId = buf.readInt();
        List<EnchantmentSkeleton> enchantmentSkeletons = new ArrayList<>();
        int elements = buf.readInt();
        for (int i = 0; i < elements; i++) {
            String jsonData = buf.readUtf();
            enchantmentSkeletons.add(gson.fromJson(jsonData, EnchantmentSkeleton.class));
        }
        ResourceLocation selectedResourceLocation = buf.readResourceLocation();
        return new CrystallineFlowerEnchantmentPacket(containerId, enchantmentSkeletons, selectedResourceLocation);
    }

    /*
     * creates the packet buffer and sets its values
     */
    public static void compose(final CrystallineFlowerEnchantmentPacket pkt, final FriendlyByteBuf buf) {
        buf.writeInt(pkt.containerId());
        buf.writeInt(pkt.enchantmentSkeletons().size());
        for (EnchantmentSkeleton enchantmentSkeleton : pkt.enchantmentSkeletons()) {
            buf.writeUtf(gson.toJson(enchantmentSkeleton));
        }
        buf.writeResourceLocation(pkt.selectedResourceLocation());
    }

    /*
     * What the client will do with the packet
     */
    public static class Handler {
        //this is what gets run on the client
        public static void handle(final CrystallineFlowerEnchantmentPacket pkt, final Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.containerMenu.containerId == pkt.containerId()) {
                    Map<ResourceLocation, EnchantmentSkeleton> map = new HashMap<>();
                    for (EnchantmentSkeleton enchantmentSkeleton : pkt.enchantmentSkeletons()) {
                        map.put(new ResourceLocation(enchantmentSkeleton.namespace, enchantmentSkeleton.path), enchantmentSkeleton);
                    }
                    CrystallineFlowerScreen.enchantmentsAvailable = map;

                    Language language = Language.getInstance();
                    CrystallineFlowerScreen.enchantmentsAvailableSortedList = map.keySet().stream().sorted((r1, r2) -> {
                        String s1 = language.getLanguageData().getOrDefault("enchantment."+r1.getNamespace()+"."+r1.getPath(), r1.getPath());
                        String s2 = language.getLanguageData().getOrDefault("enchantment."+r2.getNamespace()+"."+r2.getPath(), r2.getPath());
                        return s1.compareTo(s2);
                    }).collect(Collectors.toList());

                    if (GeneralUtilsClient.getClientPlayer().containerMenu instanceof CrystallineFlowerMenu crystallineFlowerMenu) {
                        crystallineFlowerMenu.selectedEnchantment = pkt.selectedResourceLocation().equals(new ResourceLocation("minecraft", "empty")) ? null : pkt.selectedResourceLocation();
                    }
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
