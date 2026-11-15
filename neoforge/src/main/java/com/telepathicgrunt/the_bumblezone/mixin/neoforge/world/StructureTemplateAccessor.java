package com.telepathicgrunt.the_bumblezone.mixin.neoforge.world;

import net.minecraft.core.BlockPos;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(StructureTemplate.class)
public interface StructureTemplateAccessor {

    @Invoker("addEntitiesToWorld")
    void bumblezone$callAddEntitiesToWorld(ServerLevelAccessor level, BlockPos position, StructurePlaceSettings placementIn, ProblemReporter problemReporter);
}
