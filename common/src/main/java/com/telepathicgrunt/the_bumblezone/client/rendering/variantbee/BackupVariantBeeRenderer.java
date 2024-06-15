package com.telepathicgrunt.the_bumblezone.client.rendering.variantbee;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.VariantBeeEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BackupVariantBeeRenderer extends MobRenderer<VariantBeeEntity, BackupVariantBeeModel<VariantBeeEntity>> {
    private static final ResourceLocation VANILLA_BEE_TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/bee/bee.png");

    public BackupVariantBeeRenderer(EntityRendererProvider.Context context) {
        super(context, new BackupVariantBeeModel<>(context.bakeLayer(BackupVariantBeeModel.LAYER_LOCATION)), 0.4f);
    }

    @Override
    public ResourceLocation getTextureLocation(VariantBeeEntity entity) {
        String variant = entity.getVariant();

        if(variant != null && !variant.isEmpty()) {
            String type = "";
            boolean angry = entity.isAngry();
            boolean nectar = entity.hasNectar();

            if(angry)
                type = nectar ? "_angry_nectar" : "_angry";
            else if(nectar)
                type = "_nectar";

            String path = String.format("textures/entity/bee_variants/%s/bee%s.png", variant, type);
            return ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, path);
        }

        return VANILLA_BEE_TEXTURE;
    }
}

