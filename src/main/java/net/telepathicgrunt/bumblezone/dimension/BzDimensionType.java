package net.telepathicgrunt.bumblezone.dimension;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensionType;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;
import net.minecraft.world.gen.chunk.OverworldChunkGeneratorConfig;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.generation.BzChunkGenerator;

public class BzDimensionType{

	public static FabricDimensionType BUMBLEZONE_TYPE;

	public static ChunkGeneratorType<OverworldChunkGeneratorConfig, BzChunkGenerator> BZ_CHUNK_GENERATOR;

	public static void registerChunkGenerator() {
		BzDimensionType.BZ_CHUNK_GENERATOR = FabricChunkGeneratorType.register(new Identifier(Bumblezone.MODID, Bumblezone.MODID), BzChunkGenerator::new, OverworldChunkGeneratorConfig::new, false);
	}

	public static void registerDimension() {
		BUMBLEZONE_TYPE = FabricDimensionType.builder()
				.skyLight(true)
				.factory(BzDimension::new)
				.defaultPlacer(BzPlacement.ENTERING)
				.buildAndRegister(new Identifier(Bumblezone.MODID));
	}
}
