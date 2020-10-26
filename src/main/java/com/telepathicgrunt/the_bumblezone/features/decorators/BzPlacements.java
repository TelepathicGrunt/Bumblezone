package com.telepathicgrunt.the_bumblezone.features.decorators;


import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.generation.BzBiomeProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.BiomeMaker;
import net.minecraft.world.gen.feature.FeatureSpreadConfig;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.RegistryEvent;

public class BzPlacements {
    public static final Placement<NoPlacementConfig> HONEYCOMB_HOLE_PLACER = new HoneycombHolePlacer(NoPlacementConfig.CODEC);
    public static final Placement<NoPlacementConfig> BEE_DUNGEON_PLACER = new BeeDungeonPlacer(NoPlacementConfig.CODEC);
    public static final Placement<FeatureSpreadConfig> RANDOM_3D_UNDERGROUND_CHUNK_PLACEMENT = new Random3DUndergroundChunkPlacement(FeatureSpreadConfig.CODEC);


    public static void registerPlacements(final RegistryEvent.Register<Placement<?>> event) {
        event.getRegistry().register(HONEYCOMB_HOLE_PLACER.setRegistryName(new ResourceLocation(Bumblezone.MODID, "honeycomb_hole_placer")));
        event.getRegistry().register(BEE_DUNGEON_PLACER.setRegistryName(new ResourceLocation(Bumblezone.MODID, "bee_dungeon_placer")));
        event.getRegistry().register(RANDOM_3D_UNDERGROUND_CHUNK_PLACEMENT.setRegistryName(new ResourceLocation(Bumblezone.MODID, "random_3d_underground_chunk_placement")));
    }
}
