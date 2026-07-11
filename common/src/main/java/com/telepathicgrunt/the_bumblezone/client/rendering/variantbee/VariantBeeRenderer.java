package com.telepathicgrunt.the_bumblezone.client.rendering.variantbee;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.VariantBeeEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

//TODO baby bee support (will need to extend AgeableMobRenderer to do this. All baby bees will also need their own textures. Pain)
//FIXME: extending BeeRenderer is no longer possible due to the renderstate needing to specifically be the BeeRenderState.
// we need access to the variant for textures, which is exclusive to our custom entity.
// in theory, you could use the RegisterRenderStateModifiersEvent to attach the variant to the bee state, but I will leave that up to you.

//If I had any say in this, I would recommend making the variant stuff a special layer texture that you attach to bees using EntityRenderersEvent.AddLayers.
//You could then trim the textures needed in half as you would only need a normal and pollinated one for both the baby and adult bee. (4 per variant, 2 if you dont care about the pollination overlay.It would just be the butt part of the bee)
//this route *could* also let you completely drop the variant entity altogether. Just attach the layer through the event, add the variant string via an entity attachment (you will also need to use the RegisterRenderStateModifiersEvent to actually get that info from the renderer), and change the layer texture based on said string. Tada!
//this is of course for neo, I dont know how something like this is done in fabric land (if at all)
public class VariantBeeRenderer extends MobRenderer<VariantBeeEntity, VariantBeeRenderState, VariantBeeModel> {
    private static final Identifier VANILLA_BEE_TEXTURE = Identifier.fromNamespaceAndPath("minecraft", "textures/entity/bee/bee.png");

    public VariantBeeRenderer(EntityRendererProvider.Context context) {
        super(context, new VariantBeeModel(context.bakeLayer(VariantBeeModel.LAYER_LOCATION)), 0.4f);
    }

    @Override
    public VariantBeeRenderState createRenderState() {
        return new VariantBeeRenderState();
    }

    @Override
    public void extractRenderState(VariantBeeEntity entity, VariantBeeRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.rollAmount = entity.getRollAmount(partialTicks);
        state.hasStinger = !entity.hasStung();
        state.isOnGround = entity.onGround() && entity.getDeltaMovement().lengthSqr() < 1.0E-7;
        state.isAngry = entity.isAngry();
        state.hasNectar = entity.hasNectar();
        state.variant = entity.getVariant();
    }

    @Override
    public Identifier getTextureLocation(VariantBeeRenderState state) {
        String variant = state.variant;

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

