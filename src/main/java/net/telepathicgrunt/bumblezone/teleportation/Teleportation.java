package net.telepathicgrunt.bumblezone.teleportation;

import javax.annotation.Nullable;

import net.minecraft.block.Blocks;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
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
import net.telepathicgrunt.bumblezone.world.dimension.BumblezoneDimension;
import net.telepathicgrunt.bumblezone.world.feature.placement.PlacingUtils;


@Mod.EventBusSubscriber(modid = Bumblezone.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Teleportation
{

	@CapabilityInject(IPlayerPosAndDim.class)
	public static Capability<IPlayerPosAndDim> PAST_POS_AND_DIM = null;

	@Mod.EventBusSubscriber(modid = Bumblezone.MODID)
	private static class ForgeEvents
	{

		@SubscribeEvent
		public static void ProjectileImpactEvent(net.minecraftforge.event.entity.ProjectileImpactEvent.Throwable event)
		{
			EnderPearlEntity pearlEntity; // the thrown pearl

			if (event.getEntity() instanceof EnderPearlEntity)
			{
				pearlEntity = (EnderPearlEntity) event.getEntity();
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
				if (hitHive && playerEntity.dimension != BumblezoneDimension.bumblezone())
				{
					//Store current dimension and position of hit 

					//grabs the capability attached to player for dimension hopping
					PlayerPositionAndDimension cap = (PlayerPositionAndDimension) playerEntity.getCapability(PAST_POS_AND_DIM).orElseThrow(RuntimeException::new);
					DimensionType destination;

					//first trip will always take player to bumblezone dimension
					//as default dim for cap is always null when player hasn't teleported yet
					if (cap.getDim() == null)
					{
						destination = BumblezoneDimension.bumblezone();
					}
					// if our stored dimension somehow ends up being our current dimension and isn't the bumblezone dimension, 
					else if (cap.getDim() == playerEntity.dimension)
					{
						// then just take us to bumblezone dimension instead
						if (cap.getDim() != BumblezoneDimension.bumblezone())
						{
							destination = BumblezoneDimension.bumblezone();
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
					
					//canceled the original ender pearl's teleportation.
					event.setCanceled(true);
				}
			}
		}
		

		@SubscribeEvent
		public static void playerTick(PlayerTickEvent event)
		{
			//grabs the capability attached to player for dimension hopping
			PlayerEntity playerEntity = event.player;
			PlayerPositionAndDimension cap = (PlayerPositionAndDimension) playerEntity.getCapability(PAST_POS_AND_DIM).orElseThrow(RuntimeException::new);
			
			if(!playerEntity.world.isRemote && playerEntity.world instanceof ServerWorld)
			{
				//teleported by pearl outside bumblezone dimension
				if (cap.isTeleporting)
				{
					teleportByPearl(playerEntity, cap);
				}
				//teleported by going out of bounds inside bumblezone dimension
				else if(playerEntity.dimension == BumblezoneDimension.bumblezone() && 
					     (playerEntity.getY() < 0 || playerEntity.getY() > 255)) 
				{
					teleportByOutOfBounds(playerEntity, cap, playerEntity.getY() < 0 ? true : false);
				}
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
		
		//Error. This shouldn't be. We aren't leaving the bumblezone to go to the bumblezone. 
		//Go to Overworld instead as default
		if(cap.prevDimension == BumblezoneDimension.bumblezone())
		{
			destinationWorld = minecraftServer.getWorld(DimensionType.OVERWORLD); // go to overworld by default
		}
		else 
		{
			destinationWorld = minecraftServer.getWorld(cap.prevDimension); // gets the previous dimension user came from
		}
		
		
		//scales the position to get the corresponding position in non-bumblezone dimension
		BlockPos blockpos = new BlockPos(
				playerEntity.getPosition().getX() / destinationWorld.getDimension().getMovementFactor(), 
				playerEntity.getPosition().getY(), 
				playerEntity.getPosition().getZ() / destinationWorld.getDimension().getMovementFactor());
		
		
		//Gets valid space in other world
		//Won't ever be null
		BlockPos validBlockPos = validPlayerSpawnLocationByBeehive(destinationWorld, blockpos, 16, checkingUpward);
		

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
	
	private static void teleportByPearl(PlayerEntity playerEntity, PlayerPositionAndDimension cap)
	{
		//gets the world in the destination dimension
		MinecraftServer minecraftServer = playerEntity.getServer(); // the server itself
		ServerWorld destinationWorld = minecraftServer.getWorld(cap.nextDimension); // gets the new destination dimension 

		
		//scales the position to get the corresponding position in bumblezone dimension
		BlockPos blockpos = new BlockPos(
				playerEntity.getPosition().getX() * destinationWorld.getDimension().getMovementFactor(), 
				playerEntity.getPosition().getY(), 
				playerEntity.getPosition().getZ() * destinationWorld.getDimension().getMovementFactor());
		
		
		//gets valid space in other world
		BlockPos validBlockPos = validPlayerSpawnLocation(destinationWorld, blockpos, 16);
		
		
		if (validBlockPos == null)
		{
			//No valid space found around portal. 
			//We are going to spawn player at exact spot of scaled coordinates by placing air at the spot
			destinationWorld.setBlockState(blockpos, Blocks.AIR.getDefaultState());
			destinationWorld.setBlockState(blockpos.up(), Blocks.AIR.getDefaultState());
			validBlockPos = blockpos;
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
		int radius = 16 * 16 - 1;
		
		//snaps the coordinates to chunk origin and then sets height to minimum or maximum based on search direction
		BlockPos.Mutable mutableBlockPos = new BlockPos.Mutable(
														position.getX(), 
														checkingUpward ? 0 : world.getDimension().getActualHeight(), 
														position.getZ()); 
		
		
		for (; mutableBlockPos.getY() < 0 || mutableBlockPos.getY() > world.getDimension().getActualHeight();)
		{
			for (int range = 1; range < maximumRange; range++)
			{
				for (int x = -range; x <= range; x++)
				{
					for (int z = -range; z <= range; z++)
					{
						if (x * x + z * z >= radius)
						{
							mutableBlockPos.setPos(position.getX() + x, mutableBlockPos.getY(), position.getZ() + z);
							
							if (world.getBlockState(mutableBlockPos).getBlock() == Blocks.field_226905_ma_)
							{
								//A Hive was found, try to find a valid spot next to it
								BlockPos validSpot = validPlayerSpawnLocation(world, mutableBlockPos, 3);
								if(validSpot != null) {
									return validSpot;
								}
							}
						}
					}
				}
			}
			
			//move the block pos
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
						PlacingUtils.topOfSurfaceBelowHeight(world, world.getDimension().getActualHeight(), 0, world.rand, position), 
						position.getZ());
		
		if(mutableBlockPos.getY() > 0)
		{
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

			world.setBlockState(mutableBlockPos.up(), Blocks.AIR.getDefaultState());
			return mutableBlockPos;
		}
	}
	
	@Nullable
	private static BlockPos validPlayerSpawnLocation(World world, BlockPos position, int maximumRange)
	{
		//Try to find 2 non-solid spaces around it that the player can spawn at
		int radius = 0;

		for (int range = 0; range < maximumRange; range++)
		{
			radius = range * range * range - 1;

			for (int x = -range; x <= range; x++)
			{
				for (int z = -range; z <= range; z++)
				{
					for (int y = -range; y <= range; y++)
					{
						if (x * x + z * z + y * y >= radius)
						{
							if (!world.getBlockState(position.add(x, y, z)).isSolid() && !world.getBlockState(position.add(x, y + 1, z)).isSolid())
							{
								//valid space for player is found
								return position.add(x, y, z);
							}
						}
					}
				}
			}
		}

		return null;
	}
}
