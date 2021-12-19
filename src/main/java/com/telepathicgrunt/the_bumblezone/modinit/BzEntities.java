package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import com.telepathicgrunt.the_bumblezone.entities.mobs.HoneySlimeEntity;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.PollenPuffEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BzEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Bumblezone.MODID);

    protected static final EntityType<HoneySlimeEntity> HONEY_SLIME_RAW = EntityType.Builder.<HoneySlimeEntity>of(HoneySlimeEntity::new, MobCategory.CREATURE).sized(0.6F, 1.99F).clientTrackingRange(8).build("honey_slime");
    public static final RegistryObject<EntityType<HoneySlimeEntity>> HONEY_SLIME = ENTITIES.register("honey_slime", () -> HONEY_SLIME_RAW);
    protected static final EntityType<BeehemothEntity> BEEHEMOTH_RAW = EntityType.Builder.of(BeehemothEntity::new, MobCategory.CREATURE).sized(1.2F, 1.2F).clientTrackingRange(16).build("beehemoth");
    public static final RegistryObject<EntityType<BeehemothEntity>> BEEHEMOTH = ENTITIES.register("beehemoth", () -> BEEHEMOTH_RAW);
    public static final RegistryObject<EntityType<PollenPuffEntity>> POLLEN_PUFF_ENTITY = ENTITIES.register("pollen_puff", () -> EntityType.Builder.<PollenPuffEntity>of(PollenPuffEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build("pollen_puff"));

    public static void registerAdditionalEntityInformation() {
        registerEntitySpawnRestrictions();
    }

    private static void registerEntitySpawnRestrictions() {
        SpawnPlacements.register(HONEY_SLIME.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
        SpawnPlacements.register(BEEHEMOTH.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BeehemothEntity::checkMobSpawnRules);
    }

    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(HONEY_SLIME.get(), HoneySlimeEntity.getAttributeBuilder().build());
        event.put(BEEHEMOTH.get(), BeehemothEntity.getAttributeBuilder().build());
    }
}
