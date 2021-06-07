package com.telepathicgrunt.bumblezone.client;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.client.rendering.FluidRender;
import com.telepathicgrunt.bumblezone.client.rendering.HoneySlimeRendering;
import com.telepathicgrunt.bumblezone.mixin.world.SkyPropertiesAccessor;
import com.telepathicgrunt.bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.bumblezone.modinit.BzEntities;
import com.telepathicgrunt.bumblezone.modinit.BzItems;
import com.telepathicgrunt.bumblezone.world.dimension.BzSkyProperty;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class BumblezoneClient implements ClientModInitializer {
    public static final Identifier FLUID_STILL = new Identifier(Bumblezone.MODID + ":block/sugar_water_still");
    public static final Identifier FLUID_FLOWING = new Identifier(Bumblezone.MODID + ":block/sugar_water_flow");

    @Override
    public void onInitializeClient() {
        FluidRender.setupFluidRendering(BzBlocks.SUGAR_WATER_FLUID, BzBlocks.SUGAR_WATER_FLUID_FLOWING, FLUID_STILL, FLUID_FLOWING);
        BzBlocks.registerRenderLayers();

        EntityRendererRegistry.INSTANCE.register(BzEntities.HONEY_SLIME, (dispatcher, context) -> new HoneySlimeRendering(dispatcher));
        SkyPropertiesAccessor.thebumblezone_getBY_IDENTIFIER().put(new Identifier(Bumblezone.MODID, "sky_property"), new BzSkyProperty());



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