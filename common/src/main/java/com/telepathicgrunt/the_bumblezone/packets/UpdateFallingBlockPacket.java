package com.telepathicgrunt.the_bumblezone.packets;

import com.telepathicgrunt.the_bumblezone.blocks.PileOfPollen;
import com.telepathicgrunt.the_bumblezone.mixin.blocks.FallingBlockEntityAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public record UpdateFallingBlockPacket(int fallingBlockId, short layer) {

    public static void sendToClient(Entity entity, int fallingBlockId, short layer) {
        MessageHandler.DEFAULT_CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new UpdateFallingBlockPacket(fallingBlockId, layer));
    }

    /*
     * How the client will read the packet.
     */
    public static UpdateFallingBlockPacket parse(final FriendlyByteBuf buf) {
        return new UpdateFallingBlockPacket(buf.readInt(), buf.readShort());
    }

    /*
     * creates the packet buffer and sets its values
     */
    public static void compose(final UpdateFallingBlockPacket pkt, final FriendlyByteBuf buf) {
        buf.writeInt(pkt.fallingBlockId);
        buf.writeShort(pkt.layer);
    }

    /*
     * What the client will do with the packet
     */
    public static class Handler {
        //this is what gets run on the client
        public static void handle(final UpdateFallingBlockPacket pkt, final Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                if(Minecraft.getInstance().level == null) {
                    return;
                }

                Entity entity = Minecraft.getInstance().level.getEntity(pkt.fallingBlockId);
                if (entity instanceof FallingBlockEntity fallingBlockEntity && fallingBlockEntity.getBlockState().is(BzBlocks.PILE_OF_POLLEN.get())) {
                    ((FallingBlockEntityAccessor) fallingBlockEntity).setBlockState(BzBlocks.PILE_OF_POLLEN.get().defaultBlockState().setValue(PileOfPollen.LAYERS, (int) pkt.layer));
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
