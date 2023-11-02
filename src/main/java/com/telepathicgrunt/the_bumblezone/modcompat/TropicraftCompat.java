package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;

import java.util.Optional;

public class TropicraftCompat {

    protected static Optional<EntityType<?>> TROPIBEE;

    public static void setupCompat() {
        TROPIBEE = Registry.ENTITY_TYPE.getOptional(new ResourceLocation("tropicraft", "tropibee"));

        // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.tropicraftPresent = true;
    }

    public static boolean TropicMobSpawnEvent(LivingSpawnEvent.CheckSpawn event, boolean isChild, MobSpawnType spawnReason) {
        if (TROPIBEE.isEmpty()) {
            return false;
        }

        if (spawnReason == MobSpawnType.DISPENSER && !BzModCompatibilityConfigs.allowTropicraftSpawnFromDispenserFedBroodBlock.get()) {
            return false;
        }

        Mob entity = event.getEntity();
        LevelAccessor world = event.getLevel();

        Entity newEntity = TROPIBEE.get().create(entity.getLevel());
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
