package com.telepathicgrunt.the_bumblezone.client.rendering.pileofpollen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.events.client.BzBlockRenderedOnScreenEvent;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Matrix4f;
import org.joml.Vector3f;


// CLIENT-SIDED
public class PileOfPollenRenderer {

    private static final Identifier TEXTURE_POLLEN = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/block/pile_of_pollen/pile_of_pollen.png");
    private static final Identifier TEXTURE_POLLEN_SUSPICIOUS = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/block/pile_of_pollen/pile_of_pollen_suspicious_1.png");

    public static boolean pileOfPollenOverlay(BzBlockRenderedOnScreenEvent event) {
        BlockState blockState = event.state();
        if (event.type().equals(BzBlockRenderedOnScreenEvent.Type.BLOCK) && blockState.is(BzTags.POLLEN_BLOCKS)) {
            Player playerEntity = event.player();
            PoseStack matrixStack = event.stack();
            boolean isInPollen = false;
            for(int x = -1; x <= 1; x++) {
                for(int z = -1; z <= 1; z++) {
                    for(int y = -1; y <= 1; y++) {
                        // Squared to make it positive always for the addition
                        // Skips all non corner spots
                        if((x*x) + (y*y) + (z*z) <= 2) continue;

                        double eyePosX = playerEntity.getX() + x * playerEntity.getBbWidth() * 0.155F;
                        double eyePosY = playerEntity.getEyeY() + y * 0.12F;
                        double eyePosZ = playerEntity.getZ() + z * playerEntity.getBbWidth() * 0.155F;
                        Vec3 eyePosition = new Vec3(eyePosX, eyePosY, eyePosZ);
                        BlockPos eyeBlockPos = BlockPos.containing(eyePosition);
                        BlockState eyeBlock = playerEntity.level().getBlockState(eyeBlockPos);
                        VoxelShape blockBounds = eyeBlock.getShape(playerEntity.level(), eyeBlockPos);
                        if (!blockBounds.isEmpty()) {
                            Vec3 eyePos = eyePosition.subtract(Vec3.atLowerCornerOf(eyeBlockPos));
                            if (blockBounds.bounds().contains(eyePos)) {
                                isInPollen = true;
                                x = 2;
                                z = 2;
                                break;
                            }
                        }
                    }
                }
            }

            if(!isInPollen) {
                return true;
            }

            float opacity = 0.975f;
            float brightness = 0.3f;
            float redStrength = 1f;
            float greenStrength = 0.9f;
            float blueStrength = 0.8f;

            TextureAtlasSprite textureAtlasSprite = Minecraft.getInstance().getModelManager().getBlockStateModelSet().getParticleMaterial(blockState).sprite();

            float u0 = textureAtlasSprite.getU0();
            float u1 = textureAtlasSprite.getU1();
            float v0 = textureAtlasSprite.getV0();
            float v1 = textureAtlasSprite.getV1();

            Matrix4f pose = matrixStack.last().pose();

            int color = ARGB.colorFromFloat(opacity, brightness * redStrength, brightness * greenStrength, brightness * blueStrength);
            VertexConsumer builder = event.bufferSource().getBuffer(RenderTypes.blockScreenEffect(textureAtlasSprite.atlasLocation()));

            builder.addVertex(pose, -1.0F, -1.0F, -0.5F).setUv(u1, v1).setColor(color);
            builder.addVertex(pose, 1.0F, -1.0F, -0.5F).setUv(u0, v1).setColor(color);
            builder.addVertex(pose, 1.0F, 1.0F, -0.5F).setUv(u0, v0).setColor(color);
            builder.addVertex(pose, -1.0F, 1.0F, -0.5F).setUv(u1, v0).setColor(color);

            return true;
        }
        return false;
    }
}
