package net.telepathicgrunt.bumblezone.features.placement;

import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.telepathicgrunt.bumblezone.dimension.RegUtil;

public class BzPlacements
{
	public static final Placement<NoPlacementConfig> HONEYCOMB_HOLE_PLACER = new HoneycombHolePlacer(NoPlacementConfig::deserialize);
	public static final Placement<NoPlacementConfig> BEE_DUNGEON_PLACER = new BeeDungeonPlacer(NoPlacementConfig::deserialize);
	
	
	public static void registerPlacements(RegistryEvent.Register<Placement<?>> event)
	{
		IForgeRegistry<Placement<?>> registry = event.getRegistry();
		RegUtil.register(registry, HONEYCOMB_HOLE_PLACER, "honeycomb_hole_placer");
		RegUtil.register(registry, BEE_DUNGEON_PLACER, "bee_dungeon_placer");
	}
}
