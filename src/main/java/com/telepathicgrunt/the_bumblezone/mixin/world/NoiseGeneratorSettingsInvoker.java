package com.telepathicgrunt.the_bumblezone.mixin.world;

import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(NoiseGeneratorSettings.class)
public interface NoiseGeneratorSettingsInvoker {

    @Invoker("disableMobGeneration")
    boolean thebumblezone_callDisableMobGeneration();
}
