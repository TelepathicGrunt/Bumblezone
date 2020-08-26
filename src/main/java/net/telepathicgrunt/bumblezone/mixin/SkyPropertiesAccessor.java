package net.telepathicgrunt.bumblezone.mixin;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.client.render.SkyProperties;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SkyProperties.class)
public interface SkyPropertiesAccessor {

    @Accessor("BY_ResourceLocation")
    static Object2ObjectMap<ResourceLocation, SkyProperties> getBY_ResourceLocation() {
        throw new UnsupportedOperationException();
    }
}