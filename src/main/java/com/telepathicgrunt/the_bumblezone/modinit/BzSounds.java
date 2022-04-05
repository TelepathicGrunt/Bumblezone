package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BzSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Bumblezone.MODID);

    public static final RegistryObject<SoundEvent> ANGERED_BEES = registerSoundEvent("angered_bees");
    public static final RegistryObject<SoundEvent> MUSIC_DISC_HONEY_BEE_RAT_FACED_BOY = registerSoundEvent("music_disc.honey_bee_rat_faced_boy");
    public static final RegistryObject<SoundEvent> MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV = registerSoundEvent("music_disc.flight_of_the_bumblebee_rimsky_korsakov");
    public static final RegistryObject<SoundEvent> BEEHEMOTH_HURT = registerSoundEvent("entity.the_bumblezone.beehemoth.hurt");
    public static final RegistryObject<SoundEvent> BEEHEMOTH_DEATH = registerSoundEvent("entity.the_bumblezone.beehemoth.death");
    public static final RegistryObject<SoundEvent> BEEHEMOTH_LOOP = registerSoundEvent("entity.the_bumblezone.beehemoth.loop");
    public static final RegistryObject<SoundEvent> BUMBLE_BEE_CHESTPLATE_FLYING = registerSoundEvent("entity.the_bumblezone.bumble_bee_chestplate.fly");
    public static final RegistryObject<SoundEvent> STINGER_SPEAR_HIT = registerSoundEvent("item.the_bumblezone.stinger_spear.hit");
    public static final RegistryObject<SoundEvent> STINGER_SPEAR_HIT_GROUND = registerSoundEvent("item.the_bumblezone.stinger_spear.hit_ground");
    public static final RegistryObject<SoundEvent> STINGER_SPEAR_RETURN = registerSoundEvent("item.the_bumblezone.stinger_spear.return");
    public static final RegistryObject<SoundEvent> STINGER_SPEAR_THROW = registerSoundEvent("item.the_bumblezone.stinger_spear.throw");
    public static final RegistryObject<SoundEvent> WASHING_RESIDUES = registerSoundEvent("block.the_bumblezone.washing_honey_residues");
    public static final RegistryObject<SoundEvent> HONEY_SLIME_ATTACK = registerSoundEvent("entity.the_bumblezone.honey_slime.attack");
    public static final RegistryObject<SoundEvent> HONEY_SLIME_HURT = registerSoundEvent("entity.the_bumblezone.honey_slime.hurt");
    public static final RegistryObject<SoundEvent> HONEY_SLIME_DEATH = registerSoundEvent("entity.the_bumblezone.honey_slime.death");
    public static final RegistryObject<SoundEvent> HONEY_SLIME_SQUISH = registerSoundEvent("entity.the_bumblezone.honey_slime.squish");
    public static final RegistryObject<SoundEvent> HONEY_SLIME_JUMP = registerSoundEvent("entity.the_bumblezone.honey_slime.jump");
    public static final RegistryObject<SoundEvent> HONEY_SLIME_HURT_SMALL = registerSoundEvent("entity.the_bumblezone.honey_slime.hurt_small");
    public static final RegistryObject<SoundEvent> HONEY_SLIME_DEATH_SMALL = registerSoundEvent("entity.the_bumblezone.honey_slime.death_small");
    public static final RegistryObject<SoundEvent> HONEY_SLIME_SQUISH_SMALL = registerSoundEvent("entity.the_bumblezone.honey_slime.squish_small");
    public static final RegistryObject<SoundEvent> HONEY_SLIME_JUMP_SMALL = registerSoundEvent("entity.the_bumblezone.honey_slime.jump_small");
    public static final RegistryObject<SoundEvent> POLLEN_PUFF_THROW = registerSoundEvent("entity.the_bumblezone.pollen_puff.throw");
    public static final RegistryObject<SoundEvent> SUGAR_WATER_DRINK = registerSoundEvent("item.the_bumblezone.sugar_water_bottle.drink");


    private static RegistryObject<SoundEvent> registerSoundEvent(String path) {
        return SOUND_EVENTS.register(path, () -> new SoundEvent(new ResourceLocation(Bumblezone.MODID, path)));
    }
}
