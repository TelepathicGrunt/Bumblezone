package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.entities.EntityTeleportationHookup;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.HitResult;

/**
 * For mods that want to use Bumblezone methods but would like to make sure it doesn't get changed over time and break the dependent mods.
 * Idk what methods to put here lol. Just contact me if there are methods you want exposed to go here.
 *
 * Basically these methods and class path will not change making this class and its methods safer to use in other mods.
 */
public class BumblezoneAPI {

    // Requiem mod uses this method
    /**
     * Will teleport the given entity out of Bumblezone to the last dimension they came from.
     * Position is determined by the entity's Bumblezone position scaled like how the Nether is 8:1.
     * If the entity does not have a previous dimension stored, the Overworld will be the destination instead.
     */
    public static void teleportOutOfBz(LivingEntity livingEntity) {
        EntityTeleportationHookup.teleportOutOfBz(livingEntity);
    }

    /**
     * Will check if the projectile entity hit a beehive or bee nest and teleport the thrower to Bumblezone.
     * Please only call this for throwables that teleport the entity like Enderpearls.
     * @return - Whether the projectile hit a beehive or bee nest.
     */
    public static boolean runEnderpearlImpact(HitResult hitResult, Projectile pearlEntity) {
        return EntityTeleportationHookup.runEnderpearlImpact(hitResult, pearlEntity);
    }

    /**
     * Will check if entity is pushed by piston into a beehive or bee nest and teleport to Bumblezone if so.
     */
    public static void runPistonPushed(Direction direction, LivingEntity livingEntity) {
        EntityTeleportationHookup.runPistonPushed(direction, livingEntity);
    }
}
