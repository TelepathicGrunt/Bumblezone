package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.HoneySlimeEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BzEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Bumblezone.MODID);

    protected static final EntityType<HoneySlimeEntity> HONEY_SLIME_RAW = EntityType.Builder.<HoneySlimeEntity>of(HoneySlimeEntity::new, EntityClassification.CREATURE).sized(0.6F, 1.99F).clientTrackingRange(8).build("honey_slime");
    public static final RegistryObject<EntityType<HoneySlimeEntity>> HONEY_SLIME = ENTITIES.register("honey_slime", () -> HONEY_SLIME_RAW);

    public static void registerAdditionalEntityInformation() {
        registerEntitySpawnRestrictions();
    }

    private static void registerEntitySpawnRestrictions() {
        EntitySpawnPlacementRegistry.register(HONEY_SLIME.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::checkMobSpawnRules);
    }

    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(HONEY_SLIME.get(), HoneySlimeEntity.getAttributeBuilder().build());
    }
}
