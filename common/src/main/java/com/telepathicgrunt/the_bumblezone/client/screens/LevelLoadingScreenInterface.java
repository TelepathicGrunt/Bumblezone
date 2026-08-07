package com.telepathicgrunt.the_bumblezone.client.screens;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public interface LevelLoadingScreenInterface {
	ResourceKey<Level> theBumblezone$getNewLevel();

	void theBumblezone$setNewLevel(ResourceKey<Level> level);
}
