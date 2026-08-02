package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.telepathicgrunt.the_bumblezone.blocks.EmptyHoneycombBrood;
import com.telepathicgrunt.the_bumblezone.blocks.FilledPorousHoneycomb;
import com.telepathicgrunt.the_bumblezone.blocks.PorousHoneycomb;
import com.telepathicgrunt.the_bumblezone.items.StinglessBeeHelmet;
import com.telepathicgrunt.the_bumblezone.items.essence.CalmingEssence;
import com.telepathicgrunt.the_bumblezone.loot.EntityLootDropInterface;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Mixin(value = Entity.class, priority = 1200)
public abstract class EntityMixin implements EntityLootDropInterface {

    @Unique
    public boolean thebumblezone_performedEntityDrops = false;

    @Override
    public boolean thebumblezone_hasPerformedEntityDrops() {
        return thebumblezone_performedEntityDrops;
    }

    @Override
    public void thebumblezone_performedEntityDrops() {
        thebumblezone_performedEntityDrops = true;
    }

    @Shadow
    public abstract AABB getBoundingBox();

    @Shadow
    public abstract Level level();

    @ModifyArg(method = "<init>",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityFluidInteraction;<init>(Ljava/util/Set;)V"))
    private Set<TagKey<Fluid>> bumblezone$injectBzFluidForInteractions(Set<TagKey<Fluid>> fluids) {
        Set<TagKey<Fluid>> mergedSet = new HashSet<>();
        Collections.addAll(mergedSet, BzTags.BZ_HONEY_FLUID, BzTags.ROYAL_JELLY_FLUID, BzTags.SUGAR_WATER_FLUID);
        mergedSet.addAll(fluids);
        return mergedSet;
    }

    @ModifyReturnValue(method = "getVehicleAttachmentPoint(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/phys/Vec3;",
            at = @At(value = "RETURN"),
            require = 0)
    private Vec3 bumblezone$beeRidingOffset(Vec3 original, @Local(argsOnly = true) Entity vehicle) {
        return StinglessBeeHelmet.beeRidingOffset(original, vehicle, ((Entity)(Object)this));
    }

    // let pollinated bees fill certain BZ blocks
    @Inject(method = "checkInsideBlocks(Ljava/util/List;Lnet/minecraft/world/entity/InsideBlockEffectApplier$StepBasedCollector;)V",
            at = @At(value = "HEAD"))
    private void bumblezone$pollinatedBeeBlockFilling(CallbackInfo ci) {
        if (((Object)this) instanceof Bee bee && (bee.hasNectar() || bee.getHealth() < bee.getMaxHealth())) {
            AABB aABB = this.getBoundingBox();
            BlockPos minBlockPos = BlockPos.containing(aABB.minX - 1.0E-7, aABB.minY - 1.0E-7, aABB.minZ - 1.0E-7);
            BlockPos maxBlockPos = BlockPos.containing(aABB.maxX + 1.0E-7, aABB.maxY + 1.0E-7, aABB.maxZ + 1.0E-7);
            BlockPos minThreshold = BlockPos.containing(aABB.minX + 1.0E-7, aABB.minY + 1.0E-7, aABB.minZ + 1.0E-7);
            BlockPos maxThreshold = BlockPos.containing(aABB.maxX - 1.0E-7, aABB.maxY - 1.0E-7, aABB.maxZ - 1.0E-7);
            if (level().hasChunksAt(minThreshold, maxThreshold)) {
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
                                BlockState blockState = level().getBlockState(mutableBlockPos);
                                if (blockState.is(BzBlocks.POROUS_HONEYCOMB.get())) {
                                    PorousHoneycomb.beeHoneyFill(blockState, level(), mutableBlockPos, ((Entity)(Object)this));
                                }
                                else if (blockState.is(BzBlocks.FILLED_POROUS_HONEYCOMB.get())) {
                                    FilledPorousHoneycomb.beeHoneyTake(blockState, level(), mutableBlockPos, ((Entity)(Object)this));
                                }
                                else if (blockState.is(BzBlocks.EMPTY_HONEYCOMB_BROOD.get())) {
                                    EmptyHoneycombBrood.beeHoneyFill(blockState, level(), mutableBlockPos, ((Entity)(Object)this));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Inject(method = "isAlliedTo(Lnet/minecraft/world/entity/Entity;)Z",
            at = @At(value = "RETURN"),
            cancellable = true)
    private void bumblezone$preventAngerableAtPlayer2(Entity comparer, CallbackInfoReturnable<Boolean> cir) {
        Entity current = (Entity) (Object) this;
        if (current instanceof Mob || comparer instanceof Mob) {
            if (current instanceof Player player && CalmingEssence.IsCalmingEssenceActive(player)) {
                if (!comparer.is(BzTags.CALMING_ALLOW_ANGER_THROUGH)) {
                    cir.setReturnValue(false);
                }
            }
            else if (comparer instanceof Player player && CalmingEssence.IsCalmingEssenceActive(player)) {
                if (!current.is(BzTags.CALMING_ALLOW_ANGER_THROUGH)) {
                    cir.setReturnValue(false);
                }
            }
        }
    }

    @WrapOperation(method = "lambda$checkInsideBlocks$0(ILjava/util/concurrent/atomic/AtomicInteger;ZLnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Lit/unimi/dsi/fastutil/longs/LongSet;ZLnet/minecraft/world/phys/AABB;Lnet/minecraft/world/entity/InsideBlockEffectApplier$StepBasedCollector;Lnet/minecraft/core/BlockPos;I)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;isAir()Z"),
            require = 0
    )
    private boolean bumblezone$allowBzAirInteraction(BlockState instance, Operation<Boolean> original) {
        if (instance.is(BzTags.AIR_LIKE)) {
            return false; // We need `state.isAir()` to eval to false
        }
        return original.call(instance);
    }
}