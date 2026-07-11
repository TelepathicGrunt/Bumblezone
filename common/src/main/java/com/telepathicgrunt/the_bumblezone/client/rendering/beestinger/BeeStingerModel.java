package com.telepathicgrunt.the_bumblezone.client.rendering.beestinger;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

// Made with Blockbench 4.1.3
public class BeeStingerModel extends Model<ArrowRenderState> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(Bumblezone.MODID, "bee_stinger"), "main");

    public BeeStingerModel(ModelPart root) {
        super(root, RenderTypes::entitySolid);
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("main",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-0.5F, -0.5F, -3.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 3.1416F, 0.0F));

        return LayerDefinition.create(meshdefinition, 14, 7);
    }
}