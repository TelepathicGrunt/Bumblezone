package com.telepathicgrunt.the_bumblezone.modinit;


import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.items.functions.DropContainerItems;
import com.telepathicgrunt.the_bumblezone.world.features.decorators.HoneycombHolePlacer;
import com.telepathicgrunt.the_bumblezone.world.features.decorators.Random3DClusterPlacement;
import com.telepathicgrunt.the_bumblezone.world.features.decorators.Random3DUndergroundChunkPlacement;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BzPlacements {
    public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIER = DeferredRegister.create(Registry.PLACEMENT_MODIFIER_REGISTRY, Bumblezone.MODID);

    public static final RegistryObject<PlacementModifierType<HoneycombHolePlacer>> HONEYCOMB_HOLE_PLACER = PLACEMENT_MODIFIER.register("honeycomb_hole_placer", () -> () -> HoneycombHolePlacer.CODEC);
    public static final RegistryObject<PlacementModifierType<Random3DUndergroundChunkPlacement>> RANDOM_3D_UNDERGROUND_CHUNK_PLACEMENT = PLACEMENT_MODIFIER.register("random_3d_underground_chunk_placement", () -> () -> Random3DUndergroundChunkPlacement.CODEC);
    public static final RegistryObject<PlacementModifierType<Random3DClusterPlacement>> RANDOM_3D_CLUSTER_PLACEMENT = PLACEMENT_MODIFIER.register("random_3d_cluster_placement", () -> () -> Random3DClusterPlacement.CODEC);
}
