package com.telepathicgrunt.the_bumblezone.world.structures;

import com.mojang.blaze3d.shaders.Effect;
import com.telepathicgrunt.the_bumblezone.configs.BzBeeAggressionConfigs;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.StructureManager;

public class SempiternalSanctumBehavior {

    //Apply mining fatigue when in sanctum
    public static void applyFatigueIfInTaggedStructures(ServerPlayer serverPlayer) {
        if(serverPlayer.isCreative() || serverPlayer.isSpectator()) {
            return;
        }

        StructureManager structureManager = ((ServerLevel)serverPlayer.level()).structureManager();
        if (structureManager.getStructureWithPieceAt(serverPlayer.blockPosition(), BzTags.SEMPITERNAL_SANCTUMS).isValid()) {
            MobEffectInstance effect = serverPlayer.getEffect(MobEffects.DIG_SLOWDOWN);
            if (effect == null || effect.getAmplifier() <= 2) {
                Component message = Component.translatable("system.the_bumblezone.no_essence").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.RED);
                serverPlayer.displayClientMessage(message, true);
                serverPlayer.addEffect(new MobEffectInstance(
                        MobEffects.DIG_SLOWDOWN,
                        1800,
                        3,
                        false,
                        false,
                        true));
            }
        }
    }
}
