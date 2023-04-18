package com.telepathicgrunt.the_bumblezone.mixin.blocks;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.telepathicgrunt.the_bumblezone.entities.EntityTeleportationHookup;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PistonMovingBlockEntity.class)
public class PistonMovingBlockEntityMixin {
    @Shadow
    private BlockState movedState;

    // makes entities pushed into a hive teleport to bumblezone
    @WrapOperation(method = "moveCollidedEntities(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;FLnet/minecraft/world/level/block/piston/PistonMovingBlockEntity;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/piston/PistonMovingBlockEntity;moveEntityByPiston(Lnet/minecraft/core/Direction;Lnet/minecraft/world/entity/Entity;DLnet/minecraft/core/Direction;)V", ordinal = 0))
    private static void bumblezone$teleportPushedEntities(Direction direction, Entity entity, double progress, Direction direction2, Operation<Void> original) {
        if (entity instanceof LivingEntity livingEntity && !livingEntity.level.isClientSide()) {
            EntityTeleportationHookup.runPistonPushed(direction, livingEntity);
        }
        original.call(direction, entity, progress, direction2);
    }

    // makes entities stick to royal jelly block
    @ModifyReturnValue(method = "isStickyForEntities()Z",
            at = @At(value = "RETURN"))
    private boolean bumblezone$royalJellyBlockMoveEntities(boolean isSticky) {
        if(!isSticky && this.movedState.is(BzBlocks.ROYAL_JELLY_BLOCK.get())) {
            return true;
        }
        return isSticky;
    }
}