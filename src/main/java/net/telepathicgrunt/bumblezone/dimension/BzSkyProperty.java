package net.telepathicgrunt.bumblezone.dimension;

import net.minecraft.client.render.SkyProperties;
import net.minecraft.util.math.Vec3d;

public class BzSkyProperty extends SkyProperties {
    public BzSkyProperty() {
        super(280, true, SkyType.NONE, false, false);
    }

    @Override
    public Vec3d adjustSkyColor(Vec3d color, float sunHeight) {
        return color.multiply(1.2D);
    }


    @Override
    public boolean useThickFog(int camX, int camY) {
        return false;
    }
}
