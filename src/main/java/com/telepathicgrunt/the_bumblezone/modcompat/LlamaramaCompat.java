package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzConfig;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzDimension;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.world.dimension.BzWorldSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class LlamaramaCompat {

    private static final ResourceLocation BUMBLE_LLAMA_RL = new ResourceLocation("llamarama", "bumble_llama");

    public static void setupCompat() {
       // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.llamaramaPresent = true;
    }

    public static boolean runTeleportCodeIfBumbleLlamaHitHigh(HitResult hitResult, Projectile pearlEntity) {
        Level world = pearlEntity.level; // world we threw in

        // Make sure we are on server by checking if thrower is ServerPlayer and that we are not in bumblezone.
        // If onlyOverworldHivesTeleports is set to true, then only run this code in Overworld.
        if (!world.isClientSide() &&
            hitResult instanceof EntityHitResult entityHitResult &&
            Registry.ENTITY_TYPE.getKey(entityHitResult.getEntity().getType()).equals(BUMBLE_LLAMA_RL) &&
            BzConfig.enableEntranceTeleportation &&
            pearlEntity.getOwner() instanceof ServerPlayer playerEntity &&
            !world.dimension().location().equals(Bumblezone.MOD_DIMENSION_ID) &&
            (!BzConfig.onlyOverworldHivesTeleports || world.dimension().equals(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(BzConfig.defaultDimension)))))
        {
            Vec3 hitPos = pearlEntity.position();
            AABB boundBox = entityHitResult.getEntity().getBoundingBox();
            double minYThreshold = ((boundBox.maxY - boundBox.minY) * 0.66d) + boundBox.minY;

            if (hitPos.y() < minYThreshold) {
                return false;
            }

            BlockPos hivePos = entityHitResult.getEntity().blockPosition();

            //checks if block under hive is correct if config needs one
            boolean validBelowBlock = false;
            Optional<HolderSet.Named<Block>> blockTag = Registry.BLOCK.getTag(BzTags.REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT);
            if (blockTag.isPresent() && blockTag.get().size() != 0) {
                if (world.getBlockState(hivePos.below()).is(BzTags.REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT)) {
                    validBelowBlock = true;
                }
                else if (BzConfig.warnPlayersOfWrongBlockUnderHive) {
                    //failed. Block below isn't the required block
                    Bumblezone.LOGGER.log(org.apache.logging.log4j.Level.INFO, "Bumblezone: the_bumblezone:required_blocks_under_hive_to_teleport tag does not have the block below the hive.");
                    Component message = Component.translatable("system.the_bumblezone.require_hive_blocks_failed");
                    playerEntity.displayClientMessage(message, true);
                    return false;
                }
            }
            else {
                validBelowBlock = true;
            }


            //if the pearl hit a beehive, begin the teleportation.
            if (validBelowBlock) {
                BzCriterias.TELEPORT_TO_BUMBLEZONE_PEARL_TRIGGER.trigger(playerEntity);
                BzWorldSavedData.queueEntityToTeleport(playerEntity, BzDimension.BZ_WORLD_KEY);
                pearlEntity.discard();
                return true;
            }
        }

        return false;
    }
}
