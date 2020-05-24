package net.telepathicgrunt.bumblezone.biome;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.NopeDecoratorConfig;
import net.minecraft.world.gen.feature.SpringFeatureConfig;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
import net.telepathicgrunt.bumblezone.biome.surfacebuilders.HoneySurfaceBuilder;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import net.telepathicgrunt.bumblezone.features.decorators.HoneycombHoleDecorator;

public class BzBaseBiome extends Biome {

    protected static final BlockState CORAL = Blocks.HORN_CORAL_BLOCK.getDefaultState();
    protected static final BlockState HONEY_BLOCK = Blocks.HONEY_BLOCK.getDefaultState();
    protected static final BlockState HONEYCOMB_BLOCK = Blocks.HONEYCOMB_BLOCK.getDefaultState();
    protected static final BlockState FILLED_POROUS_HONEYCOMB = BzBlocks.FILLED_POROUS_HONEYCOMB.getDefaultState();
    protected static final BlockState POROUS_HONEYCOMB = BzBlocks.POROUS_HONEYCOMB.getDefaultState();

    public static final TernarySurfaceConfig HONEY_CONFIG = new TernarySurfaceConfig(POROUS_HONEYCOMB, POROUS_HONEYCOMB, POROUS_HONEYCOMB);
    public static final SpringFeatureConfig WATER_SPRING_CONFIG = new SpringFeatureConfig(Fluids.WATER.getDefaultState(), true, 4, 1, ImmutableSet.of(Blocks.HONEYCOMB_BLOCK, Blocks.HONEY_BLOCK));

    public static final Decorator<NopeDecoratorConfig> HONEYCOMB_HOLE_PLACER = new HoneycombHoleDecorator(NopeDecoratorConfig::deserialize);

    public static final SurfaceBuilder<TernarySurfaceConfig> HONEY_SURFACE_BUILDER = new HoneySurfaceBuilder(TernarySurfaceConfig::deserialize);

    protected BzBaseBiome(Biome.Settings biomeBuilder) {
        super(biomeBuilder);
    }


}
