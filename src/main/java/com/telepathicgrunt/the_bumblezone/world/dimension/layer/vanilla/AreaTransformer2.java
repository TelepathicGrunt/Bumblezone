package com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla;

public interface AreaTransformer2 extends DimensionTransformer {

    default <R extends Area> AreaFactory<R> run(BigContext<R> bigContext, AreaFactory<R> areaFactory, AreaFactory<R> areaFactory2) {
        return () -> {
            R area = areaFactory.make();
            R area2 = areaFactory2.make();
            return bigContext.createResult((x, z) -> {
                bigContext.initRandom(x, z);
                return this.applyPixel(bigContext, area, area2, x, z);
            }, area, area2);
        };
    }

    int applyPixel(Context context, Area area, Area area2, int x, int z);
}
