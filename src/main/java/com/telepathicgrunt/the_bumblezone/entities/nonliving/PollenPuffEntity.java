package com.telepathicgrunt.the_bumblezone.entities.nonliving;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.PileOfPollen;
import com.telepathicgrunt.the_bumblezone.items.HoneyBeeLeggings;
import com.telepathicgrunt.the_bumblezone.mixin.blocks.FallingBlockEntityAccessor;
import com.telepathicgrunt.the_bumblezone.mixin.entities.BeeEntityInvoker;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.packets.UpdateFallingBlockPacket;
import dev.cafeteria.fakeplayerapi.server.FakePlayerBuilder;
import dev.cafeteria.fakeplayerapi.server.FakeServerPlayer;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.quiltmc.qsl.networking.api.PlayerLookup;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

public class PollenPuffEntity extends ThrowableItemProjectile {
    private boolean consumed = false;

    public PollenPuffEntity(EntityType<? extends PollenPuffEntity> entityType, Level world) {
        super(entityType, world);
    }

    public PollenPuffEntity(Level world, LivingEntity livingEntity) {
        super(BzEntities.POLLEN_PUFF_ENTITY, livingEntity, world);
    }

    public PollenPuffEntity(Level world, double x, double y, double z) {
        super(BzEntities.POLLEN_PUFF_ENTITY, x, y, z, world);
    }

    public void consumed() {
        consumed = true;
    }

    public boolean isConsumed() {
        return consumed;
    }

    @Override
    protected Item getDefaultItem() {
        return BzItems.POLLEN_PUFF;
    }

    @Override
    public void tick() {
        super.tick();
        if(this.level.isClientSide() && (!this.isInWater() || this.random.nextFloat() < 0.06f)) {
            for(int i = 0; i < 10; ++i) {
                PileOfPollen.spawnParticles(this.level, this.position(), this.random, 0.015D, 0.015D, -0.001D);
            }
        }

        // make pollen puff be able to hit flowers
        BlockHitResult raytraceresult = this.level.clip(new ClipContext(
                this.position(),
                this.position().add(this.getDeltaMovement().multiply(1, 1, 1)),
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.ANY,
                this));

        if (raytraceresult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockpos = raytraceresult.getBlockPos();
            BlockState blockstate = this.level.getBlockState(blockpos);
            if (blockstate.is(BzTags.FLOWERS_ALLOWED_BY_POLLEN_PUFF) && !blockstate.is(BzTags.FLOWERS_BLACKLISTED_FROM_POLLEN_PUFF)) {
                this.handleInsidePortal(blockpos);
                this.onHit(raytraceresult);
            }
            else if(blockstate.getFluidState().is(FluidTags.WATER)) {
                this.onHit(raytraceresult);
            }
        }
    }

