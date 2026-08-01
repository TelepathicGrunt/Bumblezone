package com.telepathicgrunt.the_bumblezone.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import com.telepathicgrunt.the_bumblezone.events.client.BzKeyInputEvent;
import com.telepathicgrunt.the_bumblezone.packets.BeehemothControlsPacket;
import com.telepathicgrunt.the_bumblezone.services.ClientPlatformService;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class BeehemothControls {
    public static final KeyMapping.Category CATEGORY =
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath(Bumblezone.MODID, Bumblezone.MODID));

    public static final KeyMapping KEY_BIND_BEEHEMOTH_DOWN = ClientPlatformService.INSTANCE.createKey(
    "key." + Bumblezone.MODID + ".beehemoth_down",
            BeehemothKeyContext.BEEHEMOTH_KEY_CONTEXT,
            InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_CAPS_LOCK),
            CATEGORY
    );

    public static final KeyMapping KEY_BIND_BEEHEMOTH_UP = ClientPlatformService.INSTANCE.createKey(
    "key." + Bumblezone.MODID + ".beehemoth_up",
            BeehemothKeyContext.BEEHEMOTH_KEY_CONTEXT,
            InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_SPACE),
            CATEGORY
    );

    public static void keyInput(BzKeyInputEvent event) {
        if (GeneralUtilsClient.getClientPlayer() != null &&
            GeneralUtilsClient.getClientPlayer().getVehicle() instanceof BeehemothEntity beehemothEntity)
        {
            boolean upKeyAction = KEY_BIND_BEEHEMOTH_UP.matches(event.keyEvent());
            boolean downKeyAction = KEY_BIND_BEEHEMOTH_DOWN.matches(event.keyEvent());
            int keyAction = event.action();
            Bumblezone.LOGGER.error("" + event.action());

            if ((upKeyAction || downKeyAction) && keyAction != 2) {
                BeehemothControlsPacket.sendToServer(
                        upKeyAction ? keyAction : 2,
                        downKeyAction ? keyAction : 2
                );

                if (upKeyAction) {
                    beehemothEntity.movingStraightUp = keyAction == 1;
                }
                else {
                    beehemothEntity.movingStraightDown = keyAction == 1;
                }
            }
        }
    }

    private enum BeehemothKeyContext implements KeyConflict {
        BEEHEMOTH_KEY_CONTEXT {
            @Override
            public boolean isActive() {
                return GeneralUtilsClient.getClientPlayer() != null &&
                    GeneralUtilsClient.getClientPlayer().getVehicle() instanceof BeehemothEntity;
            }

            @Override
            public boolean conflicts(KeyConflict other) {
                return this == other;
            }
        }
    }

    public interface KeyConflict {

        default boolean isActive() {
            return true;
        }

        default boolean conflicts(KeyConflict other) {
            return false;
        }
    }
}
