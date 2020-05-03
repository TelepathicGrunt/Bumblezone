package net.telepathicgrunt.bumblezone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.telepathicgrunt.bumblezone.biome.BzBaseBiome;
import net.telepathicgrunt.bumblezone.biome.BzBiomes;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import net.telepathicgrunt.bumblezone.blocks.SugarWaterEvents;
import net.telepathicgrunt.bumblezone.capabilities.CapabilityPlayerPosAndDim;
import net.telepathicgrunt.bumblezone.config.BzConfig;
import net.telepathicgrunt.bumblezone.config.BzConfig.BzConfigValues;
import net.telepathicgrunt.bumblezone.effects.BzEffects;
import net.telepathicgrunt.bumblezone.features.BzFeatures;
import net.telepathicgrunt.bumblezone.items.BzItems;
import net.telepathicgrunt.bumblezone.items.SugarWaterBottleDispenseBehavior;
import net.telepathicgrunt.bumblezone.modcompatibility.ModChecking;
import net.telepathicgrunt.bumblezone.utils.ConfigHelper;


// The value here should match an entry in the META-INF/mods.toml file
@SuppressWarnings("deprecation")
@Mod(Bumblezone.MODID)
public class Bumblezone
{
	public static final String MODID = "the_bumblezone";
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	public static BzConfigValues BzConfig = null;

	//TODO: add recipe to turn bucket of water into sugar water and bottle of water into sugar water bottle. 
	public Bumblezone()
	{
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		MinecraftForge.EVENT_BUS.register(this);
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;

		modEventBus.addListener(this::setup);

        BzBlocks.BLOCKS.register(modEventBus);
        BzItems.ITEMS.register(modEventBus);
        BzBlocks.FLUIDS.register(modEventBus);
        
    	DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> ClientEvents.subscribeClientEvents(modEventBus, forgeBus));

		//generates/handles config
		BzConfig = ConfigHelper.register(ModConfig.Type.SERVER, (builder, subscriber) -> new BzConfig.BzConfigValues(builder, subscriber));
	}


	private void setup(final FMLCommonSetupEvent event)
	{
		CapabilityPlayerPosAndDim.register();
		SugarWaterEvents.setup();
		BzBaseBiome.addSprings();
		
		IDispenseItemBehavior idispenseitembehavior = new DefaultDispenseItemBehavior() {
	         private final DefaultDispenseItemBehavior field_218406_b = new DefaultDispenseItemBehavior();

	         /**
	          * Dispense the specified stack, play the dispense sound and spawn particles.
	          */
	         public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
	            BucketItem bucketitem = (BucketItem)stack.getItem();
	            BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().get(DispenserBlock.FACING));
	            World world = source.getWorld();
	            if (bucketitem.tryPlaceContainedLiquid((PlayerEntity)null, world, blockpos, (BlockRayTraceResult)null)) {
	               bucketitem.onLiquidPlaced(world, stack, blockpos);
	               return new ItemStack(Items.BUCKET);
	            } else {
	               return this.field_218406_b.dispense(source, stack);
	            }
	         }
	      };
		DispenserBlock.registerDispenseBehavior(BzItems.SUGAR_WATER_BUCKET.get(), idispenseitembehavior); //adds compatibility with sugar water buckets in dispensers
		DispenserBlock.registerDispenseBehavior(BzItems.SUGAR_WATER_BOTTLE.get(), new SugarWaterBottleDispenseBehavior()); //adds compatibility with sugar water bottles in dispensers
		
		DeferredWorkQueue.runLater(Bumblezone::lateSetup);
	}

	//should run after most other mods just in case
	private static void lateSetup()
	{
		ModChecking.setupModCompat();
		BzBiomes.addVanillaSlimeMobs();
	}

	// You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
	// Event bus for receiving Registry Events)
	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents
	{

		@SubscribeEvent
		public static void registerBiomes(final RegistryEvent.Register<Biome> event)
		{
			//registers all my modified biomes
			BzBiomes.registerBiomes(event);
		}


		/**
		 * This method will be called by Forge when it is time for the mod to register features.
		 */
		@SubscribeEvent
		public static void onRegisterFeatures(final RegistryEvent.Register<Feature<?>> event)
		{
			BzFeatures.registerFeatures(event);
		}


		/**
		 * This method will be called by Forge when it is time for the mod to register effects.
		 */
		@SubscribeEvent
		public static void onRegisterEffects(final RegistryEvent.Register<Effect> event)
		{
			BzEffects.registerEffects(event);
		}
	}
}
