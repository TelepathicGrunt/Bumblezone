package com.telepathicgrunt.the_bumblezone.mixin.blocks;

import com.telepathicgrunt.the_bumblezone.entities.EntityTeleportationHookup;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Iterator;
import java.util.List;

@Mixin(PistonMovingBlockEntity.class)
public class PistonMovingBlockEntityMixin {
    @Shadow
    private BlockState movedState;

    // makes entities pushed into a hive teleport to bumblezone
    @ModifyArgs(method = "moveCollidedEntities(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;FLnet/minecraft/world/level/block/piston/PistonMovingBlockEntity;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/piston/PistonMovingBlockEntity;moveEntityByPiston(Lnet/minecraft/core/Direction;Lnet/minecraft/world/entity/Entity;DLnet/minecraft/core/Direction;)V", ordinal = 0))
    private static void thebumblezone_teleportPushedEntities(Args args) {
        if (args.get(1) instanceof LivingEntity livingEntity && !livingEntity.level.isClientSide()) {
            EntityTeleportationHookup.runPistonPushed(args.get(0), livingEntity);
        }
    }

    // makes entities stick to royal jelly block
    @Inject(method = "isStickyForEntities()Z",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void thebumblezone_royalJellyBlockMoveEntities(CallbackInfoReturnable<Boolean> cir) {
        if(this.movedState.is(BzBlocks.ROYAL_JELLY_BLOCK)) {
            cir.setReturnValue(true);
        }
    }
}