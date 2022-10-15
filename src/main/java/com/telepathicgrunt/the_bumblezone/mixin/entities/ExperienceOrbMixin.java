package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.telepathicgrunt.the_bumblezone.blocks.CrystallineFlower;
import com.telepathicgrunt.the_bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.the_bumblezone.enchantments.CombCutterEnchantment;
import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import com.telepathicgrunt.the_bumblezone.modinit.BzPOI;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(ExperienceOrb.class)
public abstract class ExperienceOrbMixin extends Entity {

    public ExperienceOrbMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "tick()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ExperienceOrb;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V"))
    private void thebumblezone_xpGoToClosestCrystalFlower(CallbackInfo ci) {
        if(!this.level.isClientSide()) {
            double distanceThreshold = 8;
            PoiManager pointofinterestmanager = ((ServerLevel)this.level).getPoiManager();
            List<PoiRecord> poiInRange = pointofinterestmanager.getInSquare(
                            (pointOfInterestType) -> pointOfInterestType.value() == BzPOI.CRYSTAL_FLOWER_POI.get(),
                            this.blockPosition(),
                            (int)distanceThreshold,
                            PoiManager.Occupancy.ANY)
                    .sorted((a, b) -> a.getPos().distManhattan(this.blockPosition()) - b.getPos().distManhattan(this.blockPosition()))
                    .collect(Collectors.toList());

            if(poiInRange.size() != 0) {
                for(int index = poiInRange.size() - 1; index >= 0; index--) {
                    PoiRecord poi = poiInRange.remove(index);
                    BlockState state = this.level.getBlockState(poi.getPos());
                    Vec3 centerBlockPositon = Vec3.atCenterOf(poi.getPos());
                    if(state.getBlock() instanceof CrystallineFlower) {
                        Vec3 vec3 = new Vec3(centerBlockPositon.x() - this.getX(), centerBlockPositon.y() - this.getY(), centerBlockPositon.z() - this.getZ());
                        double sqrDistance = vec3.lengthSqr();
                        if (sqrDistance < distanceThreshold * distanceThreshold) {
                            double speedFactor = 1.0D - Math.sqrt(sqrDistance) / distanceThreshold;
                            this.setDeltaMovement(this.getDeltaMovement().add(vec3.normalize().scale(speedFactor * speedFactor * 0.1D)));
                        }
                        break;
                    }
                }
            }
        }
    }
}