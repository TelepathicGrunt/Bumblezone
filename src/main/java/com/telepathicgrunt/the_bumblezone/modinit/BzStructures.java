package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.world.structures.CellMazeStructure;
import com.telepathicgrunt.the_bumblezone.world.structures.HoneyCaveRoomStructure;
import com.telepathicgrunt.the_bumblezone.world.structures.PollinatedStreamStructure;
import com.telepathicgrunt.the_bumblezone.world.structures.pieces.BuriedStructurePiece;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BzStructures {

    public static final DeferredRegister<StructureFeature<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, Bumblezone.MODID);

    public static final RegistryObject<StructureFeature<JigsawConfiguration>> POLLINATED_STREAM = STRUCTURES.register("pollinated_stream", () -> (new PollinatedStreamStructure(JigsawConfiguration.CODEC)));
    public static final RegistryObject<StructureFeature<JigsawConfiguration>> HONEY_CAVE_ROOM = STRUCTURES.register("honey_cave_room", () -> (new HoneyCaveRoomStructure(JigsawConfiguration.CODEC)));
    public static final RegistryObject<StructureFeature<JigsawConfiguration>> CELL_MAZE = STRUCTURES.register("cell_maze", () -> (new CellMazeStructure(JigsawConfiguration.CODEC)));

    public static StructurePieceType BURIED_STRUCTURE_PIECE;

    public static void setupStructures(final RegistryEvent<StructureFeature<?>> event) {
        BURIED_STRUCTURE_PIECE = Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(Bumblezone.MODID, "buried_structure_piece"), BuriedStructurePiece::new);
    }
}
