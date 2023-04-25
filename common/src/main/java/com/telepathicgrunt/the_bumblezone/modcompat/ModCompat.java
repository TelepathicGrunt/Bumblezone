package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.events.entity.EntitySpawnEvent;
import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.HitResult;

import java.util.EnumSet;
import java.util.function.Predicate;

public interface ModCompat {

    /**
     * Checked when registering the mod compat to see what events to register.
     */
    default EnumSet<Type> compatTypes() {
        return EnumSet.noneOf(Type.class);
    }

    /**
     * Called when a bee spawns in the world.
     */
    default boolean onBeeSpawn(EntitySpawnEvent event, boolean isBaby) {
        return false;
    }

    /**
     * Called when a player interacts with an Empty Honeycomb Brood.
     */
    default InteractionResult onEmptyBroodInteract(ItemStack itemstack, Player playerEntity, InteractionHand playerHand) {
        return InteractionResult.PASS;
    }

    /**
     * Used for pre-checking if a comb can be gotten from the compat.
     * Should be used to check for randomness and such.
     */
    default boolean checkCombSpawn(BlockPos pos, RandomSource random, LevelReader level, boolean spiderDungeon) {
        return false;
    }

    /**
     * Called when {@link ModCompat#checkCombSpawn} succeeds.
     */
    default StructureTemplate.StructureBlockInfo getHoneycomb(BlockPos pos, RandomSource random, LevelReader level, boolean spiderDungeon) {
        return null;
    }

    /**
     * Called when an entity is spawned in The Bumblezone.
     */
    default void onEntitySpawnInDimension(Entity entity) {

    }

    /**
     * Called to check if a block is valid for Bumblezone teleportation.
     */
    default boolean isValidBeeHiveForTeleportation(BlockState state) {
        return false;
    }

    default OptionalBoolean validateCombType(CompoundTag tag) {
        return OptionalBoolean.EMPTY;
    }

    default boolean isHostBee(Entity entity) {
        return false;
    }

    default boolean isTeleportHandled(HitResult hitResult, Entity owner, Projectile projectile) {
        return false;
    }

    default int getNumberOfMatchingEquippedItemsInCustomSlots(Entity entity, Predicate<ItemStack> itemStackPredicate) {
        return 0;
    }

    default boolean isItemExplicitlyDisallowedFromBeeGearBoosting(ItemStack itemStack) {
        return false;
    }

    enum Type {
        SPAWNS,
        EMPTY_BROOD,
        COMBS,
        DIMENSION_SPAWN,
        BLOCK_TELEPORT,
        COMB_ORE,
        HAS_HOST_BEES,
        PROJECTILE_IMPACT_HANDLED,
        CUSTOM_EQUIPMENT_SLOTS,
        BEE_GEAR_BOOSTING,
    }
}
