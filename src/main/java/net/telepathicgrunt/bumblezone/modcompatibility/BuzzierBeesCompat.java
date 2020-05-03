package net.telepathicgrunt.bumblezone.modcompatibility;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.bagel.buzzierbees.core.registry.BBBlocks;
import com.bagel.buzzierbees.core.registry.BBEntities;
import com.bagel.buzzierbees.core.registry.BBItems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.material.Material;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.ModList;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import net.telepathicgrunt.bumblezone.generation.BzChunkGenerator;

public class BuzzierBeesCompat
{
	private static final DefaultDispenseItemBehavior BEHAVIOUR_BOTTLED_BEE_DISPENSE_ITEM = new BuzzierBeeBottledBeeDispenseBehavior();
	private static List<Block> TIER_1_CANDLES_VARIANTS;
	private static List<Block> TIER_2_CANDLES_VARIANTS;
	private static List<Block> TIER_3_CANDLES_VARIANTS;
	
	public static void setupBuzzierBees() 
	{
		ModChecking.buzzierBeesPresent = true;
		
		if(Bumblezone.BzConfig.spawnHoneySlimeMob.get())
			BzChunkGenerator.MOBS_SLIME_ENTRY = new Biome.SpawnListEntry(BBEntities.HONEY_SLIME.get(), 1, 1, 1);
		
		DispenserBlock.registerDispenseBehavior(BBItems.BOTTLE_OF_BEE.get(), BEHAVIOUR_BOTTLED_BEE_DISPENSE_ITEM); //adds compatibility with honey bottles in dispensers

		TIER_1_CANDLES_VARIANTS = new ArrayList<Block>();
		TIER_2_CANDLES_VARIANTS = new ArrayList<Block>();
		TIER_3_CANDLES_VARIANTS = new ArrayList<Block>();
		
		//tier 1 candles  - no effects
		
		TIER_1_CANDLES_VARIANTS.add(BBBlocks.CANDLE.get());
		TIER_1_CANDLES_VARIANTS.add(BBBlocks.WHITE_CANDLE.get());
		TIER_1_CANDLES_VARIANTS.add(BBBlocks.ORANGE_CANDLE.get());
		TIER_1_CANDLES_VARIANTS.add(BBBlocks.MAGENTA_CANDLE.get());
		TIER_1_CANDLES_VARIANTS.add(BBBlocks.LIGHT_BLUE_CANDLE.get());
		TIER_1_CANDLES_VARIANTS.add(BBBlocks.YELLOW_CANDLE.get());
		TIER_1_CANDLES_VARIANTS.add(BBBlocks.LIME_CANDLE.get());
		TIER_1_CANDLES_VARIANTS.add(BBBlocks.PINK_CANDLE.get());
		TIER_1_CANDLES_VARIANTS.add(BBBlocks.GRAY_CANDLE.get());
		TIER_1_CANDLES_VARIANTS.add(BBBlocks.LIGHT_GRAY_CANDLE.get());
		TIER_1_CANDLES_VARIANTS.add(BBBlocks.CYAN_CANDLE.get());
		TIER_1_CANDLES_VARIANTS.add(BBBlocks.PURPLE_CANDLE.get());
		TIER_1_CANDLES_VARIANTS.add(BBBlocks.BLUE_CANDLE.get());
		TIER_1_CANDLES_VARIANTS.add(BBBlocks.BROWN_CANDLE.get());
		TIER_1_CANDLES_VARIANTS.add(BBBlocks.GREEN_CANDLE.get());
		TIER_1_CANDLES_VARIANTS.add(BBBlocks.RED_CANDLE.get());
		TIER_1_CANDLES_VARIANTS.add(BBBlocks.BLACK_CANDLE.get());
			
		//tier 2 candles - no effects plus generally negative effect candles
		
		TIER_2_CANDLES_VARIANTS.add(BBBlocks.AMBER_CANDLE.get());
		TIER_2_CANDLES_VARIANTS.add(BBBlocks.BEIGE_CANDLE.get());
		TIER_2_CANDLES_VARIANTS.add(BBBlocks.CREAM_CANDLE.get());
		TIER_2_CANDLES_VARIANTS.add(BBBlocks.DARK_GREEN_CANDLE.get());
		TIER_2_CANDLES_VARIANTS.add(BBBlocks.FOREST_GREEN_CANDLE.get());
		TIER_2_CANDLES_VARIANTS.add(BBBlocks.HOT_PINK_CANDLE.get());
		TIER_2_CANDLES_VARIANTS.add(BBBlocks.INDIGO_CANDLE.get());
		TIER_2_CANDLES_VARIANTS.add(BBBlocks.MAROON_CANDLE.get());
		TIER_2_CANDLES_VARIANTS.add(BBBlocks.NAVY_CANDLE.get());
		TIER_2_CANDLES_VARIANTS.add(BBBlocks.OLIVE_CANDLE.get());
		TIER_2_CANDLES_VARIANTS.add(BBBlocks.PALE_GREEN_CANDLE.get());
		TIER_2_CANDLES_VARIANTS.add(BBBlocks.PALE_PINK_CANDLE.get());
		TIER_2_CANDLES_VARIANTS.add(BBBlocks.PALE_YELLOW_CANDLE.get());
		TIER_2_CANDLES_VARIANTS.add(BBBlocks.SKY_BLUE_CANDLE.get());
		TIER_2_CANDLES_VARIANTS.add(BBBlocks.SLATE_GRAY_CANDLE.get());
		TIER_2_CANDLES_VARIANTS.add(BBBlocks.VIOLET_CANDLE.get());
		
		TIER_2_CANDLES_VARIANTS.add(BBBlocks.COLUMBINE_SCENTED_CANDLE.get());
		TIER_2_CANDLES_VARIANTS.add(BBBlocks.WHITE_CLOVER_SCENTED_CANDLE.get());
		TIER_2_CANDLES_VARIANTS.add(BBBlocks.PINK_CLOVER_SCENTED_CANDLE.get());
		TIER_2_CANDLES_VARIANTS.add(BBBlocks.WHITE_TULIP_SCENTED_CANDLE.get());
		TIER_2_CANDLES_VARIANTS.add(BBBlocks.ORANGE_TULIP_SCENTED_CANDLE.get());
		TIER_2_CANDLES_VARIANTS.add(BBBlocks.PINK_TULIP_SCENTED_CANDLE.get());
		TIER_2_CANDLES_VARIANTS.add(BBBlocks.RED_TULIP_SCENTED_CANDLE.get());
		TIER_2_CANDLES_VARIANTS.add(BBBlocks.AZURE_BLUET_SCENTED_CANDLE.get());
		TIER_2_CANDLES_VARIANTS.add(BBBlocks.LILY_OF_THE_VALLEY_SCENTED_CANDLE.get());

		//tier 3 candles - no effect and generally positive effect candles + wither
		
		TIER_3_CANDLES_VARIANTS.add(BBBlocks.AMBER_CANDLE.get());
		TIER_3_CANDLES_VARIANTS.add(BBBlocks.BEIGE_CANDLE.get());
		TIER_3_CANDLES_VARIANTS.add(BBBlocks.CREAM_CANDLE.get());
		TIER_3_CANDLES_VARIANTS.add(BBBlocks.DARK_GREEN_CANDLE.get());
		TIER_3_CANDLES_VARIANTS.add(BBBlocks.FOREST_GREEN_CANDLE.get());
		TIER_3_CANDLES_VARIANTS.add(BBBlocks.HOT_PINK_CANDLE.get());
		TIER_3_CANDLES_VARIANTS.add(BBBlocks.INDIGO_CANDLE.get());
		TIER_3_CANDLES_VARIANTS.add(BBBlocks.MAROON_CANDLE.get());
		TIER_3_CANDLES_VARIANTS.add(BBBlocks.NAVY_CANDLE.get());
		TIER_3_CANDLES_VARIANTS.add(BBBlocks.OLIVE_CANDLE.get());
		TIER_3_CANDLES_VARIANTS.add(BBBlocks.PALE_GREEN_CANDLE.get());
		TIER_3_CANDLES_VARIANTS.add(BBBlocks.PALE_PINK_CANDLE.get());
		TIER_3_CANDLES_VARIANTS.add(BBBlocks.PALE_YELLOW_CANDLE.get());
		TIER_3_CANDLES_VARIANTS.add(BBBlocks.SKY_BLUE_CANDLE.get());
		TIER_3_CANDLES_VARIANTS.add(BBBlocks.SLATE_GRAY_CANDLE.get());
		TIER_3_CANDLES_VARIANTS.add(BBBlocks.VIOLET_CANDLE.get());

		TIER_3_CANDLES_VARIANTS.add(BBBlocks.YELLOW_HIBISCUS_SCENTED_CANDLE.get());
		TIER_3_CANDLES_VARIANTS.add(BBBlocks.ORANGE_HIBISCUS_SCENTED_CANDLE.get());
		TIER_3_CANDLES_VARIANTS.add(BBBlocks.RED_HIBISCUS_SCENTED_CANDLE.get());
		TIER_3_CANDLES_VARIANTS.add(BBBlocks.PINK_HIBISCUS_SCENTED_CANDLE.get());
		TIER_3_CANDLES_VARIANTS.add(BBBlocks.MAGENTA_HIBISCUS_SCENTED_CANDLE.get());
		TIER_3_CANDLES_VARIANTS.add(BBBlocks.PURPLE_HIBISCUS_SCENTED_CANDLE.get());
		
		TIER_3_CANDLES_VARIANTS.add(BBBlocks.BLUE_ORCHID_SCENTED_CANDLE.get());
		TIER_3_CANDLES_VARIANTS.add(BBBlocks.DANDELION_SCENTED_CANDLE.get());
		TIER_3_CANDLES_VARIANTS.add(BBBlocks.CORNFLOWER_SCENTED_CANDLE.get());
		TIER_3_CANDLES_VARIANTS.add(BBBlocks.CARTWHEEL_SCENTED_CANDLE.get());
		TIER_3_CANDLES_VARIANTS.add(BBBlocks.POPPY_SCENTED_CANDLE.get());
		TIER_3_CANDLES_VARIANTS.add(BBBlocks.OXEYE_DAISY_SCENTED_CANDLE.get());
		TIER_3_CANDLES_VARIANTS.add(BBBlocks.DIANTHUS_SCENTED_CANDLE.get());
		TIER_3_CANDLES_VARIANTS.add(BBBlocks.BLUEBELL_SCENTED_CANDLE.get());
		TIER_3_CANDLES_VARIANTS.add(BBBlocks.VIOLET_SCENTED_CANDLE.get());

		if(ModList.get().isLoaded("atmospheric")) {
			TIER_3_CANDLES_VARIANTS.add(BBBlocks.WARM_MONKEY_BRUSH_SCENTED_CANDLE.get());
			TIER_3_CANDLES_VARIANTS.add(BBBlocks.HOT_MONKEY_BRUSH_SCENTED_CANDLE.get());
			TIER_3_CANDLES_VARIANTS.add(BBBlocks.SCALDING_MONKEY_BRUSH_SCENTED_CANDLE.get());
			TIER_3_CANDLES_VARIANTS.add(BBBlocks.GILIA_SCENTED_CANDLE.get());
			TIER_3_CANDLES_VARIANTS.add(BBBlocks.YUCCA_FLOWER_SCENTED_CANDLE.get());
		}

		if(ModList.get().isLoaded("upgrade_aquatic")) {
			TIER_3_CANDLES_VARIANTS.add(BBBlocks.PINK_SEAROCKET_SCENTED_CANDLE.get());
			TIER_3_CANDLES_VARIANTS.add(BBBlocks.WHITE_SEAROCKET_SCENTED_CANDLE.get());
		}

		TIER_3_CANDLES_VARIANTS.add(BBBlocks.ALLIUM_SCENTED_CANDLE.get());
		TIER_3_CANDLES_VARIANTS.add(BBBlocks.WITHER_ROSE_SCENTED_CANDLE.get());
		
		//if((ModList.get().isLoaded("autumnity"))
		//TIER_1_CANDLES_VARIANTS.add(BBBlocks.AUTUMN_CROCUS_SCENTED_CANDLE.get());
	}
	
