package com.telepathicgrunt.the_bumblezone.client.screens;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import com.telepathicgrunt.the_bumblezone.configs.BzClientConfigs;
import com.telepathicgrunt.the_bumblezone.modinit.BzDimension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.lwjgl.glfw.GLFW;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class GuiBees {
    private static final List<BeeSpriteState> beeSpriteStates = new ArrayList<>();
    private static float timePassedWhileGuiIsOpened = 0;
    private static boolean showGuiBeesToday = false;
    private static boolean dateCheckCached = false;

    public static void initDateCheck() {
        // Allow bypass of cache if all days is turned on and we had cached false.
        if (!showGuiBeesToday && BzClientConfigs.showBeesOnGuiAllYearRound) {
            showGuiBeesToday = true;
            return;
        }

        if (dateCheckCached) {
            return;
        }

        if (BzClientConfigs.showBeesOnGuiOnAprilFools) {
            LocalDate dateNow = LocalDate.now();
            LocalDate aprilFirst = LocalDate.of(dateNow.getYear(), Month.APRIL, 1);
            if (aprilFirst.isEqual(dateNow)) {
                showGuiBeesToday = true;
            }
        }

        dateCheckCached = true;
    }

    public static boolean isGuiBeeAllowedByConfig() {
        return GuiBees.showGuiBeesToday && (BzClientConfigs.showBeesOnGuiOnAprilFools || BzClientConfigs.showBeesOnGuiAllYearRound);
    }

    public static boolean isPlayerInDimensionTeleportScreen(Screen screen) {
        return screen instanceof LevelLoadingScreen && isPlayerInDimension();
    }

    public static boolean isPlayerInDimension() {
        return GeneralUtilsClient.getClientPlayer() != null &&
            GeneralUtilsClient.getClientPlayer().level().dimension() == BzDimension.BZ_WORLD_KEY;
    }

    public static void guiClosed(Screen screen) {
        if (timePassedWhileGuiIsOpened > 0 && screen == null && GeneralUtilsClient.getClientPlayer() != null) {
            timePassedWhileGuiIsOpened = 0;
            beeSpriteStates.clear();
        }
    }

    public static void renderBees(Screen screen, boolean onBzDimTeleportScreen, GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float realTimeDeltaPartialTick) {
        if (screen == null || GeneralUtilsClient.getClientPlayer() == null) {
            return;
        }

        if ((!onBzDimTeleportScreen && !isGuiBeeAllowedByConfig()) || (BzClientConfigs.restrictBeesOnGuiToBzDimension && !isPlayerInDimension()))
        {
            return;
        }

        guiGraphics.pose().pushMatrix();
        guiGraphics.nextStratum();
        for (int i = beeSpriteStates.size() - 1; i >= 0; i--) {
            BeeSpriteState beeSpriteState = beeSpriteStates.get(i);
            beeSpriteState.xCord += (beeSpriteState.xVelocity * realTimeDeltaPartialTick);
            beeSpriteState.yCord += (beeSpriteState.yVelocity * realTimeDeltaPartialTick);

            if ((beeSpriteState.xCord < -16 && beeSpriteState.xVelocity < 0) ||
                (beeSpriteState.xCord > guiGraphics.guiWidth() + 16 && beeSpriteState.xVelocity > 0) ||
                (beeSpriteState.yCord < -16 && beeSpriteState.yVelocity < 0) ||
                (beeSpriteState.yCord > guiGraphics.guiHeight() + 16 && beeSpriteState.yVelocity > 0))
            {
                beeSpriteStates.remove(i);
                continue;
            }

            guiGraphics.blitSprite(
                    RenderPipelines.GUI_TEXTURED,
                    BeeSpriteState.GetBeeSprite(beeSpriteState),
                    (int) (beeSpriteState.xCord - 8),
                    (int) (beeSpriteState.yCord - 8),
                    16,
                    16);

            float cosOffset = Mth.cos((timePassedWhileGuiIsOpened - beeSpriteState.creationTimestamp) / 20f);
            beeSpriteState.yVelocity += (cosOffset) * 0.01f;

            float xDiffToMouse = beeSpriteState.xCord - mouseX;
            float yDiffToMouse = beeSpriteState.yCord - mouseY;
            float exactDistanceToMouse = (float) Math.sqrt(xDiffToMouse * xDiffToMouse + yDiffToMouse * yDiffToMouse);

            if (GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().handle(), 0) == 1 && exactDistanceToMouse < 10) {
                beeSpriteState.angry = true;
            }

            if (beeSpriteState.angry) {
                float mouseDisThreshold = 500f;
                float pushStrength = (float) Math.pow(Math.max(mouseDisThreshold - exactDistanceToMouse, 0), 1.4f);
                float xAccel = pushStrength * (-xDiffToMouse / mouseDisThreshold) / 2000f;
                float yAccel = pushStrength * (-yDiffToMouse / mouseDisThreshold) / 2000f;
                beeSpriteState.xVelocity += Math.signum(xAccel) != Math.signum(beeSpriteState.xVelocity) ? xAccel * 2f : xAccel;
                beeSpriteState.yVelocity += Math.signum(yAccel) != Math.signum(beeSpriteState.yVelocity) ? yAccel * 2f : yAccel;
            }
            else {
                float mouseDisThreshold = 50f;
                float pushStrength = (float) Math.pow(Math.max(mouseDisThreshold - exactDistanceToMouse, 0), 1.4f);
                beeSpriteState.xVelocity += pushStrength * (xDiffToMouse / mouseDisThreshold) / 500f;
                beeSpriteState.yVelocity += pushStrength * (yDiffToMouse / mouseDisThreshold) / 500f;
            }
        }

        int currentBeeAmount = beeSpriteStates.size();

        boolean normalCapCheck = timePassedWhileGuiIsOpened > 500 &&
                currentBeeAmount < 100 &&
                currentBeeAmount < (int)((timePassedWhileGuiIsOpened - 500) / 85);
        boolean crazyHighCapCheck = BzClientConfigs.maximumBeesOnGui &&
                timePassedWhileGuiIsOpened > 200 &&
                currentBeeAmount < 10000;
        boolean teleportScreenCapCheck = onBzDimTeleportScreen &&
                currentBeeAmount < 25 &&
                currentBeeAmount < (int)(timePassedWhileGuiIsOpened / 65) + 2;

        if (normalCapCheck || crazyHighCapCheck || teleportScreenCapCheck) {
            BeeSpriteState beeSpriteState = new BeeSpriteState();
            beeSpriteState.xCord = Math.random() > 0.5f ? -16 : guiGraphics.guiWidth() + 16;
            beeSpriteState.yCord = (float) (guiGraphics.guiHeight() * Math.random() * 0.8f) + (guiGraphics.guiHeight() * 0.05f);
            beeSpriteState.xVelocity = beeSpriteState.xCord < 0 ? 0.4f : -0.4f;
            beeSpriteState.yVelocity = 0;
            beeSpriteState.spriteAnimationOffset = (float) Math.random();
            beeSpriteState.creationTimestamp = timePassedWhileGuiIsOpened;
            beeSpriteStates.add(beeSpriteState);
        }

        timePassedWhileGuiIsOpened += realTimeDeltaPartialTick;
        guiGraphics.pose().popMatrix();
    }


    private static class BeeSpriteState {
        public float xCord = Integer.MIN_VALUE;

        public float yCord = Integer.MIN_VALUE;

        public float xVelocity = 0;

        public float yVelocity = 0;

        public float spriteAnimationOffset = 0;

        public float creationTimestamp = 0;

        public boolean angry = false;

        private static final Identifier BEE_SPRITE_WINGS_DOWN = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "april_fools/bee_icon_wings_down");
        private static final Identifier BEE_SPRITE_WINGS_DOWN_REVERSED = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "april_fools/bee_icon_wings_down_reversed");
        private static final Identifier BEE_SPRITE_WINGS_UP = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "april_fools/bee_icon_wings_up");
        private static final Identifier BEE_SPRITE_WINGS_UP_REVERSED = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "april_fools/bee_icon_wings_up_reversed");
        private static final Identifier ANGRY_BEE_SPRITE_WINGS_DOWN = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "april_fools/angry_bee_icon_wings_down");
        private static final Identifier ANGRY_BEE_SPRITE_WINGS_DOWN_REVERSED = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "april_fools/angry_bee_icon_wings_down_reversed");
        private static final Identifier ANGRY_BEE_SPRITE_WINGS_UP = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "april_fools/angry_bee_icon_wings_up");
        private static final Identifier ANGRY_BEE_SPRITE_WINGS_UP_REVERSED = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "april_fools/angry_bee_icon_wings_up_reversed");

        private static Identifier GetBeeSprite(BeeSpriteState beeSpriteState) {
            if (beeSpriteState.angry) {
                if (beeSpriteState.xVelocity > 0) {
                    return ((timePassedWhileGuiIsOpened - beeSpriteState.spriteAnimationOffset) % 2) > 1f ? ANGRY_BEE_SPRITE_WINGS_UP : ANGRY_BEE_SPRITE_WINGS_DOWN;
                }
                else {
                    return ((timePassedWhileGuiIsOpened - beeSpriteState.spriteAnimationOffset) % 2) > 1f ? ANGRY_BEE_SPRITE_WINGS_UP_REVERSED : ANGRY_BEE_SPRITE_WINGS_DOWN_REVERSED;
                }
            }

            if (beeSpriteState.xVelocity > 0) {
                return ((timePassedWhileGuiIsOpened - beeSpriteState.spriteAnimationOffset) % 3) > 1.5f ? BEE_SPRITE_WINGS_UP : BEE_SPRITE_WINGS_DOWN;
            }
            else {
                return ((timePassedWhileGuiIsOpened - beeSpriteState.spriteAnimationOffset) % 3) > 1.5f ? BEE_SPRITE_WINGS_UP_REVERSED : BEE_SPRITE_WINGS_DOWN_REVERSED;
            }
        }
    }
}
