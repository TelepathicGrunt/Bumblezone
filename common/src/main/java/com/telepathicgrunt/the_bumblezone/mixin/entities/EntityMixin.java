package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.telepathicgrunt.the_bumblezone.blocks.EmptyHoneycombBrood;
import com.telepathicgrunt.the_bumblezone.blocks.FilledPorousHoneycomb;
import com.telepathicgrunt.the_bumblezone.blocks.PorousHoneycomb;
import com.telepathicgrunt.the_bumblezone.items.StinglessBeeHelmet;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.PlatformHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public abstract AABB getBoundingBox();

    @Shadow
    public Level level;

    @ModifyVariable(method = "positionRider(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity$MoveFunction;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getX()D"),
            require = 0)
    private double thebumblezone_beeRidingOffset(double yOffset, Entity entity) {
        return StinglessBeeHelmet.beeRidingOffset(yOffset, ((Entity)(Object)this), entity);
    }

    @ModifyReturnValue(method = "updateFluidHeightAndDoFluidPushing(Lnet/minecraft/tags/TagKey;D)Z",
            at = @At(value = "RETURN"),
            require = 0)
    private boolean thebumblezone_applyMissingWaterPhysicsForSugarWaterFluid(boolean appliedFluidPush) {
        if(!appliedFluidPush) {
            return PlatformHooks.getFluidHeight((Entity) ((Object)this), BzTags.SUGAR_WATER_FLUID, BzFluids.SUGAR_WATER_FLUID_TYPE.get()) > 0;
        }
        return true;
    }
    
    // let pollinated bees fill certain BZ blocks
    @Inject(method = "checkInsideBlocks()V",
            at = @At(value = "HEAD"))
    private void thebumblezone_pollinatedBeeBlockFilling(CallbackInfo ci) {
        if (((Object)this) instanceof Bee bee && (bee.hasNectar() || bee.getHealth() < bee.getMaxHealth())) {
            AABB aABB = this.getBoundingBox();
            BlockPos minBlockPos = BlockPos.containing(aABB.minX - 1.0E-7, aABB.minY - 1.0E-7, aABB.minZ - 1.0E-7);
            BlockPos maxBlockPos = BlockPos.containing(aABB.maxX + 1.0E-7, aABB.maxY + 1.0E-7, aABB.maxZ + 1.0E-7);
            BlockPos minThreshold = BlockPos.containing(aABB.minX + 1.0E-7, aABB.minY + 1.0E-7, aABB.minZ + 1.0E-7);
            BlockPos maxThreshold = BlockPos.containing(aABB.maxX - 1.0E-7, aABB.maxY - 1.0E-7, aABB.maxZ - 1.0E-7);
            if (this.level.hasChunksAt(minThreshold, maxThreshold)) {
                BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

                for(int x = minBlockPos.getX(); x <= maxBlockPos.getX(); ++x) {
                    for(int y = minBlockPos.getY(); y <= maxBlockPos.getY(); ++y) {
                        for(int z = minBlockPos.getZ(); z <= maxBlockPos.getZ(); ++z) {
                            int sideCheckPassed = 0;
                            if (x < minThreshold.getX()) {
                                sideCheckPassed++;
                            }
                            if (y < minThreshold.getY()) {
                                sideCheckPassed++;
                            }
                            if (z < minThreshold.getZ()) {
                                sideCheckPassed++;
                            }
                            if (x > maxThreshold.getX()) {
                                sideCheckPassed++;
                            }
                            if (y > maxThreshold.getY()) {
                                sideCheckPassed++;
                            }
                            if (z > maxThreshold.getZ()) {
                                sideCheckPassed++;
                            }

                            if (sideCheckPassed == 1) {
                                mutableBlockPos.set(x, y, z);
                                BlockState blockState = this.level.getBlockState(mutableBlockPos);
                                if (blockState.is(BzBlocks.POROUS_HONEYCOMB.get())) {
                                    PorousHoneycomb.beeHoneyFill(blockState, this.level, mutableBlockPos, ((Entity)(Object)this));
                                }
                                else if (blockState.is(BzBlocks.FILLED_POROUS_HONEYCOMB.get())) {
                                    FilledPorousHoneycomb.beeHoneyTake(blockState, this.level, mutableBlockPos, ((Entity)(Object)this));
                                }
                                else if (blockState.is(BzBlocks.EMPTY_HONEYCOMB_BROOD.get())) {
                                    EmptyHoneycombBrood.beeHoneyFill(blockState, this.level, mutableBlockPos, ((Entity)(Object)this));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}