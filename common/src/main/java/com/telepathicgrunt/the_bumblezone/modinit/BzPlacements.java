package com.telepathicgrunt.the_bumblezone.modinit;


import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistries;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistry;
import com.telepathicgrunt.the_bumblezone.world.features.decorators.ConditionBasedPlacement;
import com.telepathicgrunt.the_bumblezone.world.features.decorators.HoneycombHolePlacer;
import com.telepathicgrunt.the_bumblezone.world.features.decorators.Random3DClusterPlacement;
import com.telepathicgrunt.the_bumblezone.world.features.decorators.Random3DUndergroundChunkPlacement;
import com.telepathicgrunt.the_bumblezone.world.features.decorators.RoofedDimensionSurfacePlacement;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class BzPlacements {
    public static final ResourcefulRegistry<PlacementModifierType<?>> PLACEMENT_MODIFIER = ResourcefulRegistries.create(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE, Bumblezone.MODID);

    public static final RegistryEntry<PlacementModifierType<HoneycombHolePlacer>> HONEYCOMB_HOLE_PLACER = PLACEMENT_MODIFIER.register("honeycomb_hole_placer", () -> () -> HoneycombHolePlacer.CODEC);
    public static final RegistryEntry<PlacementModifierType<Random3DUndergroundChunkPlacement>> RANDOM_3D_UNDERGROUND_CHUNK_PLACEMENT = PLACEMENT_MODIFIER.register("random_3d_underground_chunk_placement", () -> () -> Random3DUndergroundChunkPlacement.CODEC);
    public static final RegistryEntry<PlacementModifierType<Random3DClusterPlacement>> RANDOM_3D_CLUSTER_PLACEMENT = PLACEMENT_MODIFIER.register("random_3d_cluster_placement", () -> () -> Random3DClusterPlacement.CODEC);
    public static final RegistryEntry<PlacementModifierType<ConditionBasedPlacement>> CONDITION_BASED_PLACEMENT = PLACEMENT_MODIFIER.register("condition_based_placement", () -> () -> ConditionBasedPlacement.CODEC);
    public static final RegistryEntry<PlacementModifierType<RoofedDimensionSurfacePlacement>> ROOFED_DIMENSION_SURFACE_PLACEMENT = PLACEMENT_MODIFIER.register("roofed_dimension_surface_placement", () -> () -> RoofedDimensionSurfacePlacement.CODEC);
}
