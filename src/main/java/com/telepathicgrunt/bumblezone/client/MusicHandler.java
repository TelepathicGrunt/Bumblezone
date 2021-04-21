package com.telepathicgrunt.bumblezone.client;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.modinit.BzSounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class MusicHandler {

    private static SoundInstance ANGRY_BEE_MUSIC = null;
    private static final Identifier BIOME_MUSIC = new Identifier(Bumblezone.MODID, "biome_music");

    public static void playAngryBeeMusic(PlayerEntity entity){
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if(!entity.isCreative() && entity == minecraftClient.player && !minecraftClient.getSoundManager().isPlaying(ANGRY_BEE_MUSIC)){
            ANGRY_BEE_MUSIC = PositionedSoundInstance.music(BzSounds.ANGERED_BEES);
            minecraftClient.getSoundManager().play(ANGRY_BEE_MUSIC);
        }
        minecraftClient.getSoundManager().stopSounds(BIOME_MUSIC, SoundCategory.MUSIC);
    }

    public static void stopAngryBeeMusic(PlayerEntity entity){
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if(entity == minecraftClient.player && ANGRY_BEE_MUSIC != null){
            minecraftClient.getSoundManager().stop(ANGRY_BEE_MUSIC);
        }
    }
}
