package com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla;

public enum ZoomLayer implements AreaTransformer1 {
    NORMAL,
    FUZZY {
        @Override
        protected int modeOrRandom(BigContext<?> bigContext, int n, int e, int s, int w) {
            return bigContext.random(n, e, s, w);
        }
    };

    @Override
    public int getParentX(int i) {
        return i >> 1;
    }

    @Override
    public int getParentZ(int i) {
        return i >> 1;
    }

    @Override
    public int applyPixel(BigContext<?> bigContext, Area area, int x, int z) {
        int m = area.get(this.getParentX(x), this.getParentZ(z));
        bigContext.initRandom(x >> 1 << 1, z >> 1 << 1);
        int xOffset = x & 1;
        int zOffset = z & 1;
        if (xOffset == 0 && zOffset == 0) {
            return m;
        }
        else {
            int n = area.get(this.getParentX(x), this.getParentZ(z + 1));
            int o = bigContext.random(m, n);
            if (xOffset == 0 && zOffset == 1) {
                return o;
            }
            else {
                int p = area.get(this.getParentX(x + 1), this.getParentZ(z));
                int q = bigContext.random(m, p);
                if (xOffset == 1 && zOffset == 0) {
                    return q;
                }
                else {
                    int r = area.get(this.getParentX(x + 1), this.getParentZ(z + 1));
                    return this.modeOrRandom(bigContext, m, p, n, r);
                }
            }
        }
    }

    protected int modeOrRandom(BigContext<?> bigContext, int n, int e, int s, int w) {
        if (e == s && s == w) {
            return e;
        }
        else if (n == e && n == s) {
            return n;
        }
        else if (n == e && n == w) {
            return n;
        }
        else if (n == s && n == w) {
            return n;
        }
        else if (n == e && s != w) {
            return n;
        }
        else if (n == s && e != w) {
            return n;
        }
        else if (n == w && e != s) {
            return n;
        }
        else if (e == s && n != w) {
            return e;
        }
        else if (e == w && n != s) {
            return e;
        }
        else {
            return s == w && n != e ? s : bigContext.random(n, e, s, w);
        }
    }
}
