package com.telepathicgrunt.the_bumblezone.world.dimension;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.effects.WrathOfTheHiveEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public class BzSkyProperty extends DimensionSpecialEffects {
    public BzSkyProperty() {
        super(1000, true, SkyType.NONE, false, false);
    }

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 color, float sunHeight) {
        return getFogColor().scale(0.003921568627451); // Divide by 255 to make values between 0 and 1
    }

    @Override
    public boolean isFoggyAt(int camX, int camY) {
        return true;
    }


    @Environment(EnvType.CLIENT)
    public static float REDDISH_FOG_TINT = 0;

    /**
     * Returns fog color
     * <p>
     * What I done is made it be based on the day/night cycle so the fog will darken at night but brighten during day.
     * calculateVanillaSkyPositioning returns a value which is between 0 and 1 for day/night and fogChangeSpeed is the range
     * that the fog color will cycle between.
     */
    public Vec3 getFogColor() {
        float colorFactor = 1;
        /*
         * The sky will be turned to midnight when brightness is below 50. This lets us get the
         * full range of brightness by utilizing the default brightness that the current celestial time gives.
         */
        if (Bumblezone.BZ_CONFIG.BZDimensionConfig.fogBrightnessPercentage <= 50) {
            colorFactor *= (Bumblezone.BZ_CONFIG.BZDimensionConfig.fogBrightnessPercentage / 50);
        } else {
            colorFactor *= (Bumblezone.BZ_CONFIG.BZDimensionConfig.fogBrightnessPercentage / 100);
        }

        if (WrathOfTheHiveEffect.ACTIVE_WRATH && REDDISH_FOG_TINT < 0.38f) {
            REDDISH_FOG_TINT += 0.00001f;
        } else if (REDDISH_FOG_TINT > 0) {
            REDDISH_FOG_TINT -= 0.00001f;
        }

        return new Vec3((int)(Math.min(Math.min(0.56f * colorFactor, 0.65f + REDDISH_FOG_TINT)*255, 255)),
                        ((int)(Math.min(Math.max(Math.min(0.34f * colorFactor, 0.87f) - REDDISH_FOG_TINT * 0.6f, 0)*255, 255))),
                        ((int)(Math.min(Math.max(Math.min((0.001f * colorFactor) * (colorFactor * colorFactor), 0.9f) - REDDISH_FOG_TINT * 1.9f, 0)*255, 255))));
    }
}
