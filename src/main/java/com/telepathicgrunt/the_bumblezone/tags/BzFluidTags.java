package com.telepathicgrunt.the_bumblezone.tags;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.material.Fluid;

public class BzFluidTags {
    // All tag wrappers need to be made at mod init.
    public static void tagInit(){}

    public static final Tag.Named<Fluid> HONEY_FLUID = FluidTags.createOptional(new ResourceLocation("forge", "honey"));
    public static final Tag.Named<Fluid> BZ_HONEY_FLUID = FluidTags.createOptional(new ResourceLocation(Bumblezone.MODID, "honey"));
    public static final Tag.Named<Fluid> VISUAL_HONEY_FLUID = FluidTags.createOptional(new ResourceLocation("forge", "visual/honey"));
    public static final Tag.Named<Fluid> VISUAL_WATER_FLUID = FluidTags.createOptional(new ResourceLocation("forge", "visual/water"));
    public static final Tag.Named<Fluid> CONVERTIBLE_TO_SUGAR_WATER = FluidTags.createOptional(new ResourceLocation(Bumblezone.MODID, "convertible_to_sugar_water"));

}
