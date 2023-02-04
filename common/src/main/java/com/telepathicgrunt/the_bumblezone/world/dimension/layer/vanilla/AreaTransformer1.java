package com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla;

public interface AreaTransformer1 extends DimensionTransformer {
    default <R extends Area> AreaFactory<R> run(BigContext<R> bigContext, AreaFactory<R> areaFactory) {
        return () -> {
            R area = areaFactory.make();
            return bigContext.createResult((x, z) -> {
                bigContext.initRandom(x, z);
                return this.applyPixel(bigContext, area, x, z);
            }, area);
        };
    }

    int applyPixel(BigContext<?> bigContext, Area area, int x, int z);
}
