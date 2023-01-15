package com.telepathicgrunt.the_bumblezone.fluids;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;

import java.util.function.Consumer;

public class SugarWaterFluidType extends FluidType {
    public static final ResourceLocation FLUID_STILL_TEXTURE = new ResourceLocation(Bumblezone.MODID, "block/sugar_water_still");
    public static final ResourceLocation FLUID_FLOWING_TEXTURE = new ResourceLocation(Bumblezone.MODID, "block/sugar_water_flow");
    public static final ResourceLocation FLUID_OVERLAY_TEXTURE = new ResourceLocation(Bumblezone.MODID, "block/sugar_water_overlay");
    private static final ResourceLocation TEXTURE_UNDERWATER = new ResourceLocation(Bumblezone.MODID, "textures/misc/sugar_water_underwater.png");

    public SugarWaterFluidType() {
        super(Properties.create()
                .supportsBoating(true)
                .canHydrate(true)
                .canDrown(true)
                .canExtinguish(true)
                .canPushEntity(true)
                .canSwim(true)
                .pathType(BlockPathTypes.WATER)
                .adjacentPathType(BlockPathTypes.WATER_BORDER)
                .canConvertToSource(true)
                .fallDistanceModifier(0f)
                .motionScale(0.014)
                .rarity(Rarity.COMMON)
                .viscosity(1100)
                .density(1100)
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
                return FLUID_STILL_TEXTURE;
            }

            @Override
            public ResourceLocation getFlowingTexture()
            {
                return FLUID_FLOWING_TEXTURE;
            }

            @Override
            public ResourceLocation getOverlayTexture()
            {
                return FLUID_OVERLAY_TEXTURE;
            }

            @Override
            public ResourceLocation getRenderOverlayTexture(Minecraft mc)
            {
                return TEXTURE_UNDERWATER;
            }

            @Override
            public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                return BiomeColors.getAverageWaterColor(getter, pos) | 0xFF000000;
            }
        });
    }
}
