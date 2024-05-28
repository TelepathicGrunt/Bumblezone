package com.telepathicgrunt.the_bumblezone.client;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerTickEvent;
import com.telepathicgrunt.the_bumblezone.mixin.client.SoundEngineAccessor;
import com.telepathicgrunt.the_bumblezone.mixin.client.SoundManagerAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class MusicHandler {
    public static class MusicFader {
        public final SoundInstance music;
        public final Predicate<Minecraft> stopOtherMusic;
        public final int counterStart;
        public boolean fadeIn;
        public int counter;

        public MusicFader(SoundInstance music, Predicate<Minecraft> stopOtherMusic, int counterStart, boolean fadeIn) {
            this.music = music;
            this.stopOtherMusic = stopOtherMusic;
            this.counterStart = counterStart;
            this.fadeIn = fadeIn;

            this.counter = this.counterStart;
        }
    }

    private static final Map<ResourceLocation, MusicFader> MUSIC_FADERS = new HashMap<>();
    private static SoundInstance ANGRY_BEE_MUSIC = null;
    private static SoundInstance SEMPITERNAL_SANCTUM_MUSIC = null;
    private static SoundInstance ESSENCE_EVENT_MUSIC = null;
    private static final ResourceLocation BIOME_MUSIC = new ResourceLocation(Bumblezone.MODID, "biome_music");
    public static boolean BUMBLEZONE_MUSIC_PLAYING = false;

    public static void tickMusicFader(PlayerTickEvent event) {
        Minecraft minecraftClient = Minecraft.getInstance();

        Iterator<Map.Entry<ResourceLocation, MusicFader>> iterator = MUSIC_FADERS.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<ResourceLocation, MusicFader> entry = iterator.next();
            MusicFader musicFader = entry.getValue();
            if (musicFader.fadeIn) {
                boolean success = musicFader.stopOtherMusic.test(minecraftClient);
                if (!success) {
                    minecraftClient.getSoundManager().stop(musicFader.music);
                    iterator.remove();
                    continue;
                }

                boolean isPlaying = isMusicPlaying(minecraftClient, musicFader.music);
                if (!isPlaying) {
                    minecraftClient.getSoundManager().play(musicFader.music);
                }
                else if (musicFader.counter == 0) {
                    continue;
                }

                float newVolume = Math.max(0.01f, 1 - ((float)musicFader.counter / musicFader.counterStart));
                setMusicVolume(minecraftClient, musicFader.music, newVolume);
            }
            else {
                if (musicFader.counter == 0) {
                    minecraftClient.getSoundManager().stop(musicFader.music);
                    iterator.remove();
                    continue;
                }

                float newVolume = ((float)musicFader.counter / musicFader.counterStart);
                setMusicVolume(minecraftClient, musicFader.music, newVolume);
            }

            if (musicFader.counter > 0) {
                musicFader.counter--;
            }
        }
    }

    // CLIENT-SIDED
    public static void playStopAngryBeeMusic(Player entity, boolean play) {
        Minecraft minecraftClient = Minecraft.getInstance();
        if (play && (ANGRY_BEE_MUSIC == null || !isMusicPlaying(minecraftClient, ANGRY_BEE_MUSIC))) {
            if (ESSENCE_EVENT_MUSIC != null && isMusicPlaying(minecraftClient, ESSENCE_EVENT_MUSIC)) {
                return;
            }
            if (!entity.isCreative() && !entity.isSpectator() && entity == minecraftClient.player && !isMusicPlaying(minecraftClient, ANGRY_BEE_MUSIC)) {
                ANGRY_BEE_MUSIC = SimpleSoundInstance.forMusic(BzSounds.ANGERED_BEES.get());

                BUMBLEZONE_MUSIC_PLAYING = true;
                minecraftClient.getSoundManager().play(ANGRY_BEE_MUSIC);
                setMusicVolume(minecraftClient, ANGRY_BEE_MUSIC, 0.01f);
                addMusicFade(ANGRY_BEE_MUSIC, 100, true, (m) -> {
                    if (isMusicPlaying(minecraftClient, ESSENCE_EVENT_MUSIC)) {
                        return false;
                    }

                    m.getSoundManager().stop(BIOME_MUSIC, SoundSource.MUSIC);
                    m.getSoundManager().stop(SoundEvents.MUSIC_CREATIVE.key().location(), SoundSource.MUSIC);
                    m.getSoundManager().stop(SoundEvents.MUSIC_GAME.key().location(), SoundSource.MUSIC);
                    m.getSoundManager().stop(BzSounds.SEMPITERNAL_SANCTUM.get().getLocation(), SoundSource.MUSIC);

                    return true;
                }) ;
            }
        }
        else if (!play && ANGRY_BEE_MUSIC != null) {
            BUMBLEZONE_MUSIC_PLAYING = false;
            addMusicFade(ANGRY_BEE_MUSIC, 150, false, (m) -> false);
        }
    }

    // CLIENT-SIDED
    public static void playStopSempiternalSanctumMusic(Player entity, ResourceLocation resourceLocation, boolean play) {
        Minecraft minecraftClient = Minecraft.getInstance();

        if (SEMPITERNAL_SANCTUM_MUSIC == null && ESSENCE_EVENT_MUSIC != null) {
            return;
        }

        if (BzSounds.SEMPITERNAL_SANCTUM.get().getLocation().compareTo(resourceLocation) != 0 == play && SEMPITERNAL_SANCTUM_MUSIC != null) {
            BUMBLEZONE_MUSIC_PLAYING = false;
            addMusicFade(SEMPITERNAL_SANCTUM_MUSIC, 500, false, (m) -> false);
        }
        else if (BzSounds.SEMPITERNAL_SANCTUM.get().getLocation().compareTo(resourceLocation) == 0 && play && (SEMPITERNAL_SANCTUM_MUSIC == null || !isMusicPlaying(minecraftClient, SEMPITERNAL_SANCTUM_MUSIC))) {
            if (ANGRY_BEE_MUSIC != null && isMusicPlaying(minecraftClient, ANGRY_BEE_MUSIC)) {
                return;
            }
            if (ESSENCE_EVENT_MUSIC != null && isMusicPlaying(minecraftClient, ESSENCE_EVENT_MUSIC)) {
                return;
            }
            if (entity == minecraftClient.player && !isMusicPlaying(minecraftClient, SEMPITERNAL_SANCTUM_MUSIC)) {
                SEMPITERNAL_SANCTUM_MUSIC = SimpleSoundInstance.forMusic(BzSounds.SEMPITERNAL_SANCTUM.get());

                BUMBLEZONE_MUSIC_PLAYING = true;
                minecraftClient.getSoundManager().play(SEMPITERNAL_SANCTUM_MUSIC);
                setMusicVolume(minecraftClient, SEMPITERNAL_SANCTUM_MUSIC, 0.01f);
                addMusicFade(SEMPITERNAL_SANCTUM_MUSIC, 1000, true, (m) -> {
                    if (isMusicPlaying(minecraftClient, ESSENCE_EVENT_MUSIC) || isMusicPlaying(minecraftClient, ANGRY_BEE_MUSIC)) {
                        return false;
                    }

                    m.getSoundManager().stop(SoundEvents.MUSIC_CREATIVE.key().location(), SoundSource.MUSIC);
                    m.getSoundManager().stop(BIOME_MUSIC, SoundSource.MUSIC);
                    m.getSoundManager().stop(SoundEvents.MUSIC_CREATIVE.key().location(), SoundSource.MUSIC);
                    m.getSoundManager().stop(SoundEvents.MUSIC_GAME.key().location(), SoundSource.MUSIC);

                    return true;
                });
            }
        }
    }

    public static void playStopEssenceEventMusic(Player entity, ResourceLocation resourceLocation, boolean play) {
        Minecraft minecraftClient = Minecraft.getInstance();
        SoundEvent soundEvent = minecraftClient.level.registryAccess().registryOrThrow(Registries.SOUND_EVENT).get(resourceLocation);
        if (soundEvent == null || !GeneralUtils.isInTag(minecraftClient.level.registryAccess().registryOrThrow(Registries.SOUND_EVENT), BzTags.ESSENCE_EVENT_MUSIC, soundEvent)) {
            return;
        }

        if (play && (ESSENCE_EVENT_MUSIC == null || !isMusicPlaying(minecraftClient, ESSENCE_EVENT_MUSIC))) {
            if(entity == minecraftClient.player && !isMusicPlaying(minecraftClient, ESSENCE_EVENT_MUSIC)) {
                ESSENCE_EVENT_MUSIC = SimpleSoundInstance.forMusic(soundEvent);

                BUMBLEZONE_MUSIC_PLAYING = true;
                minecraftClient.getSoundManager().play(ESSENCE_EVENT_MUSIC);
                setMusicVolume(minecraftClient, ESSENCE_EVENT_MUSIC, 0.01f);
                addMusicFade(ESSENCE_EVENT_MUSIC, 300, true, (m) -> {
                    m.getSoundManager().stop(BIOME_MUSIC, SoundSource.MUSIC);
                    m.getSoundManager().stop(SoundEvents.MUSIC_CREATIVE.key().location(), SoundSource.MUSIC);
                    m.getSoundManager().stop(SoundEvents.MUSIC_GAME.key().location(), SoundSource.MUSIC);
                    m.getSoundManager().stop(BzSounds.ANGERED_BEES.get().getLocation(), SoundSource.MUSIC);
                    m.getSoundManager().stop(BzSounds.SEMPITERNAL_SANCTUM.get().getLocation(), SoundSource.MUSIC);
                    return true;
                });
            }
        }
        else if (!play && ESSENCE_EVENT_MUSIC != null) {
            BUMBLEZONE_MUSIC_PLAYING = false;
            addMusicFade(ESSENCE_EVENT_MUSIC, 300, false, (m) -> false);
        }
    }

    private static void addMusicFade(SoundInstance soundInstance, int counterStart, boolean fadeIn, Predicate<Minecraft> stopOtherMusic) {
        if (MUSIC_FADERS.containsKey(soundInstance.getLocation())) {
            MusicFader musicFader = MUSIC_FADERS.get(soundInstance.getLocation());
            boolean originalFadeIn = musicFader.fadeIn;
            if (originalFadeIn != fadeIn) {
                musicFader.fadeIn = fadeIn;
                musicFader.counter = musicFader.counterStart - musicFader.counter;
            }
        }
        else {
            MUSIC_FADERS.put(soundInstance.getLocation(), new MusicFader(soundInstance, stopOtherMusic, counterStart, fadeIn));
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

    private static boolean isMusicPlaying(Minecraft minecraftClient, SoundInstance soundInstance) {
        return minecraftClient.getSoundManager().isActive(soundInstance);
    }
}
