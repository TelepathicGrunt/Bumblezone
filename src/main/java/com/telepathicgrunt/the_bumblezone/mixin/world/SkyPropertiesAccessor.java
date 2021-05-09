package com.telepathicgrunt.the_bumblezone.mixin.world;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DimensionRenderInfo.class)
public interface SkyPropertiesAccessor {

    @Accessor("EFFECTS")
    static Object2ObjectMap<ResourceLocation, DimensionRenderInfo> bz_getBY_ResourceLocation() {
        throw new UnsupportedOperationException();
    }
}