package com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla;

public interface BigContext<R extends Area> extends Context {
    void initRandom(long l, long m);

    R createResult(PixelTransformer pixelTransformer);

    default R createResult(PixelTransformer pixelTransformer, R area) {
        return this.createResult(pixelTransformer);
    }

    default R createResult(PixelTransformer pixelTransformer, R area, R area2) {
        return this.createResult(pixelTransformer);
    }

    default int random(int i, int j) {
        return this.nextRandom(2) == 0 ? i : j;
    }

    default int random(int i, int j, int k, int l) {
        int m = this.nextRandom(4);
        if (m == 0) {
            return i;
        } else if (m == 1) {
            return j;
        } else {
            return m == 2 ? k : l;
        }
    }
}
