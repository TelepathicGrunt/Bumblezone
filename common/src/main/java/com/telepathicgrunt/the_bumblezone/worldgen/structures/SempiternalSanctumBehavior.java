package com.telepathicgrunt.the_bumblezone.worldgen.structures;

import com.telepathicgrunt.the_bumblezone.blocks.blockentities.EssenceBlockEntity;
import com.telepathicgrunt.the_bumblezone.items.essence.EssenceOfTheBees;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzDimension;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.packets.MusicPacketFromServer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.UUID;

public class SempiternalSanctumBehavior {

    private static final HashSet<UUID> PLAYERS_IN_SANCTUMS = new HashSet<>();

    //Apply mining fatigue when in sanctum
    public static void runStructureMessagesAndFatigue(ServerPlayer serverPlayer) {
        StructureManager structureManager = ((ServerLevel)serverPlayer.level()).structureManager();
        StructureStart detectedStructure = structureManager.getStructureWithPieceAt(serverPlayer.blockPosition(), BzTags.SEMPITERNAL_SANCTUMS);
        if (detectedStructure.isValid()) {
            if (EssenceOfTheBees.hasEssence(serverPlayer)) {
                if (!PLAYERS_IN_SANCTUMS.contains(serverPlayer.getUUID())) {
                    BlockPos structureCenter = detectedStructure.getBoundingBox().getCenter().below(20);

                    // Only display message if sanctum is active with essence block
                    if (!serverPlayer.level().getBlockState(structureCenter).is(BzTags.ESSENCE_BLOCKS)) {
                        return;
                    }

                    PLAYERS_IN_SANCTUMS.add(serverPlayer.getUUID());

                    // Don't send message if player logs in while in structure.
                    if (serverPlayer.tickCount > 40) {
                        ResourceLocation resourceLocation = serverPlayer.level().registryAccess()
                                .registry(Registries.STRUCTURE).get()
                                .getKey(detectedStructure.getStructure());

                        ChatFormatting color;
                        if (resourceLocation == null) {
                            return;
                        }
                        else if (resourceLocation.getPath().contains("_red")) {
                            color = ChatFormatting.RED;
                        }
                        else if (resourceLocation.getPath().contains("_yellow")) {
                            color = ChatFormatting.YELLOW;
                        }
                        else if (resourceLocation.getPath().contains("_green")) {
                            color = ChatFormatting.GREEN;
                        }
                        else if (resourceLocation.getPath().contains("_blue")) {
                            color = ChatFormatting.BLUE;
                        }
                        else if (resourceLocation.getPath().contains("_purple")) {
                            color = ChatFormatting.LIGHT_PURPLE;
                        }
                        else {
                            color = ChatFormatting.WHITE;
                        }

                        Component message = Component.translatable("system.the_bumblezone." + resourceLocation.getPath())
                                .withStyle(ChatFormatting.BOLD)
                                .withStyle(color);

                        serverPlayer.displayClientMessage(message, true);
                    }
                }

                BzCriterias.SEMPITERNAL_SANCTUM_ENTER_WITH_BEE_ESSENCE_TRIGGER.trigger(serverPlayer);
            }
            else if(!serverPlayer.isCreative() && !serverPlayer.isSpectator()) {
                MobEffectInstance effect = serverPlayer.getEffect(MobEffects.DIG_SLOWDOWN);
                if (effect == null || effect.getAmplifier() <= 2) {
                    Component message = Component.translatable("system.the_bumblezone.no_essence").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.RED);
                    serverPlayer.displayClientMessage(message, true);
                    serverPlayer.addEffect(new MobEffectInstance(
                            MobEffects.DIG_SLOWDOWN,
                            800,
                            3,
                            false,
                            false,
                            true));
                }
            }

            if (serverPlayer.tickCount % 60 == 20) {
                MusicPacketFromServer.sendToClient(serverPlayer, BzSounds.SEMPITERNAL_SANCTUM.get().getLocation(), true);
            }

        }
        else {
            if (serverPlayer.tickCount % 60 == 20 && PLAYERS_IN_SANCTUMS.contains(serverPlayer.getUUID())) {
                MusicPacketFromServer.sendToClient(serverPlayer, BzSounds.SEMPITERNAL_SANCTUM.get().getLocation(), false);
                MusicPacketFromServer.sendToClient(serverPlayer, BzSounds.RAGING_EVENT.get().getLocation(), false);
                MusicPacketFromServer.sendToClient(serverPlayer, BzSounds.RADIANCE_EVENT.get().getLocation(), false);
                MusicPacketFromServer.sendToClient(serverPlayer, BzSounds.LIFE_EVENT.get().getLocation(), false);
                MusicPacketFromServer.sendToClient(serverPlayer, BzSounds.CALMING_EVENT.get().getLocation(), false);
                MusicPacketFromServer.sendToClient(serverPlayer, BzSounds.KNOWING_EVENT.get().getLocation(), false);
                MusicPacketFromServer.sendToClient(serverPlayer, BzSounds.CONTINUITY_EVENT.get().getLocation(), false);

                PLAYERS_IN_SANCTUMS.remove(serverPlayer.getUUID());
            }
        }
    }

    public static void onArenaAreaDeath(ServerLevel serverLevel, ServerPlayer serverPlayer) {
        if (serverLevel.dimension().equals(BzDimension.BZ_WORLD_KEY)) {

            StructureManager structureManager = serverLevel.structureManager();
            StructureStart detectedStructure = structureManager.getStructureWithPieceAt(serverPlayer.blockPosition(), BzTags.SEMPITERNAL_SANCTUMS);
            if (detectedStructure.isValid()) {

                BlockPos structureCenter = detectedStructure.getBoundingBox().getCenter().below(20);
                BlockState blockState = serverLevel.getBlockState(structureCenter);
                BlockEntity blockEntity = serverLevel.getBlockEntity(structureCenter);
                if (!(blockState.is(BzTags.ESSENCE_BLOCKS) &&
                    blockEntity instanceof EssenceBlockEntity essenceBlockEntity &&
                    AABB.ofSize(
                            Vec3.atCenterOf(structureCenter),
                            essenceBlockEntity.getArenaSize().getX() + 2,
                            essenceBlockEntity.getArenaSize().getY() + 2,
                            essenceBlockEntity.getArenaSize().getZ() + 2
                    ).contains(Vec3.atCenterOf(serverPlayer.blockPosition()))))
                {
                    return;
                }

                Direction directionOffset = Direction.Plane.HORIZONTAL.getRandomDirection(serverLevel.getRandom());
                int xOffset = (int) (((essenceBlockEntity.getArenaSize().getX() * 0.5f) + 3) * directionOffset.getStepX());
                int zOffset = (int) (((essenceBlockEntity.getArenaSize().getZ() * 0.5f) + 3) * directionOffset.getStepZ());
                Vec3 newPosition = Vec3.atCenterOf(structureCenter.offset(xOffset, -6, zOffset));
                serverPlayer.removeVehicle();
                serverPlayer.setDeltaMovement(new Vec3(0, 0, 0));
                serverPlayer.setPos(newPosition);
                serverPlayer.setOldPosAndRot();
                serverPlayer.teleportTo(serverLevel, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), serverPlayer.getYRot(), serverPlayer.getXRot());
            }
        }
    }
}
