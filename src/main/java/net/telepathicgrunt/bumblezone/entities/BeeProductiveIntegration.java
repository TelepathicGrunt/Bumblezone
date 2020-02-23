package net.telepathicgrunt.bumblezone.entities;

import io.github.alloffabric.beeproductive.init.BeeProdNectars;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.IWorld;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.dimension.BzDimensionType;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.swing.plaf.synth.Region;
import java.util.Random;

public class BeeProductiveIntegration
{
    //spawns bees with chance to bee full of pollen or be a BeeProductive mob if that mod is on
    public static Entity spawnBeeProductiveBee(Random random, Entity entity) {

        // If BeeProduction is on, add a rare chance to spawn their bees too

        /*

        float choosenChance = random.nextFloat();
        float thresholdRange = 0.006f; //total chance of 3.6% to spawn a BeeProductive bee.
        Bumblezone.LOGGER.log(Level.INFO, " within the productive class method");
        if(choosenChance < thresholdRange){
            BeeProdNectars.GAY_SKIN.onApply((BeeEntity)entity, null);
            Bumblezone.LOGGER.log(Level.INFO, " applied GAY SKIN");
        }
        else if(choosenChance < thresholdRange*2){
            BeeProdNectars.BI_SKIN.onApply((BeeEntity)entity, null);
        }
        else if(choosenChance < thresholdRange*3){
            BeeProdNectars.LESBIAN_SKIN.onApply((BeeEntity)entity, null);
        }
        else if(choosenChance < thresholdRange*4){
            BeeProdNectars.NONBINARY_SKIN.onApply((BeeEntity)entity, null);
        }
        else if(choosenChance < thresholdRange*5){
            BeeProdNectars.PAN_SKIN.onApply((BeeEntity)entity, null);
        }
        else if(choosenChance < thresholdRange*6){
            BeeProdNectars.TRANS_SKIN.onApply((BeeEntity)entity, null);
        }

         */

        return entity;
    }
}