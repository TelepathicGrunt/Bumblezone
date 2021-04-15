package com.telepathicgrunt.the_bumblezone.modinit;


import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.world.features.decorators.BeeDungeonPlacer;
import com.telepathicgrunt.the_bumblezone.world.features.decorators.HoneycombHolePlacer;
import com.telepathicgrunt.the_bumblezone.world.features.decorators.Random3DUndergroundChunkPlacement;
import net.minecraft.world.gen.feature.FeatureSpreadConfig;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BzPlacements {
    public static final DeferredRegister<Placement<?>> DECORATORS = DeferredRegister.create(ForgeRegistries.DECORATORS, Bumblezone.MODID);

    public static final RegistryObject<Placement<NoPlacementConfig>> HONEYCOMB_HOLE_PLACER = DECORATORS.register("honeycomb_hole_placer", () -> new HoneycombHolePlacer(NoPlacementConfig.CODEC));
    public static final RegistryObject<Placement<NoPlacementConfig>> BEE_DUNGEON_PLACER = DECORATORS.register("bee_dungeon_placer", () -> new BeeDungeonPlacer(NoPlacementConfig.CODEC));
    public static final RegistryObject<Placement<FeatureSpreadConfig>> RANDOM_3D_UNDERGROUND_CHUNK_PLACEMENT = DECORATORS.register("random_3d_underground_chunk_placement", () -> new Random3DUndergroundChunkPlacement(FeatureSpreadConfig.CODEC));
}
