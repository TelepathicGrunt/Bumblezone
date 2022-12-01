package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.world.surfacerules.PollinatedSurfaceSource;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class BzSurfaceRules {
    public static void registerSurfaceRules() {
        Registry.register(BuiltInRegistries.MATERIAL_RULE, new ResourceLocation(Bumblezone.MODID, "pollinated_surface_source"), PollinatedSurfaceSource.CODEC.codec());
    }
}
