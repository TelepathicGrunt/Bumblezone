package net.telepathicgrunt.bumblezone.biome.surfacebuilders;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import net.telepathicgrunt.bumblezone.dimension.RegUtil;

public class BzSurfaceBuilders
{
    protected static final BlockState HONEY = Blocks.HONEY_BLOCK.getDefaultState();
    protected static final BlockState FILLED_POROUS_HONEYCOMB = BzBlocks.FILLED_POROUS_HONEYCOMB.get().getDefaultState();
    protected static final BlockState POROUS_HONEYCOMB = BzBlocks.POROUS_HONEYCOMB.get().getDefaultState();
    
    public static final SurfaceBuilderConfig HONEY_CONFIG = new SurfaceBuilderConfig(POROUS_HONEYCOMB, POROUS_HONEYCOMB, POROUS_HONEYCOMB);
    public static final SurfaceBuilder<SurfaceBuilderConfig> HONEY_SURFACE_BUILDER = new HoneySurfaceBuilder(SurfaceBuilderConfig::deserialize);



	public static void registerSurfaceBuilders(RegistryEvent.Register<SurfaceBuilder<?>> event)
	{
		IForgeRegistry<SurfaceBuilder<?>> registry = event.getRegistry();

		RegUtil.register(registry, HONEY_SURFACE_BUILDER, "honey_surface_builder");
	}
}
