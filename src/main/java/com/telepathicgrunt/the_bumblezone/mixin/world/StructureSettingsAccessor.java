package com.telepathicgrunt.the_bumblezone.mixin.world;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StructureSettings.class)
public interface StructureSettingsAccessor {
    @Mutable
    @Accessor("DEFAULTS")
    static void bumblezone_setDEFAULTS(ImmutableMap<StructureFeature<?>, StructureFeatureConfiguration> DEFAULTS) {
        throw new UnsupportedOperationException();
    }
}
