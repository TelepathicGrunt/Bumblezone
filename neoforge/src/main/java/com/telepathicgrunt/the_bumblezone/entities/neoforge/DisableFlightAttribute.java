package com.telepathicgrunt.the_bumblezone.entities.neoforge;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.UUID;

public class DisableFlightAttribute {
    public static final ResourceLocation DISABLE_FLIGHT_RL = ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "disable_flight");
    public static final AttributeModifier DISABLE_FLIGHT = new AttributeModifier(
            DISABLE_FLIGHT_RL,
            -1D,
            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);


    public static void onPlayerTickToRemoveDisabledFlight(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide() &&
            player.level().getServer().getTickCount() % 40 == 5 && // Offset
            !player.isCreative() &&
            !player.isSpectator() &&
            player.isAlive())
        {
            AttributeInstance attributeInstance = player.getAttribute(NeoForgeMod.CREATIVE_FLIGHT);

            if (attributeInstance != null && attributeInstance.hasModifier(DISABLE_FLIGHT_RL)) {
                 removeAttributeIfNotInHeavyAir(player, attributeInstance);
            }
        }
    }

    private static void removeAttributeIfNotInHeavyAir(Player player, AttributeInstance attributeInstance) {
        AABB aABB = player.getBoundingBox();
        BlockPos blockPos = BlockPos.containing(aABB.minX + 1.0E-7, aABB.minY + 1.0E-7, aABB.minZ + 1.0E-7);
        BlockPos blockPos2 = BlockPos.containing(aABB.maxX - 1.0E-7, aABB.maxY - 1.0E-7, aABB.maxZ - 1.0E-7);

        if (player.level().hasChunksAt(blockPos, blockPos2)) {
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

            for (int x = blockPos.getX(); x <= blockPos2.getX(); ++x) {
                for (int z = blockPos.getY(); z <= blockPos2.getY(); ++z) {
                    for (int y = blockPos.getZ(); y <= blockPos2.getZ(); ++y) {

                        mutableBlockPos.set(x, z, y);
                        BlockState blockState = player.level().getBlockState(mutableBlockPos);

                        if (blockState.is(BzBlocks.HEAVY_AIR.get())) {
                            return;
                        }
                    }
                }
            }

            attributeInstance.removeModifier(DISABLE_FLIGHT);
        }
    }
}
