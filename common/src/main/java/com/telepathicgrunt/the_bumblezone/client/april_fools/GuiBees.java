package com.telepathicgrunt.the_bumblezone.client.april_fools;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzClientConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.lwjgl.glfw.GLFW;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class GuiBees {

    public static final List<BeeSpriteState> beeSpriteStates = new ArrayList<>();
    public static float timePassedWhileGuiIsOpened = 0;
    public static long initialTimeStart = 0;
    public static long lastTime = -2;
    public static boolean showGuiBeesToday = false;
    public static boolean dateCheckCached = false;

    public static void initDateCheck() {
        // Allow bypass of cache if all days is turned on and we had cached false.
        if (dateCheckCached && !(!showGuiBeesToday && BzClientConfigs.showBeesOnGuiAllYearRound)) {
            return;
        }

        if (BzClientConfigs.showBeesOnGuiAllYearRound) {
            showGuiBeesToday = true;
        }
        else if (BzClientConfigs.showBeesOnGuiOnAprilFools) {
            LocalDate dateNow = LocalDate.now();
            LocalDate aprilFirst = LocalDate.of(dateNow.getYear(), Month.APRIL, 1);
            if (aprilFirst.isEqual(dateNow)) {
                showGuiBeesToday = true;
            }
        }

        if (!dateCheckCached) {
            initialTimeStart = System.currentTimeMillis();
        }

        dateCheckCached = true;
    }

    public static void guiClosed(Screen screen) {
        if (timePassedWhileGuiIsOpened > 0 && screen == null && Minecraft.getInstance().player != null) {
            timePassedWhileGuiIsOpened = 0;
            beeSpriteStates.clear();
        }
    }

    public static void renderBees(Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY, float deltaPastLastTick) {
        if (screen == null || Minecraft.getInstance().player == null) {
            return;
        }

        long currentTime = System.currentTimeMillis() - initialTimeStart;
        if (lastTime == -2) {
            lastTime = currentTime;
        }
        float realTimeDeltaPartialTick = (currentTime - lastTime) / 45f;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, 0.0F, 9000.0F);
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

            guiGraphics.blit(
                    BeeSpriteState.GetBeeSprite(beeSpriteState),
                    (int) (beeSpriteState.xCord - 8),
                    (int) (beeSpriteState.yCord - 8),
                    0,
                    0,
                    16,
                    16,
                    16 * (beeSpriteState.xVelocity > 0 ? 1 : -1),
                    16);

            float cosOffset = Mth.cos((timePassedWhileGuiIsOpened - beeSpriteState.creationTimestamp) / 20f);
            beeSpriteState.yVelocity += (cosOffset) * 0.01f;

            float xDiffToMouse = beeSpriteState.xCord - mouseX;
            float yDiffToMouse = beeSpriteState.yCord - mouseY;
            float exactDistanceToMouse = (float) Math.sqrt(xDiffToMouse * xDiffToMouse + yDiffToMouse * yDiffToMouse);

            if (GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), 0) == 1 && exactDistanceToMouse < 10) {
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
        guiGraphics.pose().popPose();

        if (timePassedWhileGuiIsOpened > 500 && beeSpriteStates.size() < 100 && beeSpriteStates.size() < (int)((timePassedWhileGuiIsOpened - 500) / 85)) {
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
        lastTime = currentTime;
    }


    public static class BeeSpriteState {
        public float xCord = Integer.MIN_VALUE;

        public float yCord = Integer.MIN_VALUE;

        public float xVelocity = 0;

        public float yVelocity = 0;

        public float spriteAnimationOffset = 0;

        public float creationTimestamp = 0;

        public boolean angry = false;

        private static final ResourceLocation BEE_SPRITE_WINGS_DOWN = new ResourceLocation(Bumblezone.MODID, "textures/gui/april_fools/bee_icon_wings_down.png");
        private static final ResourceLocation BEE_SPRITE_WINGS_UP = new ResourceLocation(Bumblezone.MODID, "textures/gui/april_fools/bee_icon_wings_up.png");
        private static final ResourceLocation ANGRY_BEE_SPRITE_WINGS_DOWN = new ResourceLocation(Bumblezone.MODID, "textures/gui/april_fools/angry_bee_icon_wings_down.png");
        private static final ResourceLocation ANGRY_BEE_SPRITE_WINGS_UP = new ResourceLocation(Bumblezone.MODID, "textures/gui/april_fools/angry_bee_icon_wings_up.png");

        public static ResourceLocation GetBeeSprite(BeeSpriteState beeSpriteState) {
            if (beeSpriteState.angry) {
                return ((timePassedWhileGuiIsOpened - beeSpriteState.spriteAnimationOffset) % 2) > 1f ? ANGRY_BEE_SPRITE_WINGS_UP : ANGRY_BEE_SPRITE_WINGS_DOWN;
            }

            return ((timePassedWhileGuiIsOpened - beeSpriteState.spriteAnimationOffset) % 3) > 1.5f ? BEE_SPRITE_WINGS_UP : BEE_SPRITE_WINGS_DOWN;
        }
    }
}
