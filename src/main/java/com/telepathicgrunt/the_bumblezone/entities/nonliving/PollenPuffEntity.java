package com.telepathicgrunt.the_bumblezone.entities.nonliving;

import com.telepathicgrunt.the_bumblezone.blocks.PileOfPollen;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class PollenPuffEntity extends ProjectileItemEntity {
    private boolean consumed = false;

    public PollenPuffEntity(EntityType<? extends PollenPuffEntity> entityType, World world) {
        super(entityType, world);
    }

    public PollenPuffEntity(World world, LivingEntity livingEntity) {
        super(BzEntities.POLLEN_PUFF_ENTITY.get(), livingEntity, world);
    }

    public PollenPuffEntity(World world, double x, double y, double z) {
        super(BzEntities.POLLEN_PUFF_ENTITY.get(), x, y, z, world);
    }

    public void consumed(){
        consumed = true;
    }

    public boolean isConsumed(){
        return consumed;
    }

    @Override
    protected Item getDefaultItem() {
        return BzItems.POLLEN_PUFF.get();
    }

    @Override
    public void tick() {
        super.tick();
        if(this.level.isClientSide()) {
            for(int i = 0; i < 10; ++i) {
                PileOfPollen.spawnParticles(this.level, this.position(), this.random, 0.015D, 0.015D, -0.001D);
            }
        }
    }

    @Override
    protected void onHit(RayTraceResult rayTraceResult) {
        super.onHit(rayTraceResult);

        if (!this.level.isClientSide) {
            this.remove();
        }
        else {
            for(int i = 0; i < 150; ++i) {
                PileOfPollen.spawnParticles(this.level, this.position(), this.random, 0.04D, 0.04D, -0.001D);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult entityRayTraceResult) {
        if(this.level.isClientSide() || consumed) return; // do not run this code if a block already was set.

        super.onHitEntity(entityRayTraceResult);
        Entity entity = entityRayTraceResult.getEntity();

        // pollinates the bee
        if(entity instanceof BeeEntity) {
            ((BeeEntity)entity).setFlag(8, true);
            ((BeeEntity)entity).resetTicksWithoutNectarSinceExitingHive();
        }
    }

    @Override
    protected void onHitBlock(BlockRayTraceResult blockRayTraceResult) {
        if(this.level.isClientSide() || consumed) return; // do not run this code if a block already was set.

        BlockState blockstate = this.level.getBlockState(blockRayTraceResult.getBlockPos());
        blockstate.onProjectileHit(this.level, blockstate, blockRayTraceResult, this);

        if(blockstate.is(Blocks.HONEY_BLOCK) || blockstate.is(Blocks.SOUL_SAND) || blockstate.isFaceSturdy(this.level, blockRayTraceResult.getBlockPos(), blockRayTraceResult.getDirection())){
            BlockPos impactSide = blockRayTraceResult.getBlockPos().relative(blockRayTraceResult.getDirection());
            BlockState sideState = this.level.getBlockState(impactSide);
            if(sideState.isAir()) {
                this.level.setBlock(impactSide, BzBlocks.PILE_OF_POLLEN.get().defaultBlockState(), 3);
                consumed = true;
            }
            else if(sideState.is(BzBlocks.PILE_OF_POLLEN.get())) {
                PileOfPollen.stackPollen(sideState, this.level, impactSide, BzBlocks.PILE_OF_POLLEN.get().defaultBlockState());
                consumed = true;
            }
        }
    }

    @Override
    public IPacket<?> getAddEntityPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}