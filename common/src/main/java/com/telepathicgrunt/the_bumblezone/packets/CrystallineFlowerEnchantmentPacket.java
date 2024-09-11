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
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                if (GeneralUtilsClient.getClientPlayer() != null && GeneralUtilsClient.getClientPlayer().containerMenu.containerId == message.containerId){
                    if (GeneralUtilsClient.getClientPlayer().containerMenu instanceof CrystallineFlowerMenu crystallineFlowerMenu) {
                        Map<ResourceLocation, EnchantmentSkeleton> map = new HashMap<>();
                        for (EnchantmentSkeleton enchantmentSkeleton : message.enchantmentSkeletons()) {
                            map.put(new ResourceLocation(enchantmentSkeleton.namespace, enchantmentSkeleton.path), enchantmentSkeleton);
                        }
                        crystallineFlowerMenu.selectedEnchantment = null;
                        CrystallineFlowerScreen.enchantmentsAvailable = map;

                        CrystallineFlowerScreen.SortAndAssignAvailableEnchants();

                        crystallineFlowerMenu.selectedEnchantment = message.selectedResourceLocation().equals(new ResourceLocation("minecraft", "empty")) ? null : message.selectedResourceLocation();
                        if (!CrystallineFlowerScreen.enchantmentsAvailable.containsKey(crystallineFlowerMenu.selectedEnchantment)) {
                            crystallineFlowerMenu.selectedEnchantment = CrystallineFlowerScreen.enchantmentsAvailable.keySet().stream().findFirst().orElse(null);
                        }
                    }
                }
            };
        }
    }
}
