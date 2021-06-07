package com.telepathicgrunt.bumblezone.mixin.world;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.client.render.SkyProperties;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SkyProperties.class)
public interface SkyPropertiesAccessor {

    @Accessor("BY_IDENTIFIER")
    static Object2ObjectMap<Identifier, SkyProperties> thebumblezone_getBY_IDENTIFIER() {
        throw new UnsupportedOperationException();
    }
}