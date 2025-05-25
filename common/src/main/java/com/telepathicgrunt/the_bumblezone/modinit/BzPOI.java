package com.telepathicgrunt.the_bumblezone.modinit;

import com.google.common.collect.ImmutableSet;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistries;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.village.poi.PoiType;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BzPOI {
    public static final ResourcefulRegistry<PoiType> POI_TYPES = ResourcefulRegistries.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE, Bumblezone.MODID);

    public static final RegistryEntry<PoiType> BROOD_BLOCK_POI = POI_TYPES.register("brood_block_poi", () -> new PoiType(ImmutableSet.copyOf(BzBlocks.HONEYCOMB_BROOD.get().getStateDefinition().getPossibleStates()), 0, 1));
    public static final RegistryEntry<PoiType> CRYSTAL_FLOWER_POI = POI_TYPES.register("crystal_flower_poi", () -> new PoiType(ImmutableSet.copyOf(BzBlocks.CRYSTALLINE_FLOWER.get().getStateDefinition().getPossibleStates()), 0, 2));
    public static final RegistryEntry<PoiType> ESSENCE_BLOCK_POI = POI_TYPES.register("essence_block_poi", () -> new PoiType(Stream.of(
        BzBlocks.ESSENCE_BLOCK_RED.get().getStateDefinition().getPossibleStates(),
        BzBlocks.ESSENCE_BLOCK_PURPLE.get().getStateDefinition().getPossibleStates(),
        BzBlocks.ESSENCE_BLOCK_BLUE.get().getStateDefinition().getPossibleStates(),
        BzBlocks.ESSENCE_BLOCK_GREEN.get().getStateDefinition().getPossibleStates(),
        BzBlocks.ESSENCE_BLOCK_YELLOW.get().getStateDefinition().getPossibleStates(),
        BzBlocks.ESSENCE_BLOCK_WHITE.get().getStateDefinition().getPossibleStates())
            .flatMap(Collection::stream)
            .collect(Collectors.toSet()
    ), 0, 1));
}
