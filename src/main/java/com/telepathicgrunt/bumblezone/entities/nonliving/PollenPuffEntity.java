package com.telepathicgrunt.bumblezone.entities.nonliving;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.blocks.PileOfPollen;
import com.telepathicgrunt.bumblezone.mixin.entities.BeeEntityInvoker;
import com.telepathicgrunt.bumblezone.mixin.entities.PandaEntityInvoker;
import com.telepathicgrunt.bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.bumblezone.modinit.BzEntities;
import com.telepathicgrunt.bumblezone.modinit.BzItems;
import com.telepathicgrunt.bumblezone.tags.BzBlockTags;
import com.telepathicgrunt.bumblezone.tags.BzEntityTags;
import dev.cafeteria.fakeplayerapi.server.FakePlayerBuilder;
import dev.cafeteria.fakeplayerapi.server.FakeServerPlayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class PollenPuffEntity extends ThrownItemEntity {
    private boolean consumed = false;

    public PollenPuffEntity(EntityType<? extends PollenPuffEntity> entityType, World world) {
        super(entityType, world);
    }

    public PollenPuffEntity(World world, LivingEntity livingEntity) {
        super(BzEntities.POLLEN_PUFF_ENTITY, livingEntity, world);
    }

    public PollenPuffEntity(World world, double x, double y, double z) {
        super(BzEntities.POLLEN_PUFF_ENTITY, x, y, z, world);
    }

    public void consumed(){
        consumed = true;
    }

    public boolean isConsumed(){
        return consumed;
    }

    @Override
    protected Item getDefaultItem() {
        return BzItems.POLLEN_PUFF;
    }

    @Override
    public void tick() {
        super.tick();
        if(this.world.isClient() && (!this.isTouchingWater() || this.random.nextFloat() < 0.06f)) {
            for(int i = 0; i < 10; ++i) {
                PileOfPollen.spawnParticles(this.world, this.getPos(), this.random, 0.015D, 0.015D, -0.001D);
            }
        }

        // make pollen puff be able to hit flowers
        BlockHitResult raytraceresult = this.world.raycast(new RaycastContext(
                this.getPos(),
                this.getPos().add(this.getVelocity().multiply(1, 1, 1)),
                RaycastContext.ShapeType.OUTLINE,
                RaycastContext.FluidHandling.ANY,
                this));

        if (raytraceresult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockpos = raytraceresult.getBlockPos();
            BlockState blockstate = this.world.getBlockState(blockpos);
            if (blockstate.isIn(BzBlockTags.FLOWERS_ALLOWED_BY_POLLEN_PUFF) && !blockstate.isIn(BzBlockTags.FLOWERS_BLACKLISTED_FROM_POLLEN_PUFF)) {
                this.setInNetherPortal(blockpos);
                this.onCollision(raytraceresult);
            }
            else if(blockstate.getFluidState().isIn(FluidTags.WATER)) {
                this.onCollision(raytraceresult);
            }
        }
    }

    @Override
    protected void onCollision(HitResult rayTraceResult) {
        super.onCollision(rayTraceResult);

        if (!this.world.isClient()) {
            this.remove(RemovalReason.DISCARDED);
        }
        else {
            for(int i = 0; i < 150; ++i) {
                PileOfPollen.spawnParticles(this.world, this.getPos(), this.random, 0.04D, 0.04D, -0.001D);
            }
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityRayTraceResult) {
        if(this.world.isClient() || consumed) return; // do not run this code if a block already was set.

        super.onEntityHit(entityRayTraceResult);
        Entity entity = entityRayTraceResult.getEntity();

        // pollinates the bee
        if(entity instanceof BeeEntity && BzEntityTags.POLLEN_PUFF_CAN_POLLINATE.contains(entity.getType())) {
            ((BeeEntityInvoker)entity).thebumblezone_callSetHasNectar(true);
            ((BeeEntity)entity).resetPollinationTicks();
        }
        else if(entity instanceof PandaEntity) {
            ((PandaEntityInvoker)entity).thebumblezone_callSneeze();
        }
    }


    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if(this.world.isClient() || consumed) return; // do not run this code if a block already was set.

        BlockState blockstate = this.world.getBlockState(blockHitResult.getBlockPos());
        blockstate.onProjectileHit(this.world, blockstate, blockHitResult, this);

        if(blockstate.isIn(BzBlockTags.FLOWERS_ALLOWED_BY_POLLEN_PUFF) && !blockstate.isIn(BzBlockTags.FLOWERS_BLACKLISTED_FROM_POLLEN_PUFF)) {
            if(blockstate.getBlock() instanceof TallPlantBlock) {
                blockstate = blockstate.with(TallPlantBlock.HALF, DoubleBlockHalf.LOWER);
            }
            int flowerAttempts = 2 + this.random.nextInt(3);
            for(int i = 0; i < flowerAttempts; i++) {
                BlockPos newPos = blockHitResult.getBlockPos().add(
                        this.random.nextInt(5) - 2,
                        this.random.nextInt(3) - 1,
                        this.random.nextInt(5) - 2);

                if(this.world.isAir(newPos) && blockstate.canPlaceAt(this.world, newPos)) {
                    this.world.setBlockState(newPos, blockstate, 3);

                    FakeServerPlayer fakePlayer = new FakePlayerBuilder(new Identifier(Bumblezone.MODID, "default_fake_player"))
                            .create(this.world.getServer(), (ServerWorld) this.world, "placer");
                    blockstate.getBlock().onPlaced(this.world, newPos, blockstate, fakePlayer, ItemStack.EMPTY);
                }
            }
        }
        else if(blockstate.isOf(Blocks.HONEY_BLOCK) || blockstate.isOf(Blocks.SOUL_SAND) || (blockHitResult.getSide() == Direction.UP && !blockstate.getFluidState().isIn(FluidTags.WATER)) || blockstate.isSideSolidFullSquare(this.world, blockHitResult.getBlockPos(), blockHitResult.getSide())){
            BlockPos impactSide = blockHitResult.getBlockPos().offset(blockHitResult.getSide());
            BlockState sideState = this.world.getBlockState(impactSide);
            BlockState pileOfPollen = BzBlocks.PILE_OF_POLLEN.getDefaultState();

            if(sideState.isAir()) {
                this.world.setBlockState(impactSide, BzBlocks.PILE_OF_POLLEN.getDefaultState(), 3);
                consumed = true;
            }
            else if(sideState.isOf(pileOfPollen.getBlock()) && pileOfPollen.canPlaceAt(this.world, impactSide)) {
                PileOfPollen.stackPollen(sideState, this.world, impactSide, BzBlocks.PILE_OF_POLLEN.getDefaultState());
                consumed = true;
            }
        }
    }
}