	//1/10th of bees spawning will also spawn honey slime
	@SuppressWarnings("deprecation")
	public static void BBMobSpawnEvent(LivingSpawnEvent.CheckSpawn event)
	{
		MobEntity entity = (MobEntity)event.getEntity();
		IWorld world = event.getWorld();
		
		if(entity.getType() == EntityType.BEE) {
			MobEntity slimeentity = BBEntities.HONEY_SLIME.get().create(entity.world);
			
			//move down to first non-air block
			BlockPos.Mutable blockpos = new BlockPos.Mutable(entity.getPosition());
			while(world.getBlockState(blockpos).isAir()) {
				blockpos.move(Direction.DOWN);
			}
			blockpos.move(Direction.UP, 2);
			
			slimeentity.setLocationAndAngles(blockpos.getX(), blockpos.getY(), blockpos.getZ(), world.getRandom().nextFloat() * 360.0F, 0.0F);
			ILivingEntityData ilivingentitydata = null;
			ilivingentitydata = slimeentity.onInitialSpawn(world, world.getDifficultyForLocation(new BlockPos(slimeentity)), event.getSpawnReason(), ilivingentitydata, (CompoundNBT) null);
			world.addEntity(slimeentity);
		}
	}

	
	private static final BlockState STONE = Blocks.STONE.getDefaultState();
	private static final BlockState FILLED_POROUS_HONEYCOMB = BzBlocks.FILLED_POROUS_HONEYCOMB.get().getDefaultState();
	private static final BlockState POROUS_HONEYCOMB = BzBlocks.POROUS_HONEYCOMB.get().getDefaultState();
	private static final BlockState HONEYCOMB_BLOCK = Blocks.HONEYCOMB_BLOCK.getDefaultState();
	private static final BlockState WAX_BLOCK = BBBlocks.WAX_BLOCK.get().getDefaultState();
	private static final BlockState CRYSTALLIZED_HONEY_BLOCK = BBBlocks.CRYSTALLIZED_HONEY_BLOCK.get().getDefaultState();

