package com.telepathicgrunt.the_bumblezone.client.rendering.variantbee;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzClientConfigs;
import com.telepathicgrunt.the_bumblezone.entities.mobs.VariantBeeEntity;
import net.minecraft.client.model.animal.bee.AdultBeeModel;
import net.minecraft.client.model.animal.bee.BabyBeeModel;
import net.minecraft.client.model.animal.bee.BeeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.BeeRenderState;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public class VariantBeeRenderer extends AgeableMobRenderer<VariantBeeEntity, BeeRenderState, BeeModel> {
    private static final Identifier VANILLA_BEE_TEXTURE = Identifier.fromNamespaceAndPath("minecraft", "textures/entity/bee/bee.png");

    public VariantBeeRenderer(EntityRendererProvider.Context context) {
        super(context,
                BzClientConfigs.useBackupModelForVariantBee ?
                        new VariantBeeModel(context.bakeLayer(VariantBeeModel.LAYER_LOCATION)) :
                        new AdultBeeModel(context.bakeLayer(ModelLayers.BEE)),
                BzClientConfigs.useBackupModelForVariantBee ?
                        new VariantBabyBeeModel(context.bakeLayer(VariantBeeModel.LAYER_LOCATION)) :
                        new BabyBeeModel(context.bakeLayer(ModelLayers.BEE_BABY)),
                0.4f);
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

    @NonNull
    @Override
    public Identifier getTextureLocation(BeeRenderState state) {
        String variant = ((VariantBeeRenderStateInterface)state).theBumblezone$getVariant();

        if(variant != null && !variant.isEmpty()) {
            String type = "";
            String isBaby = "";
            boolean angry = state.isAngry;
            boolean nectar = state.hasNectar;

            if(angry) {
                type = nectar ? "_angry_nectar" : "_angry";
            }
            else if(nectar) {
                type = "_nectar";
            }

            if (state.isBaby) {
                isBaby = "_baby";
            }

            String path = String.format("textures/entity/bee_variants/%s/bee%s%s.png", variant, type, isBaby);
            return Identifier.fromNamespaceAndPath(Bumblezone.MODID, path);
        }

        return VANILLA_BEE_TEXTURE;
    }
}

