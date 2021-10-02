package com.telepathicgrunt.bumblezone.modinit;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.world.structures.HoneyCaveRoomStructure;
import com.telepathicgrunt.bumblezone.world.structures.PollinatedStreamStructure;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class BzStructures {

    public static final StructureFeature<DefaultFeatureConfig> POLLINATED_STREAM = new PollinatedStreamStructure(DefaultFeatureConfig.CODEC);
    public static final StructureFeature<DefaultFeatureConfig> HONEY_CAVE_ROOM = new HoneyCaveRoomStructure(DefaultFeatureConfig.CODEC);

    public static void registerStructures() {
        FabricStructureBuilder.create(new Identifier(Bumblezone.MODID, "pollinated_stream"), POLLINATED_STREAM).step(GenerationStep.Feature.UNDERGROUND_STRUCTURES).defaultConfig(new StructureConfig(12, 8, 938497222)).superflatFeature(POLLINATED_STREAM.configure(FeatureConfig.DEFAULT)).register();
        FabricStructureBuilder.create(new Identifier(Bumblezone.MODID, "honey_cave_room"), HONEY_CAVE_ROOM).step(GenerationStep.Feature.UNDERGROUND_STRUCTURES).defaultConfig(new StructureConfig(6, 3, 722299384)).superflatFeature(HONEY_CAVE_ROOM.configure(FeatureConfig.DEFAULT)).register();
    }
}
