package com.telepathicgrunt.the_bumblezone.world.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.ArrayList;
import java.util.List;


public class HangingGardenMob extends Feature<NoneFeatureConfiguration> {

    public HangingGardenMob(Codec<NoneFeatureConfiguration> code) {
        super(code);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {

        EntityType<?> entityToSpawn = context.random().nextFloat() < 0.3f ? EntityType.BEE : null;
        if (entityToSpawn == null) {
            List<EntityType<?>> spawnableExtraEntities = new ArrayList<>();
            Iterable<Holder<EntityType<?>>> holderIterable = Registry.ENTITY_TYPE.getTagOrEmpty(BzTags.ENDERPEARL_TARGET_ENTITY_HIT_ANYWHERE);
            holderIterable.forEach(h -> spawnableExtraEntities.add(h.get()));
            if (!spawnableExtraEntities.isEmpty()) {
                entityToSpawn = spawnableExtraEntities.get(context.random().nextInt(spawnableExtraEntities.size()));
            }
        }

        if (entityToSpawn == null) {
            return false;
        }

        Entity spawningEntity = entityToSpawn.create(context.level().getLevel());

        if (spawningEntity instanceof Mob mob) {
            mob.finalizeSpawn(context.level(), context.level().getCurrentDifficultyAt(context.origin()), MobSpawnType.STRUCTURE, null, null);
            mob.setPersistenceRequired();
        }

        spawningEntity.moveTo(
                (double)context.origin().getX() + 0.5D,
                context.origin().getY(),
                (double)context.origin().getZ() + 0.5D,
                0.0F,
                0.0F);

        context.level().addFreshEntityWithPassengers(spawningEntity);
        return true;
    }

}