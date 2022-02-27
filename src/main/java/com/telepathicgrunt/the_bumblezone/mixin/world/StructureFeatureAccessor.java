package com.telepathicgrunt.the_bumblezone.mixin.world;

import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(StructureFeature.class)
public interface StructureFeatureAccessor {
    @Accessor("STEP")
    static Map<StructureFeature<?>, GenerationStep.Decoration> getSTEP() {
        throw new UnsupportedOperationException();
    }
}
