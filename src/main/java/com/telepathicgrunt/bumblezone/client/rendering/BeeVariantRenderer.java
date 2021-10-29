package com.telepathicgrunt.bumblezone.client.rendering;

import com.google.common.collect.ImmutableList;
import com.telepathicgrunt.bumblezone.Bumblezone;
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

    private static final List<String> VARIANTS = ImmutableList.of("transbee", "asexualbee");
    public static EntityRendererProvider<Bee> OLD_BEE_RENDER_FACTORY = null;
    private EntityRenderer<Bee> OLD_BEE_RENDER = null;

    public BeeVariantRenderer(EntityRendererProvider.Context context) {
        super(context);
        if(OLD_BEE_RENDER_FACTORY != null) {
            OLD_BEE_RENDER = OLD_BEE_RENDER_FACTORY.create(context);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(Bee entity) {
        UUID id = entity.getUUID();
        long most = id.getMostSignificantBits();

        final double lgbtChance = Bumblezone.BZ_CONFIG.BZClientConfig.lgbtBeeRate;
        boolean lgbt = (new Random(most + 1001)).nextDouble() < lgbtChance; // + 1001 so it doesn't align exactly with quark.

        if(entity.hasCustomName() || lgbt) {
            String custName = entity.hasCustomName() ? entity.getCustomName().getString().trim() : "";
            String name = custName.toLowerCase(Locale.ROOT);

            if(!VARIANTS.contains(name)) {
                if(lgbt) name = VARIANTS.get(Math.abs((int) (most % (VARIANTS.size()))));
            }

            if(VARIANTS.contains(name)) {
                String type = "";
                boolean angery = entity.hasStung();
                boolean nectar = entity.hasNectar();

                if(angery)
                    type = nectar ? "_angry_nectar" : "_angry";
                else if(nectar)
                    type = "_nectar";

                String path = String.format("textures/entity/bee_variants/%s/%s%s.png", name, name, type);
                return new ResourceLocation(Bumblezone.MODID, path);
            }
        }

        if(OLD_BEE_RENDER != null) {
            return OLD_BEE_RENDER.getTextureLocation(entity);
        }

        return super.getTextureLocation(entity);
    }
}