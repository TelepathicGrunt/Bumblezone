package com.telepathicgrunt.the_bumblezone.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import com.telepathicgrunt.the_bumblezone.events.client.KeyInputEvent;
import com.telepathicgrunt.the_bumblezone.packets.BeehemothControlsPacket;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.NotImplementedException;
import org.lwjgl.glfw.GLFW;

public class BeehemothControls {
    public static final KeyMapping KEY_BIND_BEEHEMOTH_DOWN = createKey(
    "key." + Bumblezone.MODID + ".beehemoth_down",
            BeehemothKeyContext.BEEHEMOTH_KEY_CONTEXT,
            InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_SPACE),
            "key.categories." + Bumblezone.MODID
    );

    public static final KeyMapping KEY_BIND_BEEHEMOTH_UP = createKey(
    "key." + Bumblezone.MODID + ".beehemoth_up",
            BeehemothKeyContext.BEEHEMOTH_KEY_CONTEXT,
            InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_SPACE),
            "key.categories." + Bumblezone.MODID
    );

    public static void keyInput(KeyInputEvent event) {
        if (Minecraft.getInstance().player != null &&
            Minecraft.getInstance().player.getVehicle() instanceof BeehemothEntity beehemothEntity)
        {
            boolean upKeyAction = KEY_BIND_BEEHEMOTH_UP.matches(event.key(), event.scancode());
            boolean downKeyAction = KEY_BIND_BEEHEMOTH_DOWN.matches(event.key(), event.scancode());
            int keyAction = event.action();

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

    @ExpectPlatform
    public static KeyMapping createKey(String display, KeyConflict conflict, InputConstants.Key key, String category) {
        throw new NotImplementedException();
    }

    private enum BeehemothKeyContext implements KeyConflict {
        BEEHEMOTH_KEY_CONTEXT {
            @Override
            public boolean isActive() {
                return Minecraft.getInstance().player != null &&
                        Minecraft.getInstance().player.getVehicle() instanceof BeehemothEntity;
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
