package net.telepathicgrunt.bumblezone.features;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import com.bagel.buzzierbees.common.blocks.CandleBlock;
import com.google.common.collect.Lists;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.util.Pair;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.IFluidState;
import net.minecraft.inventory.IClearable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.shapes.BitSetVoxelShapePart;
import net.minecraft.util.math.shapes.VoxelShapePart;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import net.telepathicgrunt.bumblezone.blocks.HoneycombBrood;
import net.telepathicgrunt.bumblezone.modcompatibility.BeesourcefulRedirection;
import net.telepathicgrunt.bumblezone.modcompatibility.BuzzierBeesRedirection;
import net.telepathicgrunt.bumblezone.modcompatibility.ModChecking;


public class SpiderInfestedBeeDungeon extends Feature<NoFeatureConfig>
{
	private static final ResourceLocation CRYSTALLIZED_HONEY_BLOCK = new ResourceLocation("buzzierbees:crystallized_honey_block");
	
	public SpiderInfestedBeeDungeon(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory)
	{
		super(configFactory);
	}
	
	@Override
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> changedBlock, Random rand, BlockPos position, NoFeatureConfig p_212245_5_)
	{
		//affect rarity
		if(rand.nextInt(Bumblezone.BzConfig.spiderInfestedBeeDungeonRarity.get()) != 0) return false;
		
		BlockPos.Mutable blockpos$Mutable = new BlockPos.Mutable(position).move(-6, -2, -6);
		//Bumblezone.LOGGER.log(Level.INFO, "Spider Infested Bee Dungeon at X: "+position.getX() +", "+position.getY()+", "+position.getZ());

		TemplateManager templatemanager = ((ServerWorld) world.getWorld()).getSaveHandler().getStructureTemplateManager();
		Template template = templatemanager.getTemplate(new ResourceLocation(Bumblezone.MODID + ":bee_dungeon/shell"));

		if (template == null)
		{
			Bumblezone.LOGGER.warn("bee_dungeon/shell NTB does not exist!");
			return false;
		}

		PlacementSettings placementsettings = (new PlacementSettings()).setMirror(Mirror.NONE).setRotation(Rotation.NONE).setIgnoreEntities(false).setChunk((ChunkPos) null);
		addBlocksToWorld(template, world, blockpos$Mutable, placementsettings, 2);

		
		template = templatemanager.getTemplate(new ResourceLocation(Bumblezone.MODID + ":bee_dungeon/altar"));

		if (template == null)
		{
			Bumblezone.LOGGER.warn("bee_dungeon/altar NTB does not exist!");
			return false;
		}

		addBlocksToWorld(template, world, blockpos$Mutable.move(0, 1, 0), placementsettings, 2);
		
		
		for(int x = -8; x <= 16; x++) {
			for(int y = -3; y <= 10; y++) {
				for(int z = -8; z <= 16; z++) {
					blockpos$Mutable.setPos(position).move(x, y, z);
					if(rand.nextFloat() < 0.07f && world.getBlockState(blockpos$Mutable).getBlock() == Blocks.CAVE_AIR) {
						world.setBlockState(blockpos$Mutable, Blocks.COBWEB.getDefaultState(), 3);
					}
				}
			}
		}
		
		
		return true;

	}
	
	/**
	 * Adds blocks and entities from this structure to the given world.
	 */
	private static boolean addBlocksToWorld(Template template, IWorld world, BlockPos pos, PlacementSettings placementIn, int flags)
	{
		if (template.blocks.isEmpty())
		{
			return false;
		}
		else
		{
			List<Template.BlockInfo> list = placementIn.func_227459_a_(template.blocks, pos);
			if ((!list.isEmpty() || !placementIn.getIgnoreEntities() && !template.entities.isEmpty()) && template.size.getX() >= 1 && template.size.getY() >= 1 && template.size.getZ() >= 1)
			{
				MutableBoundingBox mutableboundingbox = placementIn.getBoundingBox();
				List<BlockPos> list1 = Lists.newArrayListWithCapacity(placementIn.func_204763_l() ? list.size() : 0);
				List<Pair<BlockPos, CompoundNBT>> list2 = Lists.newArrayListWithCapacity(list.size());
				int i = Integer.MAX_VALUE;
				int j = Integer.MAX_VALUE;
				int k = Integer.MAX_VALUE;
				int l = Integer.MIN_VALUE;
				int i1 = Integer.MIN_VALUE;
				int j1 = Integer.MIN_VALUE;

				for (Template.BlockInfo template$blockinfo : Template.processBlockInfos(template, world, pos, placementIn, list))
				{
					BlockPos blockpos = template$blockinfo.pos;
					if (mutableboundingbox == null || mutableboundingbox.isVecInside(blockpos))
					{
						IFluidState ifluidstate = placementIn.func_204763_l() ? world.getFluidState(blockpos) : null;
						BlockState blockstate = template$blockinfo.state.mirror(placementIn.getMirror()).rotate(placementIn.getRotation());
						if (template$blockinfo.nbt != null)
						{
							TileEntity tileentity = world.getTileEntity(blockpos);
							IClearable.clearObj(tileentity);
							world.setBlockState(blockpos, Blocks.BARRIER.getDefaultState(), 20);
						}

						//converts the blockstate template to the correct block and gets if the block can replace air or not
						Pair<BlockState, Boolean> pair = blockConversion(world, blockpos, blockstate.getBlock(), world.getRandom());
						blockstate = pair.getFirst();
						
						if ((pair.getSecond() || world.getBlockState(blockpos).isSolid()) && world.setBlockState(blockpos, blockstate, flags))
						{
							if(blockstate.getBlock() == Blocks.SPAWNER) {
								TileEntity tileentity = world.getTileEntity(blockpos);

								if (tileentity instanceof MobSpawnerTileEntity)
								{
									((MobSpawnerTileEntity) tileentity).getSpawnerBaseLogic()
										.setEntityType(world.getRandom().nextFloat() < 0.2f ? EntityType.CAVE_SPIDER : EntityType.SPIDER);
								}
							}
							
							i = Math.min(i, blockpos.getX());
							j = Math.min(j, blockpos.getY());
							k = Math.min(k, blockpos.getZ());
							l = Math.max(l, blockpos.getX());
							i1 = Math.max(i1, blockpos.getY());
							j1 = Math.max(j1, blockpos.getZ());
							list2.add(Pair.of(blockpos, template$blockinfo.nbt));
							if (template$blockinfo.nbt != null)
							{
								TileEntity tileentity1 = world.getTileEntity(blockpos);
								if (tileentity1 != null)
								{
									template$blockinfo.nbt.putInt("x", blockpos.getX());
									template$blockinfo.nbt.putInt("y", blockpos.getY());
									template$blockinfo.nbt.putInt("z", blockpos.getZ());
									tileentity1.read(template$blockinfo.nbt);
									tileentity1.mirror(placementIn.getMirror());
									tileentity1.rotate(placementIn.getRotation());
								}
							}

							if (ifluidstate != null && blockstate.getBlock() instanceof ILiquidContainer)
							{
								((ILiquidContainer) blockstate.getBlock()).receiveFluid(world, blockpos, blockstate, ifluidstate);
								if (!ifluidstate.isSource())
								{
									list1.add(blockpos);
								}
							}
						}
					}
				}

				boolean flag = true;
				Direction[] adirection = new Direction[] { Direction.UP, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };

				while (flag && !list1.isEmpty())
				{
					flag = false;
					Iterator<BlockPos> iterator = list1.iterator();

					while (iterator.hasNext())
					{
						BlockPos blockpos2 = iterator.next();
						BlockPos blockpos3 = blockpos2;
						IFluidState ifluidstate2 = world.getFluidState(blockpos2);

						for (int k1 = 0; k1 < adirection.length && !ifluidstate2.isSource(); ++k1)
						{
							BlockPos blockpos1 = blockpos3.offset(adirection[k1]);
							IFluidState ifluidstate1 = world.getFluidState(blockpos1);
							if (ifluidstate1.getActualHeight(world, blockpos1) > ifluidstate2.getActualHeight(world, blockpos3) || ifluidstate1.isSource() && !ifluidstate2.isSource())
							{
								ifluidstate2 = ifluidstate1;
								blockpos3 = blockpos1;
							}
						}

						if (ifluidstate2.isSource())
						{
							BlockState blockstate2 = world.getBlockState(blockpos2);
							Block block = blockstate2.getBlock();
							if (block instanceof ILiquidContainer)
							{
								((ILiquidContainer) block).receiveFluid(world, blockpos2, blockstate2, ifluidstate2);
								flag = true;
								iterator.remove();
							}
						}
					}
				}

				if (i <= l)
				{
					if (!placementIn.func_215218_i())
					{
						VoxelShapePart voxelshapepart = new BitSetVoxelShapePart(l - i + 1, i1 - j + 1, j1 - k + 1);
						int l1 = i;
						int i2 = j;
						int j2 = k;

						for (Pair<BlockPos, CompoundNBT> pair1 : list2)
						{
							BlockPos blockpos5 = pair1.getFirst();
							voxelshapepart.setFilled(blockpos5.getX() - l1, blockpos5.getY() - i2, blockpos5.getZ() - j2, true, true);
						}

						Template.func_222857_a(world, flags, voxelshapepart, l1, i2, j2);
					}

					for (Pair<BlockPos, CompoundNBT> pair : list2)
					{
						BlockPos blockpos4 = pair.getFirst();
						if (!placementIn.func_215218_i())
						{
							BlockState blockstate1 = world.getBlockState(blockpos4);
							BlockState blockstate3 = Block.getValidBlockForPosition(blockstate1, world, blockpos4);
							if (blockstate1 != blockstate3)
							{
								world.setBlockState(blockpos4, blockstate3, flags & -2 | 16);
							}

							world.notifyNeighbors(blockpos4, blockstate3.getBlock());
						}

						if (pair.getSecond() != null)
						{
							TileEntity tileentity2 = world.getTileEntity(blockpos4);
							if (tileentity2 != null)
							{
								tileentity2.markDirty();
							}
						}
					}
				}

				
				if (!placementIn.getIgnoreEntities())
				{
					Method addEntitiesToWorldReflect = ObfuscationReflectionHelper.findMethod(
							Template.class, 
							"func_207668_a", 
							IWorld.class,
							BlockPos.class, 
							PlacementSettings.class,
							Mirror.class,
							Rotation.class,
							BlockPos.class,
							MutableBoundingBox.class);
					
					try
					{
						addEntitiesToWorldReflect.invoke(
								template, 
								world, 
								pos,
								placementIn, 
								placementIn.getMirror(), 
								placementIn.getRotation(), 
								placementIn.getCenterOffset(), 
								placementIn.getBoundingBox());
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}

				return true;
			}
			else
			{
				return false;
			}
		}
	}

	
	/**
	 * Converts the incoming block to the blockstate needed
	 * @return - a pair of the blockstate to use and whether this block can replace air
	 */
	private static Pair<BlockState, Boolean> blockConversion(IWorld world, BlockPos pos, Block block, Random random)
	{
		//////////////////////////////////////////////
		//Shell
		
		//main body
		if(block == Blocks.RED_TERRACOTTA || block == Blocks.PURPLE_TERRACOTTA) {
			if(ModChecking.beesourcefulPresent && random.nextFloat() < Bumblezone.BzConfig.oreHoneycombSpawnRateSpiderBeeDungeon.get()) {
				return new Pair<>(BeesourcefulRedirection.BSGetRandomHoneycomb(random, 
						Bumblezone.BzConfig.greatHoneycombRaritySpiderBeeDungeon.get()).getDefaultState(), new Boolean(false));
			}
			
			if(random.nextFloat() < 0.15f) {
				return new Pair<>(Blocks.HONEYCOMB_BLOCK.getDefaultState(),	new Boolean(false));
			}
			else {
				return new Pair<>(BzBlocks.POROUS_HONEYCOMB.get().getDefaultState(), new Boolean(false));
			}
		}

		//south wall
		else if(block == Blocks.ORANGE_TERRACOTTA) {
			if(random.nextFloat() < 0.6f) {
				return new Pair<>(BzBlocks.DEAD_HONEYCOMB_LARVA.get().getDefaultState()
								.with(HoneycombBrood.FACING, Direction.SOUTH),	
								new Boolean(false));
			}
			else if(random.nextDouble() < Bumblezone.BzConfig.spawnerRateSpiderBeeDungeon.get()) {
				return new Pair<>(Blocks.SPAWNER.getDefaultState(), new Boolean(false));
			}
			else {
				return new Pair<>(BzBlocks.POROUS_HONEYCOMB.get().getDefaultState(), new Boolean(false));
			}
		}

		//west wall
		else if(block == Blocks.YELLOW_TERRACOTTA) {
			if(random.nextFloat() < 0.6f) {
				return new Pair<>(BzBlocks.DEAD_HONEYCOMB_LARVA.get().getDefaultState()
								.with(HoneycombBrood.FACING, Direction.WEST),	
								new Boolean(false));
			}
			else if(random.nextDouble() < Bumblezone.BzConfig.spawnerRateSpiderBeeDungeon.get()) {
				return new Pair<>(Blocks.SPAWNER.getDefaultState(), new Boolean(false));
			}
			else {
				return new Pair<>(BzBlocks.POROUS_HONEYCOMB.get().getDefaultState(), new Boolean(false));
			}
		}

		//north wall
		else if(block == Blocks.LIME_TERRACOTTA) {
			if(random.nextFloat() < 0.6f) {
				return new Pair<>(BzBlocks.DEAD_HONEYCOMB_LARVA.get().getDefaultState()
								.with(HoneycombBrood.FACING, Direction.NORTH),	
								new Boolean(false));
			}
			else if(random.nextDouble() < Bumblezone.BzConfig.spawnerRateSpiderBeeDungeon.get()) {
				return new Pair<>(Blocks.SPAWNER.getDefaultState(), new Boolean(false));
			}
			else {
				return new Pair<>(BzBlocks.POROUS_HONEYCOMB.get().getDefaultState(), new Boolean(false));
			}
		}

		//east wall
		else if(block == Blocks.BLUE_TERRACOTTA) {
			if(random.nextFloat() < 0.6f) {
				return new Pair<>(BzBlocks.DEAD_HONEYCOMB_LARVA.get().getDefaultState()
								.with(HoneycombBrood.FACING, Direction.EAST),	
								new Boolean(false));
			}
			else if(random.nextDouble() < Bumblezone.BzConfig.spawnerRateSpiderBeeDungeon.get()) {
				return new Pair<>(Blocks.SPAWNER.getDefaultState(), new Boolean(false));
			}
			else {
				return new Pair<>(BzBlocks.POROUS_HONEYCOMB.get().getDefaultState(), new Boolean(false));
			}
		}

		//sugar water stream
		else if(block == BzBlocks.SUGAR_WATER_BLOCK.get()) {
			if(ModChecking.buzzierBeesPresent) {
				return new Pair<>(ForgeRegistries.BLOCKS.getValue(CRYSTALLIZED_HONEY_BLOCK).getDefaultState(), new Boolean(false));
			}
			else {
				return new Pair<>(Blocks.CAVE_AIR.getDefaultState(), new Boolean(false));
			}
		}
		
		//air
		else if(block.getDefaultState().getMaterial() == Material.AIR) {
			if(random.nextFloat() < 0.07f)
				return new Pair<>(Blocks.COBWEB.getDefaultState(), new Boolean(false));
		}
		
		//Shell
		//////////////////////////////////////////////
		//Altar
		
		
		//base
		else if(block == Blocks.GREEN_TERRACOTTA) {
			boolean replaceAir = false;
			if(world.getBlockState(pos.up()).getMaterial() != Material.AIR && !world.getBlockState(pos.up()).isSolid())
				replaceAir = true;
			
			if(ModChecking.beesourcefulPresent && random.nextFloat() < Bumblezone.BzConfig.oreHoneycombSpawnRateSpiderBeeDungeon.get()) {
				return new Pair<>(BeesourcefulRedirection.BSGetRandomHoneycomb(random, 
						Bumblezone.BzConfig.greatHoneycombRaritySpiderBeeDungeon.get()).getDefaultState(), new Boolean(replaceAir));
			}
			
			if(random.nextFloat() < 0.15f) {
				return new Pair<>(Blocks.HONEYCOMB_BLOCK.getDefaultState(),	new Boolean(replaceAir));
			}
			else {
				return new Pair<>(BzBlocks.POROUS_HONEYCOMB.get().getDefaultState(), new Boolean(replaceAir));
			}
		}

		//outer ring
		else if(block == Blocks.GRAY_TERRACOTTA) {
			if(random.nextFloat() < 0.07f) {
				return new Pair<>(Blocks.COBWEB.getDefaultState(),	new Boolean(true));
			}
			else {
				return new Pair<>(Blocks.CAVE_AIR.getDefaultState(), new Boolean(false));
			}
		}
		
		//inner ring
		else if(block == Blocks.CYAN_TERRACOTTA) {
			if(random.nextFloat() < 0.07f) {
				return new Pair<>(Blocks.COBWEB.getDefaultState(),	new Boolean(true));
			}
			else {
				return new Pair<>(Blocks.CAVE_AIR.getDefaultState(), new Boolean(false));
			}
		}
		
		//center
		else if(block == Blocks.BLACK_TERRACOTTA) {
			if(ModChecking.buzzierBeesPresent && Bumblezone.BzConfig.allowScentedCandlesSpiderBeeDungeon.get()) {
				if(random.nextInt(4) == 0 && world.getBlockState(pos.down()).getMaterial() != Material.AIR)
					return new Pair<>(BuzzierBeesRedirection.BBGetRandomTier3Candle(random, Bumblezone.BzConfig.powerfulCandlesRaritySpiderBeeDungeon.get() + 1).getDefaultState()
							.with(CandleBlock.CANDLES, random.nextInt(random.nextInt(random.nextInt(3)+1)+1)+1) //lowest chance of 3 candles
							.with(CandleBlock.WATERLOGGED, false)
							.with(CandleBlock.LIT, true), 
							new Boolean(true));
				
				if(random.nextFloat() < 0.05f)
					return new Pair<>(Blocks.COBWEB.getDefaultState(),	new Boolean(true));
			}
			else {
				if(random.nextFloat() < 0.07f) {
					return new Pair<>(Blocks.COBWEB.getDefaultState(),	new Boolean(true));
				}
				else {
					return new Pair<>(Blocks.CAVE_AIR.getDefaultState(), new Boolean(false));
				}
			}
		}

		//Altar
		//////////////////////////////////////////////
		//Misc/air
		
		return new Pair<>(Blocks.CAVE_AIR.getDefaultState(), new Boolean(false));
	}

}
