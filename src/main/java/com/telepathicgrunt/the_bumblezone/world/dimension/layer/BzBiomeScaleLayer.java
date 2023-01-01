package com.telepathicgrunt.the_bumblezone.world.dimension.layer;

import com.telepathicgrunt.the_bumblezone.world.dimension.BiomeRegistryHolder;
import com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla.CastleTransformer;
import com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla.Context;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;

public record BzBiomeScaleLayer(Set<ResourceLocation> biomesToExpand) implements CastleTransformer {

    @Override
    public int apply(Context context, int n, int e, int s, int w, int center) {
        if (!biomesToExpand.contains(BiomeRegistryHolder.convertToRL(center))) {
            if (biomesToExpand.contains(BiomeRegistryHolder.convertToRL(n))) {
                return n;
            }
            else if (biomesToExpand.contains(BiomeRegistryHolder.convertToRL(s))) {
                return s;
            }
            else if (biomesToExpand.contains(BiomeRegistryHolder.convertToRL(e))) {
                return e;
            }
            else if (biomesToExpand.contains(BiomeRegistryHolder.convertToRL(w))) {
                return w;
            }
        }
        return center;
    }
}