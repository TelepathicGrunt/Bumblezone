package net.telepathicgrunt.bumblezone.modcompatibility;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.tfar.beesourceful.util.BeeType;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockMatcher;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.MobEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig.FillerBlockType;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.biome.BzBaseBiome;
import net.telepathicgrunt.bumblezone.biome.BzBiomes;

public class BeesourcefulCompat
{
	private static List<Block> HONEYCOMB_VARIANTS;
	
	public static void setupBeesourceful() 
	{
		ModChecking.beesourcefulPresent = true;
		
		if(Bumblezone.BzConfig.spawnBesourcefulHoneycombVariants.get()) {
			String honeycombTarget = "honeycomb_target";
			FillerBlockType.create(honeycombTarget.toUpperCase(), honeycombTarget, new BlockMatcher(Blocks.HONEYCOMB_BLOCK));

			Block diamondHoneycomb = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("beesourceful:diamond_honeycomb_block"));
			Block emeraldHoneycomb = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("beesourceful:emerald_honeycomb_block"));
			Block enderHoneycomb = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("beesourceful:ender_honeycomb_block"));
			Block goldHoneycomb = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("beesourceful:gold_honeycomb_block"));
			Block ironHoneycomb = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("beesourceful:iron_honeycomb_block"));
			Block lapisHoneycomb = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("beesourceful:lapis_honeycomb_block"));
			Block redstoneHoneycomb = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("beesourceful:redstone_honeycomb_block"));

			BzBiomes.biomes.forEach(biome -> ((BzBaseBiome)biome).addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.byName(honeycombTarget), diamondHoneycomb.getDefaultState(), 7)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(1, 6, 0, 244)))));	
			BzBiomes.biomes.forEach(biome -> ((BzBaseBiome)biome).addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.byName(honeycombTarget), emeraldHoneycomb.getDefaultState(), 4)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(1, 6, 0, 244)))));	
			BzBiomes.biomes.forEach(biome -> ((BzBaseBiome)biome).addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.byName(honeycombTarget), enderHoneycomb.getDefaultState(), 4)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(1, 200, 0, 50)))));	
			BzBiomes.biomes.forEach(biome -> ((BzBaseBiome)biome).addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.byName(honeycombTarget), goldHoneycomb.getDefaultState(), 34)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(3, 6, 0, 230)))));	
			BzBiomes.biomes.forEach(biome -> ((BzBaseBiome)biome).addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.byName(honeycombTarget), ironHoneycomb.getDefaultState(), 26)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(2, 30, 0, 210)))));	
			BzBiomes.biomes.forEach(biome -> ((BzBaseBiome)biome).addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.byName(honeycombTarget), lapisHoneycomb.getDefaultState(), 22)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(1, 6, 0, 30)))));	
			BzBiomes.biomes.forEach(biome -> ((BzBaseBiome)biome).addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.byName(honeycombTarget), redstoneHoneycomb.getDefaultState(), 22)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(1, 30, 0, 210)))));	
		
			HONEYCOMB_VARIANTS = new ArrayList<Block>();
			HONEYCOMB_VARIANTS.add(goldHoneycomb);
			HONEYCOMB_VARIANTS.add(ironHoneycomb);
			HONEYCOMB_VARIANTS.add(redstoneHoneycomb);
			HONEYCOMB_VARIANTS.add(lapisHoneycomb);
			HONEYCOMB_VARIANTS.add(emeraldHoneycomb);
			HONEYCOMB_VARIANTS.add(enderHoneycomb);
			HONEYCOMB_VARIANTS.add(diamondHoneycomb);
		}
	}
	
	//1/15th of bees spawning will also spawn BeeSourceful bees
	public static void BSMobSpawnEvent(LivingSpawnEvent.CheckSpawn event)
	{
		MobEntity entity = (MobEntity)event.getEntity();
		IWorld world = event.getWorld();
		
		if(entity.getType() == EntityType.BEE) {
			MobEntity beesourcefulBeeEntity;
			int beeTypeChance = world.getRandom().nextInt(100);
			
			if(beeTypeChance < 1) {
				beesourcefulBeeEntity = BeeType.ender.beeSupplier.get().create(entity.world);
			}
			else if(beeTypeChance < 3) {
				beesourcefulBeeEntity = BeeType.diamond.beeSupplier.get().create(entity.world);
			}
			else if(beeTypeChance < 10) {
				beesourcefulBeeEntity = BeeType.emerald.beeSupplier.get().create(entity.world);
			}
			else if(beeTypeChance < 20) {
				beesourcefulBeeEntity = BeeType.lapis.beeSupplier.get().create(entity.world);
			}
			else if(beeTypeChance < 30) {
				beesourcefulBeeEntity = BeeType.quartz.beeSupplier.get().create(entity.world);
			}
			else if(beeTypeChance < 50) {
				beesourcefulBeeEntity = BeeType.gold.beeSupplier.get().create(entity.world);
			}
			else if(beeTypeChance < 70) {
				beesourcefulBeeEntity = BeeType.redstone.beeSupplier.get().create(entity.world);
			}
			else {
				beesourcefulBeeEntity = BeeType.iron.beeSupplier.get().create(entity.world);
			}
			
			
			BlockPos.Mutable blockpos = new BlockPos.Mutable(entity.getPosition());
			
			beesourcefulBeeEntity.setLocationAndAngles(blockpos.getX(), blockpos.getY(), blockpos.getZ(), world.getRandom().nextFloat() * 360.0F, 0.0F);
			ILivingEntityData ilivingentitydata = null;
			ilivingentitydata = beesourcefulBeeEntity.onInitialSpawn(world, world.getDifficultyForLocation(new BlockPos(beesourcefulBeeEntity)), event.getSpawnReason(), ilivingentitydata, (CompoundNBT) null);
			world.addEntity(beesourcefulBeeEntity);
		}
	}


	/**
	 * Picks a random Beesourceful Honeycomb with lower index of HONEYCOMB_VARIANTS list being highly common
	 */
	public static Block BSGetRandomHoneycomb(Random random, int lowerBoundBias)
	{
		int index = HONEYCOMB_VARIANTS.size()-1;
		
		for(int i = 0; i < lowerBoundBias && index != 0; i++) {
			index = random.nextInt(index+1);
		}
		
		return HONEYCOMB_VARIANTS.get(index);
	}
}
