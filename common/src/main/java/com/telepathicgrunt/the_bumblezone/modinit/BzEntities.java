package com.telepathicgrunt.the_bumblezone.modinit;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.living.CosmicCrystalEntity;
import com.telepathicgrunt.the_bumblezone.entities.living.CosmicCrystalState;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeeQueenEntity;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeeQueenState;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import com.telepathicgrunt.the_bumblezone.entities.mobs.HoneySlimeEntity;
import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminEntity;
import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminState;
import com.telepathicgrunt.the_bumblezone.entities.mobs.VariantBeeEntity;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.BeeStingerEntity;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.DirtPelletEntity;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.ElectricRingEntity;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.HoneyCrystalShardEntity;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.PollenPuffEntity;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.PurpleSpikeEntity;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.SentryWatcherEntity;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.ThrownStingerSpearEntity;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzRegisterEntityAttributesEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzRegisterSpawnPlacementsEvent;
import com.telepathicgrunt.the_bumblezone.services.PlatformService;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Optional;
import java.util.UUID;

public class BzEntities {
    public static final ResourcefulRegistry<EntityType<?>> ENTITIES = ResourcefulRegistries.create(BuiltInRegistries.ENTITY_TYPE, Bumblezone.MODID);

    public static final RegistryEntry<EntityType<VariantBeeEntity>> VARIANT_BEE = ENTITIES.register("variant_bee", () -> PlatformService.INSTANCE.createEntityType(VariantBeeEntity::new, MobCategory.CREATURE, 0.7f, 0.6f, 8, 3, "variant_bee"));
    public static final RegistryEntry<EntityType<HoneySlimeEntity>> HONEY_SLIME = ENTITIES.register("honey_slime", () -> PlatformService.INSTANCE.createEntityType(HoneySlimeEntity::new, MobCategory.CREATURE, 1F, 1F, 0.625F, 8, 3, "honey_slime"));
    public static final RegistryEntry<EntityType<BeehemothEntity>> BEEHEMOTH = ENTITIES.register("beehemoth", () -> PlatformService.INSTANCE.createEntityType(BeehemothEntity::new, MobCategory.CREATURE, 1.2F, 16, 3, "beehemoth"));
    public static final RegistryEntry<EntityType<BeeQueenEntity>> BEE_QUEEN = ENTITIES.register("bee_queen", () -> PlatformService.INSTANCE.createEntityType(BeeQueenEntity::new, MobCategory.CREATURE, 2.9F, 16, 3, "bee_queen"));
    public static final RegistryEntry<EntityType<RootminEntity>> ROOTMIN = ENTITIES.register("rootmin", () -> PlatformService.INSTANCE.createEntityType(RootminEntity::new, MobCategory.MONSTER, 1F, 1.56f, 0.985f, 8, 3, "rootmin"));
    public static final RegistryEntry<EntityType<SentryWatcherEntity>> SENTRY_WATCHER = ENTITIES.register("sentry_watcher", () -> PlatformService.INSTANCE.createEntityType(SentryWatcherEntity::new, MobCategory.MISC, 1.99F, 1.45f, 16, 3, "sentry_watcher"));
    public static final RegistryEntry<EntityType<PollenPuffEntity>> POLLEN_PUFF_ENTITY = ENTITIES.register("pollen_puff", () -> PlatformService.INSTANCE.createEntityType(PollenPuffEntity::new, MobCategory.MISC, 0.25F, 0.25F, 0.125f, 4, 10, "pollen_puff"));
    public static final RegistryEntry<EntityType<DirtPelletEntity>> DIRT_PELLET_ENTITY = ENTITIES.register("dirt_pellet", () -> PlatformService.INSTANCE.createEntityType(DirtPelletEntity::new, MobCategory.MISC, 0.5F, 0.5F, 0.15f, 4, 10, "dirt_pellet"));
    public static final RegistryEntry<EntityType<ThrownStingerSpearEntity>> THROWN_STINGER_SPEAR_ENTITY = ENTITIES.register("thrown_stinger_spear", () -> PlatformService.INSTANCE.createEntityType(ThrownStingerSpearEntity::new, MobCategory.MISC, 0.5F, 4, 20, "thrown_stinger_spear"));
    public static final RegistryEntry<EntityType<BeeStingerEntity>> BEE_STINGER_ENTITY = ENTITIES.register("bee_stinger", () -> PlatformService.INSTANCE.createEntityType(BeeStingerEntity::new, MobCategory.MISC, 0.5F, 4, 20, "bee_stinger"));
    public static final RegistryEntry<EntityType<HoneyCrystalShardEntity>> HONEY_CRYSTAL_SHARD = ENTITIES.register("honey_crystal_shard", () -> PlatformService.INSTANCE.createEntityType(HoneyCrystalShardEntity::new, MobCategory.MISC, 0.5F, 4, 20, "honey_crystal_shard"));
    public static final RegistryEntry<EntityType<ElectricRingEntity>> ELECTRIC_RING_ENTITY = ENTITIES.register("electric_ring_entity", () -> PlatformService.INSTANCE.createEntityType(ElectricRingEntity::new, MobCategory.MISC, 3F, 3F, 1.5F, 4, 20, "electric_ring_entity"));
    public static final RegistryEntry<EntityType<PurpleSpikeEntity>> PURPLE_SPIKE_ENTITY = ENTITIES.register("purple_spike_entity", () -> PlatformService.INSTANCE.createEntityType(PurpleSpikeEntity::new, MobCategory.MISC, 1F, 1F, 0F, 4, 20, "purple_spike_entity"));
    public static final RegistryEntry<EntityType<CosmicCrystalEntity>> COSMIC_CRYSTAL_ENTITY = ENTITIES.register("cosmic_crystal_entity", () -> PlatformService.INSTANCE.createEntityType(CosmicCrystalEntity::new, MobCategory.MISC, 1F, 2f, 1f, 8, 3, "cosmic_crystal_entity"));

