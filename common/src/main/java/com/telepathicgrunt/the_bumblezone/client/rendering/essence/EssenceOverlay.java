package com.telepathicgrunt.the_bumblezone.client.rendering.essence;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzClientConfigs;
import com.telepathicgrunt.the_bumblezone.items.essence.AbilityEssenceItem;
import com.telepathicgrunt.the_bumblezone.modinit.BzDataComponents;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fStack;

public class EssenceOverlay {
    private static final Identifier TEXTURE_OVERLAY_1 = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/misc/active_essence_overlay.png");

    public static void essenceItemOverlay(Player player, GuiGraphicsExtractor guiGraphics) {
        if (BzClientConfigs.essenceItemHUDVisualEffectLayers == 0) {
            return;
        }

        ItemStack offhandItem = player.getOffhandItem();

        if (!(offhandItem.getItem() instanceof AbilityEssenceItem abilityEssenceItem) || player.getCooldowns().isOnCooldown(offhandItem)) {
            return;
        }

        if (!offhandItem.get(BzDataComponents.ABILITY_ESSENCE_ACTIVITY_DATA.get()).isActive()) {
            return;
        }

        float red = GeneralUtils.getRed(abilityEssenceItem.getColor()) / 256f;
        float green = GeneralUtils.getGreen(abilityEssenceItem.getColor()) / 256f;
        float blue = GeneralUtils.getBlue(abilityEssenceItem.getColor()) / 256f;

        int remainingUse = abilityEssenceItem.getAbilityUseRemaining(offhandItem);
        float percentageLeft = (float)remainingUse / abilityEssenceItem.getMaxAbilityUseAmount();
        float opacity = 0.1f + (percentageLeft * 0.2f);
        int color = ARGB.colorFromFloat(opacity, red, green, blue);

        Matrix3x2fStack poseStack = guiGraphics.pose();

        float rotationX = guiGraphics.guiWidth() / 2f;
        float rotationY = guiGraphics.guiHeight();

        for(int layer = 0; layer < BzClientConfigs.essenceItemHUDVisualEffectLayers; layer++) {
            poseStack.pushMatrix();

            int rotationDirection = layer % 2 == 1 ? -1 : 1;
            float squash = 0.12f - (layer * 0.04f);
            double spinSlowdown = 400 + (layer * 150);
            double currentMillisecond = System.currentTimeMillis() % (360 * spinSlowdown);
            double degrees = ((currentMillisecond / spinSlowdown) * BzClientConfigs.essenceItemHUDVisualEffectSpeed) * rotationDirection;
            float angle = (float) ((degrees + 45) * Mth.DEG_TO_RAD);

            Matrix3x2f rotationMatrix = new Matrix3x2f(
                    Mth.cos(angle), -Mth.sin(angle),
                    Mth.sin(angle), Mth.cos(angle),
                    0, 0
            );
            Matrix3x2f scalingMatrix = new Matrix3x2f(
                    1, 0,
                    0, squash,
                    0, 0
            );
            Matrix3x2f translationMatrix = new Matrix3x2f(
                    1, 0,
                    0, 1,
                    rotationX, rotationY
            );

            poseStack.mul(translationMatrix.mul(scalingMatrix).mul(rotationMatrix));


            guiGraphics.blit(
                    RenderPipelines.GUI_TEXTURED,
                    TEXTURE_OVERLAY_1,
                    (int) -rotationX,
                    (int) -rotationX,
                    0,
                    0,
                    guiGraphics.guiWidth(),
                    guiGraphics.guiWidth(),
                    guiGraphics.guiWidth(),
                    guiGraphics.guiWidth(),
                    color);

            poseStack.popMatrix();
        }
    }
}
