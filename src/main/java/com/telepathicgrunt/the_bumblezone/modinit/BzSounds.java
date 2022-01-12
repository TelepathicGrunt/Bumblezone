package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class BzSounds {
    public final static SoundEvent ANGERED_BEES = new SoundEvent(new ResourceLocation(Bumblezone.MODID, "angered_bees"));
    public final static SoundEvent BEEHEMOTH_HURT = new SoundEvent(new ResourceLocation(Bumblezone.MODID, "entity.the_bumblezone.beehemoth.hurt"));
    public final static SoundEvent BEEHEMOTH_DEATH = new SoundEvent(new ResourceLocation(Bumblezone.MODID, "entity.the_bumblezone.beehemoth.death"));
    public final static SoundEvent BEEHEMOTH_LOOP = new SoundEvent(new ResourceLocation(Bumblezone.MODID, "entity.the_bumblezone.beehemoth.loop"));
    public final static SoundEvent MUSIC_DISC_HONEY_BEE_RAT_FACED_BOY = new SoundEvent(new ResourceLocation(Bumblezone.MODID, "music_disc.honey_bee_rat_faced_boy"));
    public final static SoundEvent MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV = new SoundEvent(new ResourceLocation(Bumblezone.MODID, "music_disc.flight_of_the_bumblebee_rimsky_korsakov"));

    public static void registerSounds() {
        Registry.register(Registry.SOUND_EVENT, ANGERED_BEES.getLocation(), ANGERED_BEES);
        Registry.register(Registry.SOUND_EVENT, BEEHEMOTH_HURT.getLocation(), BEEHEMOTH_HURT);
        Registry.register(Registry.SOUND_EVENT, BEEHEMOTH_DEATH.getLocation(), BEEHEMOTH_DEATH);
        Registry.register(Registry.SOUND_EVENT, BEEHEMOTH_LOOP.getLocation(), BEEHEMOTH_LOOP);
        Registry.register(Registry.SOUND_EVENT, MUSIC_DISC_HONEY_BEE_RAT_FACED_BOY.getLocation(), MUSIC_DISC_HONEY_BEE_RAT_FACED_BOY);
        Registry.register(Registry.SOUND_EVENT, MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV.getLocation(), MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV);
    }
}
