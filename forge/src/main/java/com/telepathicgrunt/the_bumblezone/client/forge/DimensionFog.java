package com.telepathicgrunt.the_bumblezone.client.forge;

import com.mojang.blaze3d.shaders.FogShape;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzDimensionConfigs;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FogType;
import net.minecraftforge.client.event.ViewportEvent;

public class DimensionFog {
    public static void fogThicknessAdjustments(ViewportEvent.RenderFog event) {
        if (event.getMode() == FogRenderer.FogMode.FOG_TERRAIN && event.getType() == FogType.NONE) {
            Player player = Minecraft.getInstance().player;
            if (player != null &&
                DimensionSpecialEffects.forType(player.level().dimensionType()).isFoggyAt(player.getBlockX(), player.getBlockZ()) &&
                player.level().dimension().location().equals(Bumblezone.MOD_DIMENSION_ID))
            {
                for (Holder<MobEffect> mobEffectHolder : BuiltInRegistries.MOB_EFFECT.getTagOrEmpty(BzTags.FOG_ADJUSTING_EFFECTS)) {
                    if (player.hasEffect(mobEffectHolder.value())) {
                        return;
                    }
                }

                float renderDistance = Minecraft.getInstance().gameRenderer.getRenderDistance();
                float distanceRationAdjuster = 1;
                if (event.getFarPlaneDistance() > 352) {
                    distanceRationAdjuster = Math.min(renderDistance / 352, 1.25F);
                }
                else if (event.getFarPlaneDistance() < 126) {
                    distanceRationAdjuster = Math.max(renderDistance / 126, 0.75F);
                }
                double modifier = ((BzDimensionConfigs.fogThickness * distanceRationAdjuster * 0.3f) + 0.00001D);
                float fogStart = (float) (renderDistance / modifier);
                event.setNearPlaneDistance(Math.min(renderDistance, fogStart * 0.3f));
                event.setFarPlaneDistance(Math.max(renderDistance, fogStart * 0.3f));
                event.setFogShape(FogShape.CYLINDER);
                event.setCanceled(true);
            }
        }
    }
}
