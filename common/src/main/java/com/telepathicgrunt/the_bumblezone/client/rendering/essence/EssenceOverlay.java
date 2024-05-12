package com.telepathicgrunt.the_bumblezone.client.rendering.essence;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzClientConfigs;
import com.telepathicgrunt.the_bumblezone.items.essence.AbilityEssenceItem;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;

public class EssenceOverlay {
    private static final ResourceLocation TEXTURE_OVERLAY_1 = new ResourceLocation(Bumblezone.MODID, "textures/misc/active_essence_overlay.png");

    public static void essenceItemOverlay(Player player, GuiGraphics guiGraphics) {
        if (BzClientConfigs.essenceItemHUDVisualEffectLayers == 0) {
            return;
        }

        ItemStack offhandItem = player.getOffhandItem();

        if (!(offhandItem.getItem() instanceof AbilityEssenceItem abilityEssenceItem) || player.getCooldowns().isOnCooldown(abilityEssenceItem)) {
            return;
        }

        if (!AbilityEssenceItem.getIsActive(offhandItem)) {
            return;
        }

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();

        float red = GeneralUtils.getRed(abilityEssenceItem.getColor()) / 256f;
        float green = GeneralUtils.getGreen(abilityEssenceItem.getColor()) / 256f;
        float blue = GeneralUtils.getBlue(abilityEssenceItem.getColor()) / 256f;

        int remainingUse = abilityEssenceItem.getAbilityUseRemaining(offhandItem);
        float percentageLeft = (float)remainingUse / abilityEssenceItem.getMaxAbilityUseAmount();
        float opacity = 0.1f + (percentageLeft * 0.2f);
        guiGraphics.setColor(red, green, blue, opacity);

        PoseStack poseStack = guiGraphics.pose();

        float rotationX = guiGraphics.guiWidth() / 2f;
        float rotationY = guiGraphics.guiHeight();

        for(int layer = 0; layer < BzClientConfigs.essenceItemHUDVisualEffectLayers; layer++) {
            poseStack.pushPose();

            int rotationDirection = layer % 2 == 1 ? -1 : 1;
            float squash = 0.12f - (layer * 0.04f);
            double spinSlowdown = 400 + (layer * 150);
            double currentMillisecond = System.currentTimeMillis() % (360 * spinSlowdown);
            double degrees = ((currentMillisecond / spinSlowdown) * BzClientConfigs.essenceItemHUDVisualEffectSpeed) * rotationDirection;
            float angle = (float) ((degrees + 45) * Mth.DEG_TO_RAD);

            Matrix4f rotationMatrix = new Matrix4f(
                    Mth.cos(angle), -Mth.sin(angle), 0, 0,
                    Mth.sin(angle), Mth.cos(angle), 0, 0,
                    0, 0, 1, 0,
                    0, 0, 0, 1
            );
            Matrix4f scalingMatrix = new Matrix4f(
                    1, 0, 0, 0,
                    0, squash, 0, 0,
                    0, 0, 1, 0,
                    0, 0, 0, 1
            );
            Matrix4f translationMatrix = new Matrix4f(
                    1, 0, 0, 0,
                    0, 1, 0, 0,
                    0, 0, 1, 0,
                    rotationX, rotationY, 0, 1
            );

            poseStack.mulPose(translationMatrix.mul(scalingMatrix).mul(rotationMatrix));

            guiGraphics.blit(TEXTURE_OVERLAY_1,
                    (int) -rotationX,
                    (int) -rotationX,
                    -90,
                    0.0F,
                    0.0F,
                    guiGraphics.guiWidth(),
                    guiGraphics.guiWidth(),
                    guiGraphics.guiWidth(),
                    guiGraphics.guiWidth());

            poseStack.popPose();
        }

        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
