package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BzSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Bumblezone.MODID);

    public static final RegistryObject<SoundEvent> ANGERED_BEES = SOUND_EVENTS.register("angered_bees", () -> new SoundEvent(new ResourceLocation(Bumblezone.MODID, "angered_bees")));
    public static final RegistryObject<SoundEvent> MUSIC_DISC_HONEY_BEE_RAT_FACED_BOY = SOUND_EVENTS.register("music_disc_honey_bee_rat_faced_boy", () -> new SoundEvent(new ResourceLocation(Bumblezone.MODID, "music_disc.honey_bee_rat_faced_boy")));
    public static final RegistryObject<SoundEvent> MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV = SOUND_EVENTS.register("music_disc_flight_of_the_bumblebee_rimsky_korsakov", () -> new SoundEvent(new ResourceLocation(Bumblezone.MODID, "music_disc.flight_of_the_bumblebee_rimsky_korsakov")));
    public static final RegistryObject<SoundEvent> BEEHEMOTH_HURT = SOUND_EVENTS.register("entity.the_bumblezone.beehemoth.hurt", () -> new SoundEvent(new ResourceLocation(Bumblezone.MODID, "entity.the_bumblezone.beehemoth.hurt")));
    public static final RegistryObject<SoundEvent> BEEHEMOTH_DEATH = SOUND_EVENTS.register("entity.the_bumblezone.beehemoth.death", () -> new SoundEvent(new ResourceLocation(Bumblezone.MODID, "entity.the_bumblezone.beehemoth.death")));
    public static final RegistryObject<SoundEvent> BEEHEMOTH_LOOP = SOUND_EVENTS.register("entity.the_bumblezone.beehemoth.loop", () -> new SoundEvent(new ResourceLocation(Bumblezone.MODID, "entity.the_bumblezone.beehemoth.loop")));
    public static final RegistryObject<SoundEvent> STINGER_SPEAR_HIT = SOUND_EVENTS.register("item.the_bumblezone.stinger_spear.hit", () -> new SoundEvent(new ResourceLocation(Bumblezone.MODID, "item.the_bumblezone.stinger_spear.hit")));
    public static final RegistryObject<SoundEvent> STINGER_SPEAR_HIT_GROUND = SOUND_EVENTS.register("item.the_bumblezone.stinger_spear.hit_ground", () -> new SoundEvent(new ResourceLocation(Bumblezone.MODID, "item.the_bumblezone.stinger_spear.hit_ground")));
    public static final RegistryObject<SoundEvent> STINGER_SPEAR_RETURN = SOUND_EVENTS.register("item.the_bumblezone.stinger_spear.return", () -> new SoundEvent(new ResourceLocation(Bumblezone.MODID, "item.the_bumblezone.stinger_spear.return")));
    public static final RegistryObject<SoundEvent> STINGER_SPEAR_THROW = SOUND_EVENTS.register("item.the_bumblezone.stinger_spear.throw", () -> new SoundEvent(new ResourceLocation(Bumblezone.MODID, "item.the_bumblezone.stinger_spear.throw")));
}
