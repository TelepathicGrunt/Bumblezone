package com.telepathicgrunt.the_bumblezone.modinit;

import com.google.common.collect.ImmutableSet;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BzPOI {
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, Bumblezone.MODID);

    public static final RegistryObject<PoiType> BROOD_BLOCK_POI = POI_TYPES.register("brood_block_poi", () -> new PoiType(ImmutableSet.copyOf(BzBlocks.HONEYCOMB_BROOD.get().getStateDefinition().getPossibleStates()), 0, 1));
    public static final RegistryObject<PoiType> CRYSTAL_FLOWER_POI = POI_TYPES.register("crystal_flower_poi", () -> new PoiType(ImmutableSet.copyOf(BzBlocks.CRYSTALLINE_FLOWER.get().getStateDefinition().getPossibleStates()), 0, 2));
}
