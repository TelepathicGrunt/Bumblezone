package com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla;

public interface AreaTransformer0 {
    default <R extends Area> AreaFactory<R> run(BigContext<R> bigContext) {
        return () -> bigContext.createResult((x, z) -> {
            bigContext.initRandom(x, z);
            return this.applyPixel(bigContext, x, z);
        });
    }

    int applyPixel(Context context, int x, int z);
}
