package com.telepathicgrunt.the_bumblezone.features.decorators;


import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.world.gen.feature.FeatureSpreadConfig;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class BzPlacements {
    public static final DeferredRegister<Placement<?>> DECORATORS = DeferredRegister.create(ForgeRegistries.DECORATORS, Bumblezone.MODID);

    public static final RegistryObject<Placement<NoPlacementConfig>> HONEYCOMB_HOLE_PLACER = createDecorator("honeycomb_hole_placer", () -> new HoneycombHolePlacer(NoPlacementConfig.CODEC));
    public static final RegistryObject<Placement<NoPlacementConfig>> BEE_DUNGEON_PLACER = createDecorator("bee_dungeon_placer", () -> new BeeDungeonPlacer(NoPlacementConfig.CODEC));
    public static final RegistryObject<Placement<FeatureSpreadConfig>> RANDOM_3D_UNDERGROUND_CHUNK_PLACEMENT = createDecorator("random_3d_underground_chunk_placement", () -> new Random3DUndergroundChunkPlacement(FeatureSpreadConfig.CODEC));

    public static <D extends Placement<?>> RegistryObject<D> createDecorator(String name, Supplier<? extends D> decorator) {
        return DECORATORS.register(name, decorator);
    }
}
