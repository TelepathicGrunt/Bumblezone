package com.telepathicgrunt.the_bumblezone.worldgen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.modinit.BzProcessors;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class ArenaSpecialBlockHandlerProcessor extends StructureProcessor {

    public static final Codec<ArenaSpecialBlockHandlerProcessor> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.BOOL.fieldOf("clear_containers_only").forGetter(config -> config.clearContainersOnly)
    ).apply(instance, instance.stable(ArenaSpecialBlockHandlerProcessor::new)));

    private final boolean clearContainersOnly;

    public ArenaSpecialBlockHandlerProcessor(boolean clearContainersOnly) {
        this.clearContainersOnly = clearContainersOnly;
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldView, BlockPos pos, BlockPos blockPos, StructureTemplate.StructureBlockInfo structureBlockInfoLocal, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings structurePlacementData) {
        if (worldView instanceof Level level) {
            BlockState inWorldBlockState = level.getBlockState(structureBlockInfoWorld.pos());

            if (inWorldBlockState.is(BzTags.ESSENCE_ARENA_DOES_NOT_REPLACE) && !this.clearContainersOnly) {
                BlockState structureBlockState = structureBlockInfoWorld.state();
                BlockEntity blockEntity = null;
                if (structureBlockState.getBlock() instanceof EntityBlock entityBlock) {
                    blockEntity = entityBlock.newBlockEntity(structureBlockInfoWorld.pos(), structureBlockState);
                    if (blockEntity != null) {
                        blockEntity.load(structureBlockInfoWorld.nbt());
                    }
                }
                ItemStack itemStack = new ItemStack(Items.DIAMOND_PICKAXE);
                itemStack.enchant(Enchantments.SILK_TOUCH, 1);
                Block.dropResources(structureBlockState, level, structureBlockInfoWorld.pos(), blockEntity, null, itemStack);
                return null;
            }

            if (inWorldBlockState.hasBlockEntity()) {
                BlockEntity blockEntity = level.getBlockEntity(structureBlockInfoWorld.pos());
                if (blockEntity instanceof Container container) {
                    if (this.clearContainersOnly) {
                        container.clearContent();
                        container.setChanged();
                        level.setBlockEntity(blockEntity);
                    }
                    else {
                        ItemStack itemStack = new ItemStack(Items.DIAMOND_PICKAXE);
                        itemStack.enchant(Enchantments.SILK_TOUCH, 1);
                        Block.dropResources(inWorldBlockState, level, structureBlockInfoWorld.pos(), blockEntity, null, itemStack);
                        level.destroyBlock(structureBlockInfoWorld.pos(), false);
                    }
                }
            }
        }

        return structureBlockInfoWorld;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return BzProcessors.ARENA_SPECIAL_BLOCK_HANDLER_PROCESSOR.get();
    }
}