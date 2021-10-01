package com.telepathicgrunt.bumblezone.modinit;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.entities.mobs.HoneySlimeEntity;
import com.telepathicgrunt.bumblezone.entities.nonliving.PollenPuffEntity;
import com.telepathicgrunt.bumblezone.mixin.entities.SpawnRestrictionAccessor;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;

public class BzEntities {

    public static final EntityType<HoneySlimeEntity> HONEY_SLIME = EntityType.Builder.<HoneySlimeEntity>create(HoneySlimeEntity::new, SpawnGroup.CREATURE).setDimensions(0.6F, 1.99F).maxTrackingRange(8).build("honey_slime");
    public static final EntityType<PollenPuffEntity> POLLEN_PUFF_ENTITY = EntityType.Builder.<PollenPuffEntity>create(PollenPuffEntity::new, SpawnGroup.MISC).setDimensions(0.25F, 0.25F).maxTrackingRange(4).trackingTickInterval(10).build("pollen_puff");

    public static void registerEntities() {
        Registry.register(Registry.ENTITY_TYPE, new Identifier(Bumblezone.MODID, "honey_slime"), HONEY_SLIME);
        Registry.register(Registry.ENTITY_TYPE, new Identifier(Bumblezone.MODID, "pollen_puff"), POLLEN_PUFF_ENTITY);

        registerEntitySpawnRestrictions();
        registerEntityAttributes();
    }

    private static void registerEntitySpawnRestrictions(){
        SpawnRestrictionAccessor.thebumblezone_invokeRegister(HONEY_SLIME, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canMobSpawn);
    }

    private static void registerEntityAttributes(){
        FabricDefaultAttributeRegistry.register(HONEY_SLIME, HoneySlimeEntity.getAttributeBuilder());
    }
}
