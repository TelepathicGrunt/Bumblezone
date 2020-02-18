package net.telepathicgrunt.bumblezone.dimension;

import com.sun.istack.internal.Nullable;
import net.fabricmc.fabric.api.dimension.v1.EntityPlacer;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.telepathicgrunt.bumblezone.features.decorators.BzPlacingUtils;

/**
 * Derived from modmuss50 at https://github.com/modmuss50/SimpleVoidWorld/blob/1.15/src/main/java/me/modmuss50/svw/VoidPlacementHandler.java
 *
 */
public class BzPlacement
{
	//use this to teleport to any dimension
	//FabricDimensions.teleport(playerEntity, <destination dimension type>, <placement>);


	public static final EntityPlacer ENTERING = (teleported, destination, portalDir, horizontalOffset, verticalOffset) ->
	{
		Vec3d destinationPosition;

		if(teleported instanceof PlayerEntity){
			MinecraftServer minecraftServer = teleported.getServer(); // the server itself
			destinationPosition = teleportByPearl((PlayerEntity)teleported, minecraftServer.getWorld(teleported.dimension), destination);
		}
		else{
			destinationPosition = new Vec3d(0,0,0);
		}

		return new BlockPattern.TeleportTarget(
				destinationPosition,
				Vec3d.ZERO,
				(int)teleported.yaw);
	};

	public static final EntityPlacer LEAVING = (teleported, destination, portalDir, horizontalOffset, verticalOffset) ->
	{
		boolean upwardChecking = teleported.getY() > 0 ? true : false;
		Vec3d destinationPosition;

		if(teleported instanceof PlayerEntity){
			destinationPosition = teleportByOutOfBounds((PlayerEntity)teleported, destination, upwardChecking);
		}
		else{
			destinationPosition = new Vec3d(0,0,0);
		}

		return new BlockPattern.TeleportTarget(
				destinationPosition,
				Vec3d.ZERO,
				(int)teleported.yaw);
	};


	private static Vec3d teleportByOutOfBounds(PlayerEntity playerEntity, ServerWorld destination, boolean checkingUpward)
	{
		//gets the world in the destination dimension
		MinecraftServer minecraftServer = playerEntity.getServer(); // the server itself
		ServerWorld destinationWorld;

		//Error. This shouldn't be. We aren't leaving the bumblezone to go to the bumblezone. 
		//Go to Overworld instead as default
		if(true)//cap.getNonBZDim() == BzDimensionType.BUMBLEZONE_TYPE)
		{
			destinationWorld = minecraftServer.getWorld(DimensionType.OVERWORLD); // go to overworld by default
		}
		else 
		{
			if(true) //BzConfig.forceExitToOverworld
			{
				destinationWorld = minecraftServer.getWorld(DimensionType.OVERWORLD); // go to Overworld directly.
			}
			else
			{
				//destinationWorld = destination; // gets the previous dimension user came from
			}
		}


		//converts the position to get the corresponding position in non-bumblezone dimension
		double coordinateScale = destinationWorld.dimension.isNether() ? 8D / 10D : 1D / 10D;
		BlockPos blockpos = new BlockPos(
				playerEntity.getPos().getX() * coordinateScale,
				playerEntity.getPos().getY(), 
				playerEntity.getPos().getZ() * coordinateScale);


		//Gets valid space in other world
		//Won't ever be null
		BlockPos validBlockPos = validPlayerSpawnLocationByBeehive(destinationWorld, blockpos, 48, checkingUpward);


		//let game know we are gonna teleport player
		ChunkPos chunkpos = new ChunkPos(validBlockPos);
//		destinationWorld.getChunkManager().addTicket(ChunkTicketType.POST_TELEPORT, chunkpos, 1, playerEntity.getEntityId());
//
//		((ServerPlayerEntity)playerEntity).teleport(
//			destinationWorld,
//			validBlockPos.getX() + 0.5D,
//			validBlockPos.getY() + 1,
//			validBlockPos.getZ() + 0.5D,
//			playerEntity.yaw,
//			playerEntity.pitch);
		return new Vec3d(
			validBlockPos.getX() + 0.5D,
			validBlockPos.getY() + 1,
			validBlockPos.getZ() + 0.5D
		);

		//teleportation complete. 
		//cap.setTeleporting(false);
	}

