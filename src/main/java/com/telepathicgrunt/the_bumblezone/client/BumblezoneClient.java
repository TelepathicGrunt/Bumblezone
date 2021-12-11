package com.telepathicgrunt.the_bumblezone.client;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.rendering.BeeVariantRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.BeehemothModel;
import com.telepathicgrunt.the_bumblezone.client.rendering.BeehemothRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.FluidRender;
import com.telepathicgrunt.the_bumblezone.client.rendering.HoneySlimeRendering;
import com.telepathicgrunt.the_bumblezone.mixin.client.EntityRendererRegistryImplAccessor;
import com.telepathicgrunt.the_bumblezone.mixin.world.DimensionSpecialEffectsAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzParticles;
import com.telepathicgrunt.the_bumblezone.world.dimension.BzSkyProperty;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Bee;

@Environment(EnvType.CLIENT)
public class BumblezoneClient implements ClientModInitializer {
    public static final ResourceLocation SUGAR_WATER_FLUID_STILL = new ResourceLocation(Bumblezone.MODID, "block/sugar_water_still");
    public static final ResourceLocation SUGAR_WATER_FLUID_FLOWING = new ResourceLocation(Bumblezone.MODID, "block/sugar_water_flow");
    public static final ResourceLocation HONEY_FLUID_STILL = new ResourceLocation(Bumblezone.MODID, "block/honey_fluid_still");
    public static final ResourceLocation HONEY_FLUID_FLOWING = new ResourceLocation(Bumblezone.MODID, "block/honey_fluid_flow");

    @Override
    public void onInitializeClient() {
        FluidRender.setupFluidRendering(BzFluids.SUGAR_WATER_FLUID, BzFluids.SUGAR_WATER_FLUID_FLOWING, SUGAR_WATER_FLUID_STILL, SUGAR_WATER_FLUID_FLOWING, true);
        FluidRender.setupFluidRendering(BzFluids.HONEY_FLUID, BzFluids.HONEY_FLUID_FLOWING, HONEY_FLUID_STILL, HONEY_FLUID_FLOWING, false);
        registerRenderLayers();
        BzParticles.registerParticles();

        EntityRendererRegistry.register(BzEntities.POLLEN_PUFF_ENTITY, ThrownItemRenderer::new);
        EntityRendererRegistry.register(BzEntities.HONEY_SLIME, HoneySlimeRendering::new);
        EntityRendererRegistry.register(BzEntities.BEEHEMOTH, BeehemothRenderer::new);

        if(Bumblezone.BZ_CONFIG.BZClientConfig.enableLgbtBeeRenderer) {
            BeeVariantRenderer.OLD_BEE_RENDER_FACTORY = (EntityRendererProvider<Bee>)EntityRendererRegistryImplAccessor.getMap().get(EntityType.BEE);
            EntityRendererRegistry.register(EntityType.BEE, BeeVariantRenderer::new);
        }

        EntityModelLayerRegistry.registerModelLayer(BeehemothModel.LAYER_LOCATION, BeehemothModel::createBodyLayer);
        DimensionSpecialEffectsAccessor.thebumblezone_getBY_IDENTIFIER().put(new ResourceLocation(Bumblezone.MODID, "sky_property"), new BzSkyProperty());

        // Allows shield to use the blocking json file for offset
        FabricModelPredicateProviderRegistry.register(
                BzItems.HONEY_CRYSTAL_SHIELD,
                new ResourceLocation("blocking"),
                (itemStack, world, livingEntity, int1) ->
                        livingEntity != null &&
                                livingEntity.isUsingItem() &&
                                livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F
        );
    }
    
    public static void registerRenderLayers() {
        BlockRenderLayerMap.INSTANCE.putBlock(BzBlocks.STICKY_HONEY_REDSTONE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BzBlocks.STICKY_HONEY_RESIDUE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BzBlocks.HONEY_CRYSTAL, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(BzFluids.SUGAR_WATER_BLOCK, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putFluid(BzFluids.SUGAR_WATER_FLUID, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putFluid(BzFluids.SUGAR_WATER_FLUID_FLOWING, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(BzFluids.HONEY_FLUID_BLOCK, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putFluid(BzFluids.HONEY_FLUID, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putFluid(BzFluids.HONEY_FLUID_FLOWING, RenderType.translucent());
    }
}