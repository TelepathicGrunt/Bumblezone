package com.telepathicgrunt.the_bumblezone.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import com.telepathicgrunt.the_bumblezone.packets.BeehemothControlsPacket;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.event.TickEvent;
import org.apache.logging.log4j.Level;
import org.lwjgl.glfw.GLFW;

public class BeehemothControls {
    public static final KeyMapping KEY_BIND_BEEHEMOTH_DOWN = new KeyMapping(
    "key." + Bumblezone.MODID + ".beehemoth_down",
            BeehemothKeyContext.BEEHEMOTH_KEY_CONTEXT,
            InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_SPACE),
            "key.categories." + Bumblezone.MODID
    );

    public static final KeyMapping KEY_BIND_BEEHEMOTH_UP = new KeyMapping(
    "key." + Bumblezone.MODID + ".beehemoth_up",
            BeehemothKeyContext.BEEHEMOTH_KEY_CONTEXT,
            InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_SPACE),
            "key.categories." + Bumblezone.MODID
    );

    public static void keyInput(InputEvent.Key event) {
        if (Minecraft.getInstance().player != null &&
            Minecraft.getInstance().player.getVehicle() instanceof BeehemothEntity beehemothEntity)
        {
            boolean upKeyAction = KEY_BIND_BEEHEMOTH_UP.matches(event.getKey(), event.getScanCode());
            boolean downKeyAction = KEY_BIND_BEEHEMOTH_DOWN.matches(event.getKey(), event.getScanCode());
            int keyAction = event.getAction();

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

    private enum BeehemothKeyContext implements IKeyConflictContext {
        BEEHEMOTH_KEY_CONTEXT {
            @Override
            public boolean isActive() {
                return Minecraft.getInstance().player != null &&
                        Minecraft.getInstance().player.getVehicle() instanceof BeehemothEntity;
            }

            @Override
            public boolean conflicts(IKeyConflictContext other) {
                return this == other;
            }
        }
    }
}
