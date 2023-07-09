package com.telepathicgrunt.the_bumblezone.entities.nonliving;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.items.essence.EssenceOfTheBees;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.joml.Vector2d;

public class DirtPelletEntity extends ThrowableItemProjectile {
    private boolean eventBased = false;

    public DirtPelletEntity(EntityType<? extends DirtPelletEntity> entityType, Level world) {
        super(entityType, world);
    }

    public DirtPelletEntity(Level world, LivingEntity livingEntity) {
        super(BzEntities.DIRT_PELLET_ENTITY.get(), livingEntity, world);
    }

    public DirtPelletEntity(Level world, double x, double y, double z) {
        super(BzEntities.DIRT_PELLET_ENTITY.get(), x, y, z, world);
    }

    @Override
    protected Item getDefaultItem() {
        return BzItems.DIRT_PELLET.get();
    }

    private ParticleOptions getParticle() {
        ItemStack itemStack = this.getItemRaw();
        return itemStack.isEmpty() ?
                new ItemParticleOption(ParticleTypes.ITEM, this.getDefaultItem().getDefaultInstance()) :
                new ItemParticleOption(ParticleTypes.ITEM, itemStack);
    }

    @Override
    public void handleEntityEvent(byte flag) {
        if (flag == 3) {
            ParticleOptions particleOptions = this.getParticle();

            for(int i = 0; i < 8; ++i) {
                this.level().addParticle(
                        particleOptions,
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        this.random.nextGaussian() * 0.1,
                        this.random.nextGaussian() * 0.1,
                        this.random.nextGaussian() * 0.1);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        EntityType<?> type = entity.getType();
        ResourceLocation resourceLocation = BuiltInRegistries.ENTITY_TYPE.getKey(type);
        int damage = 1;

        if (!type.is(BzTags.DIRT_PELLET_FORCE_NO_EXTRA_DAMAGE)) {
            if (type.is(BzTags.DIRT_PELLET_EXTRA_DAMAGE)) {
                damage = 3;
            }
            else if (!resourceLocation.getNamespace().equals("minecraft") && !resourceLocation.getNamespace().equals(Bumblezone.MODID)) {
                if (entity instanceof FlyingMob || (entity instanceof Mob mob && mob.getMoveControl() instanceof FlyingMoveControl)) {
                    damage = 3;
                }
            }
        }

        if (eventBased) {
            if (entity instanceof ServerPlayer serverPlayer && !EssenceOfTheBees.hasEssence(serverPlayer)) {
                damage = 5;
            }
        }

        entity.hurt(this.damageSources().thrown(this, this.getOwner()), (float)damage);
        if (entity instanceof LivingEntity livingEntity) {
            Vector2d direction = new Vector2d(this.getDeltaMovement().x(), this.getDeltaMovement().z()).normalize();
            double yRotHitRadian = Mth.atan2(direction.x(), direction.y());
            livingEntity.knockback(
                1d,
                -Mth.sin((float) yRotHitRadian),
                -Mth.cos((float) yRotHitRadian)
            );
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();
        }
    }
}