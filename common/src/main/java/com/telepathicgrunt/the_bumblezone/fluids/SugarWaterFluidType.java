package com.telepathicgrunt.the_bumblezone.fluids;

import com.telepathicgrunt.the_bumblezone.fluids.base.FluidProperties;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public class SugarWaterFluidType {

    public static FluidProperties.Builder create() {
        return new FluidProperties.Builder("sugar_water")
                .supportsBoating(true)
                .canHydrate(true)
                .canDrown(true)
                .canExtinguish(true)
                .canPushEntity(true)
                .canSwim(true)
                .pathType(BlockPathTypes.WATER)
                .adjacentPathType(BlockPathTypes.WATER_BORDER)
                .canConvertToSource(true)
                .fallDistanceModifier(0f)
                .motionScale(0.014)
                .rarity(Rarity.COMMON)
                .viscosity(1100)
                .density(1100)
                .temperature(300)
                .sound("bucket_fill", () -> SoundEvents.BUCKET_FILL)
                .sound("bucket_empty", () -> SoundEvents.BUCKET_EMPTY)
                .sound("fluid_vaporize", () -> SoundEvents.FIRE_EXTINGUISH);
    }
}
