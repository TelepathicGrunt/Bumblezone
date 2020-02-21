package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.entity.thrown.ThrownEnderpearlEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.dimension.BzDimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownEnderpearlEntity.class)
public class EnderpearlImpactMixin {


    // Teleports player to Bumblezone when pearl hits bee nest
    @Inject(method = "onCollision",
            at = @At(value = "TAIL"),
            cancellable = true)
    private void onPearlHit(HitResult hitResult, CallbackInfo ci) {

        ThrownEnderpearlEntity pearlEntity = ((ThrownEnderpearlEntity)(Object)this);

        World world = pearlEntity.world; // world we threw in

        //Make sure we are on server by checking if thrower is ServerPlayerEntity
        if (!world.isClient && pearlEntity.getOwner() instanceof ServerPlayerEntity) {
            ServerPlayerEntity playerEntity = (ServerPlayerEntity) pearlEntity.getOwner(); // the thrower
            Vec3d hitBlockPos = hitResult.getPos(); //position of the collision
            boolean hitHive = false;

            //check with offset in all direction as the position of exact hit point could barely be outside the hive block
            //even through the pearl hit the block directly.
            if (world.getBlockState(new BlockPos(hitBlockPos.add(0.1D, 0, 0))).getBlock() == Blocks.BEE_NEST ||
                    world.getBlockState(new BlockPos(hitBlockPos.add(-0.1D, 0, 0))).getBlock() == Blocks.BEE_NEST ||
                    world.getBlockState(new BlockPos(hitBlockPos.add(0, 0, 0.1D))).getBlock() == Blocks.BEE_NEST ||
                    world.getBlockState(new BlockPos(hitBlockPos.add(0, 0, -0.1D))).getBlock() == Blocks.BEE_NEST ||
                    world.getBlockState(new BlockPos(hitBlockPos.add(0, 0.1D, 0))).getBlock() == Blocks.BEE_NEST ||
                    world.getBlockState(new BlockPos(hitBlockPos.add(0, -0.1D, 0))).getBlock() == Blocks.BEE_NEST) {
                hitHive = true;
            }

            //if the pearl hit a beehive and is not in our bee dimension, begin the teleportation.
            if (hitHive && playerEntity.dimension != BzDimensionType.BUMBLEZONE_TYPE) {
                Bumblezone.PLAYER_COMPONENT.get(playerEntity).setIsTeleporting(true);
                ci.cancel(); // cancel rest of the enderpearl hit stuff
            }
        }
    }

}