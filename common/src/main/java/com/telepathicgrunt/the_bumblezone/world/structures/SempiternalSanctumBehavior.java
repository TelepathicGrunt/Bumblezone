package com.telepathicgrunt.the_bumblezone.world.structures;

import com.telepathicgrunt.the_bumblezone.items.essence.EssenceOfTheBees;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public class SempiternalSanctumBehavior {

    private static final HashSet<UUID> PLAYERS_IN_SANCTUMS = new HashSet<>();

    //Apply mining fatigue when in sanctum
    public static void runStructureMessagesAndFatigue(ServerPlayer serverPlayer) {
        if(serverPlayer.isCreative() || serverPlayer.isSpectator()) {
            return;
        }

        StructureManager structureManager = ((ServerLevel)serverPlayer.level()).structureManager();
        if (structureManager.getStructureWithPieceAt(serverPlayer.blockPosition(), BzTags.SEMPITERNAL_SANCTUMS).isValid()) {
            if (EssenceOfTheBees.hasEssence(serverPlayer)) {
                if (!PLAYERS_IN_SANCTUMS.contains(serverPlayer.getUUID())) {
                    PLAYERS_IN_SANCTUMS.add(serverPlayer.getUUID());
                    Map<Structure, LongSet> allStructuresAt = structureManager.getAllStructuresAt(serverPlayer.blockPosition());

                    for (Structure structure : allStructuresAt.keySet()) {
                        ResourceLocation resourceLocation = serverPlayer.level().registryAccess().registryOrThrow(Registries.STRUCTURE).getKey(structure);
                        ChatFormatting color;
                        if (resourceLocation.getPath().contains("_red")) {
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
            }
            else {
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
        }
        else {
            PLAYERS_IN_SANCTUMS.remove(serverPlayer.getUUID());
        }
    }
}
