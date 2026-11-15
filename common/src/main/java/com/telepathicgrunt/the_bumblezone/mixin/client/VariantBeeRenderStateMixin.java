package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.telepathicgrunt.the_bumblezone.client.rendering.variantbee.VariantBeeRenderStateInterface;
import net.minecraft.client.renderer.entity.state.BeeRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BeeRenderState.class)
public class VariantBeeRenderStateMixin implements VariantBeeRenderStateInterface {

    @Unique
    String bumblezone_variant = "";

    @Override
    public String theBumblezone$getVariant() {
        return bumblezone_variant;
    }

    @Override
    public void theBumblezone$setVariant(String variant) {
        bumblezone_variant = variant;
    }
}