package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.mixin.world.StructureFeatureAccessor;
import com.telepathicgrunt.the_bumblezone.world.structures.CellMazeStructure;
import com.telepathicgrunt.the_bumblezone.world.structures.HoneyCaveRoomStructure;
import com.telepathicgrunt.the_bumblezone.world.structures.PollinatedStreamStructure;
import com.telepathicgrunt.the_bumblezone.world.structures.pieces.BuriedStructurePiece;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class BzStructures {

    public static final StructureFeature<JigsawConfiguration> POLLINATED_STREAM = new PollinatedStreamStructure(JigsawConfiguration.CODEC);
    public static final StructureFeature<JigsawConfiguration> HONEY_CAVE_ROOM = new HoneyCaveRoomStructure(JigsawConfiguration.CODEC);
    public static final StructureFeature<JigsawConfiguration> CELL_MAZE = new CellMazeStructure(JigsawConfiguration.CODEC);

    public static StructurePieceType BURIED_STRUCTURE_PIECE;

    public static void registerStructures() {
        Registry.register(Registry.STRUCTURE_FEATURE, new ResourceLocation(Bumblezone.MODID, "pollinated_stream"), POLLINATED_STREAM);
        Registry.register(Registry.STRUCTURE_FEATURE, new ResourceLocation(Bumblezone.MODID, "honey_cave_room"), HONEY_CAVE_ROOM);
        Registry.register(Registry.STRUCTURE_FEATURE, new ResourceLocation(Bumblezone.MODID, "cell_maze"), CELL_MAZE);

        StructureFeatureAccessor.getSTEP().put(POLLINATED_STREAM, GenerationStep.Decoration.SURFACE_STRUCTURES);
        StructureFeatureAccessor.getSTEP().put(HONEY_CAVE_ROOM, GenerationStep.Decoration.LOCAL_MODIFICATIONS);
        StructureFeatureAccessor.getSTEP().put(CELL_MAZE, GenerationStep.Decoration.LOCAL_MODIFICATIONS);

        BURIED_STRUCTURE_PIECE = Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(Bumblezone.MODID, "buried_structure_piece"), BuriedStructurePiece::new);
    }
}
