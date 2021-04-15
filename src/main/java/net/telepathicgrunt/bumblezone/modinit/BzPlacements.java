package net.telepathicgrunt.bumblezone.modinit;


import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.CountConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.NopeDecoratorConfig;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.world.features.decorators.BeeDungeonPlacer;
import net.telepathicgrunt.bumblezone.world.features.decorators.HoneycombHolePlacer;
import net.telepathicgrunt.bumblezone.world.features.decorators.Random3DUndergroundChunkPlacement;

public class BzPlacements {
    public static final Decorator<NopeDecoratorConfig> HONEYCOMB_HOLE_PLACER = new HoneycombHolePlacer(NopeDecoratorConfig.CODEC);
    public static final Decorator<NopeDecoratorConfig> BEE_DUNGEON_PLACER = new BeeDungeonPlacer(NopeDecoratorConfig.CODEC);
    public static final Decorator<CountConfig> RANDOM_3D_UNDERGROUND_CHUNK_PLACEMENT = new Random3DUndergroundChunkPlacement(CountConfig.CODEC);


    public static void registerPlacements() {
        Registry.register(Registry.DECORATOR, new Identifier(Bumblezone.MODID, "honeycomb_hole_placer"), HONEYCOMB_HOLE_PLACER);
        Registry.register(Registry.DECORATOR, new Identifier(Bumblezone.MODID, "bee_dungeon_placer"), BEE_DUNGEON_PLACER);
        Registry.register(Registry.DECORATOR, new Identifier(Bumblezone.MODID, "random_3d_underground_chunk_placement"), RANDOM_3D_UNDERGROUND_CHUNK_PLACEMENT);
    }
}
