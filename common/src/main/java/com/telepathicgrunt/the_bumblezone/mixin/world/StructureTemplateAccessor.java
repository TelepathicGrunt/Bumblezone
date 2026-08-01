package com.telepathicgrunt.the_bumblezone.mixin.world;

import net.minecraft.core.BlockPos;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(StructureTemplate.class)
public interface StructureTemplateAccessor {

    @Accessor("palettes")
    List<StructureTemplate.Palette> bumblezone$getBlocks();

    @Accessor("entityInfoList")
    List<StructureTemplate.StructureEntityInfo> bumblezone$getEntityInfoList();

    // TODO: Fix as neo patched this method. Need multiloader setup
    @Invoker("placeEntities")
    void bumblezone$callPlaceEntities(
        ServerLevelAccessor level,
        BlockPos position,
        Mirror mirror,
        Rotation rotation,
        BlockPos pivot,
        BoundingBox boundingBox,
        boolean finalizeEntities,
        ProblemReporter problemReporter
    );
}
