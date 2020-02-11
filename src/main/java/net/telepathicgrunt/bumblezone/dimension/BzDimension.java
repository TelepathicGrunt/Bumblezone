package net.telepathicgrunt.bumblezone.dimension;

import java.util.function.BiFunction;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.telepathicgrunt.bumblezone.Bumblezone;


@Mod.EventBusSubscriber(modid = Bumblezone.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BzDimension {

	public static final ModDimension BUMBLEZONE = new ModDimension() {
        @Override
        public BiFunction<World, DimensionType, ? extends Dimension> getFactory() {
            return BzWorldProvider::new;
        }
    };

    private static final ResourceLocation BUMBLEZONE_ID = new ResourceLocation(Bumblezone.MODID, "bumblezone");
	
    
    //registers the dimension
    @Mod.EventBusSubscriber(modid = Bumblezone.MODID)
    private static class ForgeEvents {
        @SubscribeEvent
        public static void registerDimensions(RegisterDimensionsEvent event) {
            if (DimensionType.byName(BUMBLEZONE_ID) == null) {
                DimensionManager.registerDimension(BUMBLEZONE_ID, BUMBLEZONE, null, true);
            }
        }
    }

    @SubscribeEvent
    public static void registerModDimensions(RegistryEvent.Register<ModDimension> event) {
        RegUtil.generic(event.getRegistry()).add("ultraamplified", BUMBLEZONE);
    }

    public static DimensionType bumblezone() {
        return DimensionType.byName(BUMBLEZONE_ID);
    }
    
}
