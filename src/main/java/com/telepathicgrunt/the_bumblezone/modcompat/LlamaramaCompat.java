package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.configs.BzConfig;
import com.telepathicgrunt.the_bumblezone.entities.EntityTeleportationHookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;

public class LlamaramaCompat {

    private static final ResourceLocation BUMBLE_LLAMA_RL = new ResourceLocation("llamarama", "bumble_llama");

    public static void setupCompat() {
       // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.llamaramaPresent = true;
    }

    public static boolean runTeleportCodeIfBumbleLlamaHitHigh(HitResult hitResult, Projectile pearlEntity) {
        Level world = pearlEntity.level; // world we threw in

        if (!BzConfig.allowEnderpearledLlamaramaBumbleLlamaTeleporation) {
            return false;
        }

        return EntityTeleportationHookup.attemptEntityBasedTeleportation(
                hitResult,
                pearlEntity,
                world,
                (entityHitResult) -> Registry.ENTITY_TYPE.getKey(entityHitResult.getEntity().getType()).equals(BUMBLE_LLAMA_RL),
                (entityHitResult, hitPos) -> {
                    AABB boundBox = entityHitResult.getEntity().getBoundingBox();
                    double minYThreshold = ((boundBox.maxY - boundBox.minY) * 0.66d) + boundBox.minY;
                    return hitPos.y() < minYThreshold;
                }
        );
    }
}
