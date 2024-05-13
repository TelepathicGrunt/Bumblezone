package com.telepathicgrunt.the_bumblezone.blocks;

import com.google.common.collect.MapMaker;
import com.mojang.serialization.MapCodec;
import com.telepathicgrunt.the_bumblezone.entities.TemporaryPlayerData;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzParticles;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.PlatformHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;


public class HeavyAir extends Block {

    public static final MapCodec<HeavyAir> CODEC = Block.simpleCodec(HeavyAir::new);

    private static final ConcurrentMap<String, Integer> APPLIED_PUSH_FOR_ENTITY = new MapMaker().concurrencyLevel(2).weakKeys().makeMap();

    public HeavyAir() {
        this(Properties.of()
                .strength(0.05f, 0)
                .air()
                .noCollission()
                .replaceable()
                .noLootTable()
                .noOcclusion()
                .pushReaction(PushReaction.DESTROY));
    }

    public HeavyAir(Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends HeavyAir> codec() {
        return CODEC;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return collisionContext.isHoldingItem(BzItems.HEAVY_AIR.get()) ? Shapes.block() : Shapes.empty();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.empty();
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public float getShadeBrightness(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return 1.0f;
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if (entity.getType().is(BzTags.HEAVY_AIR_IMMUNE) || entity instanceof Projectile) {
            return;
        }

        if (APPLIED_PUSH_FOR_ENTITY.getOrDefault(entity.getStringUUID(), -1) == entity.tickCount) {
            return;
        }

        if (entity instanceof LivingEntity livingEntity && livingEntity.tickCount % 10 == 0) {
            List<Holder<MobEffect>> effectsToRemove = BuiltInRegistries.MOB_EFFECT.getTag(BzTags.HEAVY_AIR_REMOVE_EFFECTS)
                    .stream()
                    .flatMap(HolderSet.ListBacked::stream)
                    .filter(Holder::isBound)
                    .toList();

            for (Holder<MobEffect> effectToRemove : effectsToRemove) {
                if (livingEntity.hasEffect(effectToRemove)) {
                    livingEntity.removeEffect(effectToRemove);
                }
            }
        }

        double extraGravity = -0.005d;

        if (entity instanceof Player player) {
            if ((player.isCreative() && player.getAbilities().flying) || player.isSpectator()) {
                return;
            }

            if (player.getAbilities().flying) {
                PlatformHooks.disableFlight(player);
            }

            if (entity instanceof TemporaryPlayerData temporaryPlayerData) {
                int ticksOffGround = temporaryPlayerData.bumblezonePlayerTickOffGroundInHeavyAir();
                int offsetTicks = Math.max(0, ticksOffGround - 10);
                extraGravity *= ((offsetTicks * 4) + 1);
            }

            for (ModCompat compat : ModChecker.HEAVY_AIR_RESTRICTED_COMPATS) {
                compat.restrictFlight(entity, extraGravity);
            }

            Entity vehicle = entity.getControlledVehicle();
            if (vehicle != null) {
                vehicle.setDeltaMovement(
                        vehicle.getDeltaMovement().x,
                        vehicle.getDeltaMovement().y + extraGravity,
                        vehicle.getDeltaMovement().z
                );
            }
        }

        entity.setDeltaMovement(entity.getDeltaMovement().add(0, extraGravity, 0));
        APPLIED_PUSH_FOR_ENTITY.put(entity.getStringUUID(), entity.tickCount);
    }

    public static boolean isInHeavyAir(Level level, AABB boundingBox) {
        for (BlockPos pos : BlockPos.betweenClosed(
                Mth.floor(boundingBox.minX),
                Mth.floor(boundingBox.minY),
                Mth.floor(boundingBox.minZ),
                Mth.floor(boundingBox.maxX),
                Mth.floor(boundingBox.maxY),
                Mth.floor(boundingBox.maxZ)))
        {
            if (level.getBlockState(pos).is(BzBlocks.HEAVY_AIR.get())) {
                return true;
            }
        }
        return false;
    }

    @Deprecated
    public void onExplosionHit(BlockState blockState, Level level, BlockPos blockPos, Explosion explosion, BiConsumer<ItemStack, BlockPos> biConsumer) {
        if (explosion.getBlockInteraction() == Explosion.BlockInteraction.TRIGGER_BLOCK) {
            return;
        }
        Block block = blockState.getBlock();
        boolean bl = explosion.getIndirectSourceEntity() instanceof Player;
        if (block.dropFromExplosion(explosion) && level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            BlockEntity blockEntity = blockState.hasBlockEntity() ? level.getBlockEntity(blockPos) : null;
            LootParams.Builder builder = new LootParams.Builder(serverLevel).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockPos)).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntity).withOptionalParameter(LootContextParams.THIS_ENTITY, explosion.getDirectSourceEntity());
            if (explosion.getBlockInteraction() == Explosion.BlockInteraction.DESTROY_WITH_DECAY) {
                builder.withParameter(LootContextParams.EXPLOSION_RADIUS, Float.valueOf(explosion.radius()));
            }
            blockState.spawnAfterBreak(serverLevel, blockPos, ItemStack.EMPTY, bl);
            blockState.getDrops(builder).forEach(itemStack -> biConsumer.accept(itemStack, blockPos));
        }
        level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
        block.wasExploded(level, blockPos, explosion);
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        if (randomSource.nextFloat() < 0.04f) {
            level.addParticle(
                    BzParticles.DUST_PARTICLE.get(),
                    (double)blockPos.getX() + randomSource.nextDouble(),
                    (double)blockPos.getY() + randomSource.nextDouble(),
                    (double)blockPos.getZ() + randomSource.nextDouble(),
                    randomSource.nextGaussian() * 0.003d,
                    randomSource.nextGaussian() * 0.0002d,
                    randomSource.nextGaussian() * 0.003d);
        }
    }
}
