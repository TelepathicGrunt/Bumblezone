package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeeQueenEntity;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import com.telepathicgrunt.the_bumblezone.entities.mobs.HoneySlimeEntity;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.BeeStingerEntity;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.HoneyCrystalShardEntity;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.PollenPuffEntity;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.ThrownStingerSpearEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import org.quiltmc.qsl.entity.api.QuiltEntityTypeBuilder;
import org.quiltmc.qsl.entity.networking.api.tracked_data.QuiltTrackedDataHandlerRegistry;

public class BzEntities {

    public static final EntityType<HoneySlimeEntity> HONEY_SLIME = QuiltEntityTypeBuilder
            .<HoneySlimeEntity>createMob()
            .spawnGroup(MobCategory.CREATURE)
            .entityFactory(HoneySlimeEntity::new)
            .defaultAttributes(HoneySlimeEntity.getAttributeBuilder())
            .setDimensions(EntityDimensions.fixed(1F, 1F))
            .maxChunkTrackingRange(8)
            .spawnRestriction(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules)
            .build();

    public static final EntityType<BeehemothEntity> BEEHEMOTH = QuiltEntityTypeBuilder
            .<BeehemothEntity>createMob()
            .spawnGroup(MobCategory.CREATURE)
            .entityFactory(BeehemothEntity::new)
            .defaultAttributes(BeehemothEntity.getAttributeBuilder())
            .setDimensions(EntityDimensions.fixed(1.2F, 1.2F))
            .maxChunkTrackingRange(16)
            .spawnRestriction(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BeehemothEntity::checkMobSpawnRules)
            .build();

    public static final EntityType<BeeQueenEntity> BEE_QUEEN = QuiltEntityTypeBuilder
            .<BeeQueenEntity>createMob()
            .spawnGroup(MobCategory.CREATURE)
            .entityFactory(BeeQueenEntity::new)
            .defaultAttributes(BeeQueenEntity.getAttributeBuilder())
            .setDimensions(EntityDimensions.fixed(2.9F, 2.9F))
            .maxChunkTrackingRange(16)
            .spawnRestriction(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BeeQueenEntity::checkMobSpawnRules)
            .build();

    public static final EntityType<PollenPuffEntity> POLLEN_PUFF_ENTITY = QuiltEntityTypeBuilder
            .<PollenPuffEntity>create(MobCategory.MISC, PollenPuffEntity::new)
            .setDimensions(EntityDimensions.fixed(0.25F, 0.25F))
            .maxChunkTrackingRange(4)
            .maxBlockTrackingRange(10)
            .build();

    public static final EntityType<ThrownStingerSpearEntity> THROWN_STINGER_SPEAR_ENTITY = QuiltEntityTypeBuilder.
            <ThrownStingerSpearEntity>create(MobCategory.MISC, ThrownStingerSpearEntity::new)
            .setDimensions(EntityDimensions.fixed(0.5F, 0.5F))
            .maxChunkTrackingRange(4)
            .maxBlockTrackingRange(20)
            .build();

    public static final EntityType<BeeStingerEntity> BEE_STINGER_ENTITY = QuiltEntityTypeBuilder.
            <BeeStingerEntity>create(MobCategory.MISC, BeeStingerEntity::new)
            .setDimensions(EntityDimensions.fixed(0.5F, 0.5F))
            .maxChunkTrackingRange(4)
            .maxBlockTrackingRange(20)
            .build();

    public static final EntityType<HoneyCrystalShardEntity> HONEY_CRYSTAL_SHARD = QuiltEntityTypeBuilder.
            <HoneyCrystalShardEntity>create(MobCategory.MISC, HoneyCrystalShardEntity::new)
            .setDimensions(EntityDimensions.fixed(0.5F, 0.5F))
            .maxChunkTrackingRange(4)
            .maxBlockTrackingRange(20)
            .build();

    public static void registerEntities() {
        Registry.register(BuiltInRegistries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "honey_slime"), HONEY_SLIME);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "beehemoth"), BEEHEMOTH);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "bee_queen"), BEE_QUEEN);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "pollen_puff"), POLLEN_PUFF_ENTITY);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "thrown_stinger_spear"), THROWN_STINGER_SPEAR_ENTITY);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "bee_stinger"), BEE_STINGER_ENTITY);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "honey_crystal_shard"), HONEY_CRYSTAL_SHARD);

        QuiltTrackedDataHandlerRegistry.register(new ResourceLocation(Bumblezone.MODID, "queen_pose_serializer"), BeeQueenEntity.QUEEN_POSE_SERIALIZER);
    }
}