	//New surface builder to use when Buzzier Bees is on
	public static void buildSurface(Random random, IChunk chunk, Biome biome, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, SurfaceBuilderConfig config)
	{
		int xpos = x & 15;
		int zpos = z & 15;
		BlockPos.Mutable blockpos$Mutable = new BlockPos.Mutable();
		boolean topMostBlock = false;

		//makes stone below sea level into end stone
		for (int ypos = 255; ypos >= 0; --ypos)
		{
			blockpos$Mutable.setPos(xpos, ypos, zpos);
			BlockState currentBlockState = chunk.getBlockState(blockpos$Mutable);

			if (currentBlockState.getBlock() != null)
			{
				if(currentBlockState.getMaterial() == Material.AIR || currentBlockState.getMaterial() == Material.WATER) 
				{
					topMostBlock = true;
				}
				else 
				{
					if (currentBlockState == STONE)
					{
						chunk.setBlockState(blockpos$Mutable, HONEYCOMB_BLOCK, false);
					}
					else if (currentBlockState == POROUS_HONEYCOMB)
					{
						if(topMostBlock) 
						{
							//uses WAX_BLOCK for very top layer of land lower than sealevel area
							if (ypos <= seaLevel + 2 + Math.max(noise, 0) + random.nextInt(2))
							{
								if(Bumblezone.BzConfig.waxBlocksWorldgen.get()) 
								{
									chunk.setBlockState(blockpos$Mutable, WAX_BLOCK, false);
								}
								else
								{
									chunk.setBlockState(blockpos$Mutable, FILLED_POROUS_HONEYCOMB, false);
								}
							}
						}
						else if(!topMostBlock) 
						{
							//uses CRYSTALLIZED_HONEY_BLOCK for very top layer of land higher than sealevel area
							if (ypos > seaLevel + 2 + Math.max(noise, 0) + random.nextInt(2))
							{
								if(Bumblezone.BzConfig.crystallizedHoneyWorldgen.get()) 
								{
									chunk.setBlockState(blockpos$Mutable, CRYSTALLIZED_HONEY_BLOCK, false);
								}
								else
								{
									chunk.setBlockState(blockpos$Mutable, POROUS_HONEYCOMB, false);
								}
							}
							else 
							{
								chunk.setBlockState(blockpos$Mutable, FILLED_POROUS_HONEYCOMB, false);
							}
						}
						else 
						{
							if (ypos <= seaLevel + 2 + Math.max(noise, 0) + random.nextInt(2))
							{
								chunk.setBlockState(blockpos$Mutable, FILLED_POROUS_HONEYCOMB, false);
							}
						}
					}
					else if (currentBlockState.getMaterial() == Material.AIR)
					{
						if (ypos < seaLevel)
						{
							chunk.setBlockState(blockpos$Mutable, defaultFluid, false);
						}
					}
					
					topMostBlock = false;
				}
			}
		}
	}
	
	
	private static final BlockState HIVE_PLANKS = BBBlocks.HIVE_PLANKS.get().getDefaultState();
	
