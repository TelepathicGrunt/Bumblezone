package com.telepathicgrunt.the_bumblezone.modinit;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.world.surfacerules.PollinatedSurfaceSource;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class BzSurfaceRules {

    public static final DeferredRegister<Codec<? extends SurfaceRules.RuleSource>> SURFACE_RULES = DeferredRegister.create(Registries.MATERIAL_RULE, Bumblezone.MODID);

    public static final RegistryObject<Codec<? extends SurfaceRules.RuleSource>> POLLINATED_SURFACE_SOURCE = SURFACE_RULES.register("pollinated_surface_source", PollinatedSurfaceSource.CODEC::codec);
}
