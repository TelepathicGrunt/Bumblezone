package com.telepathicgrunt.bumblezone.modinit;

import com.google.common.collect.ImmutableSet;
import com.telepathicgrunt.bumblezone.Bumblezone;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.poi.PointOfInterestType;

public class BzPOI {

    public static final PointOfInterestType BROOD_BLOCK_POI = PointOfInterestHelper.register(new Identifier(Bumblezone.MODID, "brood_block_poi"), 0, 1, ImmutableSet.copyOf(BzBlocks.HONEYCOMB_BROOD.getStateManager().getStates()));

    public static void registerPOIs() {
        Registry.register(Registry.POINT_OF_INTEREST_TYPE, new Identifier(Bumblezone.MODID, "brood_block_poi"), BROOD_BLOCK_POI);
    }
}
