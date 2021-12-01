package com.telepathicgrunt.bumblezone.client;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.modinit.BzSounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

@Environment(EnvType.CLIENT)
public class MusicHandler {

    private static SoundInstance ANGRY_BEE_MUSIC = null;
    private static final ResourceLocation BIOME_MUSIC = new ResourceLocation(Bumblezone.MODID, "biome_music");

    public static void playAngryBeeMusic(Player entity) {
        Minecraft minecraftClient = Minecraft.getInstance();
        if(!entity.isCreative() && entity == minecraftClient.player && !minecraftClient.getSoundManager().isActive(ANGRY_BEE_MUSIC)) {
            ANGRY_BEE_MUSIC = SimpleSoundInstance.forMusic(BzSounds.ANGERED_BEES);
            minecraftClient.getSoundManager().play(ANGRY_BEE_MUSIC);
        }
        minecraftClient.getSoundManager().stop(BIOME_MUSIC, SoundSource.MUSIC);
    }

    public static void stopAngryBeeMusic(Player entity) {
        Minecraft minecraftClient = Minecraft.getInstance();
        if(entity == minecraftClient.player && ANGRY_BEE_MUSIC != null) {
            minecraftClient.getSoundManager().stop(ANGRY_BEE_MUSIC);
        }
    }
}
