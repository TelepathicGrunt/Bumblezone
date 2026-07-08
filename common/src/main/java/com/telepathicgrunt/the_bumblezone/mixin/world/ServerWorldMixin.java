package com.telepathicgrunt.the_bumblezone.mixin.world;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.entities.BeeDedicatedSpawning;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class ServerWorldMixin extends Level {

	protected ServerWorldMixin(
			WritableLevelData levelData,
			ResourceKey<Level> dimension,
			RegistryAccess registryAccess,
			Holder<DimensionType> dimensionTypeRegistration,
			boolean isClientSide,
			boolean isDebug,
			long biomeZoomSeed,
			int maxChainedNeighborUpdates)
	{
		super(levelData, dimension, registryAccess, dimensionTypeRegistration, isClientSide, isDebug, biomeZoomSeed, maxChainedNeighborUpdates);
	}

	@Unique
	private static final int bumblezone$updateInterval = 21;

	@Unique
	private static int bumblezone$counter = 0;

	@Inject(
			method = "tick(Ljava/util/function/BooleanSupplier;)V",
			at = @At(value = "HEAD")
	)
	private void bumblezone$countBzDimEntities(CallbackInfo ci) {
		if(dimension().identifier().equals(Bumblezone.MOD_DIMENSION_ID)){
			bumblezone$counter++;
			if(bumblezone$counter % bumblezone$updateInterval == 0){
				bumblezone$counter = 0;
				ServerLevel serverLevel = (ServerLevel) (Object) this;
				BeeDedicatedSpawning.updateEntityCount(serverLevel);
				if(BzGeneralConfigs.specialBeeSpawning && serverLevel.getGameRules().get(GameRules.SPAWN_MOBS)) {
					BeeDedicatedSpawning.specialSpawnBees(serverLevel);
				}
			}
		}
	}
}
