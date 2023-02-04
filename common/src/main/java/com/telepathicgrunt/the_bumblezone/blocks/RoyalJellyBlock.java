package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.platform.BlockExtension;
import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RoyalJellyBlock extends HalfTransparentBlock implements BlockExtension {
    protected static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D);

    public RoyalJellyBlock() {
        super(Properties.of(Material.CLAY, MaterialColor.COLOR_PURPLE).speedFactor(0.4F).jumpFactor(0.5F).noOcclusion().sound(SoundType.HONEY_BLOCK));
    }

    @Override
    public boolean bz$isStickyBlock(BlockState state) {
        return state.getBlock() == BzBlocks.ROYAL_JELLY_BLOCK.get();
    }

    @Override
    public OptionalBoolean bz$canStickTo(BlockState state, BlockState other) {
        if (state.getBlock() == BzBlocks.ROYAL_JELLY_BLOCK.get()) {
            if (other.getBlock() == Blocks.HONEY_BLOCK || other.getBlock() == Blocks.SLIME_BLOCK) {
                return OptionalBoolean.FALSE;
            }
            return OptionalBoolean.TRUE;
        }
        return OptionalBoolean.EMPTY;
    }

    public static boolean isValidMoveDirection(Direction pushDirection, Direction pistonDirection) {
        return pushDirection == null || pushDirection != pistonDirection;
    }

    private static boolean doesEntityDoHoneyBlockSlideEffects(Entity entity) {
        return entity instanceof LivingEntity || entity instanceof AbstractMinecart || entity instanceof PrimedTnt || entity instanceof Boat;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public void fallOn(Level level, BlockState blockState, BlockPos blockPos, Entity entity, float fallDistance) {
        entity.playSound(BzSounds.ROYAL_JELLY_BLOCK_SLIDE.get(), 1.0F, 1.0F);
        if (!level.isClientSide) {
            showJumpParticles((ServerLevel)level, entity);
        }

        if (entity.causeFallDamage(fallDistance, 0.2F, DamageSource.FALL)) {
            entity.playSound(this.soundType.getFallSound(), this.soundType.getVolume() * 0.5F, this.soundType.getPitch() * 0.75F);
        }
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if (this.isSlidingDown(blockPos, entity)) {
            this.maybeDoSlideAchievement(entity, blockPos);
            this.doSlideMovement(entity);
            this.maybeDoSlideEffects(level, entity);
        }

        super.entityInside(blockState, level, blockPos, entity);
    }

    private boolean isSlidingDown(BlockPos blockPos, Entity entity) {
        if (entity.isOnGround()) {
            return false;
        }
        else if (entity.getY() > (double)blockPos.getY() + 0.9375D - 1.0E-7D) {
            return false;
        }
        else if (entity.getDeltaMovement().y >= -0.08D) {
            return false;
        }
        else {
            double d0 = Math.abs((double)blockPos.getX() + 0.5D - entity.getX());
            double d1 = Math.abs((double)blockPos.getZ() + 0.5D - entity.getZ());
            double d2 = 0.4375D + (double)(entity.getBbWidth() / 2.0F);
            return d0 + 1.0E-7D > d2 || d1 + 1.0E-7D > d2;
        }
    }

    private void maybeDoSlideAchievement(Entity entity, BlockPos blockPos) {
        if (entity instanceof ServerPlayer serverPlayer && serverPlayer.level.getGameTime() % 20L == 0L) {
            CriteriaTriggers.HONEY_BLOCK_SLIDE.trigger(serverPlayer, serverPlayer.level.getBlockState(blockPos));
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState blockState, LivingEntity entity, ItemStack itemStack) {
        if (entity instanceof ServerPlayer serverPlayer) {
            boolean nextToStickyPiston = false;
            for (Direction direction : Direction.values()) {
                if (level.getBlockState(pos.relative(direction)).is(Blocks.STICKY_PISTON)) {
                    nextToStickyPiston = true;
                    break;
                }
            }

            if (nextToStickyPiston) {
                BzCriterias.ROYAL_JELLY_BLOCK_PISTON_TRIGGER.trigger(serverPlayer);
            }
        }
    }

    private void doSlideMovement(Entity entity) {
        Vec3 vec3 = entity.getDeltaMovement();
        if (vec3.y < -0.13D) {
            double d0 = -0.05D / vec3.y;
            entity.setDeltaMovement(new Vec3(vec3.x * d0, -0.05D, vec3.z * d0));
        }
        else {
            entity.setDeltaMovement(new Vec3(vec3.x, -0.05D, vec3.z));
        }

        entity.resetFallDistance();
    }

    private void maybeDoSlideEffects(Level level, Entity entity) {
        if (doesEntityDoHoneyBlockSlideEffects(entity)) {
            if (level.getRandom().nextInt(5) == 0) {
                entity.playSound(BzSounds.ROYAL_JELLY_BLOCK_SLIDE.get(), 1.0F, 1.0F);
            }

            if (!level.isClientSide && level.getRandom().nextInt(5) == 0) {
                showSlideParticles((ServerLevel)level, entity);
            }
        }

    }

    public static void showSlideParticles(ServerLevel serverLevel, Entity entity) {
        showParticles(serverLevel, entity, 5);
    }

    public static void showJumpParticles(ServerLevel serverLevel, Entity entity) {
        showParticles(serverLevel, entity, 10);
    }

    private static void showParticles(ServerLevel serverLevel, Entity entity, int particleNumber) {
        BlockState blockstate = BzBlocks.ROYAL_JELLY_BLOCK.get().defaultBlockState();

        for(int i = 0; i < particleNumber; ++i) {
            serverLevel.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate), entity.getX(), entity.getY(), entity.getZ(), 0.0D, 0.0D, 0.0D);
        }
    }
}
