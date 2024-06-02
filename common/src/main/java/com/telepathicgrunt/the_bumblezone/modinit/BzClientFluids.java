package com.telepathicgrunt.the_bumblezone.modinit;

import com.teamresourceful.resourcefullib.client.fluid.data.ClientFluidProperties;
import com.teamresourceful.resourcefullib.client.registry.ResourcefulClientRegistries;
import com.teamresourceful.resourcefullib.client.registry.ResourcefulClientRegistryType;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.rendering.fluids.HoneyFluidClientProperties;
import com.telepathicgrunt.the_bumblezone.client.rendering.fluids.RoyalJellyClientProperties;
import com.telepathicgrunt.the_bumblezone.client.rendering.fluids.SugarWaterClientProperties;

public class BzClientFluids {
    public static final ResourcefulRegistry<ClientFluidProperties> CLIENT_FLUIDS = ResourcefulClientRegistries.create(ResourcefulClientRegistryType.FLUID, Bumblezone.MODID);

    public static final RegistryEntry<ClientFluidProperties> SUGAR_WATER = CLIENT_FLUIDS.register("sugar_water", SugarWaterClientProperties::create);
    public static final RegistryEntry<ClientFluidProperties> HONEY = CLIENT_FLUIDS.register("honey", HoneyFluidClientProperties::create);
    public static final RegistryEntry<ClientFluidProperties> ROYAL_JELLY = CLIENT_FLUIDS.register("royal_jelly", RoyalJellyClientProperties::create);
}
