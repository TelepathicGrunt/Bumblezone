package com.telepathicgrunt.the_bumblezone.client.rendering.fluids;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.fluids.base.ClientFluidProperties;
import com.telepathicgrunt.the_bumblezone.fluids.base.FluidProperties;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

public class SugarWaterClientProperties {

    public static final ResourceLocation FLUID_STILL_TEXTURE = new ResourceLocation(Bumblezone.MODID, "block/sugar_water_still");
    public static final ResourceLocation FLUID_FLOWING_TEXTURE = new ResourceLocation(Bumblezone.MODID, "block/sugar_water_flow");
    public static final ResourceLocation FLUID_OVERLAY_TEXTURE = new ResourceLocation(Bumblezone.MODID, "block/sugar_water_overlay");
    private static final ResourceLocation TEXTURE_UNDERWATER = new ResourceLocation(Bumblezone.MODID, "textures/misc/sugar_water_underwater.png");

    public static ClientFluidProperties create(FluidProperties properties) {
        return new ClientFluidProperties(properties)
                .still(FLUID_STILL_TEXTURE)
                .flowing(FLUID_FLOWING_TEXTURE)
                .overlay(FLUID_OVERLAY_TEXTURE)
                .screenOverlay(TEXTURE_UNDERWATER)
                .tintColor((state, level, pos) -> {
                    if (level != null && pos != null) {
                        return BiomeColors.getAverageWaterColor(level, pos) | 0xFF000000;
                    }
                    return 0xFF3F76E4;
                });
    }
}
