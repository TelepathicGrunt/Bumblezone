package net.telepathicgrunt.bumblezone.entities;

import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BeeAI {

    // Make bees not get stuck on ceiling anymore and lag people as a result. (Only for Forge version of Bumblezone)
    public static CachedPathHolder smartBeesTM(BeeEntity beeEntity, CachedPathHolder cachedPathHolder){

        if(cachedPathHolder == null || cachedPathHolder.pathTimer > 50 || cachedPathHolder.cachedPath == null ||
            (beeEntity.getVelocity().length() <= 0.05d && cachedPathHolder.pathTimer > 5) ||
            beeEntity.getBlockPos().getManhattanDistance(cachedPathHolder.cachedPath.getTarget()) <= 4)
        {
            BlockPos.Mutable mutable = new BlockPos.Mutable().set(beeEntity.getBlockPos());
            World world = beeEntity.world;

            for(int attempt = 0; attempt < 11 || beeEntity.getBlockPos().getManhattanDistance(mutable) <= 5; attempt++){
                // pick a random place to fly to
                mutable.set(beeEntity.getBlockPos()).move(
                        world.random.nextInt(21) - 10,
                        world.random.nextInt(21) - 10,
                        world.random.nextInt(21) - 10
                );

                if(world.getBlockState(mutable).isAir()){
                    break; // Valid spot to go towards.
                }
            }

            Path newPath = beeEntity.getNavigation().findPathTo(mutable, 1);
            beeEntity.getNavigation().startMovingAlong(newPath, 1);

            if(cachedPathHolder == null){
                cachedPathHolder = new CachedPathHolder();
            }
            cachedPathHolder.cachedPath = newPath;
            cachedPathHolder.pathTimer = 0;
        }
        else{
            beeEntity.getNavigation().startMovingAlong(cachedPathHolder.cachedPath, 1);
            cachedPathHolder.pathTimer += 1;
        }
        return cachedPathHolder;
    }

    public static class CachedPathHolder {
        public Path cachedPath;
        public int pathTimer = 0;

        public CachedPathHolder(){}
    }
}
