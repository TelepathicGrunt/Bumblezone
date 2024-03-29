package com.telepathicgrunt.the_bumblezone.packets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.screens.CrystallineFlowerScreen;
import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import com.telepathicgrunt.the_bumblezone.menus.CrystallineFlowerMenu;
import com.telepathicgrunt.the_bumblezone.menus.EnchantmentSkeleton;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.Packet;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketContext;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketHandler;
import net.minecraft.locale.Language;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record CrystallineFlowerEnchantmentPacket(int containerId, List<EnchantmentSkeleton> enchantmentSkeletons, ResourceLocation selectedResourceLocation) implements Packet<CrystallineFlowerEnchantmentPacket> {
    public static final Gson GSON = new GsonBuilder().create();

    public static final ResourceLocation ID = new ResourceLocation(Bumblezone.MODID, "crystalline_flower_enchantment");
    static final Handler HANDLER = new Handler();

    public static void sendToClient(ServerPlayer player, int containerId, List<EnchantmentSkeleton> enchantmentSkeletons, ResourceLocation selectedResourceLocation) {
        MessageHandler.DEFAULT_CHANNEL.sendToPlayer(new CrystallineFlowerEnchantmentPacket(containerId, enchantmentSkeletons, selectedResourceLocation), player);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<CrystallineFlowerEnchantmentPacket> getHandler() {
        return HANDLER;
    }

    private static final class Handler implements PacketHandler<CrystallineFlowerEnchantmentPacket> {

        @Override
        public void encode(CrystallineFlowerEnchantmentPacket message, FriendlyByteBuf buffer) {
            buffer.writeInt(message.containerId());
            buffer.writeCollection(message.enchantmentSkeletons(), (buf, enchantmentSkeleton) -> buf.writeUtf(GSON.toJson(enchantmentSkeleton)));
            buffer.writeResourceLocation(message.selectedResourceLocation);
        }

        @Override
        public CrystallineFlowerEnchantmentPacket decode(FriendlyByteBuf buffer) {
            return new CrystallineFlowerEnchantmentPacket(
                    buffer.readInt(),
                    buffer.readList(buf -> GSON.fromJson(buf.readUtf(), EnchantmentSkeleton.class)),
                    buffer.readResourceLocation());
        }

        @Override
        public PacketContext handle(CrystallineFlowerEnchantmentPacket message) {
            return (player, level) -> {
                if (GeneralUtilsClient.getClientPlayer() != null && GeneralUtilsClient.getClientPlayer().containerMenu.containerId == message.containerId) {
                    Map<ResourceLocation, EnchantmentSkeleton> map = new HashMap<>();
                    for (EnchantmentSkeleton enchantmentSkeleton : message.enchantmentSkeletons()) {
                        map.put(new ResourceLocation(enchantmentSkeleton.namespace, enchantmentSkeleton.path), enchantmentSkeleton);
                    }
                    CrystallineFlowerScreen.enchantmentsAvailable = map;

                    Language language = Language.getInstance();
                    CrystallineFlowerScreen.enchantmentsAvailableSortedList = map.keySet().stream().sorted((r1, r2) -> {
                        String s1 = language.getOrDefault("enchantment."+r1.getNamespace()+"."+r1.getPath(), r1.getPath());
                        String s2 = language.getOrDefault("enchantment."+r2.getNamespace()+"."+r2.getPath(), r2.getPath());
                        return s1.compareTo(s2);
                    }).collect(Collectors.toList());

                    if (GeneralUtilsClient.getClientPlayer().containerMenu instanceof CrystallineFlowerMenu crystallineFlowerMenu) {
                        crystallineFlowerMenu.selectedEnchantment = message.selectedResourceLocation().equals(new ResourceLocation("minecraft", "empty")) ? null : message.selectedResourceLocation();
                    }
                }
            };
        }
    }
}
