package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.telepathicgrunt.the_bumblezone.client.rendering.variantbee.VariantBeeRenderStateInterface;
import com.telepathicgrunt.the_bumblezone.loot.LootParamsBzVisitedLootInterface;
import net.minecraft.client.renderer.entity.state.BeeRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.LootParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashSet;
import java.util.Set;

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