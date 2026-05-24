package com.telepathicgrunt.the_bumblezone.mixin.world;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(StructureTemplate.class)
public class StructureTemplateMixin {

    @WrapOperation(
            method = "placeInWorld(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;Lnet/minecraft/util/RandomSource;I)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/BlockEntity;setChanged()V")
    )
    private void bumblezone$preventBlockEntityDeadlock(BlockEntity instance, Operation<Void> original, ServerLevelAccessor serverLevel) {
        // Only call original method if outside worldgen OR block entity does not have a level set.
        // A worldgen context BE's ServerLevel would've caused deadlock in setChanged due to neighbor update code ran.
        // Skip calling the setChanged method.
        // See https://github.com/TelepathicGrunt/Bumblezone/issues/560#issuecomment-4527418647 for more info on why a WrapOperation this time
        if (!(serverLevel instanceof WorldGenRegion) || !instance.hasLevel()) {
            original.call(instance);
        }
    }
}