	//use hive planks for roof and floor
	public static void makeBedrock(IChunk chunk, Random random) {
		BlockPos.Mutable blockpos$Mutable = new BlockPos.Mutable();
		int xStart = chunk.getPos().getXStart();
		int zStart = chunk.getPos().getZStart();
		int roofHeight = 253;
		int floorHeight = 2;

		for (BlockPos blockpos : BlockPos.getAllInBoxMutable(xStart, 0, zStart, xStart + 15, 0, zStart + 15)) 
		{
			//fills in gap between top of terrain gen and y = 255 with solid blocks
			for (int ceilingY = 255; ceilingY >= roofHeight - 7 - random.nextInt(2); --ceilingY) 
			{
				chunk.setBlockState(blockpos$Mutable.setPos(blockpos.getX(), ceilingY, blockpos.getZ()), HIVE_PLANKS, false);
			}
		
			//single layer of solid blocks
			for (int floorY = 0; floorY <= floorHeight + random.nextInt(2); ++floorY) 
			{
				chunk.setBlockState(blockpos$Mutable.setPos(blockpos.getX(), floorY, blockpos.getZ()), HIVE_PLANKS, false);
			}
		}

	}
	
	
	public static ActionResultType honeyWandTakingHoney(ItemStack itemstack, BlockState thisBlockState, World world, BlockPos position, PlayerEntity playerEntity, Hand playerHand) {
		if (itemstack.getItem() == BBItems.HONEY_WAND.get())
		{
			if (!playerEntity.isCrouching())
			{
				if(!playerEntity.isCreative())
				{
					playerEntity.setHeldItem(playerHand, new ItemStack(BBItems.STICKY_HONEY_WAND.get())); //replaced empty Honey Wand with Sticky Honey Wand in hand
				}
				
				return ActionResultType.SUCCESS;
			}
		}
		
		return ActionResultType.FAIL;
	}
	
