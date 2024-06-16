package com.telepathicgrunt.the_bumblezone.mixin.world;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(StructureTemplate.class)
public class StructureTemplateMixin {

    @ModifyReceiver(
            method = "placeInWorld(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;Lnet/minecraft/util/RandomSource;I)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/BlockEntity;setChanged()V")
    )
    private BlockEntity bumblezone$preventBlockEntityDeadlock(BlockEntity instance, ServerLevelAccessor serverLevelAccessor) {
        if(serverLevelAccessor instanceof WorldGenRegion && instance.hasLevel()) {
            instance.setLevel(null); // BE's level should never be set during worldgen or else deadlock due to neighbor update code ran.
        }
        return instance;
    }
}