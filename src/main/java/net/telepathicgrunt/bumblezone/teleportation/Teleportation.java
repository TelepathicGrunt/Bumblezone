package net.telepathicgrunt.bumblezone.teleportation;

import javax.annotation.Nullable;

import net.minecraft.block.Blocks;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.capabilities.IPlayerPosAndDim;
import net.telepathicgrunt.bumblezone.capabilities.PlayerPositionAndDimension;
import net.telepathicgrunt.bumblezone.world.dimension.BumblezoneDimension;


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


			//Make sure we are on server by checking if thrower is ServerPlayerEntity
			if (pearlEntity.getThrower() instanceof ServerPlayerEntity)
			{
				World world = pearlEntity.world; // world we threw in
				ServerPlayerEntity playerEntity = (ServerPlayerEntity)pearlEntity.getThrower(); // the thrower
				MinecraftServer minecraftserver = pearlEntity.getServer(); // the server itself
				BlockPos hitBlockPos = new BlockPos(event.getRayTraceResult().getHitVec()); //position of the block we hit

				//if the pearl hit a beehive and is not in our bee dimension, begin the teleportation.
				if (world.getBlockState(hitBlockPos).getBlock() == Blocks.field_226905_ma_ &&
					playerEntity.dimension != BumblezoneDimension.bumblezone())
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

					
					
					//gets the world in the destination dimension
					ServerWorld serverWorld = minecraftserver.getWorld(destination);

					//gets valid space in other world
					BlockPos blockpos = new BlockPos(
							playerEntity.getPosition().getX() * serverWorld.getDimension().getMovementFactor(), 
							playerEntity.getPosition().getY(), 
							playerEntity.getPosition().getZ() * serverWorld.getDimension().getMovementFactor());
					BlockPos validBlockPos = validPlayerSpawnLocation(serverWorld, blockpos, 16);

					
					if (validBlockPos == null)
					{
						//No valid space found around portal. 
						//We are going to spawn player at exact spot of scaled coordinates by placing air at the spot
						serverWorld.setBlockState(blockpos, Blocks.AIR.getDefaultState());
						serverWorld.setBlockState(blockpos.up(), Blocks.AIR.getDefaultState());
						validBlockPos = blockpos;
					}
					

					//let game know we are gonna teleport player
					ChunkPos chunkpos = new ChunkPos(validBlockPos);
					serverWorld.getChunkProvider().registerTicket(TicketType.POST_TELEPORT, chunkpos, 1, pearlEntity.getEntityId());

					//if player throws pearl at hive and then goes to sleep, they wake up
					if (((ServerPlayerEntity) playerEntity).isSleeping())
					{
						((ServerPlayerEntity) playerEntity).wakeUp();
					}

					//store current blockpos and dim before teleporting
					cap.setDim(playerEntity.dimension);
					cap.setPos(playerEntity.getPosition());

					((ServerPlayerEntity) playerEntity).teleport(
							minecraftserver.getWorld(destination), 
							validBlockPos.getX() + 0.5D, 
							validBlockPos.getY() + 1, 
							validBlockPos.getZ() + 0.5D, 
							playerEntity.rotationYaw, 
							playerEntity.rotationPitch);

					event.setCanceled(true); //canceled the original ender pearl's teleportation.
				}
			}
		}
	}
	
	@Nullable
	private static BlockPos validPlayerSpawnLocation(World world, BlockPos position, int maximumRange) 
	{
		//Try to find 2 non-solid spaces around it that the player can spawn at
		int radius = 0;

		for (int range = 1; range < maximumRange; range++)
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
