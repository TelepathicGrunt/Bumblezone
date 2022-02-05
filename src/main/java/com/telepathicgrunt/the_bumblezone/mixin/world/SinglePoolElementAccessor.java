package com.telepathicgrunt.the_bumblezone.mixin.world;

import com.mojang.datafixers.util.Either;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.structures.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SinglePoolElement.class)
public interface SinglePoolElementAccessor {
    @Accessor("template")
    Either<ResourceLocation, StructureTemplate> getTemplate();
}
