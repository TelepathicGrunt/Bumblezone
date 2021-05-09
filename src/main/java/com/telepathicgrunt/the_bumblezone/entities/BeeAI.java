package com.telepathicgrunt.the_bumblezone.entities;

import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BeeAI {

    // Make bees not get stuck on ceiling anymore and lag people as a result.
    public static CachedPathHolder smartBeesTM(BeeEntity beeEntity, CachedPathHolder cachedPathHolder){

        if(cachedPathHolder == null || cachedPathHolder.pathTimer > 50 || cachedPathHolder.cachedPath == null ||
                (beeEntity.getDeltaMovement().length() <= 0.05d && cachedPathHolder.pathTimer > 5) ||
                beeEntity.blockPosition().distManhattan(cachedPathHolder.cachedPath.getTarget()) <= 4)
        {
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            World world = beeEntity.level;

            for(int attempt = 0; attempt < 11 || beeEntity.blockPosition().distManhattan(mutable) <= 5; attempt++){
                // pick a random place to fly to
                mutable.set(beeEntity.blockPosition()).move(
                        world.random.nextInt(21) - 10,
                        world.random.nextInt(21) - 10,
                        world.random.nextInt(21) - 10
                );

                if(world.getBlockState(mutable).isAir()){
                    break; // Valid spot to go towards.
                }
            }

            Path newPath = beeEntity.getNavigation().createPath(mutable, 1);
            beeEntity.getNavigation().moveTo(newPath, 1);

            if(cachedPathHolder == null){
                cachedPathHolder = new CachedPathHolder();
            }
            cachedPathHolder.cachedPath = newPath;
            cachedPathHolder.pathTimer = 0;
        }
        else{
            beeEntity.getNavigation().moveTo(cachedPathHolder.cachedPath, 1);
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
