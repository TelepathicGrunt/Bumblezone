package com.telepathicgrunt.the_bumblezone.worldgen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.modinit.BzProcessors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Optional;

public class ChiseledBookshelfProcessor extends StructureProcessor {

    public static final Codec<ChiseledBookshelfProcessor> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            RegistryCodecs.homogeneousList(Registries.ENCHANTMENT).optionalFieldOf("allowed_book_enchantments").forGetter(config -> config.allowedBookEnchantments),
            IntProvider.codec(0, 6).optionalFieldOf("number_of_books").forGetter(config -> config.numberOfBooks),
            IntProvider.codec(0, 6).optionalFieldOf("number_of_enchanted").forGetter(config -> config.numberOfEnchanted)
    ).apply(instance, instance.stable(ChiseledBookshelfProcessor::new)));

    public final Optional<HolderSet<Enchantment>> allowedBookEnchantments;
    public final Optional<IntProvider> numberOfBooks;
    public final Optional<IntProvider> numberOfEnchanted;

    public ChiseledBookshelfProcessor(Optional<HolderSet<Enchantment>>  allowedBookEnchantments,
                                      Optional<IntProvider> numberOfBooks,
                                      Optional<IntProvider> numberOfEnchanted)
    {
        this.allowedBookEnchantments = allowedBookEnchantments;
        this.numberOfBooks = numberOfBooks;
        this.numberOfEnchanted = numberOfEnchanted;
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos pos, BlockPos blockPos, StructureTemplate.StructureBlockInfo structureBlockInfoLocal, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings settings) {
        if (structureBlockInfoWorld.state().is(Blocks.CHISELED_BOOKSHELF) &&
            this.numberOfBooks.isPresent() &&
            this.numberOfEnchanted.isPresent() &&
            this.allowedBookEnchantments.isPresent() &&
            this.allowedBookEnchantments.get().size() > 0)
        {
            NonNullList<ItemStack> items = NonNullList.withSize(6, ItemStack.EMPTY);

            RandomSource randomSource = settings.getRandom(structureBlockInfoWorld.pos());
            int maxNumberOfBooks = this.numberOfBooks.get().sample(randomSource);
            int maxNumberOfEnchanted = this.numberOfEnchanted.get().sample(randomSource);
            int currentNumOfEnchanted = 0;

            BlockState chiseledBookshelfState = structureBlockInfoWorld.state();

            // reset visual state
            chiseledBookshelfState = chiseledBookshelfState.setValue(ChiseledBookShelfBlock.SLOT_OCCUPIED_PROPERTIES.get(0), false);
            chiseledBookshelfState = chiseledBookshelfState.setValue(ChiseledBookShelfBlock.SLOT_OCCUPIED_PROPERTIES.get(1), false);
            chiseledBookshelfState = chiseledBookshelfState.setValue(ChiseledBookShelfBlock.SLOT_OCCUPIED_PROPERTIES.get(2), false);
            chiseledBookshelfState = chiseledBookshelfState.setValue(ChiseledBookShelfBlock.SLOT_OCCUPIED_PROPERTIES.get(3), false);
            chiseledBookshelfState = chiseledBookshelfState.setValue(ChiseledBookShelfBlock.SLOT_OCCUPIED_PROPERTIES.get(4), false);
            chiseledBookshelfState = chiseledBookshelfState.setValue(ChiseledBookShelfBlock.SLOT_OCCUPIED_PROPERTIES.get(5), false);

            for (int bookNum = 0; bookNum < maxNumberOfBooks; bookNum++) {
                ItemStack book = Items.BOOK.getDefaultInstance();

                if (currentNumOfEnchanted < maxNumberOfEnchanted) {
                    Optional<Holder<Enchantment>> enchantment = this.allowedBookEnchantments.get().getRandomElement(randomSource);
                    if (enchantment.isPresent()) {
                        book = Items.ENCHANTED_BOOK.getDefaultInstance();
                        book.enchant(enchantment.get().value(), randomSource.nextIntBetweenInclusive(enchantment.get().value().getMinLevel(), enchantment.get().value().getMaxLevel()));
                        currentNumOfEnchanted++;
                    }
                }

                int index = randomSource.nextInt(6);
                while (!items.get(index).isEmpty()) {
                    index = randomSource.nextInt(6);
                }

                items.set(index, book);
                chiseledBookshelfState = chiseledBookshelfState.setValue(ChiseledBookShelfBlock.SLOT_OCCUPIED_PROPERTIES.get(index), true);
            }

            CompoundTag tag = structureBlockInfoWorld.nbt() == null ? new CompoundTag() : structureBlockInfoWorld.nbt().copy();
            ContainerHelper.saveAllItems(tag, items, true);
            tag.remove("last_interacted_slot");
            return new StructureTemplate.StructureBlockInfo(structureBlockInfoWorld.pos(), chiseledBookshelfState, tag);
        }
        return structureBlockInfoWorld;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return BzProcessors.CHISELED_BOOKSHELF_PROCESSOR.get();
    }
}