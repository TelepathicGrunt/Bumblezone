package com.telepathicgrunt.the_bumblezone.client.rendering;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.util.ARGB;
import net.minecraft.world.effect.MobEffectInstance;

public class HiddenEffectIconRenderer implements MobEffectRenderer {

    @Override
    public boolean renderGuiIcon(MobEffectInstance instance, Gui gui, GuiGraphicsExtractor guiGraphics, int x, int y, float z, float alpha) {
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, Gui.getMobEffectSprite(instance.getEffect()), x + 3, y + 3, 18, 18, ARGB.white(instance.getAmplifier() == 0 ? 0.5f : 1f));
        return true;
    }
}
