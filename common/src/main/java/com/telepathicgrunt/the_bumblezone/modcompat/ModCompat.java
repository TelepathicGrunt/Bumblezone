package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.events.entity.EntitySpawnEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.EnumSet;
import java.util.Optional;

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

    default Optional<Object> getCombData(Block block, RandomSource random) {
        return Optional.empty();
    }

    default boolean placeCombOre(BlockPos.MutableBlockPos pos, ChunkAccess chunk, Object nbt, OreConfiguration.TargetBlockState target, Block block) {
        return false;
    }

    enum Type {
        SPAWNS,
        EMPTY_BROOD,
        COMBS,
        DIMENSION_SPAWN,
        BLOCK_TELEPORT,
        COMB_ORE,
        REGISTRIES
    }
}
