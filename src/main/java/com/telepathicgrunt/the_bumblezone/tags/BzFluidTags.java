package com.telepathicgrunt.the_bumblezone.tags;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.fluid.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;

public class BzFluidTags {
    // All tag wrappers need to be made at mod init.
    public static void tagInit(){}

    public static final ITag.INamedTag<Fluid> HONEY_FLUID = FluidTags.bind("forge:honey");
    public static final ITag.INamedTag<Fluid> BZ_HONEY_FLUID = FluidTags.bind(Bumblezone.MODID+":honey");

}
