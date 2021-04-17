package com.telepathicgrunt.the_bumblezone.mixin.world;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

	@Unique
	private static final int updateInterval = 20;

	@Unique
	private static int counter = 0;

	@Inject(
			method = "tick(Ljava/util/function/BooleanSupplier;)V",
			at = @At(value = "HEAD")
	)
	private void tickAltar(CallbackInfo ci) {
		ServerWorld world = ((ServerWorld) (Object) this);
		if(world.getDimensionKey().getLocation().equals(Bumblezone.MOD_DIMENSION_ID)){
			counter++;
			if(counter % updateInterval == 0){
				counter = 0;
				GeneralUtils.updateEntityCount(world);
			}
		}
	}
}
