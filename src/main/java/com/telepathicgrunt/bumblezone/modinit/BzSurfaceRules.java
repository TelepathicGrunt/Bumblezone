package com.telepathicgrunt.bumblezone.modinit;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.world.SurfaceRules.PollinatedSurfaceSource;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public class BzSurfaceRules {
    public static void registerSurfaceRules() {
        Registry.register(Registry.RULE, new ResourceLocation(Bumblezone.MODID, "pollinated_surface_source"), PollinatedSurfaceSource.CODEC);
    }
}
