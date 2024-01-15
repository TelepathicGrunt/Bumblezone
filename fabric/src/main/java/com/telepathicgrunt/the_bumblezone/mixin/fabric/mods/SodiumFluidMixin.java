package com.telepathicgrunt.the_bumblezone.mixin.fabric.mods;

import com.telepathicgrunt.the_bumblezone.modcompat.fabric.SodiumSinkingVertexBuilder;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.FluidRenderer;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.material.DefaultMaterials;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.material.Material;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.material.FluidState;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FluidRenderer.class)
public class SodiumFluidMixin {

    @Inject(method = "render(Lme/jellysquid/mods/sodium/client/world/WorldSlice;Lnet/minecraft/world/level/material/FluidState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildBuffers;)V",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void bumblezone$renderHoneyProperly(WorldSlice world, FluidState fluidState, BlockPos blockPos, BlockPos offset, ChunkBuildBuffers buffers, CallbackInfo ci) {
        if (fluidState.is(BzTags.SPECIAL_HONEY_LIKE)) {

            SodiumSinkingVertexBuilder sodiumSinkingVertexBuilder = SodiumSinkingVertexBuilder.getInstance();
            sodiumSinkingVertexBuilder.reset();

            FluidRenderHandlerRegistry.INSTANCE.get(fluidState.getType())
                    .renderFluid(blockPos, world, sodiumSinkingVertexBuilder, world.getBlockState(blockPos.getX(), blockPos.getY(), blockPos.getZ()), fluidState);

            Material material = DefaultMaterials.forFluidState(fluidState);
            sodiumSinkingVertexBuilder.flush(buffers.get(material), material, new Vector3f());

            ci.cancel();
        }
    }
}
