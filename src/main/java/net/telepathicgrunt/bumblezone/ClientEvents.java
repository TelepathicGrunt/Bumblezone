package net.telepathicgrunt.bumblezone;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;


public class ClientEvents
{
	public static void subscribeClientEvents(IEventBus modBus, IEventBus forgeBus)
	{
		modBus.addListener(ClientEvents::onClientSetup);
	}


	public static void onClientSetup(FMLClientSetupEvent event)
	{
		RenderTypeLookup.setRenderLayer(BzBlocks.SUGAR_WATER_BLOCK.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BzBlocks.SUGAR_WATER_FLUID.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BzBlocks.SUGAR_WATER_FLUID_FLOWING.get(), RenderType.getTranslucent());
	}
}