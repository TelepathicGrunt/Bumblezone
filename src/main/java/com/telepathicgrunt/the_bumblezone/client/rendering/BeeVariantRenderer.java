package com.telepathicgrunt.the_bumblezone.client.rendering;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.natamus.realisticbees.config.ConfigHandler;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzClientConfigs;
import com.telepathicgrunt.the_bumblezone.mixin.client.EntityRendererAccessor;
import com.telepathicgrunt.the_bumblezone.mixin.client.MobRendererAccessor;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.BeeRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Bee;

import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class BeeVariantRenderer extends BeeRenderer {
    // Credit to Quark for this code!
    // https://github.com/VazkiiMods/Quark/blob/master/src/main/java/vazkii/quark/content/client/render/variant/VariantBeeRenderer.java

    private static final List<String> LGBT_VARIANTS = ImmutableList.of("transbee", "asexualbee", "agenderbee", "aroacebee", "aromanticbee", "bisexualbee");
    private static final String UKRAINE_VARIANT = "ukrainebee";
    public static EntityRendererProvider<? super Bee> OLD_BEE_RENDER_FACTORY = null;
    private EntityRenderer<? super Bee> OLD_BEE_RENDER = null;

    public BeeVariantRenderer(EntityRendererProvider.Context context) {
        super(context);
        if(OLD_BEE_RENDER_FACTORY != null) {
            OLD_BEE_RENDER = OLD_BEE_RENDER_FACTORY.create(context);
        }
        shadowRadius = ((EntityRendererAccessor)OLD_BEE_RENDER).getShadowRadius();
        shadowStrength = ((EntityRendererAccessor)OLD_BEE_RENDER).getShadowStrength();
    }

    @Override
    public ResourceLocation getTextureLocation(Bee entity) {
        UUID id = entity.getUUID();
        long most = id.getMostSignificantBits();

        final double lgbtChance = BzClientConfigs.lgbtBeeRate.get();
        boolean lgbt = (new Random(most + 1001)).nextDouble() < lgbtChance; // + 1001 so it doesn't align exactly with quark.

        final double ukraineChance = BzClientConfigs.ukraineBeeRate.get();
        boolean ukraine = (new Random(most + 1001)).nextDouble() < ukraineChance; // + 1001 so it doesn't align exactly with quark.

        if(entity.hasCustomName() || lgbt || ukraine) {
            String custName = entity.hasCustomName() ? entity.getCustomName().getString().trim() : "";
            String name = custName.toLowerCase(Locale.ROOT);

            if(!LGBT_VARIANTS.contains(name) && !UKRAINE_VARIANT.equals(name)) {
                if(lgbt) name = LGBT_VARIANTS.get(Math.abs((int) (most % (LGBT_VARIANTS.size()))));
                if(ukraine) name = UKRAINE_VARIANT;
            }

            if(LGBT_VARIANTS.contains(name) || UKRAINE_VARIANT.equals(name)) {
                String type = "";
                boolean angery = entity.hasStung();
                boolean nectar = entity.hasNectar();

                if(angery)
                    type = nectar ? "_angry_nectar" : "_angry";
                else if(nectar)
                    type = "_nectar";

                String path = String.format("textures/entity/bee_variants/%s/bee%s.png", name, type);
                return new ResourceLocation(Bumblezone.MODID, path);
            }
        }

        if(OLD_BEE_RENDER != null) {
            return OLD_BEE_RENDER.getTextureLocation(entity);
        }

        return super.getTextureLocation(entity);
    }

    @Override
    protected boolean shouldShowName(Bee entity) {
        return ((MobRendererAccessor)OLD_BEE_RENDER).callShouldShowName(entity);
    }

    @Override
    public boolean shouldRender(Bee livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return OLD_BEE_RENDER.shouldRender(livingEntity, camera, camX, camY, camZ);
    }

    @Override
    public void render(Bee entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        OLD_BEE_RENDER.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }
}