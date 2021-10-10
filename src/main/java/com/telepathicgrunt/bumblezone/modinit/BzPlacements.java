package com.telepathicgrunt.bumblezone.modinit;


import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.world.features.decorators.BeeDungeonPlacer;
import com.telepathicgrunt.bumblezone.world.features.decorators.HoneycombHolePlacer;
import com.telepathicgrunt.bumblezone.world.features.decorators.Random3DUndergroundChunkPlacement;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;

public class BzPlacements {
    public static final FeatureDecorator<NoneDecoratorConfiguration> HONEYCOMB_HOLE_PLACER = new HoneycombHolePlacer(NoneDecoratorConfiguration.CODEC);
    public static final FeatureDecorator<NoneDecoratorConfiguration> BEE_DUNGEON_PLACER = new BeeDungeonPlacer(NoneDecoratorConfiguration.CODEC);
    public static final FeatureDecorator<CountConfiguration> RANDOM_3D_UNDERGROUND_CHUNK_PLACEMENT = new Random3DUndergroundChunkPlacement(CountConfiguration.CODEC);


    public static void registerPlacements() {
        Registry.register(Registry.DECORATOR, new ResourceLocation(Bumblezone.MODID, "honeycomb_hole_placer"), HONEYCOMB_HOLE_PLACER);
        Registry.register(Registry.DECORATOR, new ResourceLocation(Bumblezone.MODID, "bee_dungeon_placer"), BEE_DUNGEON_PLACER);
        Registry.register(Registry.DECORATOR, new ResourceLocation(Bumblezone.MODID, "random_3d_underground_chunk_placement"), RANDOM_3D_UNDERGROUND_CHUNK_PLACEMENT);
    }
}
