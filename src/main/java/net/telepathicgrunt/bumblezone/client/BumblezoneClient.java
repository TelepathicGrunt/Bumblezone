package net.telepathicgrunt.bumblezone.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import net.telepathicgrunt.bumblezone.client.rendering.FluidRender;
import net.telepathicgrunt.bumblezone.client.rendering.HoneySlimeRendering;
import net.telepathicgrunt.bumblezone.dimension.BzSkyProperty;
import net.telepathicgrunt.bumblezone.entities.BzEntities;
import net.telepathicgrunt.bumblezone.items.BzItems;
import net.telepathicgrunt.bumblezone.mixin.world.SkyPropertiesAccessor;

@Environment(EnvType.CLIENT)
public class BumblezoneClient implements ClientModInitializer {
    public static final Identifier FLUID_STILL = new Identifier(Bumblezone.MODID + ":block/sugar_water_still");
    public static final Identifier FLUID_FLOWING = new Identifier(Bumblezone.MODID + ":block/sugar_water_flow");

    @Override
    public void onInitializeClient() {
        FluidRender.setupFluidRendering(BzBlocks.SUGAR_WATER_FLUID, BzBlocks.SUGAR_WATER_FLUID_FLOWING, FLUID_STILL, FLUID_FLOWING);
        BzBlocks.registerRenderLayers();

        EntityRendererRegistry.INSTANCE.register(BzEntities.HONEY_SLIME, (dispatcher, context) -> new HoneySlimeRendering(dispatcher));
        SkyPropertiesAccessor.bz_getBY_IDENTIFIER().put(new Identifier(Bumblezone.MODID, "sky_property"), new BzSkyProperty());



        // Allows shield to use the blocking json file for offset
        FabricModelPredicateProviderRegistry.register(
                BzItems.HONEY_CRYSTAL_SHIELD,
                new Identifier("blocking"),
                (itemStack, world, livingEntity) ->
                        livingEntity != null &&
                                livingEntity.isUsingItem() &&
                                livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F
        );
    }
}