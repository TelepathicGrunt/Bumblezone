package com.telepathicgrunt.the_bumblezone.tags;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.material.Fluid;

public class BzFluidTags {
    // All tag wrappers need to be made at mod init.
    public static void tagInit() {}

    public static final Tag<Fluid> HONEY_FLUID = TagFactory.FLUID.create(new ResourceLocation("c:honey"));
    public static final Tag<Fluid> BZ_HONEY_FLUID = TagFactory.FLUID.create(new ResourceLocation(Bumblezone.MODID+":honey"));
}
