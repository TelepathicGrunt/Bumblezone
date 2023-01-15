package com.telepathicgrunt.the_bumblezone.client.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.effect.MobEffectInstance;

import static net.minecraft.client.gui.GuiComponent.blit;

public class HiddenEffectIconRenderer implements MobEffectRenderer {

    @Override
    public boolean renderGuiIcon(MobEffectInstance instance, Gui gui, PoseStack poseStack, int x, int y, float z, float alpha) {
        TextureAtlasSprite textureatlassprite = Minecraft.getInstance().getMobEffectTextures().get(instance.getEffect());
        RenderSystem.setShaderTexture(0, textureatlassprite.atlasLocation());
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, instance.getAmplifier() == 0 ? 0.5f : 1f);
        blit(poseStack, x + 3, y + 3, (int) z, 18, 18, textureatlassprite);
        return true;
    }
}
