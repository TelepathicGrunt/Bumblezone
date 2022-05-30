package com.telepathicgrunt.the_bumblezone.client;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.items.HoneyCompassItemProperty;
import com.telepathicgrunt.the_bumblezone.client.particles.HoneyParticle;
import com.telepathicgrunt.the_bumblezone.client.particles.PollenPuff;
import com.telepathicgrunt.the_bumblezone.client.rendering.BeeArmorModel;
import com.telepathicgrunt.the_bumblezone.client.rendering.BeeVariantRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.BeehemothModel;
import com.telepathicgrunt.the_bumblezone.client.rendering.BeehemothRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.FluidRender;
import com.telepathicgrunt.the_bumblezone.client.rendering.HoneySlimeRendering;
import com.telepathicgrunt.the_bumblezone.client.rendering.StingerSpearModel;
import com.telepathicgrunt.the_bumblezone.client.rendering.StingerSpearRenderer;
import com.telepathicgrunt.the_bumblezone.items.BeeCannon;
import com.telepathicgrunt.the_bumblezone.items.HoneyCompass;
import com.telepathicgrunt.the_bumblezone.mixin.client.DimensionSpecialEffectsAccessor;
import com.telepathicgrunt.the_bumblezone.mixin.client.EntityRendererRegistryImplAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzMenuTypes;
import com.telepathicgrunt.the_bumblezone.modinit.BzParticles;
import com.telepathicgrunt.the_bumblezone.packets.MobEffectClientSyncPacket;
import com.telepathicgrunt.the_bumblezone.packets.UpdateFallingBlockPacket;
import com.telepathicgrunt.the_bumblezone.screens.StrictChestScreen;
import com.telepathicgrunt.the_bumblezone.world.dimension.BzSkyProperty;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.fabricmc.fabric.impl.client.particle.ParticleFactoryRegistryImpl;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

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

        ParticleFactoryRegistryImpl.INSTANCE.register(BzParticles.POLLEN, PollenPuff.Factory::new);
        ParticleFactoryRegistryImpl.INSTANCE.register(BzParticles.HONEY_PARTICLE, HoneyParticle.Factory::new);

        EntityRendererRegistry.register(BzEntities.POLLEN_PUFF_ENTITY, ThrownItemRenderer::new);
        EntityRendererRegistry.register(BzEntities.HONEY_SLIME, HoneySlimeRendering::new);
        EntityRendererRegistry.register(BzEntities.BEEHEMOTH, BeehemothRenderer::new);
        EntityRendererRegistry.register(BzEntities.THROWN_STINGER_SPEAR_ENTITY, StingerSpearRenderer::new);

        if(Bumblezone.BZ_CONFIG.BZClientConfig.enableAltBeeSkinRenderer) {
            BeeVariantRenderer.OLD_BEE_RENDER_FACTORY = (EntityRendererProvider<Bee>)EntityRendererRegistryImplAccessor.getMap().get(EntityType.BEE);
            EntityRendererRegistry.register(EntityType.BEE, BeeVariantRenderer::new);
        }

        EntityModelLayerRegistry.registerModelLayer(BeehemothModel.LAYER_LOCATION, BeehemothModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(StingerSpearModel.LAYER_LOCATION, StingerSpearModel::createLayer);
        EntityModelLayerRegistry.registerModelLayer(BeeArmorModel.VARIANT_1_LAYER_LOCATION, BeeArmorModel::createVariant1);
        EntityModelLayerRegistry.registerModelLayer(BeeArmorModel.VARIANT_2_LAYER_LOCATION, BeeArmorModel::createVariant2);
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

        // Correct model when about to throw
        FabricModelPredicateProviderRegistry.register(
                BzItems.STINGER_SPEAR,
                new ResourceLocation("throwing"),
                (itemStack, world, livingEntity, int1) ->
                        livingEntity != null &&
                                livingEntity.isUsingItem() &&
                                livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F
        );

        // Allows honey compass to render the correct texture
        FabricModelPredicateProviderRegistry.register(
                BzItems.HONEY_COMPASS,
                new ResourceLocation("angle"),
                HoneyCompassItemProperty.getClampedItemPropertyFunction());

        // Correct model when about to fire
        FabricModelPredicateProviderRegistry.register(
                BzItems.BEE_CANNON,
                new ResourceLocation("primed"),
                (itemStack, world, livingEntity, int1) ->
                        livingEntity != null &&
                                livingEntity.isUsingItem() &&
                                livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F
        );

        // Correct model based on bees
        FabricModelPredicateProviderRegistry.register(
                BzItems.BEE_CANNON,
                new ResourceLocation("bee_count"),
                (itemStack, world, livingEntity, int1) ->
                        BeeCannon.getNumberOfBees(itemStack) / 10f
        );

        UpdateFallingBlockPacket.registerPacket();
        MobEffectClientSyncPacket.registerPacket();

        BzItems.STINGLESS_BEE_HELMET_1.registerRenderer().run();
        BzItems.STINGLESS_BEE_HELMET_2.registerRenderer().run();
        BzItems.BUMBLE_BEE_CHESTPLATE_1.registerRenderer().run();
        BzItems.BUMBLE_BEE_CHESTPLATE_2.registerRenderer().run();
        BzItems.TRANS_BUMBLE_BEE_CHESTPLATE_1.registerRenderer().run();
        BzItems.TRANS_BUMBLE_BEE_CHESTPLATE_2.registerRenderer().run();
        BzItems.HONEY_BEE_LEGGINGS_1.registerRenderer().run();
        BzItems.HONEY_BEE_LEGGINGS_2.registerRenderer().run();
        BzItems.CARPENTER_BEE_BOOTS_1.registerRenderer().run();
        BzItems.CARPENTER_BEE_BOOTS_2.registerRenderer().run();

        MenuScreens.register(BzMenuTypes.STRICT_9x1, StrictChestScreen::new);
        MenuScreens.register(BzMenuTypes.STRICT_9x2, StrictChestScreen::new);
        MenuScreens.register(BzMenuTypes.STRICT_9x3, StrictChestScreen::new);
        MenuScreens.register(BzMenuTypes.STRICT_9x4, StrictChestScreen::new);
        MenuScreens.register(BzMenuTypes.STRICT_9x5, StrictChestScreen::new);
        MenuScreens.register(BzMenuTypes.STRICT_9x6, StrictChestScreen::new);

        KeyBindingHelper.registerKeyBinding(BeehemothControls.KEY_BIND_BEEHEMOTH_UP);
        KeyBindingHelper.registerKeyBinding(BeehemothControls.KEY_BIND_BEEHEMOTH_DOWN);
    }
    
    public static void registerRenderLayers() {
        BlockRenderLayerMap.INSTANCE.putBlock(BzBlocks.STICKY_HONEY_REDSTONE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BzBlocks.STICKY_HONEY_RESIDUE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BzBlocks.HONEY_WEB, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(BzBlocks.REDSTONE_HONEY_WEB, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(BzBlocks.HONEY_CRYSTAL, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(BzFluids.SUGAR_WATER_BLOCK, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putFluid(BzFluids.SUGAR_WATER_FLUID, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putFluid(BzFluids.SUGAR_WATER_FLUID_FLOWING, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(BzFluids.HONEY_FLUID_BLOCK, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putFluid(BzFluids.HONEY_FLUID, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putFluid(BzFluids.HONEY_FLUID_FLOWING, RenderType.translucent());
    }
}