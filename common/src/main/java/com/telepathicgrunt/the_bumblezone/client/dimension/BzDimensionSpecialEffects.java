package com.telepathicgrunt.the_bumblezone.client.dimension;

import com.telepathicgrunt.the_bumblezone.configs.BzDimensionConfigs;
import com.telepathicgrunt.the_bumblezone.effects.WrathOfTheHiveEffect;
import com.telepathicgrunt.the_bumblezone.modinit.BzDimension;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FogType;

import java.util.function.Consumer;

public class BzDimensionSpecialEffects {

    public static Integer getBrightnessDependentFogColor(Level level, Integer color) {
        if (level.dimension() == BzDimension.BZ_WORLD_KEY) {
            return BzDimensionSpecialEffects.getFogColor();
        }
        else {
            return color;
        }
    }

    public static float REDDISH_FOG_TINT = 0;

    /**
     * Returns fog color based on if player has wrath effect or not
     */
    public static int getFogColor() {
        float colorFactor = 0.75f;
        /*
         * The sky will be turned to midnight when brightness is below 50. This lets us get the
         * full range of brightness by utilizing the default brightness that the current celestial time gives.
         */
        if (BzDimensionConfigs.fogBrightnessPercentage <= 50) {
            colorFactor *= (BzDimensionConfigs.fogBrightnessPercentage / 50);
        }
        else {
            colorFactor *= (BzDimensionConfigs.fogBrightnessPercentage / 100);
        }

        if (WrathOfTheHiveEffect.ACTIVE_WRATH && REDDISH_FOG_TINT < 0.38f) {
            REDDISH_FOG_TINT += 0.00001f;
        }
        else if (REDDISH_FOG_TINT > 0) {
            REDDISH_FOG_TINT -= 0.00001f;
        }

        // Divide by 255 to make values between 0 and 1
        double divideBy255 = 0.003921568627451d;

        int red = (int) ((int)(Math.min(Math.min(0.54f * colorFactor, 0.65f + REDDISH_FOG_TINT)*255, 255)) * divideBy255);
        int green = (int) (((int)(Math.min(Math.max(Math.min(0.3f * colorFactor, 0.87f) - REDDISH_FOG_TINT * 0.6f, 0)*255, 255))) * divideBy255);
        int blue = (int) (((int)(Math.min(Math.max(Math.min((0.001f * colorFactor) * (colorFactor * colorFactor), 0.9f) - REDDISH_FOG_TINT * 1.9f, 0)*255, 255))) * divideBy255);

        return ARGB.color(red, green, blue);
    }

    public static void fogThicknessAdjustments(
            Player player,
            float renderDistanceInChunks,
            boolean thickFog,
            FogType fogType,
            Consumer<Float> setFogStart,
            Consumer<Float> setFogEnd)
    {
        if (fogType == FogType.ATMOSPHERIC &&
            thickFog &&
            player != null &&
            player.level().dimension().equals(BzDimension.BZ_WORLD_KEY))
        {
            for (Holder<MobEffect> mobEffectHolder : BuiltInRegistries.MOB_EFFECT.getTagOrEmpty(BzTags.FOG_ADJUSTING_EFFECTS)) {
                if (player.hasEffect(mobEffectHolder)) {
                    return;
                }
            }

            float distanceRationAdjuster = 1;
            if (renderDistanceInChunks > 22) {
                distanceRationAdjuster = Math.min(renderDistanceInChunks / 22, 1.5F);
            } else if (renderDistanceInChunks < 7) {
                distanceRationAdjuster = Math.max(renderDistanceInChunks / 7, 0.75F);
            }

            float renderDistanceInBlocks = (renderDistanceInChunks * 16) - 4;
            float renderDistanceFogSpan = Mth.clamp(renderDistanceInBlocks / 10.0F, 4.0F, 64.0F);

            float fogStart = (float) (renderDistanceInChunks / ((BzDimensionConfigs.fogThickness * distanceRationAdjuster * 0.425f) + 0.00001D)) * 16;
            setFogStart.accept(Math.min(renderDistanceInBlocks - renderDistanceFogSpan, fogStart));
            setFogEnd.accept(Math.max(renderDistanceInBlocks, fogStart));
        }
    }
}
