package com.telepathicgrunt.the_bumblezone.world.dimension;

import com.mojang.blaze3d.shaders.FogShape;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzDimensionConfigs;
import com.telepathicgrunt.the_bumblezone.effects.WrathOfTheHiveEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.ViewportEvent;

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
        return BzDimensionConfigs.enableDimensionFog.get();
    }


    public static float REDDISH_FOG_TINT = 0;

    /**
     * Returns fog color based on if player has wrath effect or not
     */
    public Vec3 getFogColor() {
        float colorFactor = 0.75f;
        /*
         * The sky will be turned to midnight when brightness is below 50. This lets us get the
         * full range of brightness by utilizing the default brightness that the current celestial time gives.
         */
        if (BzDimensionConfigs.fogBrightnessPercentage.get() <= 50) {
            colorFactor *= (BzDimensionConfigs.fogBrightnessPercentage.get() / 50);
        } else {
            colorFactor *= (BzDimensionConfigs.fogBrightnessPercentage.get() / 100);
        }

        if (WrathOfTheHiveEffect.ACTIVE_WRATH && REDDISH_FOG_TINT < 0.38f) {
            REDDISH_FOG_TINT += 0.00001f;
        } else if (REDDISH_FOG_TINT > 0) {
            REDDISH_FOG_TINT -= 0.00001f;
        }

        return new Vec3((int)(Math.min(Math.min(0.54f * colorFactor, 0.65f + REDDISH_FOG_TINT)*255, 255)),
                        ((int)(Math.min(Math.max(Math.min(0.3f * colorFactor, 0.87f) - REDDISH_FOG_TINT * 0.6f, 0)*255, 255))),
                        ((int)(Math.min(Math.max(Math.min((0.001f * colorFactor) * (colorFactor * colorFactor), 0.9f) - REDDISH_FOG_TINT * 1.9f, 0)*255, 255))));
    }

    public static void fogThicknessAdjustments(ViewportEvent.RenderFog event) {
        if (event.getMode() == FogRenderer.FogMode.FOG_TERRAIN && event.getType() == FogType.NONE) {
                Player player = Minecraft.getInstance().player;
                if (player != null &&
                    DimensionSpecialEffects.forType(player.getLevel().dimensionType()).isFoggyAt(player.getBlockX(), player.getBlockZ()) &&
                    player.getLevel().dimension().location().equals(Bumblezone.MOD_DIMENSION_ID))
                {
                    float fogEnd = Minecraft.getInstance().gameRenderer.getRenderDistance();
                    float distanceRationAdjuster = 1;
                    if (event.getFarPlaneDistance() > 352) {
                        distanceRationAdjuster = Math.min(fogEnd / 352, 1.25F);
                    }
                    else if (event.getFarPlaneDistance() < 126) {
                        distanceRationAdjuster = Math.max(fogEnd / 126, 0.75F);
                    }
                    double modifier = ((BzDimensionConfigs.fogThickness.get() * distanceRationAdjuster * 0.3f) + 0.00001D);
                    float fogStart = (float) (fogEnd / modifier);
                    event.setNearPlaneDistance(fogStart/10);
                    event.setFarPlaneDistance(fogEnd);
                    event.setFogShape(FogShape.CYLINDER);
                    event.setCanceled(true);
                }
        }
    }
}
