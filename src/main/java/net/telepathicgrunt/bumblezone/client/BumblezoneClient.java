package net.telepathicgrunt.bumblezone.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.SkyProperties;
import net.minecraft.util.Identifier;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import net.telepathicgrunt.bumblezone.client.rendering.FluidRender;
import net.telepathicgrunt.bumblezone.client.rendering.HoneySlimeRendering;
import net.telepathicgrunt.bumblezone.dimension.BzDimension;
import net.telepathicgrunt.bumblezone.dimension.BzSkyProperty;
import net.telepathicgrunt.bumblezone.entities.BzEntities;
import net.telepathicgrunt.bumblezone.mixin.SkyPropertiesAccessor;

@Environment(EnvType.CLIENT)
public class BumblezoneClient implements ClientModInitializer {
    public static final Identifier FLUID_STILL = new Identifier(Bumblezone.MODID + ":block/sugar_water_still");
    public static final Identifier FLUID_FLOWING = new Identifier(Bumblezone.MODID + ":block/sugar_water_flow");

    @Override
    public void onInitializeClient() {
        FluidRender.setupFluidRendering(BzBlocks.SUGAR_WATER_FLUID, BzBlocks.SUGAR_WATER_FLUID_FLOWING, FLUID_STILL, FLUID_FLOWING);
        BzBlocks.registerRenderLayers();

        EntityRendererRegistry.INSTANCE.register(BzEntities.HONEY_SLIME,
                (dispatcher, context) -> new HoneySlimeRendering(dispatcher));

        SkyProperties skyProperty = new BzSkyProperty();
        ((SkyPropertiesAccessor) skyProperty).getBY_DIMENSION_TYPE().put(BzDimension.BZ_DIMENSION_KEY, skyProperty);
    }
}