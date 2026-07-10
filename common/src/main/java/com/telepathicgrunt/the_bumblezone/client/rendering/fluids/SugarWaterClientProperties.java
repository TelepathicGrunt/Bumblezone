package com.telepathicgrunt.the_bumblezone.client.rendering.fluids;

import com.teamresourceful.resourcefullib.client.fluid.data.ClientFluidProperties;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
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
            public Material still() {
                return new Material(FLUID_STILL_TEXTURE);
            }

            @Override
            public Material flowing() {
                return new Material(FLUID_FLOWING_TEXTURE);
            }

            @Override
            public Material overlay() {
                return new Material(FLUID_OVERLAY_TEXTURE);
            }

            @Override
            public Identifier screenOverlay() {
                return TEXTURE_UNDERWATER;
            }

            @Override
            public int tintColor(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, @Nullable FluidState state) {
                if (view != null && pos != null) {
                    return BiomeColors.getAverageWaterColor(view, pos) | 0xFF000000;
                }
                return 0xFF5685E2;
            }
        };
    }
}
