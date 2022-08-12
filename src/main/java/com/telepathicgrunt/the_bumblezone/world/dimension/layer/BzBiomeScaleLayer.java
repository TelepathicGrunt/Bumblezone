package com.telepathicgrunt.the_bumblezone.world.dimension.layer;

import com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla.CastleTransformer;
import com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla.Context;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record BzBiomeScaleLayer(Set<Integer> biomesToExpand, Registry<Biome> biomeRegistry) implements CastleTransformer {

    @Override
    public int apply(Context context, int n, int e, int s, int w, int center) {
        if (!biomesToExpand.contains(center)) {
            if (biomesToExpand.contains(n)) {
                return n;
            }
            else if (biomesToExpand.contains(s)) {
                return s;
            }
            else if (biomesToExpand.contains(e)) {
                return e;
            }
            else if (biomesToExpand.contains(w)) {
                return w;
            }
        }
        return center;
    }
}