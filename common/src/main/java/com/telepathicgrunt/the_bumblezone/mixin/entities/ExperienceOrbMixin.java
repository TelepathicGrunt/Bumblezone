package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.telepathicgrunt.the_bumblezone.blocks.CrystallineFlower;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.CrystallineFlowerBlockEntity;
import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Mixin(ExperienceOrb.class)
public abstract class ExperienceOrbMixin extends Entity {

    public ExperienceOrbMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    private BlockPos bumblezone$trackedCrystalFlower = null;

    @Unique
    private int bumblezone$trackedCrystalFlowerCooldown = 0;

    @Inject(method = "tick()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ExperienceOrb;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V"),
            require = 0)
    private void bumblezone$xpGoToClosestCrystalFlower(CallbackInfo ci) {
        if (!BzGeneralConfigs.crystallineFlowerConsumeExperienceOrbEntities) {
            return;
        }

        double distanceThreshold = 8;

        bumblezone$trackedCrystalFlowerCooldown--;
        if (this.bumblezone$trackedCrystalFlower == null && bumblezone$trackedCrystalFlowerCooldown <= 0) {
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            Set<LevelChunk> chunksInRange = new HashSet<>();
            for (int x = (int) -distanceThreshold; x <= distanceThreshold; x += distanceThreshold) {
                for (int z = (int) -distanceThreshold; z <= distanceThreshold; z += distanceThreshold) {
                    mutableBlockPos.set(this.getX() + x, 0, this.getZ() + z);
                    ChunkAccess chunk = this.level.getChunk(mutableBlockPos);
                    if(chunk instanceof LevelChunk) chunksInRange.add((LevelChunk)chunk);
                }
            }

            Optional<BlockEntity> closestCrystalFlower =
                    chunksInRange.stream()
                    .flatMap(c -> c.getBlockEntities().values().stream())
                    .filter(be -> be instanceof CrystallineFlowerBlockEntity crystallineFlowerBlockEntity && !crystallineFlowerBlockEntity.isMaxTier())
                    .min((a, b) -> a.getBlockPos().distManhattan(this.blockPosition()) - b.getBlockPos().distManhattan(this.blockPosition()));

            closestCrystalFlower.ifPresent(blockEntity -> this.bumblezone$trackedCrystalFlower = blockEntity.getBlockPos());
            bumblezone$trackedCrystalFlowerCooldown = 60;
        }

        if (this.bumblezone$trackedCrystalFlower != null) {
            Vec3 centerBlockPosition = Vec3.atCenterOf(this.bumblezone$trackedCrystalFlower);
            Vec3 vec3 = new Vec3(centerBlockPosition.x() - this.getX(), centerBlockPosition.y() - 0.5d - this.getY(), centerBlockPosition.z() - this.getZ());
            double sqrDistance = vec3.lengthSqr();
            if (sqrDistance >= distanceThreshold * distanceThreshold) {
                this.bumblezone$trackedCrystalFlower = null;
                return;
            }

            BlockState state = this.level.getBlockState(this.bumblezone$trackedCrystalFlower);
            if(state.getBlock() instanceof CrystallineFlower) {
                double speedFactor = 1.0D - Math.sqrt(sqrDistance) / distanceThreshold;
                this.setDeltaMovement(this.getDeltaMovement().add(vec3.normalize().scale(speedFactor * speedFactor * 0.11D)));
            }
        }
    }
}