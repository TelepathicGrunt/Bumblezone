package com.telepathicgrunt.bumblezone.world.dimension.layer.vanilla;

public interface DimensionOffset1Transformer extends DimensionTransformer {
    @Override
    default int getParentX(int x) {
        return x - 1;
    }

    @Override
    default int getParentZ(int z) {
        return z - 1;
    }
}
