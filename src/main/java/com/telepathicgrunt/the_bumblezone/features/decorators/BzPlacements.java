package com.telepathicgrunt.the_bumblezone.features.decorators;


import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.FeatureSpreadConfig;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;

public class BzPlacements {
    public static final Placement<NoPlacementConfig> HONEYCOMB_HOLE_PLACER = new HoneycombHolePlacer(NoPlacementConfig.CODEC);
    public static final Placement<NoPlacementConfig> BEE_DUNGEON_PLACER = new BeeDungeonPlacer(NoPlacementConfig.CODEC);
    public static final Placement<FeatureSpreadConfig> RANDOM_3D_UNDERGROUND_CHUNK_PLACEMENT = new Random3DUndergroundChunkPlacement(FeatureSpreadConfig.CODEC);


    public static void registerPlacements() {
        Registry.register(Registry.DECORATOR, new ResourceLocation(Bumblezone.MODID, "honeycomb_hole_placer"), HONEYCOMB_HOLE_PLACER);
        Registry.register(Registry.DECORATOR, new ResourceLocation(Bumblezone.MODID, "bee_dungeon_placer"), BEE_DUNGEON_PLACER);
        Registry.register(Registry.DECORATOR, new ResourceLocation(Bumblezone.MODID, "random_3d_underground_chunk_placement"), RANDOM_3D_UNDERGROUND_CHUNK_PLACEMENT);
    }
}
