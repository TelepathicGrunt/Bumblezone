package com.telepathicgrunt.the_bumblezone.tags;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.material.Fluid;

public class BzFluidTags {
    // All tag wrappers need to be made at mod init.
    public static void tagInit(){}

    public static final Tag.Named<Fluid> HONEY_FLUID = FluidTags.createOptional(new ResourceLocation("forge:honey"));
    public static final Tag.Named<Fluid> BZ_HONEY_FLUID = FluidTags.createOptional(new ResourceLocation(Bumblezone.MODID+":honey"));

}
