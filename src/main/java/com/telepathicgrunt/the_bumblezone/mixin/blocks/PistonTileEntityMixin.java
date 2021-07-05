package com.telepathicgrunt.the_bumblezone.mixin.blocks;

import com.telepathicgrunt.the_bumblezone.entities.EntityTeleportationHookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.tileentity.PistonTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;

@Mixin(PistonTileEntity.class)
public class PistonTileEntityMixin {

    // makes entities pushed into a hive teleport to bumblezone
    @Inject(method = "moveCollidedEntities(F)V",
            at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/tileentity/PistonTileEntity;moveEntityByPiston(Lnet/minecraft/util/Direction;Lnet/minecraft/entity/Entity;DLnet/minecraft/util/Direction;)V"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void thebumblezone_teleportPushedEntities(float maxProgress, CallbackInfo ci, Direction direction, double currentProgress,
                                                      VoxelShape voxelshape, AxisAlignedBB axisalignedbb, List<Entity> list,
                                                      List<AxisAlignedBB> list1, boolean isSlimeBlock, Iterator<Entity> iterator,
                                                      Entity entity)
    {
        if(entity instanceof LivingEntity && !entity.level.isClientSide()){
            EntityTeleportationHookup.runPistonPushed(direction, (LivingEntity) entity);
        }
    }
}