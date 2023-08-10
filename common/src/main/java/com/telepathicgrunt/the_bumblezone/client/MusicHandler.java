package com.telepathicgrunt.the_bumblezone.client;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

public class MusicHandler {

    private static SoundInstance ANGRY_BEE_MUSIC = null;
    private static SoundInstance SEMPITERNAL_SANCTUM_MUSIC = null;
    private static final ResourceLocation BIOME_MUSIC = new ResourceLocation(Bumblezone.MODID, "biome_music");

    // CLIENT-SIDED
    public static void playStopAngryBeeMusic(Player entity, boolean play) {
        Minecraft minecraftClient = Minecraft.getInstance();
        if (play && (ANGRY_BEE_MUSIC == null || !minecraftClient.getSoundManager().isActive(ANGRY_BEE_MUSIC))) {
            if (!entity.isCreative() && entity == minecraftClient.player && !minecraftClient.getSoundManager().isActive(ANGRY_BEE_MUSIC)) {
                ANGRY_BEE_MUSIC = SimpleSoundInstance.forMusic(BzSounds.ANGERED_BEES.get());
                minecraftClient.getSoundManager().play(ANGRY_BEE_MUSIC);
            }
            minecraftClient.getSoundManager().stop(BIOME_MUSIC, SoundSource.MUSIC);
            minecraftClient.getSoundManager().stop(SoundEvents.MUSIC_CREATIVE.key().location(), SoundSource.MUSIC);
            minecraftClient.getSoundManager().stop(BzSounds.SEMPITERNAL_SANCTUM.get().getLocation(), SoundSource.MUSIC);
        }
        else {
            if(entity == minecraftClient.player && ANGRY_BEE_MUSIC != null) {
                minecraftClient.getSoundManager().stop(ANGRY_BEE_MUSIC);
            }
        }
    }

    // CLIENT-SIDED
    public static void playStopSempiternalSanctumMusic(Player entity, boolean play) {
        Minecraft minecraftClient = Minecraft.getInstance();
        if (play && (SEMPITERNAL_SANCTUM_MUSIC == null || !minecraftClient.getSoundManager().isActive(SEMPITERNAL_SANCTUM_MUSIC))) {
            if (ANGRY_BEE_MUSIC != null && minecraftClient.getSoundManager().isActive(ANGRY_BEE_MUSIC)) {
                return;
            }
            if(!entity.isCreative() && entity == minecraftClient.player && !minecraftClient.getSoundManager().isActive(SEMPITERNAL_SANCTUM_MUSIC)) {
                SEMPITERNAL_SANCTUM_MUSIC = SimpleSoundInstance.forMusic(BzSounds.SEMPITERNAL_SANCTUM.get());
                minecraftClient.getSoundManager().play(SEMPITERNAL_SANCTUM_MUSIC);
            }
            minecraftClient.getSoundManager().stop(BIOME_MUSIC, SoundSource.MUSIC);
            minecraftClient.getSoundManager().stop(SoundEvents.MUSIC_CREATIVE.key().location(), SoundSource.MUSIC);
        }
        else {
            if(entity == minecraftClient.player && SEMPITERNAL_SANCTUM_MUSIC != null) {
                minecraftClient.getSoundManager().stop(SEMPITERNAL_SANCTUM_MUSIC);
            }
        }
    }
}
