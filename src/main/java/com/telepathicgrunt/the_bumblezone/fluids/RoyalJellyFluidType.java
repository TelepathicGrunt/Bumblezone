package com.telepathicgrunt.the_bumblezone.fluids;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Vector3f;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.rendering.FluidClientOverlay;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;

import java.util.function.Consumer;

public class RoyalJellyFluidType extends FluidType {
    public static final ResourceLocation ROYAL_JELLY_FLUID_STILL_TEXTURE = new ResourceLocation(Bumblezone.MODID, "block/royal_jelly_fluid_still");
    public static final ResourceLocation ROYAL_JELLY_FLUID_FLOWING_TEXTURE = new ResourceLocation(Bumblezone.MODID, "block/royal_jelly_fluid_flow");

    public RoyalJellyFluidType() {
        super(Properties.create()
                .supportsBoating(true)
                .canHydrate(false)
                .canDrown(true)
                .canExtinguish(true)
                .canPushEntity(true)
                .canSwim(true)
                .pathType(BlockPathTypes.WATER)
                .adjacentPathType(BlockPathTypes.WATER_BORDER)
                .canConvertToSource(false)
                .fallDistanceModifier(0.15f)
                .motionScale(0.0115)
                .rarity(Rarity.UNCOMMON)
                .viscosity(5000)
                .density(2000)
                .temperature(300)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
        );
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {

            @Override
            public ResourceLocation getStillTexture()
            {
                return ROYAL_JELLY_FLUID_STILL_TEXTURE;
            }

            @Override
            public ResourceLocation getFlowingTexture()
            {
                return ROYAL_JELLY_FLUID_FLOWING_TEXTURE;
            }

            @Override
            public ResourceLocation getOverlayTexture()
            {
                return ROYAL_JELLY_FLUID_FLOWING_TEXTURE;
            }

            @Override
            public ResourceLocation getRenderOverlayTexture(Minecraft mc)
            {
                return null;
            }

            @Override
            public int getTintColor() {
                return 0xFFFFFFFF;
            }

            @Override
            public Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                // Scale the brightness of fog but make sure it is never darker than the dimension's min brightness.
                BlockPos blockpos = new BlockPos(camera.getEntity().getX(), camera.getEntity().getEyeY(), camera.getEntity().getZ());
                float brightnessAtEyes = LightTexture.getBrightness(camera.getEntity().level.dimensionType(), camera.getEntity().level.getMaxLocalRawBrightness(blockpos));
                float brightness = (float) Math.max(
                        Math.pow(FluidClientOverlay.getDimensionBrightnessAtEyes(camera.getEntity()), 2D),
                        brightnessAtEyes
                );
                float fogRed = 0.5F * brightness;
                float fogGreen = 0.0F;
                float fogBlue = 0.55F * brightness;
                return new Vector3f(fogRed, fogGreen, fogBlue);
            }

            @Override
            public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
                RenderSystem.setShaderFogStart(0.35f);
                RenderSystem.setShaderFogEnd(4);
            }
        });
    }
}
