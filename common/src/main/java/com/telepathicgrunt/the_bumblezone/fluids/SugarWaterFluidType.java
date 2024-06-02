package com.telepathicgrunt.the_bumblezone.fluids;

import com.teamresourceful.resourcefullib.common.fluid.data.FluidProperties;
import com.telepathicgrunt.the_bumblezone.client.rendering.fluids.SugarWaterClientProperties;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.pathfinder.PathType;

public class SugarWaterFluidType {

    public static FluidProperties.Builder create() {

        return FluidProperties.builder()
                .supportsBoating(true)
                .canHydrate(true)
                .canDrown(true)
                .canExtinguish(true)
                .canPushEntity(true)
                .canSwim(true)
                .pathType(PathType.WATER)
                .adjacentPathType(PathType.WATER_BORDER)
                .canConvertToSource(true)
                .fallDistanceModifier(0f)
                .motionScale(0.014)
                .rarity(Rarity.COMMON)
                .viscosity(1100)
                .density(1100)
                .temperature(300)
                .still(SugarWaterClientProperties.FLUID_STILL_TEXTURE)
                .flowing(SugarWaterClientProperties.FLUID_FLOWING_TEXTURE)
                .overlay(SugarWaterClientProperties.FLUID_OVERLAY_TEXTURE)
                .screenOverlay(SugarWaterClientProperties.TEXTURE_UNDERWATER)
                .sounds("bucket_fill", SoundEvents.BUCKET_FILL)
                .sounds("bucket_empty", SoundEvents.BUCKET_EMPTY)
                .sounds("fluid_vaporize", SoundEvents.FIRE_EXTINGUISH);
    }
}
