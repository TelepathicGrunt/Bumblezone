package com.telepathicgrunt.the_bumblezone.client.rendering.fluids;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.fluids.HoneyFluidBlock;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import net.minecraft.client.renderer.Lightmap;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.MoonPhase;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class FluidClientOverlay {
    private static final Identifier HONEY_TEXTURE_UNDERWATER = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/misc/honey_fluid_underwater.png");
    private static final Identifier ROYAL_JELLY_TEXTURE_UNDERWATER = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/misc/royal_jelly_fluid_underwater.png");

    public static boolean renderHoneyOverlay(Player clientPlayerEntity, PoseStack poseStack, final MultiBufferSource bufferSource) {
        BlockState state = clientPlayerEntity.level().getBlockState(BlockPos.containing(clientPlayerEntity.getEyePosition(1)));
        if (state.is(BzFluids.HONEY_FLUID_BLOCK.get()) || state.is(BzFluids.ROYAL_JELLY_FLUID_BLOCK.get())) {
            if (state.hasProperty(HoneyFluidBlock.BOTTOM_LEVEL)) {
                double yOffset = Math.abs(clientPlayerEntity.getEyePosition(1).y() - Math.floor(clientPlayerEntity.getEyePosition(1).y()));
                if (state.getValue(HoneyFluidBlock.BOTTOM_LEVEL) / 8D > yOffset + 0.1) {
                    return false;
                }
            }

            // Scale the brightness of fog but make sure it is never darker than the dimension's min brightness.
            float brightness = (float) Math.max(
                    Math.pow(FluidClientOverlay.getDimensionBrightnessAtEyes(clientPlayerEntity), 2D),
                    clientPlayerEntity.level().dimensionType().ambientLight()
            );
            int color = ARGB.colorFromFloat(0.9F, brightness, brightness, brightness);

            float modifiedYaw = -clientPlayerEntity.getYRot() / (64.0F * 8F);
            float modifiedPitch = clientPlayerEntity.getXRot() / (64.0F * 8F);
            Matrix4f pose = poseStack.last().pose();
            VertexConsumer builder = bufferSource.getBuffer(RenderTypes.blockScreenEffect(state.is(BzFluids.HONEY_FLUID_BLOCK.get()) ? HONEY_TEXTURE_UNDERWATER : ROYAL_JELLY_TEXTURE_UNDERWATER));
            builder.addVertex(pose, -1.0F, -1.0F, -0.5F).setUv(1.0F + modifiedYaw, 1.0F + modifiedPitch).setColor(color);
            builder.addVertex(pose, 1.0F, -1.0F, -0.5F).setUv(0.0F + modifiedYaw, 2.0F + modifiedPitch).setColor(color);
            builder.addVertex(pose, 1.0F, 1.0F, -0.5F).setUv(1.0F + modifiedYaw, 1.0F + modifiedPitch).setColor(color);
            builder.addVertex(pose, -1.0F, 1.0F, -0.5F).setUv(2.0F + modifiedYaw, 0.0F + modifiedPitch).setColor(color);
            return true;
        }

        return false;
    }

    public static float getDimensionBrightnessAtEyes(Entity entity) {
        Level level = entity.level();
        BlockPos eyePos = BlockPos.containing(entity.getEyePosition(1));
        return Lightmap.getBrightness(level.dimensionType(), level.getMaxLocalRawBrightness(eyePos));
    }
}
