package com.telepathicgrunt.the_bumblezone.modcompat.neoforge;

import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.events.entity.EntitySpawnEvent;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;

import java.util.EnumSet;
import java.util.Optional;

public class TropicraftCompat implements ModCompat {

    protected static Optional<EntityType<?>> TROPIBEE;

    public TropicraftCompat() {
        TROPIBEE = BuiltInRegistries.ENTITY_TYPE.getOptional(ResourceLocation.fromNamespaceAndPath("tropicraft", "tropibee"));

        // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.tropicraftPresent = true;
    }

    @Override
    public EnumSet<Type> compatTypes() {
        return EnumSet.of(Type.SPAWNS);
    }

    @Override
    public boolean onBeeSpawn(EntitySpawnEvent event, boolean isChild) {
        if (!BzModCompatibilityConfigs.spawnTropicraftBeesMob || TROPIBEE.isEmpty()) {
            return false;
        }

        if (event.entity().getRandom().nextFloat() >= BzModCompatibilityConfigs.spawnrateOfTropicraftBeesMobs) {
            return false;
        }

        if (event.spawnType() == MobSpawnType.DISPENSER && !BzModCompatibilityConfigs.allowTropicraftSpawnFromDispenserFedBroodBlock) {
            return false;
        }

        Mob entity = event.entity();
        LevelAccessor world = event.level();

        Entity newEntity = TROPIBEE.get().create(entity.level());
        if (!(newEntity instanceof Mob tropibee)) {
            return false;
        }

        tropibee.moveTo(
                entity.getX(),
                entity.getY(),
                entity.getZ(),
                tropibee.getRandom().nextFloat() * 360.0F,
                0.0F);

        tropibee.setBaby(isChild);
        world.addFreshEntity(tropibee);
        return true;
    }
}
