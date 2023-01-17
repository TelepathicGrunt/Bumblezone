package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistries;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistry;
import com.telepathicgrunt.the_bumblezone.world.structures.GenericOptimizedStructure;
import com.telepathicgrunt.the_bumblezone.world.structures.HoneyCaveRoomStructure;
import com.telepathicgrunt.the_bumblezone.world.structures.PollinatedStreamStructure;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;

public class BzStructures {

    public static final ResourcefulRegistry<StructureType<?>> STRUCTURES = ResourcefulRegistries.create(BuiltInRegistries.STRUCTURE_TYPE, Bumblezone.MODID);

    public static final RegistryEntry<StructureType<PollinatedStreamStructure>> POLLINATED_STREAM = STRUCTURES.register("pollinated_stream", () -> () -> PollinatedStreamStructure.CODEC);
    public static final RegistryEntry<StructureType<HoneyCaveRoomStructure>> HONEY_CAVE_ROOM = STRUCTURES.register("honey_cave_room", () -> () -> HoneyCaveRoomStructure.CODEC);
    public static final RegistryEntry<StructureType<GenericOptimizedStructure>> GENERIC_OPTIMIZED_STRUCTURE = STRUCTURES.register("generic_optimized_structure", () -> () -> GenericOptimizedStructure.CODEC);
}
