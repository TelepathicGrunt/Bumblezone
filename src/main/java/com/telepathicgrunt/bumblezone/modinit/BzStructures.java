package com.telepathicgrunt.bumblezone.modinit;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.world.structures.HoneyCaveRoomStructure;
import com.telepathicgrunt.bumblezone.world.structures.PollinatedStreamStructure;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;

public class BzStructures {

    public static final StructureFeature<JigsawConfiguration> POLLINATED_STREAM = new PollinatedStreamStructure(JigsawConfiguration.CODEC);
    public static final StructureFeature<JigsawConfiguration> HONEY_CAVE_ROOM = new HoneyCaveRoomStructure(JigsawConfiguration.CODEC);

    public static void registerStructures() {
        FabricStructureBuilder.create(new ResourceLocation(Bumblezone.MODID, "pollinated_stream"), POLLINATED_STREAM).step(GenerationStep.Decoration.UNDERGROUND_STRUCTURES).defaultConfig(new StructureFeatureConfiguration(12, 8, 938497222)).register();
        FabricStructureBuilder.create(new ResourceLocation(Bumblezone.MODID, "honey_cave_room"), HONEY_CAVE_ROOM).step(GenerationStep.Decoration.UNDERGROUND_STRUCTURES).defaultConfig(new StructureFeatureConfiguration(6, 3, 722299384)).register();
    }
}
