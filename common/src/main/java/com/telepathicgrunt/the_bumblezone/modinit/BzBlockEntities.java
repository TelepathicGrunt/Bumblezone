package com.telepathicgrunt.the_bumblezone.modinit;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.CrystallineFlowerBlockEntity;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.EssenceBlockEntity;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.HoneyCocoonBlockEntity;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.InfinityBarrierBlockEntity;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.PotionCandleBlockEntity;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.StateFocusedBrushableBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;


public class BzBlockEntities {
    public static final ResourcefulRegistry<BlockEntityType<?>> BLOCK_ENTITIES = ResourcefulRegistries.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Bumblezone.MODID);

    //Blocks
    public static final RegistryEntry<BlockEntityType<HoneyCocoonBlockEntity>> HONEY_COCOON = BLOCK_ENTITIES.register("honey_cocoon", () -> new BlockEntityType<>(HoneyCocoonBlockEntity::new, Set.of(BzBlocks.HONEY_COCOON.get())));
    public static final RegistryEntry<BlockEntityType<PotionCandleBlockEntity>> POTION_CANDLE = BLOCK_ENTITIES.register("potion_candle", () -> new BlockEntityType<>(PotionCandleBlockEntity::new, Set.of(BzBlocks.POTION_BASE_CANDLE.get())));
    public static final RegistryEntry<BlockEntityType<CrystallineFlowerBlockEntity>> CRYSTALLINE_FLOWER = BLOCK_ENTITIES.register("crystalline_flower", () -> new BlockEntityType<>(CrystallineFlowerBlockEntity::new, Set.of(BzBlocks.CRYSTALLINE_FLOWER.get())));
    public static final RegistryEntry<BlockEntityType<EssenceBlockEntity>> ESSENCE_BLOCK = BLOCK_ENTITIES.register("essence_block", () -> new BlockEntityType<>(EssenceBlockEntity::new, Set.of(BzBlocks.ESSENCE_BLOCK_RED.get(), BzBlocks.ESSENCE_BLOCK_PURPLE.get(), BzBlocks.ESSENCE_BLOCK_BLUE.get(), BzBlocks.ESSENCE_BLOCK_GREEN.get(), BzBlocks.ESSENCE_BLOCK_YELLOW.get(), BzBlocks.ESSENCE_BLOCK_WHITE.get())));
    public static final RegistryEntry<BlockEntityType<InfinityBarrierBlockEntity>> INFINITY_BARRIER = BLOCK_ENTITIES.register("infinity_barrier", () -> new BlockEntityType<>(InfinityBarrierBlockEntity::new, Set.of(BzBlocks.INFINITY_BARRIER.get())));
    public static final RegistryEntry<BlockEntityType<StateFocusedBrushableBlockEntity>> STATE_FOCUSED_BRUSHABLE_BLOCK_ENTITY = BLOCK_ENTITIES.register("state_focused_brushable_block_entity", () -> new BlockEntityType<>(StateFocusedBrushableBlockEntity::new, Set.of(BzBlocks.PILE_OF_POLLEN_SUSPICIOUS.get())));
}