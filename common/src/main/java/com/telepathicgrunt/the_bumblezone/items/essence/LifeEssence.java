package com.telepathicgrunt.the_bumblezone.items.essence;

import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class LifeEssence extends AbilityEssenceItem {

    private static final Supplier<Integer> cooldownLengthInTicks = () -> BzGeneralConfigs.lifeEssenceCooldown;
    private static final Supplier<Integer> abilityUseAmount = () -> BzGeneralConfigs.lifeEssenceAbilityUse;

    public LifeEssence(Properties properties) {
        super(properties, cooldownLengthInTicks, abilityUseAmount);
    }

    @Override
    public int getColor() {
        return 0x4DE52B;
    }

    @Override
    void addDescriptionComponents(List<Component> components) {
        components.add(Component.translatable("item.the_bumblezone.essence_life_description_1").withStyle(ChatFormatting.GREEN).withStyle(ChatFormatting.ITALIC));
        components.add(Component.translatable("item.the_bumblezone.essence_life_description_2").withStyle(ChatFormatting.GREEN).withStyle(ChatFormatting.ITALIC));
    }

    public void decrementAbilityUseRemaining(ItemStack stack, ServerPlayer serverPlayer, int amount) {
        int getRemainingUse = getAbilityUseRemaining(stack) - amount;
        setAbilityUseRemaining(stack, getRemainingUse);
        if (getRemainingUse == 0) {
            setDepleted(stack, serverPlayer, false);
        }
    }

    @Override
    public void applyAbilityEffects(ItemStack stack, Level level, ServerPlayer serverPlayer) {
        if (((long)serverPlayer.tickCount + serverPlayer.getUUID().getLeastSignificantBits()) % 10L == 0 &&
            level instanceof ServerLevel serverLevel &&
            getIsActive(stack))
        {
            int radius = 16;
            healFriendlyNearby(stack, serverLevel, serverPlayer, radius);
            growNearbyPlants(stack, serverLevel, serverPlayer, radius);
        }
    }

    private void healFriendlyNearby(ItemStack stack, Level level, ServerPlayer serverPlayer, int radius) {
        List<Entity> entities = level.getEntities(serverPlayer, new AABB(
                serverPlayer.getX() - radius,
                serverPlayer.getY() - radius,
                serverPlayer.getZ() - radius,
                serverPlayer.getX() + radius,
                serverPlayer.getY() + radius,
                serverPlayer.getZ() + radius
        ));

        for (Entity entity : entities) {
            healFriendlyEntity(stack, serverPlayer, entity);

            if (getForcedCooldown(stack)) {
                return;
            }
        }
    }

    private void healFriendlyEntity(ItemStack stack, ServerPlayer serverPlayer, Entity entity) {
        if (entity instanceof TamableAnimal tamableAnimal && tamableAnimal.isOwnedBy(serverPlayer)) {
            cureEntityOfEffects(stack, serverPlayer, tamableAnimal);
        }
        else if (entity instanceof LivingEntity livingEntity && entity.getTeam() != null && entity.getTeam().isAlliedTo(serverPlayer.getTeam())) {
            cureEntityOfEffects(stack, serverPlayer, livingEntity);
        }
    }

    private void cureEntityOfEffects(ItemStack stack, ServerPlayer serverPlayer, LivingEntity livingEntity) {
        if (livingEntity.getHealth() < livingEntity.getMaxHealth()) {
            livingEntity.heal(1);
            spawnParticles(serverPlayer.serverLevel(), serverPlayer.position(), serverPlayer.getRandom());
            decrementAbilityUseRemaining(stack, serverPlayer, 1);
        }
        for (MobEffectInstance effect : new ArrayList<>(livingEntity.getActiveEffects())) {
            if (GeneralUtils.isInTag(BuiltInRegistries.MOB_EFFECT, BzTags.LIFE_CURE_EFFECTS, effect.getEffect())) {
                livingEntity.removeEffect(effect.getEffect());
                decrementAbilityUseRemaining(stack, serverPlayer, 1);
            }
        }
    }

    private void growNearbyPlants(ItemStack stack, ServerLevel level, ServerPlayer serverPlayer, int radius) {
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

                    growPlantBlock(stack, level, serverPlayer, mutableBlockPos, state);
                    if (getForcedCooldown(stack)) {
                        return;
                    }
                }
            }
        }
    }

    private void growPlantBlock(ItemStack stack, ServerLevel level, ServerPlayer serverPlayer, BlockPos blockPos, BlockState state) {
        if (state.is(BzTags.GROW_PLANTS) && !state.is(BzTags.FORCE_DISALLOWED_GROW_PLANT)) {
            Block block = state.getBlock();
            boolean grewBlock = false;

            if (!GeneralUtils.isPermissionAllowedAtSpot(level, serverPlayer, blockPos, true)) {
                return;
            }

            if (block instanceof CropBlock cropBlock) {
                if (!cropBlock.isMaxAge(state)) {
                    BlockState newState = cropBlock.getStateForAge(cropBlock.getAge(state) + 1);
                    level.setBlock(blockPos, newState, 3);
                    grewBlock = true;
                }
            }
            else if (block instanceof StemBlock stemBlock) {
                int age = state.getValue(StemBlock.AGE);
                if (age < 7) {
                    BlockState newState = state.setValue(StemBlock.AGE, age + 1);
                    level.setBlock(blockPos, newState, 3);
                    grewBlock = true;
                }
                else if (age == 7 && doesNotHaveFruitNearby(level, stemBlock, blockPos)) {
                    block.randomTick(state, level, blockPos, level.getRandom());
                    grewBlock = true;
                }
            }
            else if (block instanceof NetherWartBlock) {
                int age = state.getValue(NetherWartBlock.AGE);
                if (age < NetherWartBlock.MAX_AGE) {
                    BlockState newState = state.setValue(NetherWartBlock.AGE, age + 1);
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
                bonemealableBlock.performBonemeal(level, level.getRandom(), blockPos, state);
                grewBlock = true;
            }

            if (grewBlock) {
                spawnParticles(serverPlayer.serverLevel(), serverPlayer.position(), serverPlayer.getRandom());
                if (level.getRandom().nextFloat() < 0.4F) {
                    decrementAbilityUseRemaining(stack, serverPlayer, 1);
                }
            }
        }
    }

    private boolean doesNotHaveFruitNearby(ServerLevel level, StemBlock stemblock, BlockPos blockPos) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockState neighborState = level.getBlockState(blockPos.relative(direction));
            if (neighborState.is(stemblock.getFruit())) {
                return false;
            }
        }

        return true;
    }

    public static void spawnParticles(ServerLevel world, Vec3 location, RandomSource random) {
        world.sendParticles(
                ParticleTypes.HAPPY_VILLAGER,
                location.x(),
                location.y() + 1,
                location.z(),
                2,
                random.nextGaussian() * 0.2D,
                (random.nextGaussian() * 0.25D) + 0.1,
                random.nextGaussian() * 0.2D,
                random.nextFloat() * 0.2 + 0.1f);
    }
}