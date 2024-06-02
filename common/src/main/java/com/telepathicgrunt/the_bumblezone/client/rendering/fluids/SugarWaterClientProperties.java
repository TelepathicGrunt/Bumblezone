package com.telepathicgrunt.the_bumblezone.client.rendering.fluids;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.fluid.data.ClientFluidProperties;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

public class SugarWaterClientProperties {

    public static final ResourceLocation FLUID_STILL_TEXTURE = new ResourceLocation(Bumblezone.MODID, "block/sugar_water_still");
    public static final ResourceLocation FLUID_FLOWING_TEXTURE = new ResourceLocation(Bumblezone.MODID, "block/sugar_water_flow");
    public static final ResourceLocation FLUID_OVERLAY_TEXTURE = new ResourceLocation(Bumblezone.MODID, "block/sugar_water_overlay");
    public static final ResourceLocation TEXTURE_UNDERWATER = new ResourceLocation(Bumblezone.MODID, "textures/misc/sugar_water_underwater.png");

    public static ClientFluidProperties create() {
        return new ClientFluidProperties() {
            @Override
            public ResourceLocation still(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state) {
                return FLUID_STILL_TEXTURE;
            }

            @Override
            public ResourceLocation flowing(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state) {
                return FLUID_FLOWING_TEXTURE;
            }

            @Override
            public ResourceLocation overlay(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state) {
                return FLUID_OVERLAY_TEXTURE;
            }

            @Override
            public ResourceLocation screenOverlay() {
                return TEXTURE_UNDERWATER;
            }

            @Override
            public void renderOverlay(Minecraft minecraft, PoseStack stack) {
                FluidClientOverlay.renderHoneyOverlay(minecraft.player, stack);
            }

            @Override
            public int tintColor(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state) {
                if (view != null && pos != null) {
                    return BiomeColors.getAverageWaterColor(view, pos) | 0xFF000000;
                }
                return 0xFF5685E2;
            }
        };
    }
}
