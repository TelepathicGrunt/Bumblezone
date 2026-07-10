package com.telepathicgrunt.the_bumblezone.client.fabric;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.telepathicgrunt.the_bumblezone.client.armor.ArmorModelProvider;
import com.telepathicgrunt.the_bumblezone.client.armor.BeeArmorModelProvider;
import com.telepathicgrunt.the_bumblezone.client.rendering.armor.BeeArmorModel;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterArmorProviderEvent;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class FabricArmorRenderer implements ArmorRenderer {

    private final ArmorModelProvider provider;

    public FabricArmorRenderer(ArmorModelProvider provider) {
        this.provider = provider;
    }

    @Override
    public void render(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, ItemStack stack, HumanoidRenderState humanoidRenderState, EquipmentSlot slot, int light, HumanoidModel<HumanoidRenderState> contextModel) {
        if (provider instanceof BeeArmorModelProvider beeArmorModelProvider) {
            Model model = provider.getFinalModel(stack, contextModel);
            Identifier armorTexture = beeArmorModelProvider.getArmorTexture(stack);
            ArmorRenderer.submitTransformCopyingModel(
                    contextModel,
                    humanoidRenderState,
                    model,
                    humanoidRenderState,
                    false,
                    submitNodeCollector,
                    poseStack,
                    RenderTypes.armorCutoutNoCull(armorTexture),
                    light,
                    OverlayTexture.NO_OVERLAY,
                    0,
                    null);
        }
    }

    public static void setupArmor() {
        BzRegisterArmorProviderEvent.EVENT.invoke(new BzRegisterArmorProviderEvent((item, provider) -> {
            ArmorModelProvider.register(item, provider);
            ArmorRenderer.register(new FabricArmorRenderer(provider), item);
        }));
    }
}
