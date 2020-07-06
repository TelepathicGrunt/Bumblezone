package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.entities.PlayerTeleportation;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderPearlEntity.class)
public class EnderpearlImpactMixin {

    // Teleports player to Bumblezone when pearl hits bee nest
    @Inject(method = "onCollision",
            at = @At(value = "TAIL"),
            cancellable = true)
    private void onPearlHit(HitResult hitResult, CallbackInfo ci) {
        if(PlayerTeleportation.runEnderpearlImpact(hitResult, ((EnderPearlEntity) (Object) this))) ci.cancel();
    }


}