    @Override
    protected void onHit(HitResult rayTraceResult) {
        super.onHit(rayTraceResult);

        if (!this.level.isClientSide()) {
            this.remove(RemovalReason.DISCARDED);
        }
        else {
            for(int i = 0; i < 150; ++i) {
                PileOfPollen.spawnParticles(this.level, this.position(), this.random, 0.04D, 0.04D, -0.001D);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityRayTraceResult) {
        if(this.level.isClientSide() || consumed) return; // do not run this code if a block already was set.

        super.onHitEntity(entityRayTraceResult);
        Entity entity = entityRayTraceResult.getEntity();

        // pollinates the bee
        if(entity instanceof Bee && entity.getType().is(BzTags.POLLEN_PUFF_CAN_POLLINATE)) {
            ((BeeEntityInvoker)entity).thebumblezone_callSetHasNectar(true);
            ((Bee)entity).resetTicksWithoutNectarSinceExitingHive();

            if(this.getOwner() instanceof ServerPlayer) {
                BzCriterias.POLLEN_PUFF_POLLINATED_BEE_TRIGGER.trigger((ServerPlayer) this.getOwner());
            }
        }
        else if(entity instanceof Panda panda) {
            panda.sneeze(true);

            if(this.getOwner() instanceof ServerPlayer) {
                BzCriterias.POLLEN_PUFF_PANDA_TRIGGER.trigger((ServerPlayer) this.getOwner());
            }
        }
        else if(entity instanceof Fireball && ((Fireball)entity).getOwner() instanceof Ghast) {
            if(this.getOwner() instanceof ServerPlayer)
                BzCriterias.POLLEN_PUFF_FIREBALL_TRIGGER.trigger((ServerPlayer) this.getOwner());
        }
        else if(entity instanceof FallingBlockEntity fallingBlockEntity && fallingBlockEntity.getBlockState().is(BzBlocks.PILE_OF_POLLEN)) {
            BlockState fallingState = fallingBlockEntity.getBlockState();
            int newLayer = Math.min(8, fallingState.getValue(PileOfPollen.LAYERS) + 1);
            ((FallingBlockEntityAccessor)fallingBlockEntity).thebumblezone_setBlock(fallingState.setValue(PileOfPollen.LAYERS, newLayer));

            FriendlyByteBuf passedData = new FriendlyByteBuf(Unpooled.buffer());
            passedData.writeInt(fallingBlockEntity.getId());
            passedData.writeShort((short)newLayer);
            PlayerLookup.world((ServerLevel) this.level).forEach(player ->
                    ServerPlayNetworking.send(player, UpdateFallingBlockPacket.PACKET_ID, passedData));
        }

        ItemStack beeLeggings = HoneyBeeLeggings.getEntityBeeLegging(entity);
        if(!entity.isCrouching() && !beeLeggings.isEmpty()) {
            HoneyBeeLeggings.setPollinated(beeLeggings);
        }
    }


    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        if(this.level.isClientSide() || consumed) return; // do not run this code if a block already was set.

        BlockState blockstate = this.level.getBlockState(blockHitResult.getBlockPos());
        blockstate.onProjectileHit(this.level, blockstate, blockHitResult, this);

        if(blockstate.is(BzTags.FLOWERS_ALLOWED_BY_POLLEN_PUFF) && !blockstate.is(BzTags.FLOWERS_BLACKLISTED_FROM_POLLEN_PUFF)) {
            boolean isTallPlant = false;
            if(blockstate.getBlock() instanceof DoublePlantBlock) {
                blockstate = blockstate.setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER);
                isTallPlant = true;
            }
            int flowerAttempts = 2 + this.random.nextInt(3);
            for(int i = 0; i < flowerAttempts; i++) {
                BlockPos newPos = blockHitResult.getBlockPos().offset(
                        this.random.nextInt(5) - 2,
                        this.random.nextInt(3) - 1,
                        this.random.nextInt(5) - 2);

                if(this.level.isEmptyBlock(newPos) && blockstate.canSurvive(this.level, newPos)) {
                    this.level.setBlock(newPos, blockstate, 3);

                    FakeServerPlayer fakePlayer = new FakePlayerBuilder(new ResourceLocation(Bumblezone.MODID, "default_fake_player"))
                            .create(this.level.getServer(), (ServerLevel) this.level, "placer");
                    blockstate.getBlock().setPlacedBy(this.level, newPos, blockstate, fakePlayer, ItemStack.EMPTY);

                    if(isTallPlant && this.getOwner() instanceof ServerPlayer) {
                        BzCriterias.POLLEN_PUFF_POLLINATED_TALL_FLOWER_TRIGGER.trigger((ServerPlayer) this.getOwner());
                    }
                }
            }
        }
        else if(blockstate.is(Blocks.HONEY_BLOCK) ||
                blockstate.is(Blocks.SOUL_SAND) ||
                blockstate.isFaceSturdy(this.level, blockHitResult.getBlockPos(), blockHitResult.getDirection()))
        {
            BlockState pileOfPollen = BzBlocks.PILE_OF_POLLEN.defaultBlockState();
            BlockPos impactSide = blockHitResult.getBlockPos().relative(blockHitResult.getDirection());
            BlockState sideState = this.level.getBlockState(impactSide);
            BlockState belowSideState = this.level.getBlockState(impactSide.below());
            boolean belowSideStateHasCollision = !belowSideState.getCollisionShape(this.level, impactSide.below()).isEmpty();

            if(sideState.is(pileOfPollen.getBlock())) {
                PileOfPollen.stackPollen(sideState, this.level, impactSide, pileOfPollen);
                consumed = true;
            }
            else if((!belowSideStateHasCollision && sideState.isAir()) || (belowSideStateHasCollision && pileOfPollen.canSurvive(this.level, impactSide) && sideState.getMaterial().isReplaceable())) {
                this.level.setBlock(impactSide, pileOfPollen, 3);
                consumed = true;
            }
        }
    }
}