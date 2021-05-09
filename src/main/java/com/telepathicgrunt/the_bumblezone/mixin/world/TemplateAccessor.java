package com.telepathicgrunt.the_bumblezone.mixin.world;

import net.minecraft.world.gen.feature.template.Template;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(Template.class)
public interface TemplateAccessor {

    @Accessor("palettes")
    List<Template.Palette> bz_getBlocks();
}
