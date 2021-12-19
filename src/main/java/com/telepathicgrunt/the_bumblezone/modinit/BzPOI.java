package com.telepathicgrunt.the_bumblezone.modinit;

import com.google.common.collect.ImmutableSet;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;

public class BzPOI {

    public static final PoiType BROOD_BLOCK_POI = PointOfInterestHelper.register(new ResourceLocation(Bumblezone.MODID, "brood_block_poi"), 0, 1, ImmutableSet.copyOf(BzBlocks.HONEYCOMB_BROOD.getStateDefinition().getPossibleStates()));

    public static void registerPOIs() {
        // PointOfInterestHelper.register registers the poi for me. Dumb but ok. Highly unusual compared to all the other helpers...
    }
}
