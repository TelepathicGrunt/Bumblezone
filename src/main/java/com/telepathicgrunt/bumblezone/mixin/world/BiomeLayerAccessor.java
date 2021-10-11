package com.telepathicgrunt.bumblezone.mixin.world;

import net.minecraft.world.level.newbiome.area.LazyArea;
import net.minecraft.world.level.newbiome.layer.Layer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Layer.class)
public interface BiomeLayerAccessor {

    @Accessor("area")
    LazyArea thebumblezone_getSampler();
}
