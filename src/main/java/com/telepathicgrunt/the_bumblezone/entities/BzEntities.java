package com.telepathicgrunt.the_bumblezone.entities;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.HoneySlimeEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class BzEntities
{
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Bumblezone.MODID);
	
    public static final RegistryObject<EntityType<HoneySlimeEntity>> HONEY_SLIME = createEntity("honey_slime", () -> EntityType.Builder.<HoneySlimeEntity>create(HoneySlimeEntity::new, EntityClassification.CREATURE).size(0.6F, 1.99F).maxTrackingRange(8).build("honey_slime"));
    
    private static <E extends EntityType<?>> RegistryObject<E> createEntity(String name, Supplier<E> entity)
    {
		return ENTITIES.register(name, entity);
	}
    
    public static void registerAdditionalEntityInformation()
    {
    	registerEntitySpawnRestrictions();
    	registerEntityAttributes();
    }

    private static void registerEntitySpawnRestrictions() {
        EntitySpawnPlacementRegistry.register(HONEY_SLIME.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
    }

    private static void registerEntityAttributes() {
        GlobalEntityTypeAttributes.put(HONEY_SLIME.get(), HoneySlimeEntity.getAttributeBuilder().build());
    }
}
