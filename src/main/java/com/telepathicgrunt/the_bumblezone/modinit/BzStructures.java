package com.telepathicgrunt.the_bumblezone.modinit;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.world.structures.HoneyCaveRoomStructure;
import com.telepathicgrunt.the_bumblezone.world.structures.PollinatedStreamStructure;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class BzStructures {

    public static final DeferredRegister<StructureType<?>> STRUCTURES = DeferredRegister.create(Registry.STRUCTURE_TYPE_REGISTRY, Bumblezone.MODID);

    public static final RegistryObject<StructureType<PollinatedStreamStructure>> POLLINATED_STREAM = STRUCTURES.register("pollinated_stream", () -> () -> PollinatedStreamStructure.CODEC);
    public static final RegistryObject<StructureType<HoneyCaveRoomStructure>> HONEY_CAVE_ROOM = STRUCTURES.register("honey_cave_room", () -> () -> HoneyCaveRoomStructure.CODEC);
}
