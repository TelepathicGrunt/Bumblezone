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
    public final static SoundEvent BUMBLE_BEE_CHESTPLATE_FLYING = new SoundEvent(new ResourceLocation(Bumblezone.MODID, "entity.the_bumblezone.bumble_bee_chestplate.fly"));
    public final static SoundEvent MUSIC_DISC_HONEY_BEE_RAT_FACED_BOY = new SoundEvent(new ResourceLocation(Bumblezone.MODID, "music_disc.honey_bee_rat_faced_boy"));
    public final static SoundEvent MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV = new SoundEvent(new ResourceLocation(Bumblezone.MODID, "music_disc.flight_of_the_bumblebee_rimsky_korsakov"));
    public static final SoundEvent STINGER_SPEAR_HIT = new SoundEvent(new ResourceLocation(Bumblezone.MODID, "item.the_bumblezone.stinger_spear.hit"));
    public static final SoundEvent STINGER_SPEAR_HIT_GROUND = new SoundEvent(new ResourceLocation(Bumblezone.MODID, "item.the_bumblezone.stinger_spear.hit_ground"));
    public static final SoundEvent STINGER_SPEAR_RETURN = new SoundEvent(new ResourceLocation(Bumblezone.MODID, "item.the_bumblezone.stinger_spear.return"));
    public static final SoundEvent STINGER_SPEAR_THROW = new SoundEvent(new ResourceLocation(Bumblezone.MODID, "item.the_bumblezone.stinger_spear.throw"));

    public static void registerSounds() {
        Registry.register(Registry.SOUND_EVENT, ANGERED_BEES.getLocation(), ANGERED_BEES);
        Registry.register(Registry.SOUND_EVENT, BEEHEMOTH_HURT.getLocation(), BEEHEMOTH_HURT);
        Registry.register(Registry.SOUND_EVENT, BEEHEMOTH_DEATH.getLocation(), BEEHEMOTH_DEATH);
        Registry.register(Registry.SOUND_EVENT, BEEHEMOTH_LOOP.getLocation(), BEEHEMOTH_LOOP);
        Registry.register(Registry.SOUND_EVENT, BUMBLE_BEE_CHESTPLATE_FLYING.getLocation(), BUMBLE_BEE_CHESTPLATE_FLYING);
        Registry.register(Registry.SOUND_EVENT, MUSIC_DISC_HONEY_BEE_RAT_FACED_BOY.getLocation(), MUSIC_DISC_HONEY_BEE_RAT_FACED_BOY);
        Registry.register(Registry.SOUND_EVENT, MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV.getLocation(), MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV);
        Registry.register(Registry.SOUND_EVENT, STINGER_SPEAR_HIT.getLocation(), STINGER_SPEAR_HIT);
        Registry.register(Registry.SOUND_EVENT, STINGER_SPEAR_HIT_GROUND.getLocation(), STINGER_SPEAR_HIT_GROUND);
        Registry.register(Registry.SOUND_EVENT, STINGER_SPEAR_RETURN.getLocation(), STINGER_SPEAR_RETURN);
        Registry.register(Registry.SOUND_EVENT, STINGER_SPEAR_THROW.getLocation(), STINGER_SPEAR_THROW);
    }
}
