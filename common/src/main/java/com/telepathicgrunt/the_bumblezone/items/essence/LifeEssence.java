package com.telepathicgrunt.the_bumblezone.items.essence;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class LifeEssence extends AbilityEssenceItem {

    private static final int cooldownLengthInTicks = 12000;
    private static final int abilityUseAmount = 1000;
    private static final String ABILITY_USE_REMAINING_TAG = "abilityUseRemaining";
    private static final String LAST_ABILITY_CHARGE_TIMESTAMP_TAG = "lastChargeTime";

    public LifeEssence(Properties properties) {
        super(properties, cooldownLengthInTicks);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> components, TooltipFlag tooltipFlag) {
        if (getIsActive(stack)) {
            components.add(Component.translatable("item.the_bumblezone.essence_active"));
            components.add(Component.translatable("item.the_bumblezone.essence_usage", getAbilityUseRemaining(stack), abilityUseAmount));
        }
        else if (getForcedCooldown(stack)) {
            components.add(Component.translatable("item.the_bumblezone.essence_depleted"));
            components.add(Component.translatable("item.the_bumblezone.essence_cooldown", (cooldownLengthInTicks - getCooldownTime(stack)) / 20));
        }
        else {
            components.add(Component.translatable("item.the_bumblezone.essence_ready"));
            components.add(Component.translatable("item.the_bumblezone.essence_usage", getAbilityUseRemaining(stack), abilityUseAmount));
        }
    }

    public static void setAbilityUseRemaining(ItemStack stack, int abilityUseRemaining) {
        stack.getOrCreateTag().putInt(ABILITY_USE_REMAINING_TAG, abilityUseRemaining);
    }

    public static int getAbilityUseRemaining(ItemStack stack) {
        if (!stack.getOrCreateTag().contains(ABILITY_USE_REMAINING_TAG)) {
            setAbilityUseRemaining(stack, abilityUseAmount);
            return abilityUseAmount;
        }

        return stack.getOrCreateTag().getInt(ABILITY_USE_REMAINING_TAG);
    }

    public static void decrementAbilityUseRemaining(ItemStack stack) {
        int getRemainingUse = getAbilityUseRemaining(stack) - 1;
        setAbilityUseRemaining(stack, getRemainingUse);
        if (getRemainingUse == 0) {
            setIsActive(stack, false);
            setForcedCooldown(stack, true);
        }
    }

    public static void setLastAbilityChargeTimestamp(ItemStack stack, int gametime) {
        stack.getOrCreateTag().putInt(LAST_ABILITY_CHARGE_TIMESTAMP_TAG, gametime);
    }

    public static int getLastAbilityChargeTimestamp(ItemStack stack) {
        return stack.getOrCreateTag().getInt(LAST_ABILITY_CHARGE_TIMESTAMP_TAG);
    }

    @Override
    void rechargeAbilitySlowly(ItemStack stack, Level level, ServerPlayer serverPlayer) {
        int abilityUseRemaining = getAbilityUseRemaining(stack);
        if (abilityUseRemaining < abilityUseAmount) {
            int lastChargeTime = getLastAbilityChargeTimestamp(stack);
            if (lastChargeTime == 0 || serverPlayer.tickCount < lastChargeTime) {
                setLastAbilityChargeTimestamp(stack, serverPlayer.tickCount);
            }
            else {
                int timeFromLastCharge = serverPlayer.tickCount - lastChargeTime;
                int chargeTimeIncrement = cooldownLengthInTicks / abilityUseAmount;
                if (timeFromLastCharge % chargeTimeIncrement == 0) {
                    setAbilityUseRemaining(stack, abilityUseRemaining + 1);
                    setLastAbilityChargeTimestamp(stack, serverPlayer.tickCount);
                }
            }
        }
    }

    @Override
    void rechargeAbilityEntirely(ItemStack stack) {
        setAbilityUseRemaining(stack, abilityUseAmount);
    }

    @Override
    void applyAbilityEffects(ItemStack stack, Level level, ServerPlayer serverPlayer) {
        if (((long)serverPlayer.tickCount + serverPlayer.getUUID().getLeastSignificantBits()) % 10L == 0) {
            int radius = 16;
            healFriendlyNearby(stack, level, serverPlayer, radius);
            growNearbyPlants(stack, level, serverPlayer, radius);
        }

        if (getAbilityUseRemaining(stack) <= 0) {
            setForcedCooldown(stack, true);
            setCooldownTime(stack, 0);
            setIsActive(stack, false);
            serverPlayer.getCooldowns().addCooldown(stack.getItem(), cooldownLengthInTicks);
        }
    }

    private static void healFriendlyNearby(ItemStack stack, Level level, ServerPlayer serverPlayer, int radius) {
        List<Entity> entities = level.getEntities(serverPlayer, new AABB(
                serverPlayer.getX() - radius,
                serverPlayer.getY() - radius,
                serverPlayer.getZ() - radius,
                serverPlayer.getX() + radius,
                serverPlayer.getY() + radius,
                serverPlayer.getZ() + radius
        ));

        for (Entity entity : entities) {
            healFriendlyEntity(serverPlayer, entity);

            if (getForcedCooldown(stack)) {
                return;
            }
        }
    }

    private static void healFriendlyEntity(ServerPlayer serverPlayer, Entity entity) {
        if (entity instanceof TamableAnimal tamableAnimal && tamableAnimal.isOwnedBy(serverPlayer)) {
            if (tamableAnimal.getHealth() < tamableAnimal.getMaxHealth()) {
                tamableAnimal.heal(1);
            }
        }
        else if (entity instanceof LivingEntity livingEntity && entity.getTeam() != null && entity.getTeam().isAlliedTo(serverPlayer.getTeam())) {
            if (livingEntity.getHealth() < livingEntity.getMaxHealth()) {
                livingEntity.heal(1);
            }
        }
    }

    private static void growNearbyPlants(ItemStack stack, Level level, ServerPlayer serverPlayer, int radius) {
        if (getForcedCooldown(stack)) {
            return;
        }

        BlockPos playerPos = serverPlayer.blockPosition();
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        ChunkPos currentCachedChunkPos = new ChunkPos(playerPos);
        LevelChunk cachedChunk = null;

        int minX = playerPos.getX() - radius;
        int maxX = playerPos.getX() + radius;
        int minY = Math.max(level.getMinBuildHeight(), playerPos.getY() - radius);
        int maxY = Math.min(level.getMaxBuildHeight(), playerPos.getY() + radius);
        int minZ = playerPos.getZ() - radius;
        int maxZ = playerPos.getZ() + radius;
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {

                if (cachedChunk == null ||
                    currentCachedChunkPos.x != SectionPos.blockToSectionCoord(x) ||
                    currentCachedChunkPos.z != SectionPos.blockToSectionCoord(z))
                {
                    currentCachedChunkPos = new ChunkPos(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z));
                    cachedChunk = level.getChunk(currentCachedChunkPos.x, currentCachedChunkPos.z);
                }

                for (int y = maxY; y >= minY; y--) {
                    mutableBlockPos.set(x, y, z);
                    BlockState state = cachedChunk.getBlockState(mutableBlockPos);

                    growPlantBlock(stack, level, mutableBlockPos, state);
                    if (getForcedCooldown(stack)) {
                        return;
                    }
                }
            }
        }
    }

    private static void growPlantBlock(ItemStack stack, Level level, BlockPos blockPos, BlockState state) {
        if (state.is(BlockTags.BEE_GROWABLES) || state.is(BlockTags.SAPLINGS)) {
            Block block = state.getBlock();
            boolean grewBlock = false;

            if (block instanceof CropBlock cropBlock) {
                if (!cropBlock.isMaxAge(state)) {
                    BlockState newState = cropBlock.getStateForAge(cropBlock.getAge(state) + 1);
                    level.setBlock(blockPos, newState, 3);
                    grewBlock = true;
                }
            }
            else if (block instanceof StemBlock) {
                int age = state.getValue(StemBlock.AGE);
                if (age < 7) {
                    BlockState newState = state.setValue(StemBlock.AGE, age + 1);
                    level.setBlock(blockPos, newState, 3);
                    grewBlock = true;
                }
            }
            else if (state.is(Blocks.SWEET_BERRY_BUSH)) {
                int age = state.getValue(SweetBerryBushBlock.AGE);
                if (age < 3) {
                    BlockState newState = state.setValue(SweetBerryBushBlock.AGE, age + 1);
                    level.setBlock(blockPos, newState, 3);
                    grewBlock = true;
                }
            }
            else if (block instanceof BonemealableBlock bonemealableBlock &&
                bonemealableBlock.isValidBonemealTarget(level, blockPos, state, false))
            {
                bonemealableBlock.performBonemeal((ServerLevel) level, level.getRandom(), blockPos, state);
                grewBlock = true;
            }

            if (grewBlock) {
                decrementAbilityUseRemaining(stack);
            }
        }
    }
}