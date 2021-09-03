package com.telepathicgrunt.the_bumblezone.client.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;


// CLIENT-SIDED
public class PileOfPollenRenderer {

    private static final ResourceLocation TEXTURE_POLLEN = new ResourceLocation(Bumblezone.MODID + ":textures/block/pile_of_pollen.png");

    public static void pileOfPollenOverlay(RenderBlockOverlayEvent event)
    {
        BlockPos blockPos = event.getBlockPos();
        PlayerEntity playerEntity = event.getPlayer();
        BlockState state = playerEntity.level.getBlockState(blockPos);
        if (state.is(BzBlocks.PILE_OF_POLLEN.get()))
        {
            if(!state.getShape(playerEntity.level, blockPos).bounds().contains(playerEntity.getEyePosition(1).subtract(Vector3d.atLowerCornerOf(blockPos)))) {
                event.setCanceled(true);
            }
            
            Minecraft minecraftIn = Minecraft.getInstance();
            minecraftIn.getTextureManager().bind(TEXTURE_POLLEN);
            BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            float f7 = -event.getPlayer().yRot / 64.0F;
            float f8 = event.getPlayer().xRot / 64.0F;
            Matrix4f matrix4f = event.getMatrixStack().last().pose();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR_TEX);
            float opacity = 1f;
            float brightness = 0.3f;
            float redStrength = 1f;
            float greenStrength = 0.9f;
            float blueStrength = 0.8f;
            bufferbuilder.vertex(matrix4f, -1.0F, -1.0F, -0.5F).color(brightness * redStrength, brightness * greenStrength, brightness * blueStrength, opacity).uv(4.0F + f7, 4.0F + f8).endVertex();
            bufferbuilder.vertex(matrix4f, 1.0F, -1.0F, -0.5F).color(brightness * redStrength, brightness * greenStrength, brightness * blueStrength, opacity).uv(0.0F + f7, 4.0F + f8).endVertex();
            bufferbuilder.vertex(matrix4f, 1.0F, 1.0F, -0.5F).color(brightness * redStrength, brightness * greenStrength, brightness * blueStrength, opacity).uv(0.0F + f7, 0.0F + f8).endVertex();
            bufferbuilder.vertex(matrix4f, -1.0F, 1.0F, -0.5F).color(brightness * redStrength, brightness * greenStrength, brightness * blueStrength, opacity).uv(4.0F + f7, 0.0F + f8).endVertex();
            bufferbuilder.end();
            WorldVertexBufferUploader.end(bufferbuilder);
            RenderSystem.disableBlend();
            event.setCanceled(true);
        }
    }
}
