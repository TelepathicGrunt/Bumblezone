package com.telepathicgrunt.the_bumblezone.packets;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.items.BumbleBeeChestplate;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.Packet;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketContext;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record BumbleBeeChestplateFlyingPacket(byte isFlying) implements Packet<BumbleBeeChestplateFlyingPacket> {

    public static final ResourceLocation ID = new ResourceLocation(Bumblezone.MODID, "bumblebee_chestplate_flying");
    public static final Handler HANDLER = new Handler();

    public static void sendToServer(boolean isFlying) {
        MessageHandler.DEFAULT_CHANNEL.sendToServer(new BumbleBeeChestplateFlyingPacket((byte) (isFlying ? 1 : 0)));
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<BumbleBeeChestplateFlyingPacket> getHandler() {
        return HANDLER;
    }


    private static class Handler implements PacketHandler<BumbleBeeChestplateFlyingPacket> {

        @Override
        public void encode(BumbleBeeChestplateFlyingPacket message, FriendlyByteBuf buffer) {
            buffer.writeByte(message.isFlying);
        }

        @Override
        public BumbleBeeChestplateFlyingPacket decode(FriendlyByteBuf buffer) {
            return new BumbleBeeChestplateFlyingPacket(buffer.readByte());
        }

        @Override
        public PacketContext handle(BumbleBeeChestplateFlyingPacket message) {
            return (player, level) -> {
                ItemStack itemStack = BumbleBeeChestplate.getEntityBeeChestplate(player);
                if(!itemStack.isEmpty()) {
                    CompoundTag tag = itemStack.getOrCreateTag();
                    tag.putBoolean("isFlying", message.isFlying() != 0);
                }
            };
        }
    }
}
