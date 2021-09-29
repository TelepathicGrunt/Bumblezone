package com.telepathicgrunt.bumblezone.tags;

import com.telepathicgrunt.bumblezone.Bumblezone;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class BzFluidTags {
    // All tag wrappers need to be made at mod init.
    public static void tagInit(){}

    public static final Tag<Fluid> HONEY_FLUID = TagRegistry.fluid(new Identifier("c:honey"));
    public static final Tag<Fluid> BZ_HONEY_FLUID = TagRegistry.fluid(new Identifier(Bumblezone.MODID+":honey"));
}
