package com.telepathicgrunt.the_bumblezone.packets;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.base.ServerboundPacketType;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.menus.CrystallineFlowerMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public record CrystallineFlowerClickedEnchantmentButtonPacket(int containerId, ResourceLocation clickedButton) implements Packet<CrystallineFlowerClickedEnchantmentButtonPacket> {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "crystalline_flower_clicked_enchantment_button_packet");
    public static final ServerboundPacketType<CrystallineFlowerClickedEnchantmentButtonPacket> TYPE = new CrystallineFlowerClickedEnchantmentButtonPacket.Handler();

    public static void sendToServer(int containIdIn, ResourceLocation ClickedButtonIn) {
        MessageHandler.DEFAULT_CHANNEL.sendToServer(new CrystallineFlowerClickedEnchantmentButtonPacket(containIdIn, ClickedButtonIn));
    }

    @Override
    public PacketType<CrystallineFlowerClickedEnchantmentButtonPacket> type() {
        return TYPE;
    }

    private static class Handler implements ServerboundPacketType<CrystallineFlowerClickedEnchantmentButtonPacket> {

        @Override
        public void encode(CrystallineFlowerClickedEnchantmentButtonPacket message, RegistryFriendlyByteBuf buffer) {
            buffer.writeInt(message.containerId);
            buffer.writeResourceLocation(message.clickedButton);
        }

        @Override
        public CrystallineFlowerClickedEnchantmentButtonPacket decode(RegistryFriendlyByteBuf buffer) {
            return new CrystallineFlowerClickedEnchantmentButtonPacket(buffer.readInt(), buffer.readResourceLocation());
        }

        @Override
        public Consumer<Player> handle(CrystallineFlowerClickedEnchantmentButtonPacket message) {
            return (player) -> {
                if(player == null) {
                    return;
                }

                if (player.containerMenu.containerId == message.containerId() && player.containerMenu instanceof CrystallineFlowerMenu flowerMenu) {
                    flowerMenu.clickMenuEnchantment(player, message.clickedButton());
                }
            };
        }

        @Override
        public Class<CrystallineFlowerClickedEnchantmentButtonPacket> type() {
            return CrystallineFlowerClickedEnchantmentButtonPacket.class;
        }

        @Override
        public ResourceLocation id() {
            return ID;
        }
    }

}