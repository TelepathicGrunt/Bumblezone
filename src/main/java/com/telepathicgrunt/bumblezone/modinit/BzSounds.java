package com.telepathicgrunt.bumblezone.modinit;

import com.telepathicgrunt.bumblezone.Bumblezone;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class BzSounds {
    public final static SoundEvent ANGERED_BEES = new SoundEvent(new ResourceLocation(Bumblezone.MODID, "angered_bees"));
    public final static SoundEvent BEEHEMOTH_HURT = new SoundEvent(new ResourceLocation(Bumblezone.MODID, "entity.beehemoth.hurt"));
    public final static SoundEvent BEEHEMOTH_DEATH = new SoundEvent(new ResourceLocation(Bumblezone.MODID, "entity.beehemoth.death"));
    public final static SoundEvent MUSIC_DISC_HONEY_BEE_RAT_FACED_BOY = new SoundEvent(new ResourceLocation(Bumblezone.MODID, "music_disc.honey_bee_rat_faced_boy"));
    public final static SoundEvent MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV = new SoundEvent(new ResourceLocation(Bumblezone.MODID, "music_disc.flight_of_the_bumblebee_rimsky_korsakov"));

    public static void registerSounds() {
        Registry.register(Registry.SOUND_EVENT, new ResourceLocation(Bumblezone.MODID, "angered_bees"), ANGERED_BEES);
        Registry.register(Registry.SOUND_EVENT, new ResourceLocation(Bumblezone.MODID, "entity.beehemoth.hurt"), BEEHEMOTH_HURT);
        Registry.register(Registry.SOUND_EVENT, new ResourceLocation(Bumblezone.MODID, "entity.beehemoth.death"), BEEHEMOTH_DEATH);
        Registry.register(Registry.SOUND_EVENT, new ResourceLocation(Bumblezone.MODID, "music_disc_honey_bee_rat_faced_boy"), MUSIC_DISC_HONEY_BEE_RAT_FACED_BOY);
        Registry.register(Registry.SOUND_EVENT, new ResourceLocation(Bumblezone.MODID, "music_disc_flight_of_the_bumblebee_rimsky_korsakov"), MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV);
    }
}
