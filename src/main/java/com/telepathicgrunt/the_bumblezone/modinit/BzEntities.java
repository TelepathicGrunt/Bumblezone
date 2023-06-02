package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeeQueenEntity;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import com.telepathicgrunt.the_bumblezone.entities.mobs.HoneySlimeEntity;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.BeeStingerEntity;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.HoneyCrystalShardEntity;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.PollenPuffEntity;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.ThrownStingerSpearEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import org.quiltmc.qsl.entity.networking.api.tracked_data.QuiltTrackedDataHandlerRegistry;

public class BzEntities {

    public static final EntityType<HoneySlimeEntity> HONEY_SLIME = FabricEntityTypeBuilder
            .<HoneySlimeEntity>createMob()
            .spawnGroup(MobCategory.CREATURE)
            .entityFactory(HoneySlimeEntity::new)
            .defaultAttributes(HoneySlimeEntity::getAttributeBuilder)
            .dimensions(EntityDimensions.scalable(1F, 1F))
            .trackRangeChunks(8)
            .spawnRestriction(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules)
            .build();

    public static final EntityType<BeehemothEntity> BEEHEMOTH = FabricEntityTypeBuilder
            .<BeehemothEntity>createMob()
            .spawnGroup(MobCategory.CREATURE)
            .entityFactory(BeehemothEntity::new)
            .defaultAttributes(BeehemothEntity::getAttributeBuilder)
            .dimensions(EntityDimensions.fixed(1.2F, 1.2F))
            .trackRangeChunks(16)
            .spawnRestriction(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BeehemothEntity::checkMobSpawnRules)
            .build();

    public static final EntityType<BeeQueenEntity> BEE_QUEEN = FabricEntityTypeBuilder
            .<BeeQueenEntity>createMob()
            .spawnGroup(MobCategory.CREATURE)
            .entityFactory(BeeQueenEntity::new)
            .defaultAttributes(BeeQueenEntity::getAttributeBuilder)
            .dimensions(EntityDimensions.fixed(2.9F, 2.9F))
            .trackRangeChunks(16)
            .spawnRestriction(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BeeQueenEntity::checkMobSpawnRules)
            .build();

    public static final EntityType<PollenPuffEntity> POLLEN_PUFF_ENTITY = FabricEntityTypeBuilder
            .<PollenPuffEntity>create(MobCategory.MISC, PollenPuffEntity::new)
            .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
            .trackRangeChunks(4)
            .trackedUpdateRate(10)
            .build();

    public static final EntityType<ThrownStingerSpearEntity> THROWN_STINGER_SPEAR_ENTITY = FabricEntityTypeBuilder.
            <ThrownStingerSpearEntity>create(MobCategory.MISC, ThrownStingerSpearEntity::new)
            .dimensions(EntityDimensions.fixed(0.5F, 0.5F))
            .trackRangeChunks(4)
            .trackedUpdateRate(20)
            .build();

    public static final EntityType<BeeStingerEntity> BEE_STINGER_ENTITY = FabricEntityTypeBuilder.
            <BeeStingerEntity>create(MobCategory.MISC, BeeStingerEntity::new)
            .dimensions(EntityDimensions.fixed(0.5F, 0.5F))
            .trackRangeChunks(4)
            .trackedUpdateRate(20)
            .build();

    public static final EntityType<HoneyCrystalShardEntity> HONEY_CRYSTAL_SHARD = FabricEntityTypeBuilder.
            <HoneyCrystalShardEntity>create(MobCategory.MISC, HoneyCrystalShardEntity::new)
            .dimensions(EntityDimensions.fixed(0.5F, 0.5F))
            .trackRangeChunks(4)
            .trackedUpdateRate(20)
            .build();

    public static void registerEntities() {
        Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "honey_slime"), HONEY_SLIME);
        Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "beehemoth"), BEEHEMOTH);
        Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "bee_queen"), BEE_QUEEN);
        Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "pollen_puff"), POLLEN_PUFF_ENTITY);
        Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "thrown_stinger_spear"), THROWN_STINGER_SPEAR_ENTITY);
        Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "bee_stinger"), BEE_STINGER_ENTITY);
        Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "honey_crystal_shard"), HONEY_CRYSTAL_SHARD);

        QuiltTrackedDataHandlerRegistry.register(new ResourceLocation(Bumblezone.MODID, "queen_pose_serializer"), BeeQueenEntity.QUEEN_POSE_SERIALIZER);
    }
}
