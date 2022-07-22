package com.telepathicgrunt.the_bumblezone.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import com.telepathicgrunt.the_bumblezone.mixin.client.KeyMappingAccessor;
import com.telepathicgrunt.the_bumblezone.packets.BeehemothControlsPacket;
import io.netty.buffer.Unpooled;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import org.lwjgl.glfw.GLFW;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

public class BeehemothControls {

    public static final KeyMapping KEY_BIND_BEEHEMOTH_DOWN = createSafeKeyMapping("key." + Bumblezone.MODID + ".beehemoth_down", GLFW.GLFW_KEY_CAPS_LOCK);
    public static final KeyMapping KEY_BIND_BEEHEMOTH_UP = createSafeKeyMapping("key." + Bumblezone.MODID + ".beehemoth_up", GLFW.GLFW_KEY_SPACE);

    public static void keyInput(int key, int scancode, int action) {
        if (Minecraft.getInstance().player != null &&
            Minecraft.getInstance().player.getVehicle() instanceof BeehemothEntity beehemothEntity)
        {
            boolean upKeyAction = KEY_BIND_BEEHEMOTH_UP.matches(key, scancode);
            boolean downKeyAction = KEY_BIND_BEEHEMOTH_DOWN.matches(key, scancode);

            if ((upKeyAction || downKeyAction) && action != 2) {
                FriendlyByteBuf passedData = new FriendlyByteBuf(Unpooled.buffer());
                passedData.writeByte(KEY_BIND_BEEHEMOTH_UP.matches(key, scancode) ? action : 2);
                passedData.writeByte(KEY_BIND_BEEHEMOTH_DOWN.matches(key, scancode) ? action : 2);
                ClientPlayNetworking.send(BeehemothControlsPacket.PACKET_ID, passedData);

                if (upKeyAction) {
                    beehemothEntity.movingStraightUp = action == 1;
                }
                else {
                    beehemothEntity.movingStraightDown = action == 1;
                }
            }
        }
    }

    private static KeyMapping createSafeKeyMapping(String description, int keycode) {
        InputConstants.Key key = InputConstants.Type.KEYSYM.getOrCreate(keycode);
        KeyMapping oldMapping = KeyMappingAccessor.getMAP().get(key);
        KeyMapping keyMapping = new KeyMapping(description, keycode, "key.categories." + Bumblezone.MODID);
        KeyMappingAccessor.getMAP().put(key, oldMapping);
        KeyMappingAccessor.getALL().remove(description);
        return keyMapping;
    }
}
