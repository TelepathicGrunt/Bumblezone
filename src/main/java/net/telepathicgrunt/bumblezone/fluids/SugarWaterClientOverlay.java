package net.telepathicgrunt.bumblezone.fluids;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.modinit.BzBlocks;

public class SugarWaterClientOverlay {
    private static final Identifier TEXTURE_UNDERWATER = new Identifier(Bumblezone.MODID + ":textures/misc/sugar_water_underwater.png");

    public static boolean sugarWaterOverlay(PlayerEntity player, BlockPos pos, MatrixStack matrixStack) {

        if (player.world.getBlockState(new BlockPos(player.getCameraPosVec(1))).getBlock() == BzBlocks.SUGAR_WATER_BLOCK) {
            MinecraftClient minecraftIn = MinecraftClient.getInstance();
            minecraftIn.getTextureManager().bindTexture(TEXTURE_UNDERWATER);
            BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
            float f = minecraftIn.player.getBrightnessAtEyes();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            float f7 = -minecraftIn.player.yaw / 64.0F;
            float f8 = minecraftIn.player.pitch / 64.0F;
            Matrix4f matrix4f = matrixStack.peek().getModel();
            bufferbuilder.begin(7, VertexFormats.POSITION_COLOR_TEXTURE);
            bufferbuilder.vertex(matrix4f, -1.0F, -1.0F, -0.5F).color(f, f, f, 0.42F).texture(4.0F + f7, 4.0F + f8).next();
            bufferbuilder.vertex(matrix4f, 1.0F, -1.0F, -0.5F).color(f, f, f, 0.42F).texture(0.0F + f7, 4.0F + f8).next();
            bufferbuilder.vertex(matrix4f, 1.0F, 1.0F, -0.5F).color(f, f, f, 0.42F).texture(0.0F + f7, 0.0F + f8).next();
            bufferbuilder.vertex(matrix4f, -1.0F, 1.0F, -0.5F).color(f, f, f, 0.42F).texture(4.0F + f7, 0.0F + f8).next();
            bufferbuilder.end();
            BufferRenderer.draw(bufferbuilder);
            RenderSystem.disableBlend();
            return true;
        }

        return false;
    }
}
