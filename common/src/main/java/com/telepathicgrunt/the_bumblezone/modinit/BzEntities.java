package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeeQueenEntity;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import com.telepathicgrunt.the_bumblezone.entities.mobs.HoneySlimeEntity;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.BeeStingerEntity;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.HoneyCrystalShardEntity;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.PollenPuffEntity;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.ThrownStingerSpearEntity;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.RegisterEntityAttributesEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.RegisterSpawnPlacementsEvent;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistries;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;

public class BzEntities {
    public static final ResourcefulRegistry<EntityType<?>> ENTITIES = ResourcefulRegistries.create(BuiltInRegistries.ENTITY_TYPE, Bumblezone.MODID);

    public static final RegistryEntry<EntityType<HoneySlimeEntity>> HONEY_SLIME = ENTITIES.register("honey_slime", () -> EntityType.Builder.<HoneySlimeEntity>of(HoneySlimeEntity::new, MobCategory.CREATURE).sized(1F, 1F).clientTrackingRange(8).build("honey_slime"));
    public static final RegistryEntry<EntityType<BeehemothEntity>> BEEHEMOTH = ENTITIES.register("beehemoth", () -> EntityType.Builder.of(BeehemothEntity::new, MobCategory.CREATURE).sized(1.2F, 1.2F).clientTrackingRange(16).build("beehemoth"));
    public static final RegistryEntry<EntityType<BeeQueenEntity>> BEE_QUEEN = ENTITIES.register("bee_queen", () -> EntityType.Builder.of(BeeQueenEntity::new, MobCategory.CREATURE).sized(2.9F, 2.9F).clientTrackingRange(16).build("bee_queen"));
    public static final RegistryEntry<EntityType<PollenPuffEntity>> POLLEN_PUFF_ENTITY = ENTITIES.register("pollen_puff", () -> EntityType.Builder.<PollenPuffEntity>of(PollenPuffEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build("pollen_puff"));
    public static final RegistryEntry<EntityType<ThrownStingerSpearEntity>> THROWN_STINGER_SPEAR_ENTITY = ENTITIES.register("thrown_stinger_spear", () -> EntityType.Builder.<ThrownStingerSpearEntity>of(ThrownStingerSpearEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("thrown_stinger_spear"));
    public static final RegistryEntry<EntityType<BeeStingerEntity>> BEE_STINGER_ENTITY = ENTITIES.register("bee_stinger", () -> EntityType.Builder.<BeeStingerEntity>of(BeeStingerEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("bee_stinger"));
    public static final RegistryEntry<EntityType<HoneyCrystalShardEntity>> HONEY_CRYSTAL_SHARD = ENTITIES.register("honey_crystal_shard", () -> EntityType.Builder.<HoneyCrystalShardEntity>of(HoneyCrystalShardEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("honey_crystal_shard"));

    public static void registerEntitySpawnRestrictions(RegisterSpawnPlacementsEvent event) {
        event.register(HONEY_SLIME.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
        event.register(BEEHEMOTH.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BeehemothEntity::checkMobSpawnRules);
        event.register(BEE_QUEEN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BeeQueenEntity::checkMobSpawnRules);
    }

    public static void registerEntityAttributes(RegisterEntityAttributesEvent event) {
        event.register(HONEY_SLIME.get(), HoneySlimeEntity.getAttributeBuilder());
        event.register(BEEHEMOTH.get(), BeehemothEntity.getAttributeBuilder());
        event.register(BEE_QUEEN.get(), BeeQueenEntity.getAttributeBuilder());
    }
}
