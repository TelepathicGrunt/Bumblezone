package com.telepathicgrunt.the_bumblezone.modinit;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistries;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistry;
import com.telepathicgrunt.the_bumblezone.world.surfacerules.PollinatedSurfaceSource;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.SurfaceRules;

public class BzSurfaceRules {

    public static final ResourcefulRegistry<Codec<? extends SurfaceRules.RuleSource>> SURFACE_RULES = ResourcefulRegistries.create(Registries.MATERIAL_RULE, Bumblezone.MODID);

    public static final RegistryEntry<Codec<? extends SurfaceRules.RuleSource>> POLLINATED_SURFACE_SOURCE = SURFACE_RULES.register("pollinated_surface_source", PollinatedSurfaceSource.CODEC::codec);
}
