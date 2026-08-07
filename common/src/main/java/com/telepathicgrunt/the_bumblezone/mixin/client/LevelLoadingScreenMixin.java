package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.telepathicgrunt.the_bumblezone.client.screens.LevelLoadingScreenInterface;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LevelLoadingScreen.class)
public class LevelLoadingScreenMixin implements LevelLoadingScreenInterface {

    @Unique
    ResourceKey<Level> bumblezone_level = null;

    @Override
    public ResourceKey<Level> theBumblezone$getNewLevel() {
        return bumblezone_level;
    }

    @Override
    public void theBumblezone$setNewLevel(ResourceKey<Level> level) {
        bumblezone_level = level;
    }
}