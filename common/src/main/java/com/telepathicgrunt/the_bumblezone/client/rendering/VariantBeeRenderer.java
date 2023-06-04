package com.telepathicgrunt.the_bumblezone.client.rendering;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.VariantBeeEntity;
import net.minecraft.client.renderer.entity.BeeRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Bee;

public class VariantBeeRenderer extends BeeRenderer {
    private static final ResourceLocation VANILLA_BEE_TEXTURE = new ResourceLocation("minecraft", "textures/entity/bee/bee.png");

    public VariantBeeRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(Bee entity) {
        String variant = ((VariantBeeEntity)entity).getVariant();

        if(variant != null && !variant.isEmpty()) {
            String type = "";
            boolean angery = entity.hasStung();
            boolean nectar = entity.hasNectar();

            if(angery)
                type = nectar ? "_angry_nectar" : "_angry";
            else if(nectar)
                type = "_nectar";

            String path = String.format("textures/entity/bee_variants/%s/bee%s.png", variant, type);
            return new ResourceLocation(Bumblezone.MODID, path);
        }

        return VANILLA_BEE_TEXTURE;
    }
}

