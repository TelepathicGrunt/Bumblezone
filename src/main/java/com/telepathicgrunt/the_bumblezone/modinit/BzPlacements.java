package com.telepathicgrunt.the_bumblezone.modinit;


import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.world.features.decorators.HoneycombHolePlacer;
import com.telepathicgrunt.the_bumblezone.world.features.decorators.Random3DClusterPlacement;
import com.telepathicgrunt.the_bumblezone.world.features.decorators.Random3DUndergroundChunkPlacement;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class BzPlacements {
    public static PlacementModifierType<?> HONEYCOMB_HOLE_PLACER;
    public static PlacementModifierType<?> RANDOM_3D_UNDERGROUND_CHUNK_PLACEMENT;
    public static PlacementModifierType<?> RANDOM_3D_CLUSTER_PLACEMENT;

    public static void registerPlacements() {
        HONEYCOMB_HOLE_PLACER = register(new ResourceLocation(Bumblezone.MODID, "honeycomb_hole_placer"), HoneycombHolePlacer.CODEC);
        RANDOM_3D_UNDERGROUND_CHUNK_PLACEMENT = register(new ResourceLocation(Bumblezone.MODID, "random_3d_underground_chunk_placement"), Random3DUndergroundChunkPlacement.CODEC);
        RANDOM_3D_CLUSTER_PLACEMENT = register(new ResourceLocation(Bumblezone.MODID, "random_3d_cluster_placement"), Random3DClusterPlacement.CODEC);
    }

    private static <P extends PlacementModifier> PlacementModifierType<P> register(ResourceLocation resourceLocation, Codec<P> codec) {
        return Registry.register(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE, resourceLocation, () -> codec);
    }
}
