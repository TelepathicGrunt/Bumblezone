package com.telepathicgrunt.the_bumblezone.client.rendering.essence;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import com.telepathicgrunt.the_bumblezone.items.essence.KnowingEssence;
import com.telepathicgrunt.the_bumblezone.mixin.client.LevelRendererAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EnderChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector4d;

import java.util.Map;

public class KnowingEssenceLootBlockOutlining {
    private static final double DRAW_RADIUS = 0.45D;
    private static final double MIN_CORNER = 0.5D - DRAW_RADIUS;
    private static final double MAX_CORNER = 0.5D + DRAW_RADIUS;
    private static final Vector4d VECTOR_4D_MIN = new Vector4d(MIN_CORNER, MIN_CORNER, MIN_CORNER, 1.0D);
    private static final Vector4d VECTOR_4D_MAX = new Vector4d(MAX_CORNER, MAX_CORNER, MAX_CORNER, 1.0D);

    public static void outlineLootBlocks(PoseStack poseStack, Camera camera, LevelRenderer levelRenderer) {
        Player player = GeneralUtilsClient.getClientPlayer();
        if (KnowingEssence.IsKnowingEssenceActive(player)) {
            Level level = player.level();

            Vec3 cameraPos = camera.getPosition();
            BlockPos worldSpot = BlockPos.containing(cameraPos);

            poseStack.pushPose();

            Tesselator tesselator = Tesselator.getInstance();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            RenderSystem.disableDepthTest();
            BufferBuilder bufferbuilder = tesselator.getBuilder();
            bufferbuilder.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);

            int chunkRadius = 4;
            ChunkPos centerChunkPos = new ChunkPos(worldSpot);
            for (int x = -chunkRadius; x <= chunkRadius; x++) {
                for (int z = -chunkRadius; z <= chunkRadius; z++) {
                    LevelChunk chunk = level.getChunk(x + centerChunkPos.x, z + centerChunkPos.z);
                    for (Map.Entry<BlockPos, BlockEntity> blockEntityEntry : chunk.getBlockEntities().entrySet()) {
                        BlockEntity blockEntity = blockEntityEntry.getValue();
                        BlockState blockState = blockEntity.getBlockState();
                        Block block = blockState.getBlock();
                        if ((blockState.is(BzTags.KNOWING_BLOCK_ENTITY_FORCED_HIGHLIGHTING) ||
                            blockEntity instanceof RandomizableContainerBlockEntity ||
                            blockEntity instanceof BrushableBlockEntity ||
                            blockEntity instanceof EnderChestBlockEntity ||
                            block instanceof EnderChestBlock)
                            && !blockState.is(BzTags.KNOWING_BLOCK_ENTITY_PREVENT_HIGHLIGHTING))
                        {
                             BlockPos lootBlockPos = blockEntityEntry.getKey();

                            if (!((LevelRendererAccessor)levelRenderer).getCullingFrustum().isVisible(new AABB(
                                    lootBlockPos.getX() + MIN_CORNER,
                                    lootBlockPos.getY() + MIN_CORNER,
                                    lootBlockPos.getZ() + MIN_CORNER,
                                    lootBlockPos.getX() + MAX_CORNER,
                                    lootBlockPos.getY() + MAX_CORNER,
                                    lootBlockPos.getZ() + MAX_CORNER)))
                            {
                                continue;
                            }

                            int colorInt = block.defaultMapColor().col;
                            int red = FastColor.ARGB32.red(colorInt);
                            int green = FastColor.ARGB32.green(colorInt);
                            int blue = FastColor.ARGB32.blue(colorInt);

                            renderLineBox(
                                    bufferbuilder,
                                    poseStack.last().pose(),
                                    (float) (VECTOR_4D_MIN.x() + lootBlockPos.getX() - cameraPos.x()),
                                    (float) (VECTOR_4D_MIN.y() + lootBlockPos.getY() - cameraPos.y()),
                                    (float) (VECTOR_4D_MIN.z() + lootBlockPos.getZ() - cameraPos.z()),
                                    (float) (VECTOR_4D_MAX.x() + lootBlockPos.getX() - cameraPos.x()),
                                    (float) (VECTOR_4D_MAX.y() + lootBlockPos.getY() - cameraPos.y()),
                                    (float) (VECTOR_4D_MAX.z() + lootBlockPos.getZ() - cameraPos.z()),
                                    red,
                                    green,
                                    blue,
                                    255);
                        }
                    }
                }
            }
            tesselator.end();
            poseStack.popPose();
            RenderSystem.enableDepthTest();
            RenderType.cutout().clearRenderState();
        }
    }

    private static void renderLineBox(BufferBuilder builder, Matrix4f pose, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, int red, int green, int blue, int alpha) {
        builder.vertex(pose, minX, minY, minZ).color(red, green, blue, alpha).normal(1.0F, 0.0F, 0.0F).endVertex();
        builder.vertex(pose, maxX, minY, minZ).color(red, green, blue, alpha).normal(1.0F, 0.0F, 0.0F).endVertex();
        builder.vertex(pose, minX, minY, minZ).color(red, green, blue, alpha).normal(0.0F, 1.0F, 0.0F).endVertex();
        builder.vertex(pose, minX, maxY, minZ).color(red, green, blue, alpha).normal(0.0F, 1.0F, 0.0F).endVertex();
        builder.vertex(pose, minX, minY, minZ).color(red, green, blue, alpha).normal(0.0F, 0.0F, 1.0F).endVertex();
        builder.vertex(pose, minX, minY, maxZ).color(red, green, blue, alpha).normal(0.0F, 0.0F, 1.0F).endVertex();
        builder.vertex(pose, maxX, minY, minZ).color(red, green, blue, alpha).normal(0.0F, 1.0F, 0.0F).endVertex();
        builder.vertex(pose, maxX, maxY, minZ).color(red, green, blue, alpha).normal(0.0F, 1.0F, 0.0F).endVertex();
        builder.vertex(pose, maxX, maxY, minZ).color(red, green, blue, alpha).normal(-1.0F, 0.0F, 0.0F).endVertex();
        builder.vertex(pose, minX, maxY, minZ).color(red, green, blue, alpha).normal(-1.0F, 0.0F, 0.0F).endVertex();
        builder.vertex(pose, minX, maxY, minZ).color(red, green, blue, alpha).normal(0.0F, 0.0F, 1.0F).endVertex();
        builder.vertex(pose, minX, maxY, maxZ).color(red, green, blue, alpha).normal(0.0F, 0.0F, 1.0F).endVertex();
        builder.vertex(pose, minX, maxY, maxZ).color(red, green, blue, alpha).normal(0.0F, -1.0F, 0.0F).endVertex();
        builder.vertex(pose, minX, minY, maxZ).color(red, green, blue, alpha).normal(0.0F, -1.0F, 0.0F).endVertex();
        builder.vertex(pose, minX, minY, maxZ).color(red, green, blue, alpha).normal(1.0F, 0.0F, 0.0F).endVertex();
        builder.vertex(pose, maxX, minY, maxZ).color(red, green, blue, alpha).normal(1.0F, 0.0F, 0.0F).endVertex();
        builder.vertex(pose, maxX, minY, maxZ).color(red, green, blue, alpha).normal(0.0F, 0.0F, -1.0F).endVertex();
        builder.vertex(pose, maxX, minY, minZ).color(red, green, blue, alpha).normal(0.0F, 0.0F, -1.0F).endVertex();
        builder.vertex(pose, minX, maxY, maxZ).color(red, green, blue, alpha).normal(1.0F, 0.0F, 0.0F).endVertex();
        builder.vertex(pose, maxX, maxY, maxZ).color(red, green, blue, alpha).normal(1.0F, 0.0F, 0.0F).endVertex();
        builder.vertex(pose, maxX, minY, maxZ).color(red, green, blue, alpha).normal(0.0F, 1.0F, 0.0F).endVertex();
        builder.vertex(pose, maxX, maxY, maxZ).color(red, green, blue, alpha).normal(0.0F, 1.0F, 0.0F).endVertex();
        builder.vertex(pose, maxX, maxY, minZ).color(red, green, blue, alpha).normal(0.0F, 0.0F, 1.0F).endVertex();
        builder.vertex(pose, maxX, maxY, maxZ).color(red, green, blue, alpha).normal(0.0F, 0.0F, 1.0F).endVertex();
    }
}
