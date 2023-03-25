package com.telepathicgrunt.the_bumblezone.packets;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.Packet;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketContext;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketHandler;
import com.telepathicgrunt.the_bumblezone.screens.CrystallineFlowerMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record CrystallineFlowerClickedEnchantmentButtonPacket(int containerId, int clickedButton) implements Packet<CrystallineFlowerClickedEnchantmentButtonPacket> {

    public static final ResourceLocation ID = new ResourceLocation(Bumblezone.MODID, "crystalline_flower_clicked_enchantment_button_packet");
    public static final Handler HANDLER = new Handler();

    public static void sendToServer(int containIdIn, int ClickedButtonIn) {
        MessageHandler.DEFAULT_CHANNEL.sendToServer(new CrystallineFlowerClickedEnchantmentButtonPacket(containIdIn, ClickedButtonIn));
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<CrystallineFlowerClickedEnchantmentButtonPacket> getHandler() {
        return HANDLER;
    }

    private static class Handler implements PacketHandler<CrystallineFlowerClickedEnchantmentButtonPacket> {

        @Override
        public void encode(CrystallineFlowerClickedEnchantmentButtonPacket message, FriendlyByteBuf buffer) {
            buffer.writeInt(message.containerId);
            buffer.writeVarInt(message.clickedButton);
        }

        @Override
        public CrystallineFlowerClickedEnchantmentButtonPacket decode(FriendlyByteBuf buffer) {
            return new CrystallineFlowerClickedEnchantmentButtonPacket(buffer.readInt(), buffer.readVarInt());
        }

        @Override
        public PacketContext handle(CrystallineFlowerClickedEnchantmentButtonPacket message) {
            return (player, level) -> {
                if(player == null) {
                    return;
                }

                if (player.containerMenu.containerId == message.containerId() && player.containerMenu instanceof CrystallineFlowerMenu flowerMenu) {
                    flowerMenu.clickMenuButton(player, message.clickedButton());
                }
            };
        }
    }

}