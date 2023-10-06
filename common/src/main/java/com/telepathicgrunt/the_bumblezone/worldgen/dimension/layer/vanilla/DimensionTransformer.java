package com.telepathicgrunt.the_bumblezone.worldgen.dimension.layer.vanilla;

public interface DimensionTransformer {
    int getParentX(int x);

    int getParentZ(int z);
}
