package com.telepathicgrunt.the_bumblezone.modinit;

import com.google.common.collect.ImmutableSet;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BzPOI {
    public static final DeferredRegister<PointOfInterestType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, Bumblezone.MODID);

    // POI
    public static final RegistryObject<PointOfInterestType> BROOD_BLOCK_POI = POI_TYPES.register("brood_block_poi", () -> new PointOfInterestType(Bumblezone.MODID + ":brood_block_poi", ImmutableSet.copyOf(BzBlocks.HONEYCOMB_BROOD.get().getStateDefinition().getPossibleStates()), 0, 1));
}
