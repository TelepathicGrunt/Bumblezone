package net.telepathicgrunt.bumblezone.modcompatibility;

import java.util.ArrayList;
import java.util.List;

import cy.jdkdigital.productivebees.block.AdvancedBeehive;
import cy.jdkdigital.productivebees.block.AdvancedBeehiveAbstract;
import cy.jdkdigital.productivebees.block.ExpansionBox;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.MobEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.telepathicgrunt.bumblezone.Bumblezone;

public class ProductiveBeesCompat {
    
    public static List<EntityType<?>> productiveBeesList = new ArrayList<EntityType<?>>();
    
    public static void setupProductiveBees() 
    {
	ModChecking.productiveBeesPresent = true;
	
	//create list of all Productive Bees' bees
	for(EntityType<?> productiveBeeType : ForgeRegistries.ENTITIES) 
	{
	    if(productiveBeeType.getRegistryName().getNamespace().equals("productivebees") &&
	       productiveBeeType.getRegistryName().getPath().equals("bee")) 
	    {
		productiveBeesList.add(productiveBeeType);
	    }
	}
    }

    /**
     * Is block is a ProductiveBees nest or beenest block
     */
    public static boolean PBIsAdvancedBeehiveAbstractBlock(BlockState block) {
	
	if (block.getBlock() instanceof ExpansionBox && block.get(AdvancedBeehive.EXPANDED)) {
	    return true; // expansion boxes only count as beenest when they expand a hive.
	} 
	else if (block.getBlock() instanceof AdvancedBeehiveAbstract) {
	    return true; // nests/hives here so return true
	}

	return false;
    }

    // 1/15th of bees spawning will also spawn Productive Bees' bees
    public static void PBMobSpawnEvent(LivingSpawnEvent.CheckSpawn event) {
	
	if(productiveBeesList.size() == 0) {
	    Bumblezone.LOGGER.warn("Error! List of productive bees is empty! Cannot spawn their bees. Please let TelepathicGrunt (The Bumblezone dev) know about this!"); 
	    return;
	}
	
	MobEntity entity = (MobEntity) event.getEntity();
	IWorld world = event.getWorld();

	//randomly pick a productive bee
	MobEntity productiveBeeEntity = (MobEntity) productiveBeesList.get(world.getRandom().nextInt(productiveBeesList.size())).create(entity.world);

	BlockPos.Mutable blockpos = new BlockPos.Mutable(entity.getPosition());
	productiveBeeEntity.setLocationAndAngles(
		blockpos.getX(), 
		blockpos.getY(), 
		blockpos.getZ(),
		world.getRandom().nextFloat() * 360.0F, 
		0.0F);
	
	ILivingEntityData ilivingentitydata = null;
	ilivingentitydata = productiveBeeEntity.onInitialSpawn(
		world,
		world.getDifficultyForLocation(new BlockPos(productiveBeeEntity)), 
		event.getSpawnReason(),
		ilivingentitydata, 
		(CompoundNBT) null);
	
	world.addEntity(productiveBeeEntity);
    }
}
