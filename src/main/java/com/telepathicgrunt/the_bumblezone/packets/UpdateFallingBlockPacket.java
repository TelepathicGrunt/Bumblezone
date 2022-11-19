package com.telepathicgrunt.the_bumblezone.packets;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.PileOfPollen;
import com.telepathicgrunt.the_bumblezone.mixin.blocks.FallingBlockEntityAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;

public class UpdateFallingBlockPacket {

    public static ResourceLocation PACKET_ID = new ResourceLocation(Bumblezone.MODID, "update_falling_blocks_packet");

    public static void registerPacket() {
        ClientPlayNetworking.registerGlobalReceiver(PACKET_ID,
            (client, handler, buf, responseSender) -> {
                int fallingBlockId = buf.readInt();
                short layer = buf.readShort();

                client.execute(() -> {
                    if(Minecraft.getInstance().level == null) {
                        return;
                    }

                    Entity entity = Minecraft.getInstance().level.getEntity(fallingBlockId);
                    if(entity instanceof FallingBlockEntity fallingBlockEntity && fallingBlockEntity.getBlockState().is(BzBlocks.PILE_OF_POLLEN)) {
                        ((FallingBlockEntityAccessor)fallingBlockEntity).setBlock(BzBlocks.PILE_OF_POLLEN.defaultBlockState().setValue(PileOfPollen.LAYERS, (int)layer));
                    }
                });
            });
    }
}
