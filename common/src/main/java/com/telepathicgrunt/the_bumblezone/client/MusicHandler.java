package com.telepathicgrunt.the_bumblezone.client;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerTickEvent;
import com.telepathicgrunt.the_bumblezone.mixin.client.SoundEngineAccessor;
import com.telepathicgrunt.the_bumblezone.mixin.client.SoundManagerAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MusicHandler {
    public static class MusicFader {
        public final SoundInstance music;
        public int counterStart;
        public boolean fadeIn;
        public int counter;

        public MusicFader(SoundInstance music, int counterStart, boolean fadeIn) {
            this.music = music;
            this.counterStart = counterStart;
            this.fadeIn = fadeIn;

            this.counter = this.counterStart;
        }
    }

    private static final Map<ResourceLocation, MusicFader> MUSIC_FADERS = new HashMap<>();
    private static SoundInstance ANGRY_BEE_MUSIC = null;
    private static SoundInstance SEMPITERNAL_SANCTUM_MUSIC = null;
    private static final ResourceLocation BIOME_MUSIC = new ResourceLocation(Bumblezone.MODID, "biome_music");

    public static void tickMusicFader(PlayerTickEvent event) {
        Minecraft minecraftClient = Minecraft.getInstance();

        Iterator<Map.Entry<ResourceLocation, MusicFader>> iterator = MUSIC_FADERS.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<ResourceLocation, MusicFader> entry = iterator.next();
            MusicFader musicFader = entry.getValue();
            if (musicFader.fadeIn) {
                if (musicFader.counter == 0) {
                    iterator.remove();
                    continue;
                }

                float newVolume = Math.max(0.01f, 1 - ((float)musicFader.counter / musicFader.counterStart));
                setMusicVolume(minecraftClient, musicFader.music, newVolume);
            }
            else {
                if (musicFader.counter == 0) {
                    iterator.remove();
                    minecraftClient.getSoundManager().stop(musicFader.music);
                    continue;
                }

                float newVolume = ((float)musicFader.counter / musicFader.counterStart);
                setMusicVolume(minecraftClient, musicFader.music, newVolume);
            }

            musicFader.counter--;
        }
    }

    // CLIENT-SIDED
    public static void playStopAngryBeeMusic(Player entity, boolean play) {
        Minecraft minecraftClient = Minecraft.getInstance();
        if (play && (ANGRY_BEE_MUSIC == null || !minecraftClient.getSoundManager().isActive(ANGRY_BEE_MUSIC))) {
            if (!entity.isCreative() && entity == minecraftClient.player && !minecraftClient.getSoundManager().isActive(ANGRY_BEE_MUSIC)) {
                ANGRY_BEE_MUSIC = SimpleSoundInstance.forMusic(BzSounds.ANGERED_BEES.get());

                minecraftClient.getSoundManager().play(ANGRY_BEE_MUSIC);
                setMusicVolume(minecraftClient, ANGRY_BEE_MUSIC, 0.01f);
                addMusicFade(ANGRY_BEE_MUSIC, 100, true);
            }
            minecraftClient.getSoundManager().stop(BIOME_MUSIC, SoundSource.MUSIC);
            minecraftClient.getSoundManager().stop(SoundEvents.MUSIC_CREATIVE.key().location(), SoundSource.MUSIC);
            minecraftClient.getSoundManager().stop(BzSounds.SEMPITERNAL_SANCTUM.get().getLocation(), SoundSource.MUSIC);
        }
        else if (!play && ANGRY_BEE_MUSIC != null) {
            addMusicFade(ANGRY_BEE_MUSIC, 150, false);
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
                setMusicVolume(minecraftClient, SEMPITERNAL_SANCTUM_MUSIC, 0.01f);
                addMusicFade(SEMPITERNAL_SANCTUM_MUSIC, 1000, true);
            }
            minecraftClient.getSoundManager().stop(BIOME_MUSIC, SoundSource.MUSIC);
            minecraftClient.getSoundManager().stop(SoundEvents.MUSIC_CREATIVE.key().location(), SoundSource.MUSIC);
            minecraftClient.getSoundManager().stop(SoundEvents.MUSIC_GAME.key().location(), SoundSource.MUSIC);
        }
        else if (!play && SEMPITERNAL_SANCTUM_MUSIC != null) {
            addMusicFade(SEMPITERNAL_SANCTUM_MUSIC, 1000, false);
        }
    }

    private static void addMusicFade(SoundInstance soundInstance, int counterStart, boolean fadeIn) {
        if (MUSIC_FADERS.containsKey(soundInstance.getLocation())) {
            MusicFader musicFader = MUSIC_FADERS.get(soundInstance.getLocation());
            boolean originalFadeIn = musicFader.fadeIn;
            if (originalFadeIn != fadeIn) {
                musicFader.fadeIn = fadeIn;
                musicFader.counter = musicFader.counterStart - musicFader.counter;
            }
        }
        else {
            MUSIC_FADERS.put(soundInstance.getLocation(), new MusicFader(soundInstance, counterStart, fadeIn));
        }
    }

    private static void setMusicVolume(Minecraft minecraftClient, SoundInstance soundInstance, float volume) {
        float playerSetVolume = minecraftClient.options.getSoundSourceVolume(soundInstance.getSource());

        ChannelAccess.ChannelHandle channelHandle = ((SoundEngineAccessor)((SoundManagerAccessor) minecraftClient.getSoundManager())
                .getSoundEngine())
                .getInstanceToChannel()
                .get(soundInstance);

        if (channelHandle != null) {
            channelHandle.execute((channel -> channel.setVolume(Math.min(volume, playerSetVolume))));
        }
    }
}
