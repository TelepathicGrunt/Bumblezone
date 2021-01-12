package com.telepathicgrunt.the_bumblezone.dimension;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.effects.WrathOfTheHiveEffect;
import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BzSkyProperty extends DimensionRenderInfo {
    public BzSkyProperty() {
        super(1000, true, DimensionRenderInfo.FogType.NONE, false, false);
    }

    @Override
    public Vector3d func_230494_a_(Vector3d color, float sunHeight) {
        return getFogColor().scale(0.003921568627451); // Divide by 255 to amke values between 0 and 1
    }

    @Override
    public boolean func_230493_a_(int camX, int camY) {
        return true;
    }


    public static float REDDISH_FOG_TINT = 0;

    /**
     * Returns fog color
     * <p>
     * What I done is made it be based on the day/night cycle so the fog will darken at night but brighten during day.
     * calculateVanillaSkyPositioning returns a value which is between 0 and 1 for day/night and fogChangeSpeed is the range
     * that the fog color will cycle between.
     */
    public Vector3d getFogColor() {
        float colorFactor = 1;
        /*
         * The sky will be turned to midnight when brightness is below 50. This lets us get the
         * full range of brightness by utilizing the default brightness that the current celestial time gives.
         */
        if (Bumblezone.BzDimensionConfig.fogBrightnessPercentage.get() <= 50) {
            colorFactor *= (Bumblezone.BzDimensionConfig.fogBrightnessPercentage.get() / 50);
        }
        else {
            colorFactor *= (Bumblezone.BzDimensionConfig.fogBrightnessPercentage.get() / 100);
        }

        if (WrathOfTheHiveEffect.ACTIVE_WRATH && REDDISH_FOG_TINT < 0.38f) {
            REDDISH_FOG_TINT += 0.00001f;
        }
        else if (REDDISH_FOG_TINT > 0) {
            REDDISH_FOG_TINT -= 0.00001f;
        }

        return new Vector3d((int) (Math.min(Math.min(0.56f * colorFactor, 0.65f + REDDISH_FOG_TINT) * 255, 255)),
                ((int) (Math.min(Math.max(Math.min(0.34f * colorFactor, 0.87f) - REDDISH_FOG_TINT * 0.6f, 0) * 255, 255))),
                ((int) (Math.min(Math.max(Math.min((0.001f * colorFactor) * (colorFactor * colorFactor), 0.9f) - REDDISH_FOG_TINT * 1.9f, 0) * 255, 255))));
    }
}
