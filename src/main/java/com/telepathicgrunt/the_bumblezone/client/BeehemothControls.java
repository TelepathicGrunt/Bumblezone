package com.telepathicgrunt.the_bumblezone.client;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import com.telepathicgrunt.the_bumblezone.packets.BeehemothControlsPacket;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import org.lwjgl.glfw.GLFW;

public class BeehemothControls {
    public static final KeyMapping KEY_BIND_BEEHEMOTH_DOWN = new KeyMapping(
    "key." + Bumblezone.MODID + ".beehemoth_down", GLFW.GLFW_KEY_CAPS_LOCK, "key.categories." + Bumblezone.MODID
    );

    public static final KeyMapping KEY_BIND_BEEHEMOTH_UP = new KeyMapping(
    "key." + Bumblezone.MODID + ".beehemoth_up", GLFW.GLFW_KEY_SPACE, "key.categories." + Bumblezone.MODID
    );

    public static void keyInput(InputEvent.KeyInputEvent event) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.getVehicle() instanceof BeehemothEntity) {
            BeehemothControlsPacket.sendToServer(
                event.getKey() == KEY_BIND_BEEHEMOTH_UP.getKey().getValue() ? event.getAction() : 2,
                event.getKey() == KEY_BIND_BEEHEMOTH_DOWN.getKey().getValue() ? event.getAction() : 2
            );
        }
    }
}
