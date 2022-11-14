package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.configs.BzConfig;
import com.telepathicgrunt.the_bumblezone.entities.EntityTeleportationHookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;

public class MCDACompat {
    // TODO: Turn this into a generic class that checks collisions with any mob and an item tag for checking armor to trigger teleportation.

    private static final ResourceLocation BEEHIVE_HELMET_RL = new ResourceLocation("mcda", "beehive_armor_helmet");
    private static final ResourceLocation BEEHIVE_CHESTPLATE_RL = new ResourceLocation("mcda", "beehive_armor_chestplate");
    private static final ResourceLocation BEENEST_HELMET_RL = new ResourceLocation("mcda", "beenest_armor_helmet");
    private static final ResourceLocation BEENEST_CHESTPLATE_RL = new ResourceLocation("mcda", "beenest_armor_chestplate");

    public static void setupCompat() {
       // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.mcdaPresent = true;
    }


    public static boolean runTeleportCodeIfBeeHelmetChestplateHit(HitResult hitResult, Projectile pearlEntity) {
        Level world = pearlEntity.level; // world we threw in

        if (!BzConfig.allowEnderpearledMCDABeeHivebeeNestArmorTeleporation) {
            return false;
        }

        return EntityTeleportationHookup.attemptEntityBasedTeleportation(
                hitResult,
                pearlEntity,
                world,
                (entityHitResult) -> hasBeeNestHelmet(entityHitResult.getEntity()),
                (entityHitResult, hitPos) -> {
                    AABB boundBox = entityHitResult.getEntity().getBoundingBox();
                    double minYThreshold = ((boundBox.maxY - boundBox.minY) * 0.33d) + boundBox.minY;
                    return hitPos.y() < minYThreshold;
                }
        );
    }

    public static boolean hasBeeNestHelmet(Entity entity) {
        for(ItemStack stack : entity.getArmorSlots()) {
            if (stack == null) {
                continue;
            }
            ResourceLocation armorRL = Registry.ITEM.getKey(stack.getItem());
            if (armorRL.equals(BEENEST_HELMET_RL) ||
                armorRL.equals(BEENEST_CHESTPLATE_RL) ||
                armorRL.equals(BEEHIVE_HELMET_RL) ||
                armorRL.equals(BEEHIVE_CHESTPLATE_RL))
            {
                return true;
            }
        }
        return false;
    }
}
