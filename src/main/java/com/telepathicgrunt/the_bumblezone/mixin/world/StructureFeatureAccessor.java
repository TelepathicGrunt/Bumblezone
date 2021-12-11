package com.telepathicgrunt.the_bumblezone.mixin.world;

import net.minecraft.world.level.levelgen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(StructureFeature.class)
public interface StructureFeatureAccessor {
    @Mutable
    @Accessor("NOISE_AFFECTING_FEATURES")
    static void bumblezone_setNOISE_AFFECTING_FEATURES(List<StructureFeature<?>> NOISE_AFFECTING_FEATURES) {
        throw new UnsupportedOperationException();
    }
}