	private static Vec3d teleportByPearl(PlayerEntity playerEntity, ServerWorld originalWorld, ServerWorld bumblezoneWorld)
	{


		//converts the position to get the corresponding position in bumblezone dimension
		double coordinateScale = originalWorld.dimension.isNether() ? 10D / 8D : 10D;
		BlockPos blockpos = new BlockPos(
				playerEntity.getPos().getX() * coordinateScale,
				playerEntity.getPos().getY(), 
				playerEntity.getPos().getZ() * coordinateScale);


		//gets valid space in other world
		BlockPos validBlockPos = validPlayerSpawnLocation(bumblezoneWorld, blockpos, 10);


		//No valid space found around destination. Begin secondary valid spot algorithms
		if (validBlockPos == null)
		{
			//Check if destination position is in air and if so, go down to first solid land.
			if(bumblezoneWorld.getBlockState(blockpos).getMaterial() == Material.AIR &&
			   bumblezoneWorld.getBlockState(blockpos.up()).getMaterial() == Material.AIR) 
			{
				validBlockPos = new BlockPos(
						blockpos.getX(), 
						BzPlacingUtils.topOfSurfaceBelowHeight(bumblezoneWorld, blockpos.getY(), 0, blockpos),
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
				bumblezoneWorld.setBlockState(blockpos, Blocks.AIR.getDefaultState());
				bumblezoneWorld.setBlockState(blockpos.up(), Blocks.AIR.getDefaultState());
				bumblezoneWorld.setBlockState(blockpos.down(), Blocks.BEE_NEST.getDefaultState());
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
//		bumblezoneWorld.getChunkManager().addTicket(ChunkTicketType.POST_TELEPORT, chunkpos, 1, playerEntity.getEntityId());
//
//		((ServerPlayerEntity)playerEntity).teleport(
//			bumblezoneWorld,
//			validBlockPos.getX() + 0.5D,
//			validBlockPos.getY() + 1,
//			validBlockPos.getZ() + 0.5D,
//			playerEntity.yaw,
//			playerEntity.pitch);

		return new Vec3d(
				validBlockPos.getX() + 0.5D,
				validBlockPos.getY() + 1,
				validBlockPos.getZ() + 0.5D
		);

		//teleportation complete. 
		//cap.setTeleporting(false);
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
				mutableBlockPos.set(position.getX() + x, 0, position.getZ() + z);
				if(!world.isChunkLoaded(mutableBlockPos.getX() >> 4, mutableBlockPos.getZ() + z >> 4))
				{
					//make game generate chunk so we can get max height of blocks in it
					world.getChunk(mutableBlockPos);
				}
				maxHeight = Math.max(maxHeight, world.getTopY(Heightmap.Type.MOTION_BLOCKING, mutableBlockPos.getX(), mutableBlockPos.getZ()));
			}
		}
		maxHeight = Math.min(maxHeight, world.getEffectiveHeight()-1); //cannot place user at roof of other dimension

		//snaps the coordinates to chunk origin and then sets height to minimum or maximum based on search direction
		mutableBlockPos.set(position.getX(), checkingUpward ? 0 : maxHeight, position.getZ());


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
							mutableBlockPos.set(position.getX() + x2, mutableBlockPos.getY(), position.getZ() + z2);

							if (world.getBlockState(mutableBlockPos).getBlock() == Blocks.BEE_NEST)
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
				mutableBlockPos.setOffset(Direction.UP);
			}
			else
			{
				mutableBlockPos.setOffset(Direction.DOWN);
			}
		}


		//no valid spot was found, generate a hive and spawn us on the highest land
		//This if statement is so we dont get placed on roof of other roofed dimension
		if(maxHeight + 1 < world.getEffectiveHeight())
		{
			maxHeight += 1;
		}
		mutableBlockPos.set(
						position.getX(), 
						BzPlacingUtils.topOfSurfaceBelowHeight(world, maxHeight, 0, position), 
						position.getZ());

		if(mutableBlockPos.getY() > 0)
		{
			world.setBlockState(mutableBlockPos, Blocks.BEE_NEST.getDefaultState());
			world.setBlockState(mutableBlockPos.up(), Blocks.AIR.getDefaultState());
			return mutableBlockPos;
		}
		else
		{
			//No valid spot was found. Just place character on a generate hive at center of height of coordinate 
			//Basically just f*** it at this point lol
			mutableBlockPos.set(
							position.getX(), 
							world.getEffectiveHeight()/2,
							position.getZ());

			world.setBlockState(mutableBlockPos, Blocks.BEE_NEST.getDefaultState());
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
							currentPos.set(position.add(x2, y2, z2));
							if (world.getBlockState(currentPos.down()).isOpaque() &&
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
