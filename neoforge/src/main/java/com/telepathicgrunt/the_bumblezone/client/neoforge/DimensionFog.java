package com.telepathicgrunt.the_bumblezone.client.neoforge;

import com.telepathicgrunt.the_bumblezone.client.dimension.BzDimensionSpecialEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.ViewportEvent;

public class DimensionFog {
    public static void fogThicknessAdjustments(ViewportEvent.RenderFog event) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            if (BzDimensionSpecialEffects.fogThicknessAdjustments(
                    player,
                    Minecraft.getInstance().gameRenderer.getRenderDistance(),
                    DimensionSpecialEffects.forType(player.level().dimensionType()).isFoggyAt(player.getBlockX(), player.getBlockZ()),
                    event.getMode(),
                    event.getType(),
                    event::setNearPlaneDistance,
                    event::setFarPlaneDistance,
                    event::setFogShape))
            {
                event.setCanceled(true);
            }
        }
    }
}
