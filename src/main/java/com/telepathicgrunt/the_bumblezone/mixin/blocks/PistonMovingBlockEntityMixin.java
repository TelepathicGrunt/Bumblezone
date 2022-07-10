package com.telepathicgrunt.the_bumblezone.mixin.blocks;

import com.telepathicgrunt.the_bumblezone.entities.EntityTeleportationHookup;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;

@Mixin(PistonMovingBlockEntity.class)
public class PistonMovingBlockEntityMixin {
    @Shadow
    private BlockState movedState;

    // makes entities pushed into a hive teleport to bumblezone
    @Inject(method = "moveCollidedEntities(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;FLnet/minecraft/world/level/block/piston/PistonMovingBlockEntity;)V",
            at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/world/level/block/piston/PistonMovingBlockEntity;moveEntityByPiston(Lnet/minecraft/core/Direction;Lnet/minecraft/world/entity/Entity;DLnet/minecraft/core/Direction;)V"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private static void thebumblezone_teleportPushedEntities(Level world, BlockPos pos, float maxProgress, PistonMovingBlockEntity blockEntity,
                                                             CallbackInfo ci, Direction direction, double currentProgress, VoxelShape voxelshape,
                                                             AABB box, List<Entity> list, List<AABB> list1, boolean isSlimeBlock,
                                                             Iterator<Entity> iterator, Entity entity)
    {
        if(entity instanceof LivingEntity && !entity.level.isClientSide()) {
            EntityTeleportationHookup.runPistonPushed(direction, (LivingEntity) entity);
        }
    }

    // makes entities stick to royal jelly block
    @Inject(method = "isStickyForEntities()Z",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void thebumblezone_stickyBlockMoveEntities(CallbackInfoReturnable<Boolean> cir) {
        if(this.movedState.is(BzBlocks.ROYAL_JELLY_BLOCK.get())) {
            cir.setReturnValue(true);
        }
    }
}