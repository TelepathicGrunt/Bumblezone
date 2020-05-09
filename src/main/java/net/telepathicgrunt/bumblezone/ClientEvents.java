package net.telepathicgrunt.bumblezone;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import net.telepathicgrunt.bumblezone.fluids.SugarWaterClientEvents;


public class ClientEvents
{
	public static void subscribeClientEvents(IEventBus modBus, IEventBus forgeBus)
	{
		modBus.addListener(ClientEvents::onClientSetup);
		forgeBus.addListener(SugarWaterClientEvents::sugarWaterOverlay);
	}


	public static void onClientSetup(FMLClientSetupEvent event)
	{
		RenderTypeLookup.setRenderLayer(BzBlocks.STICKY_HONEY_REDSTONE.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(BzBlocks.STICKY_HONEY_RESIDUE.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(BzBlocks.HONEY_CRYSTAL.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BzBlocks.SUGAR_WATER_BLOCK.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BzBlocks.SUGAR_WATER_FLUID.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BzBlocks.SUGAR_WATER_FLUID_FLOWING.get(), RenderType.getTranslucent());
	}
}