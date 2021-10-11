package com.telepathicgrunt.bumblezone.tags;

import com.telepathicgrunt.bumblezone.Bumblezone;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.material.Fluid;

public class BzFluidTags {
    // All tag wrappers need to be made at mod init.
    public static void tagInit(){}

    public static final Tag<Fluid> HONEY_FLUID = TagRegistry.fluid(new ResourceLocation("c:honey"));
    public static final Tag<Fluid> BZ_HONEY_FLUID = TagRegistry.fluid(new ResourceLocation(Bumblezone.MODID+":honey"));
}
