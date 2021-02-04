package net.telepathicgrunt.bumblezone.entities;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.SharedConstants;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.entities.mobs.HoneySlimeEntity;
import net.telepathicgrunt.bumblezone.mixin.entities.SpawnRestrictionAccessor;

public class BzEntities {

    public static final EntityType<HoneySlimeEntity> HONEY_SLIME;
    static {
        // turns off "No data fixer registered for honey_slime" log spam just for our entity.
        boolean datafixer = SharedConstants.useChoiceTypeRegistrations;
        SharedConstants.useChoiceTypeRegistrations = false;
        HONEY_SLIME = EntityType.Builder.<HoneySlimeEntity>create(HoneySlimeEntity::new, SpawnGroup.CREATURE).setDimensions(0.6F, 1.99F).maxTrackingRange(8).build("honey_slime");
        SharedConstants.useChoiceTypeRegistrations = datafixer;
    }

    public static void registerEntities() {
        Registry.register(Registry.ENTITY_TYPE, new Identifier(Bumblezone.MODID, "honey_slime"), HONEY_SLIME);

        registerEntitySpawnRestrictions();
        registerEntityAttributes();
    }

    private static void registerEntitySpawnRestrictions(){
        SpawnRestrictionAccessor.bz_invokeRegister(HONEY_SLIME, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canMobSpawn);
    }

    private static void registerEntityAttributes(){
        FabricDefaultAttributeRegistry.register(HONEY_SLIME, HoneySlimeEntity.getAttributeBuilder());
    }
}
