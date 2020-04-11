package net.telepathicgrunt.bumblezone.modcompatibility;

import com.bagel.buzzierbees.common.entities.HoneySlimeEntity;
import com.bagel.buzzierbees.core.registry.BBEntities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.MobEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.telepathicgrunt.bumblezone.generation.BzChunkGenerator;

public class BuzzierBeesCompat
{
	
	
	public static void setupBuzzierBees() 
	{
		ModChecking.buzzierBeesPresent = true;
		BzChunkGenerator.MOBS_SLIME_ENTRY = new Biome.SpawnListEntry(BBEntities.HONEY_SLIME.get(), 1, 1, 1);
		//BzBiomes.biomes.forEach(biome -> ((BzBaseBiome)biome).addModMobs(EntityClassification.CREATURE, BBEntities.HONEY_SLIME.get(), 1, 4, 8));
		
	}
	
	//1/10th of bees spawning will also spawn honey slime
	@SuppressWarnings("deprecation")
	public static void BBMobSpawnEvent(LivingSpawnEvent.CheckSpawn event)
	{
		MobEntity entity = (MobEntity)event.getEntity();
		IWorld world = event.getWorld();
		
		if(entity.getType() == EntityType.BEE && world.getRandom().nextInt(10) == 0) {
			MobEntity slimeentity = new HoneySlimeEntity(BBEntities.HONEY_SLIME.get(), entity.world);
			
			//move down to first non-air block
			BlockPos.Mutable blockpos = new BlockPos.Mutable(entity.getPosition());
			while(world.getBlockState(blockpos).isAir()) {
				blockpos.move(Direction.DOWN);
			}
			blockpos.move(Direction.UP);
			
			slimeentity.setLocationAndAngles(blockpos.getX(), blockpos.getY(), blockpos.getZ(), world.getRandom().nextFloat() * 360.0F, 0.0F);
			ILivingEntityData ilivingentitydata = null;
			ilivingentitydata = slimeentity.onInitialSpawn(world, world.getDifficultyForLocation(new BlockPos(slimeentity)), event.getSpawnReason(), ilivingentitydata, (CompoundNBT) null);
			world.addEntity(slimeentity);
		}
	}
}
