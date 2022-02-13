package com.telepathicgrunt.the_bumblezone.modinit;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.mixin.world.StructureFeatureAccessor;
import com.telepathicgrunt.the_bumblezone.mixin.world.StructureSettingsAccessor;
import com.telepathicgrunt.the_bumblezone.world.structures.CellMazeStructure;
import com.telepathicgrunt.the_bumblezone.world.structures.HoneyCaveRoomStructure;
import com.telepathicgrunt.the_bumblezone.world.structures.PollinatedStreamStructure;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BzStructures {

    public static final DeferredRegister<StructureFeature<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, Bumblezone.MODID);

    public static final RegistryObject<StructureFeature<JigsawConfiguration>> POLLINATED_STREAM = STRUCTURES.register("pollinated_stream", () -> (new PollinatedStreamStructure(JigsawConfiguration.CODEC)));
    public static final RegistryObject<StructureFeature<JigsawConfiguration>> HONEY_CAVE_ROOM = STRUCTURES.register("honey_cave_room", () -> (new HoneyCaveRoomStructure(JigsawConfiguration.CODEC)));
    public static final RegistryObject<StructureFeature<JigsawConfiguration>> CELL_MAZE = STRUCTURES.register("cell_maze", () -> (new CellMazeStructure(JigsawConfiguration.CODEC)));

    public static void setupStructures() {
        setupMapSpacingAndLand(
                POLLINATED_STREAM.get(),
                new StructureFeatureConfiguration(12,
                        8,
                        938497222),
                true);

        setupMapSpacingAndLand(
                HONEY_CAVE_ROOM.get(),
                new StructureFeatureConfiguration(12,
                        4,
                        722299384),
                false);

        setupMapSpacingAndLand(
                CELL_MAZE.get(),
                new StructureFeatureConfiguration(50,
                        20,
                        456768898),
                true);
    }

    public static <F extends StructureFeature<?>> void setupMapSpacingAndLand(
            F structure,
            StructureFeatureConfiguration structureFeatureConfiguration,
            boolean transformSurroundingLand)
    {
        StructureFeature.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);

        if(transformSurroundingLand){
            StructureFeatureAccessor.bumblezone_setNOISE_AFFECTING_FEATURES(
                    ImmutableList.<StructureFeature<?>>builder()
                            .addAll(StructureFeature.NOISE_AFFECTING_FEATURES)
                            .add(structure)
                            .build());
        }

        StructureSettingsAccessor.setDEFAULTS(
                ImmutableMap.<StructureFeature<?>, StructureFeatureConfiguration>builder()
                        .putAll(StructureSettings.DEFAULTS)
                        .put(structure, structureFeatureConfiguration)
                        .build());
    }
}
