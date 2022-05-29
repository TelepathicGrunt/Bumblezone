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
    public static final SoundEvent WASHING_RESIDUES = new SoundEvent(new ResourceLocation(Bumblezone.MODID, "block.the_bumblezone.washing_honey_residues"));
    public static final SoundEvent HONEY_SLIME_ATTACK = new SoundEvent(new ResourceLocation(Bumblezone.MODID, "entity.the_bumblezone.honey_slime.attack"));
    public static final SoundEvent HONEY_SLIME_HURT = new SoundEvent(new ResourceLocation(Bumblezone.MODID, "entity.the_bumblezone.honey_slime.hurt"));
    public static final SoundEvent HONEY_SLIME_DEATH = new SoundEvent(new ResourceLocation(Bumblezone.MODID, "entity.the_bumblezone.honey_slime.death"));
    public static final SoundEvent HONEY_SLIME_SQUISH = new SoundEvent(new ResourceLocation(Bumblezone.MODID, "entity.the_bumblezone.honey_slime.squish"));
    public static final SoundEvent HONEY_SLIME_JUMP = new SoundEvent(new ResourceLocation(Bumblezone.MODID, "entity.the_bumblezone.honey_slime.jump"));
    public static final SoundEvent HONEY_SLIME_HURT_SMALL = new SoundEvent(new ResourceLocation(Bumblezone.MODID, "entity.the_bumblezone.honey_slime.hurt_small"));
    public static final SoundEvent HONEY_SLIME_DEATH_SMALL = new SoundEvent(new ResourceLocation(Bumblezone.MODID, "entity.the_bumblezone.honey_slime.death_small"));
    public static final SoundEvent HONEY_SLIME_SQUISH_SMALL = new SoundEvent(new ResourceLocation(Bumblezone.MODID, "entity.the_bumblezone.honey_slime.squish_small"));
    public static final SoundEvent HONEY_SLIME_JUMP_SMALL = new SoundEvent(new ResourceLocation(Bumblezone.MODID, "entity.the_bumblezone.honey_slime.jump_small"));
    public static final SoundEvent POLLEN_PUFF_THROW = new SoundEvent(new ResourceLocation(Bumblezone.MODID, "entity.the_bumblezone.pollen_puff.throw"));
    public static final SoundEvent SUGAR_WATER_DRINK = new SoundEvent(new ResourceLocation(Bumblezone.MODID, "item.the_bumblezone.sugar_water_bottle.drink"));
    public static final SoundEvent HONEY_COMPASS_BLOCK_LOCK = new SoundEvent(new ResourceLocation(Bumblezone.MODID, "item.the_bumblezone.the_bumblezone.honey_compass.block_lock"));
    public static final SoundEvent HONEY_COMPASS_STRUCTURE_LOCK = new SoundEvent(new ResourceLocation(Bumblezone.MODID, "item.the_bumblezone.the_bumblezone.honey_compass.structure_lock"));

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
        Registry.register(Registry.SOUND_EVENT, WASHING_RESIDUES.getLocation(), WASHING_RESIDUES);
        Registry.register(Registry.SOUND_EVENT, HONEY_SLIME_ATTACK.getLocation(), HONEY_SLIME_ATTACK);
        Registry.register(Registry.SOUND_EVENT, HONEY_SLIME_HURT.getLocation(), HONEY_SLIME_HURT);
        Registry.register(Registry.SOUND_EVENT, HONEY_SLIME_DEATH.getLocation(), HONEY_SLIME_DEATH);
        Registry.register(Registry.SOUND_EVENT, HONEY_SLIME_SQUISH.getLocation(), HONEY_SLIME_SQUISH);
        Registry.register(Registry.SOUND_EVENT, HONEY_SLIME_JUMP.getLocation(), HONEY_SLIME_JUMP);
        Registry.register(Registry.SOUND_EVENT, HONEY_SLIME_HURT_SMALL.getLocation(), HONEY_SLIME_HURT_SMALL);
        Registry.register(Registry.SOUND_EVENT, HONEY_SLIME_DEATH_SMALL.getLocation(), HONEY_SLIME_DEATH_SMALL);
        Registry.register(Registry.SOUND_EVENT, HONEY_SLIME_SQUISH_SMALL.getLocation(), HONEY_SLIME_SQUISH_SMALL);
        Registry.register(Registry.SOUND_EVENT, HONEY_SLIME_JUMP_SMALL.getLocation(), HONEY_SLIME_JUMP_SMALL);
        Registry.register(Registry.SOUND_EVENT, POLLEN_PUFF_THROW.getLocation(), POLLEN_PUFF_THROW);
        Registry.register(Registry.SOUND_EVENT, SUGAR_WATER_DRINK.getLocation(), SUGAR_WATER_DRINK);
        Registry.register(Registry.SOUND_EVENT, HONEY_COMPASS_BLOCK_LOCK.getLocation(), HONEY_COMPASS_BLOCK_LOCK);
        Registry.register(Registry.SOUND_EVENT, HONEY_COMPASS_STRUCTURE_LOCK.getLocation(), HONEY_COMPASS_STRUCTURE_LOCK);
    }
}
