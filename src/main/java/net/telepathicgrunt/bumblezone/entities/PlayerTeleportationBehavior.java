package net.telepathicgrunt.bumblezone.entities;

public class PlayerTeleportationBehavior
{

//	@CapabilityInject(IPlayerPosAndDim.class)
//	public static Capability<IPlayerPosAndDim> PAST_POS_AND_DIM = null;
//
//	@Mod.EventBusSubscriber(modid = Bumblezone.MODID)
//	private static class ForgeEvents
//	{
//
//		@SubscribeEvent
//		public static void ProjectileImpactEvent(net.minecraftforge.event.entity.ProjectileImpactEvent.Throwable event)
//		{
//			EnderPearlEntity pearlEntity; 
//
//			if (event.getEntity() instanceof EnderPearlEntity)
//			{
//				pearlEntity = (EnderPearlEntity) event.getEntity(); // the thrown pearl
//			}
//			else
//			{
//				return; //not a pearl, exit event
//			}
//
//			World world = pearlEntity.world; // world we threw in
//
//			//Make sure we are on server by checking if thrower is ServerPlayerEntity
//			if (!world.isRemote && pearlEntity.getThrower() instanceof ServerPlayerEntity)
//			{
//				ServerPlayerEntity playerEntity = (ServerPlayerEntity) pearlEntity.getThrower(); // the thrower
//				Vec3d hitBlockPos = event.getRayTraceResult().getHitVec(); //position of the collision
//				boolean hitHive = false;
//				
//				//check with offset in all direction as the position of exact hit point could barely be outside the hive block
//				//even through the pearl hit the block directly.
//				if(world.getBlockState(new BlockPos(hitBlockPos.add(0.1D, 0, 0))).getBlock() == Blocks.field_226905_ma_ ||
//				   world.getBlockState(new BlockPos(hitBlockPos.add(-0.1D, 0, 0))).getBlock() == Blocks.field_226905_ma_ ||
//				   world.getBlockState(new BlockPos(hitBlockPos.add(0, 0, 0.1D))).getBlock() == Blocks.field_226905_ma_ ||
//				   world.getBlockState(new BlockPos(hitBlockPos.add(0, 0, -0.1D))).getBlock() == Blocks.field_226905_ma_ ||
//				   world.getBlockState(new BlockPos(hitBlockPos.add(0, 0.1D, 0))).getBlock() == Blocks.field_226905_ma_ ||
//				   world.getBlockState(new BlockPos(hitBlockPos.add(0, -0.1D, 0))).getBlock() == Blocks.field_226905_ma_  ) 
//				{
//					hitHive = true;
//				}
//
//				//if the pearl hit a beehive and is not in our bee dimension, begin the teleportation.
//				if (hitHive && playerEntity.dimension != BzDimension.bumblezone())
//				{
//					//Store current dimension and position of hit 
//
//					//grabs the capability attached to player for dimension hopping
//					PlayerPositionAndDimension cap = (PlayerPositionAndDimension) playerEntity.getCapability(PAST_POS_AND_DIM).orElseThrow(RuntimeException::new);
//					DimensionType destination = BzDimension.bumblezone();
//
//
//					//Store current dim, next dim, and tells player they are in teleporting phase now.
//					//
//					//We have to do the actual teleporting during the player tick event as if we try and teleport
//					//in this event, the game will crash as it would be removing an entity during entity ticking.
//					cap.setDestDim(destination);
//					cap.setTeleporting(true);
//					
//					//canceled the original ender pearl's event so other mods don't do stuff.
//					event.setCanceled(true);
//					
//					// remove enderpearl so it cannot teleport us
//					pearlEntity.remove(); 
//				}
//			}
//		}
//		
//
//		@SubscribeEvent
//		public static void playerTick(PlayerTickEvent event)
//		{
//			//grabs the capability attached to player for dimension hopping
//			PlayerEntity playerEntity = event.player;
//			
//			if(playerEntity.world instanceof ServerWorld)
//			{
//				PlayerPositionAndDimension cap = (PlayerPositionAndDimension) playerEntity.getCapability(PAST_POS_AND_DIM).orElseThrow(RuntimeException::new);
//			
//				//teleported by pearl to enter into bumblezone dimension
//				if (cap.isTeleporting)
//				{
//					teleportByPearl(playerEntity, cap);
//					reAddPotionEffect(playerEntity);
//				}
//				//teleported by going out of bounds to leave bumblezone dimension
//				else if(playerEntity.dimension == BzDimension.bumblezone() && 
//					    (playerEntity.getY() < -1 || playerEntity.getY() > 255)) 
//				{
//					teleportByOutOfBounds(playerEntity, cap, playerEntity.getY() < -1 ? true : false);
//					reAddPotionEffect(playerEntity);
//				}
//			}
//		}
//		
//
//
//		// Fires just before the teleportation to new dimension begins
//		@SubscribeEvent
//		public static void entityTravelToDimensionEvent(EntityTravelToDimensionEvent event)
//		{
//			if(event.getEntity() instanceof PlayerEntity)
//			{
//				// Updates the non-BZ dimension that the player is leaving if going to BZ
//				PlayerEntity playerEntity = (PlayerEntity) event.getEntity();
//				PlayerPositionAndDimension cap = (PlayerPositionAndDimension) playerEntity.getCapability(PAST_POS_AND_DIM).orElseThrow(RuntimeException::new);
//				if(playerEntity.dimension != BzDimension.bumblezone())
//				{
//					cap.setNonBZDim(playerEntity.dimension);
//				}
//			}
//		}
//	}
//	
//	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	//Effects
//	
//	/**
//	 * Temporary fix until Mojang patches the bug that makes potion effect icons disappear when changing dimension.
//	 * To fix it ourselves, we remove the effect and re-add it to the player.
//	 */
//	private static void reAddPotionEffect(PlayerEntity playerEntity) 
//	{
//		//re-adds potion effects so the icon remains instead of disappearing when changing dimensions due to a bug
//		ArrayList<EffectInstance> effectInstanceList = new ArrayList<EffectInstance>(playerEntity.getActivePotionEffects());
//		for(int i = effectInstanceList.size() - 1; i >= 0; i--)
//		{
//			EffectInstance effectInstance = effectInstanceList.get(i);
//			if(effectInstance != null) 
//			{
//				playerEntity.removeActivePotionEffect(effectInstance.getPotion());
//				playerEntity.addPotionEffect(
//						new EffectInstance(
//								effectInstance.getPotion(), 
//								effectInstance.getDuration(), 
//								effectInstance.getAmplifier(), 
//								effectInstance.isAmbient(), 
//								effectInstance.doesShowParticles(), 
//								effectInstance.isShowIcon()));
//			}
//		}
//	}
//
}
