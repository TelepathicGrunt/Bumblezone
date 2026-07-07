package com.telepathicgrunt.the_bumblezone.client.neoforge;

import com.telepathicgrunt.the_bumblezone.client.dimension.BzDimensionSpecialEffects;
import com.telepathicgrunt.the_bumblezone.configs.BzDimensionConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.ViewportEvent;

public class DimensionFog {
    public static void fogThicknessAdjustments(ViewportEvent.RenderFog event) {
        Player player = Minecraft.getInstance().player;
        BzDimensionSpecialEffects.fogThicknessAdjustments(
            player,
            Minecraft.getInstance().gameRenderer.getGameRenderState().optionsRenderState.renderDistance,
            BzDimensionConfigs.enableDimensionFog,
            event.getType(),
            event::setNearPlaneDistance,
            event::setFarPlaneDistance);
    }
}
