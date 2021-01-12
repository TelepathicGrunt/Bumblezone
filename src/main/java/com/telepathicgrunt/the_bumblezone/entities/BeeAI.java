package com.telepathicgrunt.the_bumblezone.entities;

import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BeeAI {

    // Make bees not get stuck on ceiling anymore and lag people as a result. (Only for Forge version of Bumblezone)
    public static CachedPathHolder smartBeesTM(BeeEntity beeEntity, CachedPathHolder cachedPathHolder){

        if(cachedPathHolder == null || cachedPathHolder.pathTimer > 100 || cachedPathHolder.cachedPath == null ||
            beeEntity.getPosition().withinDistance(cachedPathHolder.cachedPath.getTarget(), 3) ||
            (cachedPathHolder.cachedPath.getFinalPathPoint() != null && cachedPathHolder.cachedPath.getFinalPathPoint().distanceToTarget > 5))
        {
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            World world = beeEntity.world;

            for(int attempt = 0; attempt < 11; attempt++){
                // pick a random place to fly to
                mutable.setPos(beeEntity.getPosition()).move(
                        world.rand.nextInt(21) - 10,
                        world.rand.nextInt(21) - 10,
                        world.rand.nextInt(21) - 10
                );

                if(world.getBlockState(mutable).isAir()){
                    break; // Valid spot to go towards.
                }
            }

            Path newPath = beeEntity.getNavigator().getPathToPos(mutable, 1);
            beeEntity.getNavigator().setPath(newPath, 1);

            if(cachedPathHolder == null){
                cachedPathHolder = new CachedPathHolder();
            }
            cachedPathHolder.cachedPath = newPath;
            cachedPathHolder.pathTimer = 0;
            return cachedPathHolder;
        }
        else{
            beeEntity.getNavigator().setPath(cachedPathHolder.cachedPath, 1);
            cachedPathHolder.pathTimer += 1;
            return cachedPathHolder;
        }
    }

    public static class CachedPathHolder {
        public Path cachedPath;
        public int pathTimer = 0;

        public CachedPathHolder(){}
    }
}
