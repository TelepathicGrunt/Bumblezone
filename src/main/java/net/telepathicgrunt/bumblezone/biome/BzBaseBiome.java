package net.telepathicgrunt.bumblezone.biome;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SpringFeatureConfig;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.biome.surfacebuilders.HoneySurfaceBuilder;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import net.telepathicgrunt.bumblezone.effects.WrathOfTheHiveEffect;

public class BzBaseBiome extends Biome {

    protected static final BlockState POROUS_HONEYCOMB = BzBlocks.POROUS_HONEYCOMB.getDefaultState();
    public static final TernarySurfaceConfig HONEY_CONFIG = new TernarySurfaceConfig(POROUS_HONEYCOMB, POROUS_HONEYCOMB, POROUS_HONEYCOMB);
    public static final SurfaceBuilder<TernarySurfaceConfig> HONEY_SURFACE_BUILDER = new HoneySurfaceBuilder(TernarySurfaceConfig.CODEC);

    protected BzBaseBiome(Biome.Settings biomeBuilder) {
        super(biomeBuilder);
    }

    public static void addSprings() {
        SpringFeatureConfig SUGAR_WATER_SPRING_CONFIG = new SpringFeatureConfig(BzBlocks.SUGAR_WATER_FLUID.getDefaultState(), true, 4, 1, ImmutableSet.of(Blocks.HONEY_BLOCK, Blocks.HONEYCOMB_BLOCK));
        BzBiomes.HIVE_PILLAR.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.SPRING_FEATURE.configure(SUGAR_WATER_SPRING_CONFIG).createDecoratedFeature(Decorator.COUNT_BIASED_RANGE.configure(new RangeDecoratorConfig(1, 128, 0, 128))));
        BzBiomes.HIVE_PILLAR.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.SPRING_FEATURE.configure(SUGAR_WATER_SPRING_CONFIG).createDecoratedFeature(Decorator.COUNT_BIASED_RANGE.configure(new RangeDecoratorConfig(5, 16, 0, 72))));


        BzBiomes.HIVE_WALL.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.SPRING_FEATURE.configure(SUGAR_WATER_SPRING_CONFIG).createDecoratedFeature(Decorator.COUNT_BIASED_RANGE.configure(new RangeDecoratorConfig(1, 128, 0, 128))));
        BzBiomes.HIVE_WALL.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.SPRING_FEATURE.configure(SUGAR_WATER_SPRING_CONFIG).createDecoratedFeature(Decorator.COUNT_BIASED_RANGE.configure(new RangeDecoratorConfig(5, 16, 0, 72))));


        BzBiomes.SUGAR_WATER.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.SPRING_FEATURE.configure(SUGAR_WATER_SPRING_CONFIG).createDecoratedFeature(Decorator.COUNT_BIASED_RANGE.configure(new RangeDecoratorConfig(1, 128, 0, 256))));
        BzBiomes.SUGAR_WATER.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.SPRING_FEATURE.configure(SUGAR_WATER_SPRING_CONFIG).createDecoratedFeature(Decorator.COUNT_BIASED_RANGE.configure(new RangeDecoratorConfig(7, 16, 0, 128))));
    }


    @Environment(EnvType.CLIENT)
    public static float REDDISH_FOG_TINT = 0;

    /**
     * mimics vanilla Overworld sky timer
     * <p>
     * Returns a value between 0 and 1. .25 is dusk and .75 is dawn 0 is noon. 0.5 is midnight
     */
    @Environment(EnvType.CLIENT)
    protected float calculateVanillaSkyPositioning(long worldTime) {
        double fractionComponent = MathHelper.fractionalPart((double) worldTime / 24000.0D - 0.25D);
        double d1 = 0.5D - Math.cos(fractionComponent * Math.PI) / 2.0D;
        return (float) (fractionComponent * 2.0D + d1) / 3.0F;
    }

    /**
     * Returns fog color
     * <p>
     * What I done is made it be based on the day/night cycle so the fog will darken at night but brighten during day.
     * calculateVanillaSkyPositioning returns a value which is between 0 and 1 for day/night and fogChangeSpeed is the range
     * that the fog color will cycle between.
     */
    @Environment(EnvType.CLIENT)
    public int getFogColor() {
        PlayerEntity player = MinecraftClient.getInstance().player;
        World world = player.world;

        float colorFactor = 1;

        if (Bumblezone.BZ_CONFIG.dayNightCycle) {
            // Modifies the sky angle to be in range of 0 to 1 with 0 as night and 1 as day.
            float scaledAngle = Math.abs(0.5f - calculateVanillaSkyPositioning(world.getTime())) * 2;

            // Limits angle between 0 to 1 and sharply changes color between 0.333... and 0.666...‬
            colorFactor = Math.min(Math.max(scaledAngle * 3 - 1f, 0), 1);

            // Scales the returned factor by user chosen brightness.
            colorFactor *= (Bumblezone.BZ_CONFIG.fogBrightnessPercentage / 100);
        } else {
            /*
             * The sky will be turned to midnight when brightness is below 50 and the day/night cycle is off. This lets us get the
             * full range of brightness by utilizing the default brightness that the current celestial time gives.
             */
            if (Bumblezone.BZ_CONFIG.fogBrightnessPercentage <= 50) {
                colorFactor *= (Bumblezone.BZ_CONFIG.fogBrightnessPercentage / 50);
            } else {
                colorFactor *= (Bumblezone.BZ_CONFIG.fogBrightnessPercentage / 100);
            }
        }

        if (WrathOfTheHiveEffect.ACTIVE_WRATH && REDDISH_FOG_TINT < 0.65f) {
            REDDISH_FOG_TINT += 0.00003f;
        } else if (REDDISH_FOG_TINT > 0) {
            REDDISH_FOG_TINT -= 0.00003f;
        }

        return ((int)(Math.min(0.9f * colorFactor, 1.1f + REDDISH_FOG_TINT)*255) << 0 ) |
                ((int)(Math.max(Math.min(0.63f * colorFactor, 1.1f) - REDDISH_FOG_TINT * 0.4f, 0)*255) << 8 ) |
                ((int)(Math.max(Math.min(0.0015f * colorFactor, 1.1f) - REDDISH_FOG_TINT * 1.75f, 0)*255) << 16 );
    }
}
