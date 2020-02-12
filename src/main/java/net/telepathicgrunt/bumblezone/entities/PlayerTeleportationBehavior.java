package net.telepathicgrunt.bumblezone.entities;

import java.util.ArrayList;

import javax.annotation.Nullable;

import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.capabilities.IPlayerPosAndDim;
import net.telepathicgrunt.bumblezone.capabilities.PlayerPositionAndDimension;
import net.telepathicgrunt.bumblezone.config.BzConfig;
import net.telepathicgrunt.bumblezone.dimension.BzDimension;
import net.telepathicgrunt.bumblezone.dimension.BzWorldProvider;
import net.telepathicgrunt.bumblezone.effects.BzEffects;
import net.telepathicgrunt.bumblezone.features.placement.BzPlacingUtils;


@Mod.EventBusSubscriber(modid = Bumblezone.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PlayerTeleportationBehavior
{

	@CapabilityInject(IPlayerPosAndDim.class)
	public static Capability<IPlayerPosAndDim> PAST_POS_AND_DIM = null;

	@Mod.EventBusSubscriber(modid = Bumblezone.MODID)
	private static class ForgeEvents
	{

		@SubscribeEvent
		public static void ProjectileImpactEvent(net.minecraftforge.event.entity.ProjectileImpactEvent.Throwable event)
		{
			EnderPearlEntity pearlEntity; 

			if (event.getEntity() instanceof EnderPearlEntity)
			{
				pearlEntity = (EnderPearlEntity) event.getEntity(); // the thrown pearl
			}
			else
			{
				return; //not a pearl, exit event
			}

			World world = pearlEntity.world; // world we threw in

			//Make sure we are on server by checking if thrower is ServerPlayerEntity
			if (!world.isRemote && pearlEntity.getThrower() instanceof ServerPlayerEntity)
			{
				ServerPlayerEntity playerEntity = (ServerPlayerEntity) pearlEntity.getThrower(); // the thrower
				Vec3d hitBlockPos = event.getRayTraceResult().getHitVec(); //position of the collision
				boolean hitHive = false;
				
				//check with offset in all direction as the position of exact hit point could barely be outside the hive block
				//even through the pearl hit the block directly.
				if(world.getBlockState(new BlockPos(hitBlockPos.add(0.1D, 0, 0))).getBlock() == Blocks.field_226905_ma_ ||
				   world.getBlockState(new BlockPos(hitBlockPos.add(-0.1D, 0, 0))).getBlock() == Blocks.field_226905_ma_ ||
				   world.getBlockState(new BlockPos(hitBlockPos.add(0, 0, 0.1D))).getBlock() == Blocks.field_226905_ma_ ||
				   world.getBlockState(new BlockPos(hitBlockPos.add(0, 0, -0.1D))).getBlock() == Blocks.field_226905_ma_ ||
				   world.getBlockState(new BlockPos(hitBlockPos.add(0, 0.1D, 0))).getBlock() == Blocks.field_226905_ma_ ||
				   world.getBlockState(new BlockPos(hitBlockPos.add(0, -0.1D, 0))).getBlock() == Blocks.field_226905_ma_  ) 
				{
					hitHive = true;
				}

				//if the pearl hit a beehive and is not in our bee dimension, begin the teleportation.
				if (hitHive && playerEntity.dimension != BzDimension.bumblezone())
				{
					//Store current dimension and position of hit 

					//grabs the capability attached to player for dimension hopping
					PlayerPositionAndDimension cap = (PlayerPositionAndDimension) playerEntity.getCapability(PAST_POS_AND_DIM).orElseThrow(RuntimeException::new);
					DimensionType destination;

					//first trip will always take player to bumblezone dimension
					//as default dim for cap is always null when player hasn't teleported yet
					if (cap.getDim() == null)
					{
						destination = BzDimension.bumblezone();
					}
					// if our stored dimension somehow ends up being our current dimension and isn't the bumblezone dimension, 
					else if (cap.getDim() == playerEntity.dimension)
					{
						// then just take us to bumblezone dimension instead
						if (cap.getDim() != BzDimension.bumblezone())
						{
							destination = BzDimension.bumblezone();
						}
						//if the store dimension and player dimension is ultra amplified world, take us to overworld
						else
						{
							destination = DimensionType.OVERWORLD;
						}
					}
					//gets and stores destination dimension
					else
					{
						destination = cap.getDim();
					}


					//Store current blockpos, current dim, next dim, and tells player they are in teleporting phase now.
					//
					//We have to do the actual teleporting during the player tick event as if we try and teleport
					//in this event, the game will crash as it would be removing an entity during entity ticking.
					cap.setPos(playerEntity.getPosition());
					cap.setDim(playerEntity.dimension);
					cap.setDestDim(destination);
					cap.setTeleporting(true);
					
					//canceled the original ender pearl's event so other mods don't do stuff.
					event.setCanceled(true);
					
					// remove enderpearl so it cannot teleport us
					pearlEntity.remove(); 
				}
			}
		}
		

		@SubscribeEvent
		public static void playerTick(PlayerTickEvent event)
		{
			//grabs the capability attached to player for dimension hopping
			PlayerEntity playerEntity = event.player;
			
			if(playerEntity.world instanceof ServerWorld)
			{
				PlayerPositionAndDimension cap = (PlayerPositionAndDimension) playerEntity.getCapability(PAST_POS_AND_DIM).orElseThrow(RuntimeException::new);
			
				//teleported by pearl to enter into bumblezone dimension
				if (cap.isTeleporting)
				{
					teleportByPearl(playerEntity, cap);
					reAddPotionEffect(playerEntity);
				}
				//teleported by going out of bounds to leave bumblezone dimension
				else if(playerEntity.dimension == BzDimension.bumblezone() && 
					    (playerEntity.getY() < -1 || playerEntity.getY() > 255)) 
				{
					teleportByOutOfBounds(playerEntity, cap, playerEntity.getY() < -1 ? true : false);
					reAddPotionEffect(playerEntity);
				}
				
				//removes the wrath of the hive if it is disallowed outside dimension
				if(!(BzConfig.allowWrathOfTheHiveOutsideBumblezone || playerEntity.dimension == BzDimension.bumblezone()) &&
					playerEntity.isPotionActive(BzEffects.WRATH_OF_THE_HIVE))
				{
					playerEntity.removePotionEffect(BzEffects.WRATH_OF_THE_HIVE);
				}
			}
			
			//Makes it so player does not get killed for falling into the void
			if(playerEntity.dimension == BzDimension.bumblezone() && playerEntity.getY() < -3)
			{
				playerEntity.setPosition(playerEntity.getX(), -3, playerEntity.getZ());
			}

			//Makes the fog redder when this effect is active
			if(playerEntity.isPotionActive(BzEffects.WRATH_OF_THE_HIVE))
			{
				BzWorldProvider.ACTIVE_WRATH = true;
			}
			else
			{
				BzWorldProvider.ACTIVE_WRATH = false;
			}
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Effects
	
	/**
	 * Temporary fix until Mojang patches the bug that makes potion effect icons disappear when changing dimension.
	 * To fix it ourselves, we remove the effect and re-add it to the player.
	 */
	private static void reAddPotionEffect(PlayerEntity playerEntity) 
	{
		//re-adds potion effects so the icon remains instead of disappearing when changing dimensions due to a bug
		ArrayList<EffectInstance> effectInstanceList = new ArrayList<EffectInstance>(playerEntity.getActivePotionEffects());
		for(int i = effectInstanceList.size() - 1; i >= 0; i--)
		{
			EffectInstance effectInstance = effectInstanceList.get(i);
			if(effectInstance != null) 
			{
				playerEntity.removeActivePotionEffect(effectInstance.getPotion());
				playerEntity.addPotionEffect(
						new EffectInstance(
								effectInstance.getPotion(), 
								effectInstance.getDuration(), 
								effectInstance.getAmplifier(), 
								effectInstance.isAmbient(), 
								effectInstance.doesShowParticles(), 
								effectInstance.isShowIcon()));
			}
		}
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Teleporting
	
	
	private static void teleportByOutOfBounds(PlayerEntity playerEntity, PlayerPositionAndDimension cap, boolean checkingUpward)
	{
		//gets the world in the destination dimension
		MinecraftServer minecraftServer = playerEntity.getServer(); // the server itself
		ServerWorld destinationWorld;
		ServerWorld bumblezoneWorld = minecraftServer.getWorld(BzDimension.bumblezone());
		
		//Error. This shouldn't be. We aren't leaving the bumblezone to go to the bumblezone. 
		//Go to Overworld instead as default
		if(cap.prevDimension == BzDimension.bumblezone())
		{
			destinationWorld = minecraftServer.getWorld(DimensionType.OVERWORLD); // go to overworld by default
		}
		else 
		{
			destinationWorld = minecraftServer.getWorld(cap.prevDimension); // gets the previous dimension user came from
		}
		
		
		//converts the position to get the corresponding position in non-bumblezone dimension
		BlockPos blockpos = new BlockPos(
				playerEntity.getPosition().getX() / destinationWorld.getDimension().getMovementFactor() * bumblezoneWorld.getDimension().getMovementFactor(), 
				playerEntity.getPosition().getY(), 
				playerEntity.getPosition().getZ() / destinationWorld.getDimension().getMovementFactor() * bumblezoneWorld.getDimension().getMovementFactor());
		
		
		//Gets valid space in other world
		//Won't ever be null
		BlockPos validBlockPos = validPlayerSpawnLocationByBeehive(destinationWorld, blockpos, 48, checkingUpward);
		

		//let game know we are gonna teleport player
		ChunkPos chunkpos = new ChunkPos(validBlockPos);
		destinationWorld.getChunkProvider().registerTicket(TicketType.POST_TELEPORT, chunkpos, 1, playerEntity.getEntityId());
		
		((ServerPlayerEntity)playerEntity).teleport(
			destinationWorld, 
			validBlockPos.getX() + 0.5D, 
			validBlockPos.getY() + 1, 
			validBlockPos.getZ() + 0.5D, 
			playerEntity.rotationYaw, 
			playerEntity.rotationPitch);

		
		//teleportation complete. 
		cap.setTeleporting(false);
	}
	
	private static void teleportByPearl(PlayerEntity playerEntity, PlayerPositionAndDimension cap)
	{
		//gets the world in the destination dimension
		MinecraftServer minecraftServer = playerEntity.getServer(); // the server itself
		ServerWorld originalWorld = minecraftServer.getWorld(cap.prevDimension);
		ServerWorld destinationWorld = minecraftServer.getWorld(cap.nextDimension); // gets the new destination dimension 

		
		//converts the position to get the corresponding position in bumblezone dimension
		BlockPos blockpos = new BlockPos(
				playerEntity.getPosition().getX() / destinationWorld.getDimension().getMovementFactor() * originalWorld.getDimension().getMovementFactor(), 
				playerEntity.getPosition().getY(), 
				playerEntity.getPosition().getZ() / destinationWorld.getDimension().getMovementFactor() * originalWorld.getDimension().getMovementFactor());
		
		
		//gets valid space in other world
		BlockPos validBlockPos = validPlayerSpawnLocation(destinationWorld, blockpos, 10);

		
		//No valid space found around destination. Begin secondary valid spot algorithms
		if (validBlockPos == null)
		{
			//Check if destination position is in air and if so, go down to first solid land.
			if(destinationWorld.getBlockState(blockpos).getMaterial() == Material.AIR && 
			   destinationWorld.getBlockState(blockpos.up()).getMaterial() == Material.AIR) 
			{
				validBlockPos = new BlockPos(
						blockpos.getX(), 
						BzPlacingUtils.topOfSurfaceBelowHeight(destinationWorld, blockpos.getY(), 0, blockpos),
						blockpos.getZ());
				
				//No solid land was found. Who digs out an entire chunk?!
				if(validBlockPos.getY() == 0)
				{
					validBlockPos = null;
				}
			}
			
			//still no valid position, time to force a valid location ourselves
			if(validBlockPos == null) 
			{
				//We are going to spawn player at exact spot of scaled coordinates by placing air at the spot with honeycomb bottom
				//This is the last resort
				destinationWorld.setBlockState(blockpos, Blocks.AIR.getDefaultState());
				destinationWorld.setBlockState(blockpos.up(), Blocks.AIR.getDefaultState());
				destinationWorld.setBlockState(blockpos.down(), Blocks.field_226908_md_.getDefaultState());
				validBlockPos = blockpos;
			}
			
		}

		//if player throws pearl at hive and then goes to sleep, they wake up
		if (playerEntity.isSleeping())
		{
			playerEntity.wakeUp();
		}


		//let game know we are gonna teleport player
		ChunkPos chunkpos = new ChunkPos(validBlockPos);
		destinationWorld.getChunkProvider().registerTicket(TicketType.POST_TELEPORT, chunkpos, 1, playerEntity.getEntityId());
		
		((ServerPlayerEntity)playerEntity).teleport(
			destinationWorld, 
			validBlockPos.getX() + 0.5D, 
			validBlockPos.getY() + 1, 
			validBlockPos.getZ() + 0.5D, 
			playerEntity.rotationYaw, 
			playerEntity.rotationPitch);
		
		//teleportation complete. 
		cap.setTeleporting(false);
	}

	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Util
	
	
	private static BlockPos validPlayerSpawnLocationByBeehive(World world, BlockPos position, int maximumRange, boolean checkingUpward)
	{
		
		// Gets the height of highest block over the area so we aren't checking an 
		// excessive amount of area above that doesn't need checking.
		int maxHeight = 0;
		int halfRange = maximumRange/2;
		BlockPos.Mutable mutableBlockPos = new BlockPos.Mutable(); 
		for(int x = -halfRange; x < halfRange; x++)
		{
			for(int z = -halfRange; z < halfRange; z++)
			{	
				mutableBlockPos.setPos(position.getX() + x, 0, position.getZ() + z);
				if(!world.chunkExists(mutableBlockPos.getX() >> 4, mutableBlockPos.getZ() + z >> 4))
				{
					//make game generate chunk so we can get max height of blocks in it
					world.getChunk(mutableBlockPos);
				}
				maxHeight = Math.max(maxHeight, world.getHeight(Heightmap.Type.MOTION_BLOCKING, mutableBlockPos.getX(), mutableBlockPos.getZ()));
			}
		}
		
		
		//snaps the coordinates to chunk origin and then sets height to minimum or maximum based on search direction
		mutableBlockPos.setPos(position.getX(), checkingUpward ? 0 : maxHeight, position.getZ()); 
		
		
		//scans range from y = 0 to dimension max height for a bee_nest
		//Does it by checking each y layer at a time
		for (; mutableBlockPos.getY() >= 0 && mutableBlockPos.getY() <= maxHeight;)
		{
			for (int range = 0; range < maximumRange; range++)
			{
				int radius = range * range;
				int nextRadius = range+1 * range+1;
				for (int x = 0; x <= range * 2; x++){
					int x2 = x > range ? -(x - range) : x;
					
					for (int z = 0; z <= range * 2; z++){
						int z2 = z > range ? -(z - range) : x;
						
						//checks within the circular ring and not check the same positions multiple times
						if (x2 * x2 + z2 * z2 >= radius && x2 * x2 + z2 * z2 < nextRadius)
						{
							mutableBlockPos.setPos(position.getX() + x2, mutableBlockPos.getY(), position.getZ() + z2);
							
							if (world.getBlockState(mutableBlockPos).getBlock() == Blocks.field_226905_ma_)
							{
								//A Hive was found, try to find a valid spot next to it
								BlockPos validSpot = validPlayerSpawnLocation(world, mutableBlockPos, 4);
								if(validSpot != null) {
									return validSpot;
								}
							}
						}
					}
				}
			}
			
			//move the block pos in the direction it needs to go
			if(checkingUpward)
			{
				mutableBlockPos.move(Direction.UP);
			}
			else
			{
				mutableBlockPos.move(Direction.DOWN);
			}
		}
		
		
		//no valid spot was found, generate a hive and spawn us on the highest land
		mutableBlockPos.setPos(
						position.getX(), 
						BzPlacingUtils.topOfSurfaceBelowHeight(world, maxHeight+1, 0, position), 
						position.getZ());
		
		if(mutableBlockPos.getY() > 0)
		{
			world.setBlockState(mutableBlockPos, Blocks.field_226905_ma_.getDefaultState());
			world.setBlockState(mutableBlockPos.up(), Blocks.AIR.getDefaultState());
			return mutableBlockPos;
		}
		else
		{
			//No valid spot was found. Just place character on a generate hive at center of height of coordinate 
			//Basically just f*** it at this point lol
			mutableBlockPos.setPos(
							position.getX(), 
							world.getDimension().getActualHeight()/2, 
							position.getZ());

			world.setBlockState(mutableBlockPos, Blocks.field_226905_ma_.getDefaultState());
			world.setBlockState(mutableBlockPos.up(), Blocks.AIR.getDefaultState());
			return mutableBlockPos;
		}
	}
	
	@Nullable
	private static BlockPos validPlayerSpawnLocation(World world, BlockPos position, int maximumRange)
	{
		//Try to find 2 non-solid spaces around it that the player can spawn at
		int radius = 0;
		int outterRadius = 0;
		int distanceSq = 0;
		BlockPos.Mutable currentPos = new BlockPos.Mutable(position);

		//checks for 2 non-solid blocks with solid block below feet
		//checks outward from center position in both x, y, and z.
		//The x2, y2, and z2 is so it checks at center of the range box instead of the corner.
		for (int range = 0; range < maximumRange; range++){
			radius = range * range;
			outterRadius = (range + 1) * (range + 1);

			for (int y = 0; y <= range * 2; y++){
				int y2 = y > range ? -(y - range) : y;
				
				
				for (int x = 0; x <= range * 2; x++){
					int x2 = x > range ? -(x - range) : x;
					
					
					for (int z = 0; z <= range * 2; z++){
						int z2 = z > range ? -(z - range) : z;
				
						distanceSq = x2 * x2 + z2 * z2 + y2 * y2;
						if (distanceSq >= radius && distanceSq < outterRadius)
						{
							currentPos.setPos(position.add(x2, y2, z2));
							if (world.getBlockState(currentPos.down()).isSolid() && 
								world.getBlockState(currentPos).getMaterial() == Material.AIR && 
								world.getBlockState(currentPos.up()).getMaterial() == Material.AIR)
							{
								//valid space for player is found
								return currentPos;
							}
						}
					}
				}
			}
		}
		
		return null;
	}
}
