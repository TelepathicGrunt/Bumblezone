package com.telepathicgrunt.the_bumblezone.client.dimension;

import com.mojang.blaze3d.shaders.FogShape;
import com.telepathicgrunt.the_bumblezone.configs.BzDimensionConfigs;
import com.telepathicgrunt.the_bumblezone.effects.WrathOfTheHiveEffect;
import com.telepathicgrunt.the_bumblezone.mixin.util.Vec3Accessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzDimension;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

public class BzDimensionSpecialEffects extends DimensionSpecialEffects {
    public BzDimensionSpecialEffects() {
        super(-1000000f, true, SkyType.NONE, false, false);
    }

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 color, float sunHeight) {
        return getFogColor(color);
    }

    @Override
    public boolean isFoggyAt(int camX, int camY) {
        return BzDimensionConfigs.enableDimensionFog;
    }


    public static float REDDISH_FOG_TINT = 0;

    /**
     * Returns fog color based on if player has wrath effect or not
     */
    public Vec3 getFogColor(Vec3 colorHolder) {
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

        // Micro-optimized to not create a new vec3. Instead, modify the incoming one which is already initialized just
        // to pass the newly made Vec3 into BzDimensionSpecialEffects$getBrightnessDependentFogColor. Thus, it should be
        // safe to modify this as nothing else is going to ever use this Vec3 object between when it was made and when
        // it is passed to BzDimensionSpecialEffects$getBrightnessDependentFogColor.
        ((Vec3Accessor)colorHolder).bumblezone$setX((int)(Math.min(Math.min(0.54f * colorFactor, 0.65f + REDDISH_FOG_TINT)*255, 255)) * divideBy255);
        ((Vec3Accessor)colorHolder).bumblezone$setY(((int)(Math.min(Math.max(Math.min(0.3f * colorFactor, 0.87f) - REDDISH_FOG_TINT * 0.6f, 0)*255, 255))) * divideBy255);
        ((Vec3Accessor)colorHolder).bumblezone$setZ(((int)(Math.min(Math.max(Math.min((0.001f * colorFactor) * (colorFactor * colorFactor), 0.9f) - REDDISH_FOG_TINT * 1.9f, 0)*255, 255))) * divideBy255);
        return colorHolder;
    }

    public static boolean fogThicknessAdjustments(
            Player player,
            float renderDistance,
            boolean thickFog,
            FogRenderer.FogMode fogMode,
            FogType fogType,
            Consumer<Float> setFogStart,
            Consumer<Float> setFogEnd,
            Consumer<FogShape> setFogShape)
    {
        if (fogMode == FogRenderer.FogMode.FOG_TERRAIN &&
            fogType == FogType.NONE &&
            thickFog &&
            player != null &&
            player.level().dimension().equals(BzDimension.BZ_WORLD_KEY))
        {
            for (Holder<MobEffect> mobEffectHolder : BuiltInRegistries.MOB_EFFECT.getTagOrEmpty(BzTags.FOG_ADJUSTING_EFFECTS)) {
                if (player.hasEffect(mobEffectHolder)) {
                    return false;
                }
            }

            float distanceRationAdjuster = 1;
            if (renderDistance > 352) {
                distanceRationAdjuster = Math.min(renderDistance / 352, 1.25F);
            } else if (renderDistance < 126) {
                distanceRationAdjuster = Math.max(renderDistance / 126, 0.75F);
            }
            float fogStart = (float) (renderDistance / ((BzDimensionConfigs.fogThickness * distanceRationAdjuster * 0.3f) + 0.00001D));
            setFogStart.accept(Math.min(renderDistance, fogStart));
            setFogEnd.accept(Math.max(renderDistance, fogStart));
            setFogShape.accept(FogShape.CYLINDER);
            return true;
        }

        return false;
    }
}
