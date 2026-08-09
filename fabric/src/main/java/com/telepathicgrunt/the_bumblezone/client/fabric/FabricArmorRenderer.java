package com.telepathicgrunt.the_bumblezone.client.fabric;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.the_bumblezone.client.armor.ArmorModelProvider;
import com.telepathicgrunt.the_bumblezone.client.rendering.armor.BeeArmorModel;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterArmorProviderEvent;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.TransformCopyingModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;

public class FabricArmorRenderer implements ArmorRenderer {

    private final ArmorModelProvider provider;

    public FabricArmorRenderer(ArmorModelProvider provider) {
        this.provider = provider;
    }

    @Override
    public void render(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, ItemStack itemStack, HumanoidRenderState humanoidRenderState, EquipmentSlot slot, int light, HumanoidModel<HumanoidRenderState> contextModel) {
        if (provider instanceof ArmorModelProvider armorModelProvider) {
            Model model = provider.getFinalModel(itemStack, slot, contextModel);
            if (model == null) {
                return;
            }

            Identifier armorTexture = armorModelProvider.getArmorTexture(itemStack);

            if (itemStack.has(DataComponents.DYED_COLOR) || itemStack.is(BzItems.FLOWER_HEADWEAR.get())) {
                int dyeColor = DyedItemColor.getOrDefault(itemStack, itemStack.is(BzItems.FLOWER_HEADWEAR.get()) ? -1682375 : 0);
                submitNodeCollector.order(1).submitModel(
                        TransformCopyingModel.create(contextModel, model, false),
                        Pair.of(humanoidRenderState, humanoidRenderState),
                        poseStack,
                        RenderTypes.armorCutoutNoCull(armorTexture),
                        light,
                        OverlayTexture.NO_OVERLAY,
                        dyeColor,
                        null,
                        0,
                        null);
            }
            else {
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

            if (model instanceof BeeArmorModel beeArmorModel) {
                beeArmorModel.setupAnimByStack(humanoidRenderState, itemStack);
            }
        }
    }

    public static void setupArmor() {
        BzRegisterArmorProviderEvent.EVENT.invoke(new BzRegisterArmorProviderEvent((item, provider) -> {
            ArmorModelProvider.register(item, provider);
            ArmorRenderer.register(new FabricArmorRenderer(provider), item);
        }));
    }
}
