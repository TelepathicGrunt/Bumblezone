package com.telepathicgrunt.the_bumblezone.modcompat.neoforge;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import com.telepathicgrunt.the_bumblezone.modinit.BzDimension;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

public class CorailTombstoneCompat implements ModCompat {
    public CorailTombstoneCompat() {
        ModChecker.corailTombstonePresent = true;
    }

    @EventBusSubscriber(modid = Bumblezone.MODID)
    public static final class CorailCompatEvents {

        // Mods.toml has Corail Tombstone as optional and runs after Bumblezone.
        // EventBusSubscriber respect this ordering from mods.toml even if both mod's event are same priority.
        @SubscribeEvent(priority = EventPriority.LOW)
        public static void onDrops(LivingDropsEvent event) {
            // check server player (and not fakePlayer) and if the rule keepInventory is not enabled
            if (ModChecker.corailTombstonePresent &&
                !event.getDrops().isEmpty() &&
                event.getEntity() instanceof ServerPlayer serverPlayer &&
                !serverPlayer.isFakePlayer() && !serverPlayer.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY))
            {
                if (serverPlayer.level().dimension().equals(BzDimension.BZ_WORLD_KEY)) {

                    StructureManager structureManager = serverPlayer.level().structureManager();
                    StructureStart detectedStructure = structureManager.getStructureWithPieceAt(serverPlayer.blockPosition(), BzTags.SEMPITERNAL_SANCTUMS);
                    if (detectedStructure.isValid()) {

                        BlockPos structureCenter = detectedStructure.getBoundingBox().getCenter().below(20);
                        if (!(serverPlayer.level().getBlockState(structureCenter).is(BzTags.ESSENCE_BLOCKS) &&
                            AABB.ofSize(Vec3.atCenterOf(structureCenter), 40, 20, 40).contains(Vec3.atCenterOf(serverPlayer.blockPosition()))))
                        {
                            return;
                        }

                        for (ItemEntity itemEntity : event.getDrops()) {
                            itemEntity.setUnlimitedLifetime();
                            itemEntity.setPickUpDelay(40);
                            itemEntity.setPos(serverPlayer.getEyePosition());
                            itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().multiply(0.0, 1.0, 0.0));
                            serverPlayer.level().addFreshEntity(itemEntity);
                        }

                        serverPlayer.displayClientMessage(Component.translatable("system.the_bumblezone.corail_tombstone_drop_compat").withStyle(ChatFormatting.GOLD), false);

                        // clear the collection to prevent a grave to spawn
                        event.getDrops().clear();
                    }
                }
            }
        }
    }
}
