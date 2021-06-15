package com.telepathicgrunt.bumblezone.modinit;

import com.google.common.collect.ImmutableSet;
import com.telepathicgrunt.bumblezone.Bumblezone;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.util.Identifier;
import net.minecraft.world.poi.PointOfInterestType;

public class BzPOI {

    public static final PointOfInterestType BROOD_BLOCK_POI = PointOfInterestHelper.register(new Identifier(Bumblezone.MODID, "brood_block_poi"), 0, 1, ImmutableSet.copyOf(BzBlocks.HONEYCOMB_BROOD.getStateManager().getStates()));

    public static void registerPOIs() {
        // PointOfInterestHelper.register registers the poi for me. Dumb but ok. Highly unusual compared to all the other helpers...
    }
}
