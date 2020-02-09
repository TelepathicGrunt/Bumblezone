package net.telepathicgrunt.bumblezone.world.biome;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.LiquidsConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.telepathicgrunt.bumblezone.blocks.BlocksInit;
import net.telepathicgrunt.bumblezone.world.biomes.surfacebuilders.HoneySurfaceBuilder;

public class BzBaseBiome extends Biome {

    protected static final BlockState CORAL = Blocks.HORN_CORAL_BLOCK.getDefaultState();
    protected static final BlockState HONEY = Blocks.field_226907_mc_.getDefaultState();
    protected static final BlockState FILLED_POROUS_HONEYCOMB = BlocksInit.FILLED_POROUS_HONEYCOMB.get().getDefaultState();
    protected static final BlockState POROUS_HONEYCOMB = BlocksInit.POROUS_HONEYCOMB.get().getDefaultState();
    
    public static final SurfaceBuilderConfig HONEY_CONFIG = new SurfaceBuilderConfig(POROUS_HONEYCOMB, POROUS_HONEYCOMB, POROUS_HONEYCOMB);
    public static final LiquidsConfig WATER_SPRING_CONFIG = new LiquidsConfig(Fluids.WATER.getDefaultState(), true, 4, 1, ImmutableSet.of(Blocks.field_226907_mc_, Blocks.field_226908_md_));

    public static final SurfaceBuilder<SurfaceBuilderConfig> HONEY_SURFACE_BUILDER = new HoneySurfaceBuilder(SurfaceBuilderConfig::deserialize);

	protected BzBaseBiome(Biome.Builder biomeBuilder) {
		super(biomeBuilder);
	}
	
	
}
