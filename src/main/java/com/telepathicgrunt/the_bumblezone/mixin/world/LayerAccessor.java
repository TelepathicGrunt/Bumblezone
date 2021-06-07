package com.telepathicgrunt.the_bumblezone.mixin.world;

import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.layer.Layer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Layer.class)
public interface LayerAccessor {

    @Accessor("area")
    LazyArea thebumblezone_getSampler();
}
