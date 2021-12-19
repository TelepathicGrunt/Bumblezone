package com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla;

public interface CastleTransformer extends AreaTransformer1, DimensionOffset1Transformer {
    int apply(Context context, int n, int e, int s, int w, int c);

    @Override
    default int applyPixel(BigContext<?> bigContext, Area area, int x, int z) {
        return this.apply(bigContext,
                area.get(this.getParentX(x + 1), this.getParentZ(z + 0)),
                area.get(this.getParentX(x + 2), this.getParentZ(z + 1)),
                area.get(this.getParentX(x + 1), this.getParentZ(z + 2)),
                area.get(this.getParentX(x + 0), this.getParentZ(z + 1)),
                area.get(this.getParentX(x + 1), this.getParentZ(z + 1)));
    }
}