	public static ActionResultType honeyWandGivingHoney(ItemStack itemstack, BlockState thisBlockState, World world, BlockPos position, PlayerEntity playerEntity, Hand playerHand) {
		if (itemstack.getItem() == BBItems.STICKY_HONEY_WAND.get())
		{
			if (!playerEntity.isCrouching())
			{
				if(!playerEntity.isCreative())
				{
					playerEntity.setHeldItem(playerHand, new ItemStack(BBItems.HONEY_WAND.get())); //replaced Sticky Honey Wand with empty Honey Wand in hand
				}
				
				return ActionResultType.SUCCESS;
			}
		}
		
		return ActionResultType.FAIL;
	}
	
	public static ActionResultType bottledBeeInteract(ItemStack itemstack, BlockState thisBlockState, World world, BlockPos position, PlayerEntity playerEntity, Hand playerHand) {
		if (itemstack.getItem() == BBItems.BOTTLE_OF_BEE.get())
		{
			if (!playerEntity.isCrouching())
			{
				if(!playerEntity.isCreative())
				{
					playerEntity.setHeldItem(playerHand, new ItemStack(Items.GLASS_BOTTLE)); //replaced bottled bee with glass bottle
				}
				
				return ActionResultType.SUCCESS;
			}
		}
		
		return ActionResultType.FAIL;
	}

	
	/**
	 * Picks a random BuzzierBees candle with lower indices list being more common
	 */
	public static Block BBGetRandomTier1Candle(Random random)
	{
		int index = random.nextInt(TIER_1_CANDLES_VARIANTS.size());
		return TIER_1_CANDLES_VARIANTS.get(index);
	}
	
	/**
	 * Picks a random BuzzierBees scented candle with lower indices list being more common
	 */
	public static Block BBGetRandomTier2Candle(Random random)
	{
		int index = TIER_2_CANDLES_VARIANTS.size()-1;
		
		for(int i = 0; i < 2 && index != 0; i++) {
			index = random.nextInt(index+1);
		}
		
		return TIER_2_CANDLES_VARIANTS.get(index);
	}
	
	/**
	 * Picks a random BuzzierBees scented candle with lower indices list being more common
	 */
	public static Block BBGetRandomTier3Candle(Random random)
	{
		int index = TIER_3_CANDLES_VARIANTS.size()-1;
		
		for(int i = 0; i < 2 && index != 0; i++) {
			index = random.nextInt(index+1);
		}
		
		return TIER_3_CANDLES_VARIANTS.get(index);
	}
}
