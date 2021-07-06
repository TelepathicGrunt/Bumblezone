package com.telepathicgrunt.bumblezone.mixin.blocks;

import com.telepathicgrunt.bumblezone.entities.EntityTeleportationHookup;
import net.minecraft.block.entity.PistonBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;

@Mixin(PistonBlockEntity.class)
public class PistonBlockEntityMixin {

    // makes entities pushed into a hive teleport to bumblezone
    @Inject(method = "pushEntities(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;FLnet/minecraft/block/entity/PistonBlockEntity;)V",
            at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/block/entity/PistonBlockEntity;moveEntity(Lnet/minecraft/util/math/Direction;Lnet/minecraft/entity/Entity;DLnet/minecraft/util/math/Direction;)V"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private static void thebumblezone_teleportPushedEntities(World world, BlockPos pos, float maxProgress, PistonBlockEntity blockEntity,
                                                             CallbackInfo ci, Direction direction, double currentProgress, VoxelShape voxelshape,
                                                             Box box, List<Entity> list, List<Box> list1, boolean isSlimeBlock,
                                                             Iterator<Entity> iterator, Entity entity)
    {
        if(entity instanceof LivingEntity && !entity.world.isClient()){
            EntityTeleportationHookup.runPistonPushed(direction, (LivingEntity) entity);
        }
    }
}