    public static final EntityDataSerializer<CosmicCrystalState> COSMIC_CRYSTAL_STATE_SERIALIZER = EntityDataSerializer.forValueType(CosmicCrystalState.STREAM_CODEC);
    public static final EntityDataSerializer<BeeQueenState> QUEEN_POSE_SERIALIZER = EntityDataSerializer.forValueType(BeeQueenState.STREAM_CODEC);
    public static final EntityDataSerializer<RootminState> ROOTMIN_POSE_SERIALIZER = EntityDataSerializer.forValueType(RootminState.STREAM_CODEC);
    public static final EntityDataSerializer<Optional<UUID>> UUID_ENTITY_DATA_SERIALIZER = EntityDataSerializer.forValueType(UUIDUtil.STREAM_CODEC.apply(ByteBufCodecs::optional));

    public static void registerEntitySpawnRestrictions(BzRegisterSpawnPlacementsEvent event) {
        event.register(HONEY_SLIME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
        event.register(BEEHEMOTH.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BeehemothEntity::checkMobSpawnRules);
        event.register(BEE_QUEEN.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BeeQueenEntity::checkMobSpawnRules);
        event.register(ROOTMIN.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
        event.register(VARIANT_BEE.get(), SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (beeEntityType, serverLevelAccessor, EntitySpawnReason, blockPos, randomSource) -> true);
        event.register(EntityType.BEE, SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (beeEntityType, serverLevelAccessor, EntitySpawnReason, blockPos, randomSource) -> true);
    }

    public static void registerEntityAttributes(BzRegisterEntityAttributesEvent event) {
        event.register(VARIANT_BEE.get(), Bee.createAttributes());
        event.register(HONEY_SLIME.get(), HoneySlimeEntity.getAttributeBuilder());
        event.register(BEEHEMOTH.get(), BeehemothEntity.getAttributeBuilder());
        event.register(BEE_QUEEN.get(), BeeQueenEntity.getAttributeBuilder());
        event.register(ROOTMIN.get(), RootminEntity.getAttributeBuilder());
        event.register(COSMIC_CRYSTAL_ENTITY.get(), CosmicCrystalEntity.getAttributeBuilder());
    }
}
