package com.telepathicgrunt.the_bumblezone.world.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;


public class BlockEntityCombOre extends Feature<OreConfiguration> {
	public BlockEntityCombOre(Codec<OreConfiguration> configFactory) {
		super(configFactory);
	}

	@Override
	public boolean place(FeaturePlaceContext<OreConfiguration> context) {
		BlockPos.MutableBlockPos blockposMutable = new BlockPos.MutableBlockPos();
		BlockState blockToReplace;
		float angleOfRotation = (float) (Math.PI * context.random().nextFloat());
		float sinOfAngle = Mth.sin(angleOfRotation);
		float cosOfAngle = Mth.cos(angleOfRotation);
		float size = context.config().size * 0.5f;
		ChunkAccess cachedChunk;
		float stretchedFactor = 0.7f;
		if (context.config().size < 10) stretchedFactor = 1;
		int maxY = (int) (size / 3);
		int minY = -maxY - 1;

		ModCompat dataCompat = null;
		Optional<Object> data = Optional.empty();
		Block targetBlock = context.config().targetStates.get(0).state.getBlock();
		for (ModCompat compat : ModChecker.COMB_ORE_COMPATS) {
			data = compat.getCombData(targetBlock, context.random());
			dataCompat = compat;
			if (data.isPresent()) break;
		}

		for(int y = minY; y <= maxY; y++) {
			float yModified = y;
			if(y < 0){
				yModified = y + 0.25f;
			}
			else if (y > 0){
				y = (int)(y + 0.5f);
			}

			float percentageOfRadius = 1f - (yModified / size) * (yModified / size) * 3;
			float majorRadiusSq = (size * percentageOfRadius) * (size * percentageOfRadius);
			float minorRadiusSq = (size * stretchedFactor * percentageOfRadius) * (size * stretchedFactor * percentageOfRadius);
			
			for(int x = (int) -size; x < size; x++) {
				for(int z = (int) -size; z < size; z++) {
					float majorComp;
					float minorComp;

					majorComp = (x + 0.5f) * cosOfAngle - (z + 0.5f) * sinOfAngle;
					minorComp = (x + 0.5f) * sinOfAngle + (z + 0.5f) * cosOfAngle;

					float result = ((majorComp * majorComp) / (majorRadiusSq * majorRadiusSq)) +
									((minorComp * minorComp) / (minorRadiusSq * minorRadiusSq));

					if(result * 100f < 1f && !(x == 0 && z == 0 && y * y >= (size * size))) {
						blockposMutable.set(context.origin().getX() + x, context.origin().getY() + y, context.origin().getZ() + z);
						cachedChunk = getCachedChunk(context.level(), blockposMutable);

						blockToReplace = cachedChunk.getBlockState(blockposMutable);
						for(OreConfiguration.TargetBlockState targetBlockState : context.config().targetStates) {
							if(targetBlockState.target.test(blockToReplace, context.random())) {
								if (dataCompat != null && data.isPresent()) {
									if (dataCompat.placeCombOre(blockposMutable, cachedChunk, data.get(), targetBlockState, targetBlockState.state.getBlock())) {
										continue;
									}
								}

								cachedChunk.setBlockState(blockposMutable, targetBlockState.state, false);
							}
						}
					}
				}
			}
		}
		
		return true;
	}


	private static final Map<ResourceKey<Level>, Map<Long, ChunkAccess>> CACHED_CHUNKS_ALL_WORLDS = new WeakHashMap<>();
	public ChunkAccess getCachedChunk(ServerLevelAccessor world, BlockPos blockpos) {

		// get the world's cache or make one if map doesnt exist.
		ResourceKey<Level> worldKey = world.getLevel().dimension();
		Map<Long, ChunkAccess> worldStorage = CACHED_CHUNKS_ALL_WORLDS.computeIfAbsent(worldKey, k -> new WeakHashMap<>());

		// shrink cache if it is too large to clear out old chunk refs no longer needed.
		if(worldStorage.size() > 9){
			worldStorage.clear();
		}

		// gets the chunk saved or does the expensive .getChunk to get it if it isn't cached yet.
		long posLong = (long) (blockpos.getX() >> 4) & 4294967295L | ((long)(blockpos.getZ() >> 4) & 4294967295L) << 32;
		ChunkAccess cachedChunk = worldStorage.get(posLong);
		if(cachedChunk == null){
			cachedChunk = world.getChunk(blockpos);
			worldStorage.put(posLong, cachedChunk);
		}

		return cachedChunk;
	}
}
