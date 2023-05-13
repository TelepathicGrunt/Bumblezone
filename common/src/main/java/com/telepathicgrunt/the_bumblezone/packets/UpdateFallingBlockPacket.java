package com.telepathicgrunt.the_bumblezone.packets;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.PileOfPollen;
import com.telepathicgrunt.the_bumblezone.mixin.blocks.FallingBlockEntityAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.Packet;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketContext;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;

public record UpdateFallingBlockPacket(int fallingBlockId, short layer) implements Packet<UpdateFallingBlockPacket> {

    public static final ResourceLocation ID = new ResourceLocation(Bumblezone.MODID, "update_falling_block");
    public static final Handler HANDLER = new Handler();

    public static void sendToClient(Entity entity, int fallingBlockId, short layer) {
        MessageHandler.DEFAULT_CHANNEL.sendToAllLoaded(new UpdateFallingBlockPacket(fallingBlockId, layer), entity.level(), entity.blockPosition());
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<UpdateFallingBlockPacket> getHandler() {
        return HANDLER;
    }

    private static final class Handler implements PacketHandler<UpdateFallingBlockPacket> {

        @Override
        public void encode(UpdateFallingBlockPacket message, FriendlyByteBuf buffer) {
            buffer.writeVarInt(message.fallingBlockId);
            buffer.writeShort(message.layer);
        }

        @Override
        public UpdateFallingBlockPacket decode(FriendlyByteBuf buffer) {
            return new UpdateFallingBlockPacket(buffer.readVarInt(), buffer.readShort());
        }

        @Override
        public PacketContext handle(UpdateFallingBlockPacket message) {
            return (player, level) -> {
                Entity entity = level.getEntity(message.fallingBlockId);
                if (entity instanceof FallingBlockEntity fallingBlockEntity && fallingBlockEntity.getBlockState().is(BzBlocks.PILE_OF_POLLEN.get())) {
                    ((FallingBlockEntityAccessor) fallingBlockEntity).setBlockState(BzBlocks.PILE_OF_POLLEN.get().defaultBlockState().setValue(PileOfPollen.LAYERS, (int) message.layer));
                }
            };
        }
    }
}
