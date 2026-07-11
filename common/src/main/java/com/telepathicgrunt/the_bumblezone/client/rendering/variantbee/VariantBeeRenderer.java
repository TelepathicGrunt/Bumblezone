package com.telepathicgrunt.the_bumblezone.client.rendering.variantbee;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.VariantBeeEntity;
import com.telepathicgrunt.the_bumblezone.mixin.client.VariantBeeRenderStateMixin;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.BeeRenderState;
import net.minecraft.resources.Identifier;

public class VariantBeeRenderer extends MobRenderer<VariantBeeEntity, BeeRenderState, VariantBeeModel> {
    private static final Identifier VANILLA_BEE_TEXTURE = Identifier.fromNamespaceAndPath("minecraft", "textures/entity/bee/bee.png");

    public VariantBeeRenderer(EntityRendererProvider.Context context) {
        super(context, new VariantBeeModel(context.bakeLayer(VariantBeeModel.LAYER_LOCATION)), 0.4f);
    }

    @Override
    public BeeRenderState createRenderState() {
        return new BeeRenderState();
    }

    @Override
    public void extractRenderState(VariantBeeEntity entity, BeeRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.rollAmount = entity.getRollAmount(partialTicks);
        state.hasStinger = !entity.hasStung();
        state.isOnGround = entity.onGround() && entity.getDeltaMovement().lengthSqr() < 1.0E-7;
        state.isAngry = entity.isAngry();
        state.hasNectar = entity.hasNectar();
        ((VariantBeeRenderStateInterface)state).theBumblezone$setVariant(entity.getVariant());
    }

    @Override
    public Identifier getTextureLocation(BeeRenderState state) {
        String variant = ((VariantBeeRenderStateInterface)state).theBumblezone$getVariant();

        if(variant != null && !variant.isEmpty()) {
            String type = "";
            boolean angry = state.isAngry;
            boolean nectar = state.hasNectar;

            if(angry)
                type = nectar ? "_angry_nectar" : "_angry";
            else if(nectar)
                type = "_nectar";

            String path = String.format("textures/entity/bee_variants/%s/bee%s.png", variant, type);
            return Identifier.fromNamespaceAndPath(Bumblezone.MODID, path);
        }

        return VANILLA_BEE_TEXTURE;
    }
}

