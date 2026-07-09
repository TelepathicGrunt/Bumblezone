package com.telepathicgrunt.the_bumblezone.client.rendering.fluids;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.fluid.data.ClientFluidProperties;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.BlockAndLightGetter;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

public class SugarWaterClientProperties {

    public static final Identifier FLUID_STILL_TEXTURE = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "block/sugar_water/still");
    public static final Identifier FLUID_FLOWING_TEXTURE = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "block/sugar_water/flow");
    public static final Identifier FLUID_OVERLAY_TEXTURE = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "block/sugar_water/overlay");
    public static final Identifier TEXTURE_UNDERWATER = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/misc/sugar_water_underwater.png");

    public static ClientFluidProperties create() {
        return new ClientFluidProperties() {
            @Override
            public Identifier still(@Nullable BlockAndLightGetter view, @Nullable BlockPos pos, FluidState state) {
                return FLUID_STILL_TEXTURE;
            }

            @Override
            public Identifier flowing(@Nullable BlockAndLightGetter view, @Nullable BlockPos pos, FluidState state) {
                return FLUID_FLOWING_TEXTURE;
            }

            @Override
            public Identifier overlay(@Nullable BlockAndLightGetter view, @Nullable BlockPos pos, FluidState state) {
                return FLUID_OVERLAY_TEXTURE;
            }

            @Override
            public Identifier screenOverlay() {
                return TEXTURE_UNDERWATER;
            }

            @Override
            public void renderOverlay(Minecraft minecraft, PoseStack stack) {
                FluidClientOverlay.renderHoneyOverlay(minecraft.player, stack);
            }

            @Override
            public int tintColor(@Nullable BlockAndLightGetter view, @Nullable BlockPos pos, FluidState state) {
                if (view != null && pos != null) {
                    return BiomeColors.getAverageWaterColor(view, pos) | 0xFF000000;
                }
                return 0xFF5685E2;
            }
        };
    }
}
