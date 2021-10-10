package com.telepathicgrunt.bumblezone.modinit;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.world.structures.HoneyCaveRoomStructure;
import com.telepathicgrunt.bumblezone.world.structures.PollinatedStreamStructure;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;

public class BzStructures {

    public static final StructureFeature<NoneFeatureConfiguration> POLLINATED_STREAM = new PollinatedStreamStructure(NoneFeatureConfiguration.CODEC);
    public static final StructureFeature<NoneFeatureConfiguration> HONEY_CAVE_ROOM = new HoneyCaveRoomStructure(NoneFeatureConfiguration.CODEC);

    public static void registerStructures() {
        FabricStructureBuilder.create(new ResourceLocation(Bumblezone.MODID, "pollinated_stream"), POLLINATED_STREAM).step(GenerationStep.Decoration.UNDERGROUND_STRUCTURES).defaultConfig(new StructureFeatureConfiguration(12, 8, 938497222)).superflatFeature(POLLINATED_STREAM.configured(FeatureConfiguration.NONE)).register();
        FabricStructureBuilder.create(new ResourceLocation(Bumblezone.MODID, "honey_cave_room"), HONEY_CAVE_ROOM).step(GenerationStep.Decoration.UNDERGROUND_STRUCTURES).defaultConfig(new StructureFeatureConfiguration(6, 3, 722299384)).superflatFeature(HONEY_CAVE_ROOM.configured(FeatureConfiguration.NONE)).register();
    }
}
