package com.telepathicgrunt.the_bumblezone.client;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import com.telepathicgrunt.the_bumblezone.packets.BeehemothControlsPacket;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import org.lwjgl.glfw.GLFW;

public class BeehemothControls {
    public static final KeyMapping KEY_BIND_BEEHEMOTH_DOWN = new KeyMapping(
    "key." + Bumblezone.MODID + ".beehemoth_down", GLFW.GLFW_KEY_CAPS_LOCK, "key.categories." + Bumblezone.MODID
    );

    public static final KeyMapping KEY_BIND_BEEHEMOTH_UP = new KeyMapping(
    "key." + Bumblezone.MODID + ".beehemoth_up", GLFW.GLFW_KEY_SPACE, "key.categories." + Bumblezone.MODID
    );

    public static void keyInput(int key, int action) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.getVehicle() instanceof BeehemothEntity) {
            FriendlyByteBuf passedData = new FriendlyByteBuf(Unpooled.buffer());
            passedData.writeByte(key == KEY_BIND_BEEHEMOTH_UP.getDefaultKey().getValue() ? action : 2);
            passedData.writeByte(key == KEY_BIND_BEEHEMOTH_DOWN.getDefaultKey().getValue() ? action : 2);
            ClientPlayNetworking.send(BeehemothControlsPacket.PACKET_ID, passedData);
        }
    }
}
