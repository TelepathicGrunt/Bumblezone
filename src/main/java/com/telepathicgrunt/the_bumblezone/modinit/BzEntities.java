package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import com.telepathicgrunt.the_bumblezone.entities.mobs.HoneySlimeEntity;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.PollenPuffEntity;
import com.telepathicgrunt.the_bumblezone.mixin.entities.SpawnPlacementsAccessor;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;

public class BzEntities {

    public static final EntityType<HoneySlimeEntity> HONEY_SLIME = FabricEntityTypeBuilder.<HoneySlimeEntity>create(MobCategory.CREATURE, HoneySlimeEntity::new).dimensions(EntityDimensions.fixed(0.6F, 1.99F)).trackRangeChunks(8).build();
    public static final EntityType<BeehemothEntity> BEEHEMOTH = FabricEntityTypeBuilder.create(MobCategory.CREATURE, BeehemothEntity::new).dimensions(EntityDimensions.fixed(1.2F, 1.2F)).trackRangeChunks(16).build();
    public static final EntityType<PollenPuffEntity> POLLEN_PUFF_ENTITY = FabricEntityTypeBuilder.<PollenPuffEntity>create(MobCategory.MISC, PollenPuffEntity::new).dimensions(EntityDimensions.fixed(0.25F, 0.25F)).trackRangeChunks(4).trackedUpdateRate(10).build();

    public static void registerEntities() {
        Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "honey_slime"), HONEY_SLIME);
        Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "beehemoth"), BEEHEMOTH);
        Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "pollen_puff"), POLLEN_PUFF_ENTITY);
        registerEntitySpawnRestrictions();
        registerEntityAttributes();
    }

    private static void registerEntitySpawnRestrictions() {
        SpawnPlacementsAccessor.thebumblezone_invokeRegister(HONEY_SLIME, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
        SpawnPlacementsAccessor.thebumblezone_invokeRegister(BEEHEMOTH, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BeehemothEntity::checkMobSpawnRules);
    }

    private static void registerEntityAttributes() {
        FabricDefaultAttributeRegistry.register(HONEY_SLIME, HoneySlimeEntity.getAttributeBuilder());
        FabricDefaultAttributeRegistry.register(BEEHEMOTH, BeehemothEntity.getAttributeBuilder());
    }
}
