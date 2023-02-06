package com.telepathicgrunt.the_bumblezone.world.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import com.telepathicgrunt.the_bumblezone.world.features.configs.NbtOreConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.Map;
import java.util.WeakHashMap;


public class BlockEntityCombOre extends Feature<NbtOreConfiguration> {
	public BlockEntityCombOre(Codec<NbtOreConfiguration> configFactory) {
		super(configFactory);
	}

	@Override
	public boolean place(FeaturePlaceContext<NbtOreConfiguration> context) {
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

		CompoundTag stateNbt = context.config().targetStates.get(0).stateNbt;
		OptionalBoolean data = OptionalBoolean.EMPTY;
		for (ModCompat compat : ModChecker.COMB_ORE_COMPATS) {
			data = compat.validateCombType(stateNbt);
			if (data.isPresent()) break;
		}
		if (data.isEmpty()) {
			return false;
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
						for(NbtOreConfiguration.TargetBlockState targetBlockState : context.config().targetStates) {
							if(targetBlockState.target.test(blockToReplace, context.random())) {
								cachedChunk.setBlockState(blockposMutable, targetBlockState.state, false);
								BlockEntity blockentity = ((EntityBlock)targetBlockState.state.getBlock()).newBlockEntity(blockposMutable, targetBlockState.state);
								if (blockentity == null) return false;
								blockentity.load(targetBlockState.stateNbt);
								cachedChunk.setBlockEntity(blockentity);
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
