package net.telepathicgrunt.bumblezone.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.entities.mobs.HoneySlimeEntity;

public class BzEntities {

    public static final EntityType<HoneySlimeEntity> HONEY_SLIME = EntityType.Builder.<HoneySlimeEntity>create(HoneySlimeEntity::new, SpawnGroup.CREATURE).setDimensions(0.6F, 1.99F).maxTrackingRange(8).build("honey_slime");

    public static void registerEntities() {
        Registry.register(Registry.ENTITY_TYPE, new Identifier(Bumblezone.MODID, "honey_slime"), HONEY_SLIME);
    }
}
