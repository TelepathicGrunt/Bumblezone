package net.telepathicgrunt.bumblezone.items;

import net.minecraft.entity.EntityType;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundTag;
import net.telepathicgrunt.bumblezone.entities.BzEntities;

public class HoneySlimeSpawnEgg extends SpawnEggItem {
    public HoneySlimeSpawnEgg(EntityType<?> typeIn, int primaryColorIn, int secondaryColorIn, Settings builder) {
        super(null, primaryColorIn, secondaryColorIn, builder);
    }

    @Override
    public EntityType<?> getEntityType(CompoundTag p_208076_1_) {
        return BzEntities.HONEY_SLIME;
    }
}