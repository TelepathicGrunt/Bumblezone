package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BzSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Bumblezone.MODID);

    public static final RegistryObject<SoundEvent> ANGERED_BEES = SOUND_EVENTS.register("angered_bees", () -> new SoundEvent(new ResourceLocation(Bumblezone.MODID, "angered_bees")));
    public static final RegistryObject<SoundEvent> MUSIC_DISC_HONEY_BEE_RAT_FACED_BOY = SOUND_EVENTS.register("music_disc_honey_bee_rat_faced_boy", () -> new SoundEvent(new ResourceLocation(Bumblezone.MODID, "music_disc.honey_bee_rat_faced_boy")));
    public static final RegistryObject<SoundEvent> MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV = SOUND_EVENTS.register("music_disc_flight_of_the_bumblebee_rimsky_korsakov", () -> new SoundEvent(new ResourceLocation(Bumblezone.MODID, "music_disc.flight_of_the_bumblebee_rimsky_korsakov")));
}
