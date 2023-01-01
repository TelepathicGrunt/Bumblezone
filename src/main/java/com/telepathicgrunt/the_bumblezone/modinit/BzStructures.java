package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.world.structures.GenericOptimizedStructure;
import com.telepathicgrunt.the_bumblezone.world.structures.HoneyCaveRoomStructure;
import com.telepathicgrunt.the_bumblezone.world.structures.PollinatedStreamStructure;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.StructureType;

public class BzStructures {

    public static final StructureType<PollinatedStreamStructure> POLLINATED_STREAM = () -> PollinatedStreamStructure.CODEC;
    public static final StructureType<HoneyCaveRoomStructure> HONEY_CAVE_ROOM = () -> HoneyCaveRoomStructure.CODEC;
    public static final StructureType<GenericOptimizedStructure> GENERIC_OPTIMIZED_STRUCTURE = () -> GenericOptimizedStructure.CODEC;

    public static void registerStructures() {
        Registry.register(Registry.STRUCTURE_TYPES, new ResourceLocation(Bumblezone.MODID, "pollinated_stream"), POLLINATED_STREAM);
        Registry.register(Registry.STRUCTURE_TYPES, new ResourceLocation(Bumblezone.MODID, "honey_cave_room"), HONEY_CAVE_ROOM);
        Registry.register(Registry.STRUCTURE_TYPES, new ResourceLocation(Bumblezone.MODID, "generic_optimized_structure"), GENERIC_OPTIMIZED_STRUCTURE);
    }
}
