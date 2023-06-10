package com.telepathicgrunt.the_bumblezone.client.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.IdentityHashMap;
import java.util.Map;

public interface MobEffectRenderer {

    Map<MobEffect, MobEffectRenderer> RENDERERS = new IdentityHashMap<>();

    default boolean renderGuiIcon(MobEffectInstance instance, Gui gui, GuiGraphics guiGraphics, int x, int y, float z, float alpha) {
        return false;
    }

}
