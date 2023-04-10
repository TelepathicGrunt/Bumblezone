package com.telepathicgrunt.the_bumblezone.mixin.world;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.telepathicgrunt.the_bumblezone.modinit.BzProcessors;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StructureTemplate.class)
public class StructureTemplateMixin {

    @Inject(
            method = "placeInWorld(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;Lnet/minecraft/util/RandomSource;I)Z",
            at = @At(value = "HEAD")
    )
    private void thebumblezone_preventAutoWaterlogging(ServerLevelAccessor serverLevelAccessor,
                                                              BlockPos blockPos1,
                                                              BlockPos blockPos2,
                                                              StructurePlaceSettings structurePlaceSettings,
                                                              RandomSource random,
                                                              int flag,
                                                              CallbackInfoReturnable<Boolean> cir) {

        if(structurePlaceSettings.getProcessors().stream().anyMatch(processor ->
                ((StructureProcessorAccessor)processor).callGetType() == BzProcessors.WATERLOGGING_FIX_PROCESSOR.get()))
        {
            structurePlaceSettings.setKeepLiquids(false);
        }
    }

    @ModifyReceiver(
            method = "placeInWorld(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;Lnet/minecraft/util/RandomSource;I)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/BlockEntity;setChanged()V")
    )
    private BlockEntity thebumblezone_preventBlockEntityDeadlock(BlockEntity instance, ServerLevelAccessor serverLevelAccessor) {
        if(serverLevelAccessor instanceof WorldGenRegion && instance.hasLevel()) {
            instance.setLevel(null); // BE's level should never be set during worldgen or else deadlock due to neighbor update code ran.
        }
        return instance;
    }
}